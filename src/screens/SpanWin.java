package screens;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import utils.TimeUtils;

public class SpanWin extends JFrame {
  GameController gameController;

  public SpanWin(GameController gameController) {
    setTitle("Parabens");
    setSize(400, 400);
    setVisible(true);
    setLocationRelativeTo(null);
    var leaderboard = gameController.getScoreManager().topScore(15);
    setLayout(new GridBagLayout());
    GridBagConstraints props = new GridBagConstraints();

    JLabel titleSpan = new JLabel("Parabens!!!!");
    titleSpan.setHorizontalAlignment(SwingConstants.CENTER);
    titleSpan.setVerticalAlignment(SwingConstants.NORTH);
    props.gridx = 0;
    props.gridy = 0;
    props.weighty = 0.1;
    add(titleSpan, props);

    JLabel message = new JLabel("Voce Ganhou!!!!!! Seu tempo foi de: "
        + TimeUtils.convertSecondsToFormattedText(gameController.getGameTimer().getCount()));
    message.setHorizontalAlignment(SwingConstants.CENTER);
    message.setVerticalAlignment(SwingConstants.NORTH);
    props.weighty = 0.1;
    props.gridx = 0;
    props.gridy = 1;
    add(message, props);

    int counter = 1;
    String ranking = "";
    for (var rank : leaderboard) {
      ranking += (counter + ": " + rank.getName() + " - " + TimeUtils.convertSecondsToFormattedText(rank.getTime())
          + "<br/>");
      counter++;
    }
    JLabel rankLabel = new JLabel("<html>" + ranking + "</html>");
    props.weighty = 0.8;
    props.gridx = 0;
    props.gridy = 2;
    rankLabel.setHorizontalAlignment(SwingConstants.CENTER);
    rankLabel.setVerticalAlignment(SwingConstants.NORTH);
    add(rankLabel, props);

  }
}
