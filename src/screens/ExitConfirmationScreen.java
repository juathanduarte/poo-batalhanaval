package screens;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingConstants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.GridLayout;

public class ExitConfirmationScreen extends JFrame implements ActionListener {

  private JButton restartGame;
  private JButton newGame;
  private JButton quitGame;

  private GameController gameController;

  public ExitConfirmationScreen(GameController gameController) {
    super();
    setTitle("Sair");
    setSize(200, 200);
    setVisible(true);
    setLocationRelativeTo(null);
    setLayout(new GridLayout(3, 0));

    this.gameController = gameController;

    restartGame = new JButton("Reiniciar jogo");
    restartGame.setHorizontalAlignment(SwingConstants.CENTER);
    add(restartGame);

    newGame = new JButton("Novo jogo");
    newGame.setHorizontalAlignment(SwingConstants.CENTER);
    add(newGame);

    quitGame = new JButton("Sair do jogo");
    quitGame.setHorizontalAlignment(SwingConstants.CENTER);
    add(quitGame);

    restartGame.addActionListener(this);
    newGame.addActionListener(this);
    quitGame.addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {

    if (e.getSource() == quitGame) {
      System.exit(0);
    }
    if (e.getSource() == newGame) {
      this.dispose();
      Main.restartGame();
    }

    if (e.getSource() == restartGame) {
      gameController.resetGame();
      gameController.resetBoard();
      gameController.setGameState();

      this.dispose();
    }
  }
}