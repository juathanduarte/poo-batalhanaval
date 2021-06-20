package screens;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import logic.Context;

public class MainScreen extends JFrame implements ActionListener {

  private JFrame frame = new JFrame();

  private JButton buttonRandomGame;
  private JButton buttonCreateGame;
  private JTextField playerName;
  private String spanTitle;
  private String spanError;
  private GameScreen gameScreen;

  private Context context = new Context();

  public GameScreen getGameScreen() {
    return gameScreen;
  }

  public void constructor() {
    frame.setTitle("Batalha Naval - Juathan, Henrique & Leonardo");
    frame.setSize(780, 350);
    frame.setResizable(false);
    frame.setMinimumSize(new Dimension(780, 350));
    frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);

    // Divide a tela principal em 2 linnhas
    frame.setLayout(new GridLayout(2, 0));

    JPanel upperPanel = new JPanel();
    upperPanel.setSize(760, 200);
    upperPanel.setLayout(new GridLayout(3, 0));
    frame.add(upperPanel);

    JPanel insertNamePanel = new JPanel();

    JPanel bottomPanel = new JPanel(); // Grid para encapsular a parte inferior
      bottomPanel.setLayout(new GridLayout(0, 2)); // Divide a parte inferior em 2 colunas
    frame.add(bottomPanel);

    JLabel textInitial = new JLabel("Bem Vindo ao Batalha Naval!");
      textInitial.setFont(new Font("Tahoma", Font.BOLD, 40));
      textInitial.setHorizontalAlignment(SwingConstants.CENTER);
      textInitial.setVerticalAlignment(SwingConstants.CENTER);
    upperPanel.add(textInitial);

    JLabel textSubtitle = new JLabel("Integrantes: Juathan Duarte, Henrique Garcia & Leonardo Madruga");
      textSubtitle.setFont(new Font("Tahoma", Font.ITALIC, 20));
      textSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
      textSubtitle.setVerticalAlignment(SwingConstants.CENTER);
    upperPanel.add(textSubtitle);

    JLabel nomePlayer = new JLabel("Insira seu nome:");
      nomePlayer.setFont(new Font("Tahoma", Font.BOLD, 24));
      nomePlayer.setHorizontalAlignment(SwingConstants.CENTER);
      nomePlayer.setVerticalAlignment(SwingConstants.CENTER);
    insertNamePanel.add(nomePlayer);
    upperPanel.add(insertNamePanel);

    playerName = new JTextField();
      playerName.setColumns(20);
      playerName.addActionListener(this);
    insertNamePanel.add(playerName);

    buttonCreateGame = new JButton("Definir seu jogo");
      buttonCreateGame.addActionListener(this);
    bottomPanel.add(buttonCreateGame);

    buttonRandomGame = new JButton("Jogo aleatorio");
      buttonRandomGame.addActionListener(this);
    bottomPanel.add(buttonRandomGame);

    frame.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {

    if (playerName.getText().length() == 0) {
      spanTitle = "O nome está incorreto!";
      spanError = "Insira um nome válido!";
      new Span(spanTitle, spanError);
    } else {
      GameController gameController = new GameController(context);
      context.setName(playerName.getText());
      gameScreen = new GameScreen(gameController);

      boolean isRandomGame = false;
      if (e.getSource() == buttonCreateGame) {
        isRandomGame = false;
      }
      if (e.getSource() == buttonRandomGame) {
        isRandomGame = true;
      }
      context.getUser().setIsRandom(isRandomGame);

      gameScreen.createPanels();
      frame.dispose();
    }
  }
}