package screens;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;

import logic.Constants;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ComputerSide extends JPanel {

  private int i;
  private String empty = "";

  private String str = null;

  protected GridMaker grid;

  private GameController gameController;

  public ComputerSide(GameController gameController) {
    this.gameController = gameController;
    this.grid = gameController.getComputer().getGrid();
    constructor();
  }

  public void constructor() {
    setLayout(new GridBagLayout());
    setBackground(new Color(255, 0, 0));
    GridBagConstraints props = new GridBagConstraints();

    JLabel tittleComputerSide = new JLabel("Adversario");
    tittleComputerSide.setFont(new Font("Tahoma", Font.BOLD, 15));
    tittleComputerSide.setHorizontalAlignment(SwingConstants.CENTER);
    tittleComputerSide.setVerticalAlignment(SwingConstants.CENTER);
    props.fill = GridBagConstraints.BOTH;
    props.gridx = 1;
    props.gridy = 0;
    props.weightx = 1;
    props.weighty = 0.25;

    add(tittleComputerSide, props);

    JPanel matrizXPanel = new JPanel();
    matrizXPanel.setLayout(new GridBagLayout());
    matrizXPanel.setBackground(new Color(255, 0, 0));
    props.gridx = 1;
    props.gridy = 1;
    props.weightx = 1;
    props.weighty = 0;
    props.insets = new Insets(0, 0, 0, 15);
    add(matrizXPanel, props);

    for (i = 0; i <= 9; i++) {
      JLabel matrizX = new JLabel(empty + (i + 1));
      matrizX.setFont(new Font("Tahoma", Font.BOLD, 15));
      matrizX.setHorizontalAlignment(SwingConstants.CENTER);
      matrizX.setVerticalAlignment(SwingConstants.CENTER);
      props.gridx = i;
      props.gridy = 0;
      props.insets = new Insets(0, 0, 0, 0);

      matrizXPanel.add(matrizX, props);
    }

    JPanel matrizYPanel = new JPanel();
    matrizYPanel.setLayout(new GridBagLayout());
    matrizYPanel.setBackground(new Color(255, 0, 0));
    props.gridx = 0;
    props.gridy = 2;
    props.weightx = 0;
    props.weighty = 0;
    props.insets = new Insets(5, 0, 15, 0);
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
      props.insets = new Insets(0, 5, 0, 5);

      matrizYPanel.add(matrizY, props);
    }

    props.fill = GridBagConstraints.BOTH;
    props.gridx = 1;
    props.gridy = 2;
    props.weighty = 0.75;
    props.weightx = 0;
    props.insets = new Insets(5, 0, 15, 15);

    add(this.grid, props);
  }
}