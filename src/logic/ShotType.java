package logic;

public enum ShotType {
    SHOT_PORTA_AVIAO(
        "PortaAviao50x50.png",
        new Vector2(0,0)
     ),
    SHOT_SUBMARINO(
        "Submarino50x50.png",
        new Vector2(0,0)
     ),
    SHOT_ESCOLTA(
        "Escolta50x50.png",
        new Vector2(0,0),
        new Vector2(1,0)
    ),
    SHOT_CACA(
        "Caca50x50.png",
        new Vector2(-1,0),
        new Vector2(1,0),
        new Vector2(0,0),
        new Vector2(0,1),
        new Vector2(0,-1)
    ),
    SONAR(null);
    
    private String imageName;
    private Vector2[] pattern;

    private ShotType(String imageName,Vector2... pattern){
         this.pattern = pattern;
         this.imageName = imageName;
    }

    public String getImageName() {
    return imageName;
    }
 
    public Vector2[] getPattern() {
        return pattern;
    }
}
