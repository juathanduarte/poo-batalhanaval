package logic;

import java.util.ArrayList;
import java.util.List;

public class Vessel {

  private Vector2 placedPosition;
  private VesselTypes type;
  private int life;
  private List<Vector2> shotsTaken;

  public Vessel(VesselTypes type) {
    this.type = type;
    this.life = type.getSize();
    this.shotsTaken = new ArrayList<Vector2>();
  }

  public VesselTypes getType() {
    return this.type;
  }

  public boolean isInHitbox(Vector2 hit) {

    if (this.placedPosition == null)
      return false;
    int startX = this.placedPosition.getX();
    int endX = startX + (type.getSize() - 1);

    int startY = this.placedPosition.getY();
    int endY = startY;

    return startX <= hit.getX() && hit.getX() <= endX && startY <= hit.getY() && hit.getY() <= endY;
  }

  /**
   * Dada uma posição (hit) retorna qual parte da embarcação deve ser renderizada
   * por exemplo, embarcação com tamanho 4, vai retornar um número de 1 a 4
   */
  public int getVesselFraction(Vector2 hit) {
    int startX = this.placedPosition.getX();
    int hitX = hit.getX();

    return (hitX - startX) + 1;
  }

  public void setPlacedPosition(Vector2 position) {
    this.placedPosition = position;
  }

  public boolean isPlaced() {
    return placedPosition != null;
  }

  public void hit(Vector2 position) {
    if (!shotsTaken.contains(position)) {
      shotsTaken.add(position);
      this.life--;
    }
  }

  public boolean isAlive() {
    return this.life > 0;
  }

  public void resetLife() {
    this.shotsTaken.clear();
    this.life = this.type.getSize();
  }

  public int getLife() {
    return life;
  }

  public boolean isInCross(Vector2 hit) {

    if (this.placedPosition == null)
      return false;
    int startX = this.placedPosition.getX();
    int endX = startX + (type.getSize() - 1);

    int startY = this.placedPosition.getY();
    int endY = startY;

    return startX <= hit.getX() && hit.getX() <= endX || startY <= hit.getY() && hit.getY() <= endY;
  }
}