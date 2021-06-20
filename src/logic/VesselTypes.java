package logic;

public enum VesselTypes {
    
    CACA("Caca", 2, ShotType.SHOT_CACA),
    PORTA_AVIAO("PortaAviao", 4, ShotType.SHOT_PORTA_AVIAO),
    SUBMARINO("Submarino", 2, ShotType.SHOT_SUBMARINO),
    ESCOLTA("Escolta", 3, ShotType.SHOT_ESCOLTA);

    private String name;
    private int size;
    private ShotType shotType;

     private VesselTypes(String name,int size, ShotType shotType) {
        this.name = name;
        this.size = size;
        this.shotType = shotType;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
      return size;
    }
    
    public ShotType getShotType() {
      return this.shotType;
    }
}