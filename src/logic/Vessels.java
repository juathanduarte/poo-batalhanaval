package logic;
public class Vessels {
  private String name;
  private int size;
  // private String url;
  private int life;
  private boolean isPlaced;


  public Vessels(String name, int size) {
    this.name = name;
    this.size = size;
    this.life = size;
    // this.url = name + ".png";
    this.isPlaced = false;
  }

  public String getName() {
    return name;
  }

  public int getSize() {
    return this.size;
  }

  // public String getUrl() {
  //   return url;
  // }

  public boolean isisPlaced() {
    return isPlaced;
  }

  public void setisPlaced(boolean isPlaced) {
    this.isPlaced = isPlaced;
  }

  public void hit() {
    this.life--;

  }
}