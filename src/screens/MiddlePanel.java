package screens;

import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import logic.Context;
import logic.ShotType;
import logic.Vector2;
import logic.VesselTypes;
import utils.TimeUtils;
import logic.Constants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class MiddlePanel extends JPanel implements ActionListener {

  private JLabel stopwatch;

  private HashMap<VesselTypes, JButton> confirmationButtons = new HashMap<>();
  private HashMap<VesselTypes, JTextField> positionInputs = new HashMap<>();

  private JButton buttonStartGame;
  private JButton buttonDica;
  private JButton buttonClearGrid;
  private JButton buttonSair;

  private Context context;

  private GameController gameController;

  private boolean isRandom;

  public MiddlePanel(GameController gameController) {
    this.gameController = gameController;
    this.context = gameController.getContext();
    this.isRandom = context.getUser().getIsRandom();

    createPannels();

    this.gameController.onGameTimerTick.addListener(this::onGameTick);
    this.gameController.onGameStateChange.addListener(this::onGameStateChange);
    this.gameController.onSonarExausted.addListener((paramter) -> this.buttonDica.setEnabled(false));
    this.gameController.onSonarRestaured.addListener((paramter) -> this.buttonDica.setEnabled(true));
  }

  public void onGameTick(int seconds) {
    stopwatch.setText(TimeUtils.convertSecondsToFormattedText(seconds));
  }

  public void onGameStateChange(GameState value) {
    if (value != GameState.PLAYING) {
      return;
    }

    this.buttonStartGame.setEnabled(false);
    this.buttonDica.setEnabled(true);

    this.buttonClearGrid.setEnabled(false);

    for (var button : this.confirmationButtons.values()) {
      button.setEnabled(false);
    }
    for (var button : this.positionInputs.values()) {
      button.setEnabled(false);
    }
  }

  public void createPannels() {
    setLayout(new GridBagLayout());
    GridBagConstraints props = new GridBagConstraints();
    GridBagConstraints buttonPropieties = new GridBagConstraints();
    props.anchor = GridBagConstraints.PAGE_START;

    int vesselCount = 1;
    for (var vesselType : VesselTypes.values()) {
      // Image of the vessel
      GridBagConstraints imageProps = new GridBagConstraints();
      imageProps.anchor = GridBagConstraints.PAGE_START;
      imageProps.gridx = 0;
      imageProps.gridy = vesselCount;
      imageProps.weightx = 1;
      imageProps.weighty = 0.25;
      imageProps.gridwidth = 1;

      var imageLabel = new JLabel(new ImageIcon("src/resources/images/" + vesselType.getName() + ".png"));
      add(imageLabel, imageProps);

      // Text input of vessel
      var textInput = new JTextField();
      GridBagConstraints textProps = new GridBagConstraints();
      textProps.anchor = GridBagConstraints.PAGE_START;
      textInput.setPreferredSize(new java.awt.Dimension(25, 25));
      textProps.gridx = 1;
      textProps.gridy = vesselCount;
      textProps.weightx = 1;
      textProps.weighty = 0.25;
      textProps.gridwidth = 1;
      this.positionInputs.put(vesselType, textInput);
      add(textInput, textProps);

      // Button to confirm
      var confirmationButton = new JButton("OK");
      GridBagConstraints buttonProps = new GridBagConstraints();
      buttonProps.anchor = GridBagConstraints.PAGE_START;
      buttonProps.gridx = 3;
      buttonProps.gridy = vesselCount;
      buttonProps.weightx = 1;
      buttonProps.weighty = 0.25;
      buttonProps.gridwidth = 1;
      buttonPropieties.insets = new Insets(15, 15, 15, 15);
      confirmationButton.addActionListener(this);
      this.confirmationButtons.put(vesselType, confirmationButton);
      add(confirmationButton, buttonProps);

      vesselCount++;
    }

    stopwatch = new JLabel("00:00");
    props.anchor = GridBagConstraints.PAGE_END;
    props.gridx = 0;
    props.gridy = 6;
    props.gridwidth = 6;
    props.insets = new Insets(0, 15, 250, 15);
    add(stopwatch, props);

    buttonDica = new JButton("Dica");
    buttonPropieties.fill = GridBagConstraints.HORIZONTAL;
    buttonPropieties.anchor = GridBagConstraints.PAGE_END;
    buttonPropieties.gridx = 0;
    buttonPropieties.gridy = 6;
    buttonPropieties.gridwidth = 6;
    buttonPropieties.insets = new Insets(0, 15, 175, 15);
    add(buttonDica, buttonPropieties);

    buttonClearGrid = new JButton("Limpar tabuleiro");
    buttonPropieties.gridx = 0;
    buttonPropieties.gridy = 6;
    buttonPropieties.gridwidth = 6;
    buttonPropieties.insets = new Insets(0, 15, 136, 15);
    add(buttonClearGrid, buttonPropieties);

    buttonStartGame = new JButton("Jogar");
    buttonPropieties.ipady = 20;
    buttonPropieties.gridx = 0;
    buttonPropieties.gridy = 6;
    buttonPropieties.gridwidth = 4;
    buttonPropieties.insets = new Insets(0, 15, 75, 15);
    add(buttonStartGame, buttonPropieties);

    buttonSair = new JButton("Sair");
    buttonPropieties.ipady = 20;
    buttonPropieties.gridx = 0;
    buttonPropieties.gridy = 6;
    buttonPropieties.gridwidth = 4;
    buttonPropieties.insets = new Insets(0, 15, 15, 15);
    add(buttonSair, buttonPropieties);

    buttonClearGrid.addActionListener(this);
    buttonDica.addActionListener(this);
    buttonSair.addActionListener(this);
    buttonStartGame.addActionListener(this);

    this.isRandom = context.getUser().getIsRandom();

    var enableButtons = !this.isRandom;

    for (var button : this.confirmationButtons.values()) {
      button.setEnabled(enableButtons);
    }
    for (var input : this.positionInputs.values()) {
      input.setEnabled(enableButtons);
    }
    buttonStartGame.setEnabled(enableButtons);

  }

  private Vector2 parsePosition(String string) {
    // Garantimos que se o usuário informou uma letra minuscula, tratamos ela como
    // se fosse maiuscula
    string = string.toUpperCase();
    if (string.isEmpty()) {
      return null;
    }

    // Calcula o valor da letra, exempo A10 vai pegar o valor A e converter para um
    // numero
    // Pega o primeiro caracter da string que o usuário informou
    Character firstChar = Character.valueOf(string.charAt(0));
    // Se o usuário enviar uma letra que não existe no tabuleiro, não conseguimos
    // deduzir a posição
    if (!Constants.GRID_LETTERS.contains(firstChar)) {
      return null;
    }
    // Pega a posição correspondente da letra
    int letterValue = Constants.GRID_LETTERS.indexOf(firstChar);

    // Pega só a parte do numero da string, ou seja corta o primeiro char
    String numberString = string.substring(1);
    int numberValue;
    try {
      // Tenta transformar para um int
      numberValue = Integer.valueOf(numberString);
    } catch (NumberFormatException ex) {
      // E se não conseguir ( usuário mandou letra tipo AB) vai dar erro
      return null;
    }
    // Retorna a posição resolvida
    return new Vector2(numberValue - 1, letterValue);
  }

  @Override
  public void actionPerformed(ActionEvent e) {

    if (e.getSource() == buttonSair)
      new ExitConfirmationScreen(gameController);
    if (e.getSource() == buttonDica)
      gameController.setSelectedShotType(ShotType.SONAR);

    if (e.getSource() == buttonStartGame) {
      gameController.start(false);
    }

    for (var vesselType : VesselTypes.values()) {
      var confirmationButton = this.confirmationButtons.get(vesselType);
      if (e.getSource() == confirmationButton) {
        var textInput = this.positionInputs.get(vesselType);
        var position = parsePosition(textInput.getText());
        if (position == null) {
          new Span("Erro!", "Nao consegui entender a posicao! Certifique-se de enviar a letra e o numero. Exemplo: C9");
          return;
        }
        if (!gameController.getHuman().getPlayer().canVesselBePlacedAt(position, vesselType)) {
          new Span("Erro!", "Nao consigo colocar a embarcao nessa posicao! Ela ja esta ocupada ou fora do mapa!");
          return;
        }

        gameController.getHuman().getPlayer().placeVessel(position, context.getUser().getVesselByType(vesselType));
        confirmationButton.setEnabled(false);
        textInput.setEnabled(false);
        gameController.updateGrid();
      }
    }

    if (e.getSource() == buttonClearGrid) {
      for (var vesselType : VesselTypes.values()) {
        var confirmationButton = this.confirmationButtons.get(vesselType);
        var textInput = this.positionInputs.get(vesselType);
        confirmationButton.setEnabled(true);
        textInput.setEnabled(true);
        textInput.setText("");
        gameController.getHuman().getPlayer().getVesselByType(vesselType).setPlacedPosition(null);
        gameController.updateGrid();
      }
    }
  }
}