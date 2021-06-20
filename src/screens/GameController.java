package screens;

import logic.AudioClip;
import logic.Context;
import logic.EventEmitter;
import logic.Player;
import logic.ShotType;
import logic.Vector2;
import logic.score.Score;
import logic.score.ScoreManager;

import javax.swing.JButton;

import java.util.HashMap;

class GameController {

  // Called when the game changes state
  public EventEmitter<GameState> onGameStateChange = new EventEmitter<>();
  // Called when the shot type changes
  public EventEmitter<ShotType> onShotTypeChange = new EventEmitter<>();
  // Called when the human player shot in the computer
  public EventEmitter<ShotType> onHumanShot = new EventEmitter<>();
  // Called when the timer ticks a second, this is emmited once per second
  public EventEmitter<Integer> onGameTimerTick = new EventEmitter<>();
  // Called when the player used all 3 sonars

  public EventEmitter<Void> onSonarExausted = new EventEmitter<>();
  public EventEmitter<Void> onSonarRestaured = new EventEmitter<>();

  private AudioClip explosionAudio;
  private AudioClip sonarAudio;
  private Context context;

  private PlayerController human;
  private PlayerController computer;
  private GameTimer gameTimer;
  private ScoreManager scoreManager;

  // The current game state
  private GameState state = GameState.BEFORE_GAME;
  // Qual turno é o atual, esse numero é somado a cada turno
  private int turnCount = 0;

  private int sonarCounter = 3;

  // Player settings
  private ShotType selectedShotType;
  private HashMap<ShotType, JButton> buttonsMap;
  private ShotType lastShot;

  // Salve o ultimo turno que utilizou um tipo de tiro
  private HashMap<ShotType, Integer> lastTimeUsedByShotType = new HashMap<>();

  public GameController(Context context) {
    this.context = context;
    this.explosionAudio = new AudioClip("src/resources/audio/explosao.wav");
    this.sonarAudio = new AudioClip("src/resources/audio/sonarwave.wav");
    var humanGrid = new GridMaker(this, context.getUser());
    this.human = new PlayerController(context.getUser(), humanGrid);

    var computerGrid = new GridMaker(this, context.getComputer());
    this.computer = new PlayerController(context.getComputer(), computerGrid);
    this.gameTimer = new GameTimer(this);
    this.scoreManager = new ScoreManager();
  }

  public GameTimer getGameTimer() {
    return gameTimer;
  }

  public Context getContext() {
    return context;
  }

  /**
   * Function called to start the game
   */
  public void start(boolean isRandom) {
    onGameStateChange.emit(GameState.PLAYING);
    state = GameState.PLAYING;
    getComputer().getPlayer().placeRandomVessels();
    if (isRandom) {
      getHuman().getPlayer().placeRandomVessels();
      getHuman().getPlayer().printVessels();
    }

    setSelectedShotType(ShotType.SHOT_SUBMARINO);

    this.gameTimer.start();
    this.updateGrid();
  }

  /**
   * Function called when the player clicked on the computer grid, at the given
   * position
   */
  void playerClicked(Vector2 position) {
    if (state != GameState.PLAYING) {
      return;
    }

    // Evita tiros no em lugar ja alvejado
    if (context.getComputer().getShotAt(position) != null)
      return;

    if (getSelectedShotType() == ShotType.SONAR) {
      this.runSonar(position);
      sonarAudio.play();
      return;
    }

    // para o tiro de porta avião
    turnCount++;
    // Barulho de tiro
    explosionAudio.play();
    // Pega o tabuleiro do computador e diz que levou um tiro na posição recebida
    this.computer.getPlayer().takeShot(position, getSelectedShotType());
    this.lastTimeUsedByShotType.put(getSelectedShotType(), turnCount);

    this.onHumanShot.emit(getSelectedShotType());

    // Precisa verificar se o jogador venceu depois do tiro, para pular a proxima
    // jogada do computador
    if (checkWinner()) {
      this.updateGrid();
      return;
    }
    // Agora gera a posição de tira no tabuleiro do usuário
    // Atenção, usamos o tabuleiro do usuario, pois as informações se já recebeu um
    // tiro em determinada posição está armazenada nele
    var computerShotPosition = context.getUser().generateComputerShotPosition();
    // E agora informamos para o jogador que levou um tiro na posição
    this.human.getPlayer().takeShot(computerShotPosition, context.getComputer().getAvailableShotType());

    // Renderizamos novamente o tabuleiro, para garantir que as mudanças desse round
    // sejão mostradas na tela
    this.updateGrid();

    // Checa pra ver se o jogo acabou
    var isFinished = checkWinner();
    if (!isFinished)
      checkAvailableShots();
  }

  public boolean checkWinner() {

    if (!context.getComputer().isAlive()) {
      win(context.getUser());
      gameTimer.stop();
      new SpanWin(this);

      return true;
    }
    if (!context.getUser().isAlive()) {
      win(context.getComputer());
      gameTimer.stop();
      new Span("Parece que você perdeu!", "Tente novamente!!!");

      return true;
    }
    return false;
  }

  public void win(Player player) { // tem que implementar
    if (state != GameState.PLAYING)
      return;

    state = GameState.ENDED;
    onGameStateChange.emit(GameState.ENDED);
    System.out.println(context.getUser().getName() + "Ganhou");
    gameTimer.stop();

    if (!player.isCpu()) {
      // Jogador humano ganhou
      scoreManager.savePlayer(new Score(player.getName(), gameTimer.count));
    }
  }

  public void setGameState() {
    state = GameState.PLAYING;
  }

  private void runSonar(Vector2 position) {
    if (isSonarAvailable()) {
      this.sonarCounter--;
      boolean isInCross = getComputer().getPlayer().sonar(position);
      if (isInCross) {
        new Span("Dica:", "Tem um navio nessa linha/coluna");
      } else {
        new Span("Dica:", "Nao tem um navio nessa linha/coluna");
      }
    }

    if (!isSonarAvailable())
      this.onSonarExausted.emit(null);

    setSelectedShotType(getHuman().getPlayer().getAvailableShotType());
  }

  public boolean isSonarAvailable() {
    return this.sonarCounter > 0;
  }

  public void setButtonsMap(HashMap<ShotType, JButton> buttonsMap) {
    this.buttonsMap = buttonsMap;
  }

  public void setLastShot(ShotType type) {
    this.lastShot = type;
  }

  public void checkAvailableShots() {
    var availableShotType = this.getContext().getUser().getAliveVessels();
    var rng = context.getRng();

    if (availableShotType.size() == 1 && availableShotType.get(0) == ShotType.SHOT_PORTA_AVIAO) {
      buttonsMap.get(ShotType.SHOT_PORTA_AVIAO).setEnabled(true);
      setSelectedShotType(ShotType.SHOT_PORTA_AVIAO);
    } else {
      if (!availableShotType.isEmpty()) {
        for (ShotType shotType : this.buttonsMap.keySet()) {
          var button = this.buttonsMap.get(shotType);
          button.setEnabled(false);
        }
        for (ShotType shotType : availableShotType) {
          this.buttonsMap.get(shotType).setEnabled(true);
        }
      }
      if (lastShot == ShotType.SHOT_PORTA_AVIAO)
        this.buttonsMap.get(lastShot).setEnabled(false);

      if (!availableShotType.contains(selectedShotType)) {
        availableShotType.remove(ShotType.SHOT_PORTA_AVIAO);
        availableShotType.get(rng.nextInt(availableShotType.size()));
      }
    }
  }

  public void resetGame() {
    context.getUser().resetPlayer();
    this.checkAvailableShots();
    context.getComputer().clear();
    context.getComputer().placeRandomVessels();
    this.sonarCounter = 3;

    this.gameTimer.reset();
    updateGrid();

    this.onSonarRestaured.emit(null);
  }

  public void updateGrid() {
    this.computer.getGrid().updateGrid();
    this.human.getGrid().updateGrid();

  }

  public void resetBoard() {
    this.computer.getGrid().resetBoard();
    this.human.getGrid().resetBoard();
  }

  public PlayerController getComputer() {
    return computer;
  }

  public PlayerController getHuman() {
    return human;
  }

  public void setSelectedShotType(ShotType selectedShotType) {
    this.selectedShotType = selectedShotType;
    this.onShotTypeChange.emit(selectedShotType);
  }

  public ShotType getSelectedShotType() {
    return selectedShotType;
  }

  public int getTurnCount() {
    return turnCount;
  }

  public HashMap<ShotType, Integer> getLastTimeUsedByShotType() {
    return lastTimeUsedByShotType;
  }

  public ScoreManager getScoreManager() {
    return scoreManager;
  }
}