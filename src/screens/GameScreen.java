package screens;

import java.awt.GridLayout;

import javax.swing.JFrame;

public class GameScreen extends JFrame {
  private GameController gameController;
  PlayerSide playerPanel;
  ComputerSide computerPanel;
  MiddlePanel middlePanel;

  public GameScreen(GameController gameController) {
    this.gameController = gameController;
  }

  public void createPanels() {

    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setTitle("Que comecem os jogos!!");
    this.setSize(1411, 686);
    this.setLocationRelativeTo(null);
    this.setVisible(true);
    this.setLayout(new GridLayout(0, 3));

    playerPanel = new PlayerSide(gameController);
    computerPanel = new ComputerSide(gameController);
    middlePanel = new MiddlePanel(gameController);

    this.add(playerPanel);
    this.add(middlePanel);
    this.add(computerPanel);

    if (gameController.getHuman().getPlayer().isRandom()) {
      gameController.start(true);
    }

    gameController.updateGrid();

  }

}