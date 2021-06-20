package screens;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import logic.Player;
import logic.Vector2;

public class GridMaker extends JPanel implements ActionListener {

  private static final Color WATER_COLOR = new Color(120, 195, 255);
  private static final Color PLAYER_COLOR = new Color(108, 191, 102);
  private static final Color WALLHACK_COLOR = new Color(155, 61, 196);
  private static final Color SHOT_COLOR = new Color(188, 78, 78);
  private static final Color SHOT_ON_WATER = new Color(0, 0, 255);
  private static final boolean WALL_HACK = false;

  private JButton[][] gridButton = new JButton[10][10];
  private int size, counter;
  private String vesselName, url;

  private GameController gameController;
  private Player player;

  public GridMaker(GameController gameController, Player player) {
    this.gameController = gameController;
    this.player = player;
    this.setup();
  }

  public void updateButton(int col, int row) {
    Vector2 position = new Vector2(col, row);
    var button = gridButton[col][row];
    var vessel = this.player.getVesselAt(position);
    var isVessel = vessel != null;
    var isShot = this.player.getShotAt(position) != null;

    Color color = WATER_COLOR;

    if (isVessel && (!this.player.isCpu() || isShot)) {
      color = PLAYER_COLOR;
      var type = vessel.getType();
      this.size = type.getSize();
      this.vesselName = type.getName();
      this.counter = vessel.getVesselFraction(position);

      this.url = "src/resources/images/" + this.vesselName + this.counter + ".png";
      button.setIcon(new ImageIcon(url));
      button.setPreferredSize(new java.awt.Dimension(20, 7));
    } else {
      button.setIcon(null);
    }

    if (this.player.isCpu()) {
      if (WALL_HACK && vessel != null) {
        color = WALLHACK_COLOR;
      }
    }
    if (isShot) {
      if (isVessel)
        color = SHOT_COLOR;
      else
        color = SHOT_ON_WATER;
    }
    button.setBackground(color);
  }

  public void setup() {
    setVisible(true);
    setLayout(new GridBagLayout());
    addAllToGrid();
  }

  public void addAllToGrid() {
    for (int row = 0; row < 10; row++) {
      for (int col = 0; col < 10; col++) {
        GridBagConstraints props = new GridBagConstraints();
        var button = new JButton("");
        gridButton[col][row] = button;
        button.addActionListener(this);
        button.setBackground(WATER_COLOR);
        props.gridx = col;
        props.gridy = row;
        props.weightx = 1;
        props.weighty = 1;
        props.fill = GridBagConstraints.BOTH;
        add(button, props);
      }
    }
  }

  public void updateGrid() {
    for (int row = 0; row < 10; row++) {
      for (int col = 0; col < 10; col++) {
        this.updateButton(col, row);
      }
    }
  }

  public void resetBoard() {
    for (int row = 0; row < 10; row++) {
      for (int col = 0; col < 10; col++) {
        gridButton[col][row].setBackground(WATER_COLOR);
        gridButton[col][row].setEnabled(true);
      }
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (!this.player.isCpu()) {
      return;
    }
    for (int row = 0; row < 10; row++) {
      for (int col = 0; col < 10; col++) {
        if (e.getSource() == gridButton[col][row]) {
          this.gameController.playerClicked(new Vector2(col, row));
        }
      }
    }
  }
}