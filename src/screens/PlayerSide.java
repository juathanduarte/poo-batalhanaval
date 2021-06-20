package screens;

import logic.Constants;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import logic.ShotType;

public class PlayerSide extends JPanel implements ActionListener {

  private int i;

  private String str = null;
  private HashMap<ShotType, JButton> buttonsMap;

  private GameController gameController;
  private GridMaker grid;

  public PlayerSide(GameController gameController) {
    this.gameController = gameController;
    this.grid = gameController.getHuman().getGrid();
    this.buttonsMap = new HashMap<>();
    gameController.setButtonsMap(buttonsMap);

    this.gameController.onHumanShot.addListener(this::onHumanShot);
    this.gameController.onShotTypeChange.addListener(this::setActiveShotOnScreen);
    createPanels();
  }

  public void onHumanShot(ShotType type) {
    gameController.setLastShot(type);
    gameController.setButtonsMap(buttonsMap);

    if (type == ShotType.SHOT_PORTA_AVIAO) {
      this.buttonsMap.get(ShotType.SHOT_PORTA_AVIAO).setEnabled(false);
      var shotType = gameController.getContext().getUser().getNewShot();
      System.out.println(shotType);
      this.buttonsMap.get(shotType).setEnabled(true);
      setActiveShotOnScreen(shotType);
      gameController.setSelectedShotType(shotType);
    } else {
      var lastTurnUsed = this.gameController.getLastTimeUsedByShotType().get(ShotType.SHOT_PORTA_AVIAO);
      var currentTurn = this.gameController.getTurnCount();
      if (lastTurnUsed == null || lastTurnUsed + 2 <= currentTurn) {
        this.buttonsMap.get(ShotType.SHOT_PORTA_AVIAO).setEnabled(true);
      }
    }
  }

  public void setActiveShotOnScreen(ShotType activeShotType) {
    for (ShotType shotType : buttonsMap.keySet()) {
      var button = buttonsMap.get(shotType);
      if (shotType == activeShotType) {
        button.setBackground(new Color(0, 255, 255));
      } else {
        button.setBackground(new Color(200, 200, 200));
      }
    }
  }

  public void createPanels() {
    setLayout(new GridBagLayout());
    setBackground(new Color(0, 0, 255));

    GridBagConstraints props = new GridBagConstraints();
    GridBagConstraints propsShotButtons = new GridBagConstraints();

    JLabel titlePlayerSide = new JLabel("Selecione seu modo de tiro");
    titlePlayerSide.setFont(new Font("Tahoma", Font.BOLD, 15));
    titlePlayerSide.setHorizontalAlignment(SwingConstants.CENTER);
    titlePlayerSide.setVerticalAlignment(SwingConstants.NORTH);
    props.fill = GridBagConstraints.BOTH;
    props.gridx = 1;
    props.gridy = 0;
    props.insets = new Insets(15, 0, 0, 0);
    add(titlePlayerSide, props);

    JPanel topPannel = new JPanel();
    topPannel.setBackground(Color.BLUE);
    topPannel.setVisible(true);
    topPannel.setLayout(new GridBagLayout());
    props.gridy = 0;
    props.weightx = 1;
    props.weighty = 0.10;
    props.gridwidth = 3;
    add(topPannel, props);

    int xPosition = 1;
    for (ShotType shotType : ShotType.values()) {
      if (shotType.getImageName() == null) {
        continue;
      }
      var image = new ImageIcon("src/resources/images/" + shotType.getImageName());
      var button = new JButton(image);
      button.setPreferredSize(new java.awt.Dimension(50, 50));
      propsShotButtons.gridx = xPosition;
      propsShotButtons.gridy = 1;
      propsShotButtons.insets = new Insets(10, 15, 0, 15);
      topPannel.add(button, propsShotButtons);
      buttonsMap.put(shotType, button);

      button.addActionListener(this);
      xPosition++;
    }

    JPanel matrizXPanel = new JPanel();
    matrizXPanel.setLayout(new GridBagLayout());
    matrizXPanel.setBackground(new Color(0, 0, 255));
    props.fill = GridBagConstraints.BOTH;
    props.gridx = 1;
    props.gridy = 1;
    props.weightx = 1;
    props.weighty = 0;
    props.gridwidth = 1;
    props.insets = new Insets(0, 0, 0, 0);
    add(matrizXPanel, props);

    for (i = 0; i <= 9; i++) {
      JLabel matrizX = new JLabel(String.valueOf(i + 1));
      matrizX.setFont(new Font("Tahoma", Font.BOLD, 15));
      matrizX.setHorizontalAlignment(SwingConstants.CENTER);
      matrizX.setVerticalAlignment(SwingConstants.CENTER);
      props.gridx = i;
      props.gridy = 0;
      props.gridwidth = 1;
      props.insets = new Insets(0, 0, 0, 0);

      matrizXPanel.add(matrizX, props);
    }

    JPanel matrizYPanel = new JPanel();
    matrizYPanel.setLayout(new GridBagLayout());
    matrizYPanel.setBackground(new Color(0, 0, 255));
    props.fill = GridBagConstraints.BOTH;
    props.gridx = 0;
    props.gridy = 2;
    props.weightx = 0;
    props.weighty = 0;
    props.gridwidth = 1;
    props.insets = new Insets(0, 0, 0, 0);

    add(matrizYPanel, props);

    for (char i : Constants.GRID_LETTERS) {
      str = Character.toString(i);
      JLabel matrizY = new JLabel(str);
      matrizY.setFont(new Font("Tahoma", Font.BOLD, 15));
      matrizY.setBackground(Color.RED);
      matrizY.setHorizontalAlignment(SwingConstants.LEFT);
      matrizY.setVerticalAlignment(SwingConstants.CENTER);
      props.gridx = 0;
      props.gridy = i;
      props.weighty = 1;
      props.insets = new Insets(0, 0, 0, 0);
      matrizYPanel.add(matrizY, props);
    }

    props.fill = GridBagConstraints.BOTH;
    props.gridx = 1;
    props.gridy = 2;
    props.weighty = 0.75;
    props.weightx = 0;
    props.gridwidth = 4;
    props.insets = new Insets(5, 5, 15, 15);
    add(this.grid, props);

  }

  @Override
  public void actionPerformed(ActionEvent e) {

    for (ShotType shotType : buttonsMap.keySet()) {
      var button = buttonsMap.get(shotType);
      if (e.getSource() == button) {
        gameController.setSelectedShotType(shotType);
        return;
      }
    }
  }
}