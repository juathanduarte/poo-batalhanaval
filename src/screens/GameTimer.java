package screens;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

public class GameTimer {

  private GameController gameController;
  Timer timer;
  long start;

  int count = 0;

  JLabel stopwatch;

  public GameTimer(GameController gameController) {
    this.gameController = gameController;
  }

  public void start() {
    this.timer = new Timer();
    this.start = System.currentTimeMillis();
    this.timer.scheduleAtFixedRate(new TimerTask() {

      @Override
      public void run() {
        tick();
      }
    }, 1000, 1000);
  }

  public void reset() {
    this.count = 0;
    this.stop();
    this.start();
  }

  public void stop() {
    if (timer == null)
      return;
    this.timer.cancel();
    timer.purge();

    this.timer = null;
  }

  public void tick() {
    count++; // A cada 1 segundo, o contador é incrementado.
    gameController.onGameTimerTick.emit(count);
  }

  public int getCount() {
    return this.count;
  }

}