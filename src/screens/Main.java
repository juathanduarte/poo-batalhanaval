package screens;

public class Main {

  public static MainScreen activeScreen = null;

  public static void main(String[] args) {
    restartGame();
  }

  public static void restartGame() {
    if (activeScreen != null) {
      activeScreen.setVisible(false);
      activeScreen.dispose();
      if (activeScreen.getGameScreen() != null)
        activeScreen.getGameScreen().setVisible(false);
      activeScreen.getGameScreen().dispose();
    }
    activeScreen = new MainScreen();
    activeScreen.constructor();
  }
}