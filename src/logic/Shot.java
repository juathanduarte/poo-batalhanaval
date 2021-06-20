package logic;

import java.util.ArrayList;
import java.util.List;

public class Shot {

    private Vector2 position;
    private ShotType type;
    private List<Vector2> hitPositions;

    public Shot(Vector2 position, ShotType type) {
        this.position = position;
        this.type = type;
        hitPositions = new ArrayList<>();
        for (Vector2 patternPosition : this.type.getPattern()) {
            hitPositions.add(this.position.add(patternPosition));
        }
    }

    public boolean isInHitbox(Vector2 position) {
        if (hitPositions.isEmpty())
            return false;
        for (Vector2 hitPosition : hitPositions) {
            if (hitPosition.equals(position)) {
                return true;
            }
        }
        return false;
    }

    public List<Vector2> getHitPositions() {
        return hitPositions;
    }

}