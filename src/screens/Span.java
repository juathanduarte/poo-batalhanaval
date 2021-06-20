package screens;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.GridLayout;

public class Span extends JFrame {
  String error;
  String title;

  public Span(String title, String error) {
    setTitle("Erro");
    setSize(800, 200);
    setVisible(true);
    setLocationRelativeTo(null);
    setLayout(new GridLayout(2, 0));

    this.error = error;
    this.title = title;

    JLabel titleSpan = new JLabel(title);
    titleSpan.setHorizontalAlignment(SwingConstants.CENTER);
    add(titleSpan);

    JLabel errorMessage = new JLabel(error);
    errorMessage.setHorizontalAlignment(SwingConstants.CENTER);
    add(errorMessage);
  }
}
