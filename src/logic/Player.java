package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Player {

    private List<Vessel> vessels;
    private List<Shot> shots;

    private String name;
    private boolean isRandom;
    private Context context;
    private boolean cpu;

    public Player(String name, Context context) {
        this.name = name;
        this.context = context;
        this.shots = new ArrayList<>();

        resetVessels();
    }

    public void resetVessels() {
        this.vessels = new ArrayList<>();
        this.vessels.addAll(Arrays.asList(new Vessel(VesselTypes.CACA), new Vessel(VesselTypes.SUBMARINO),
                new Vessel(VesselTypes.ESCOLTA), new Vessel(VesselTypes.PORTA_AVIAO)));
    }

    public void clear() {
        this.resetVessels();
        this.shots.clear();
    }

    public void clearShots() {
        this.shots.clear();
    }

    public boolean isCpu() {
        return cpu;
    }

    public void setCpu(boolean cpu) {
        this.cpu = cpu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vessel getVesselAt(Vector2 position) {
        for (Vessel vessel : this.vessels) {
            if (vessel.isInHitbox(position)) {
                return vessel;
            }
        }
        return null;
    }

    public Shot getShotAt(Vector2 position) {
        for (Shot shot : this.shots) {
            if (shot.isInHitbox(position)) {
                return shot;
            }
        }
        return null;
    }

    public Vessel getVesselByType(VesselTypes type) {
        for (Vessel vessel : vessels) {
            if (vessel.getType() == type) {
                return vessel;
            }
        }
        return null;
    }

    public void placeVessel(Vector2 position, Vessel vessel) {

        vessel.setPlacedPosition(position);
    }

    public boolean canVesselBePlacedAt(Vector2 position, VesselTypes type) {

        // Check if location is inside grid
        if (position.getX() + (type.getSize() - 1) > 9 || position.getX() < 0 || position.getY() > 9
                || position.getY() < 0)
            return false;

        int placedPieces = 0;
        int col = position.getX();
        int row = position.getY();
        while (placedPieces < type.getSize()) {
            if (getVesselAt(new Vector2(col, row)) != null)
                return false;

            col++;
            placedPieces++;
        }
        return true;
    }

    public boolean isRandom() {
        return isRandom;
    }

    public void setIsRandom(boolean isRandom) {
        this.isRandom = isRandom;
    }

    public boolean getIsRandom() {
        return this.isRandom;
    }

    public void placeRandomVessels() {

        Random random = new Random();
        for (Vessel vessel : this.vessels) {
            int col = 0, row = 0;
            boolean space = false;
            do {
                do {
                    col = random.nextInt(10);
                    row = random.nextInt(10);
                } while (vessel.getType().getSize() + col >= 10);

                space = canVesselBePlacedAt(new Vector2(col, row), vessel.getType());
            } while (!space);
            placeVessel(new Vector2(col, row), vessel);
        }
    }

    public boolean takeShot(Vector2 position, ShotType type) {

        var shot = new Shot(position, type);
        this.shots.add(shot);

        for (Vector2 hitPosition : shot.getHitPositions()) {
            var vessel = this.getVesselAt(hitPosition);
            if (vessel != null) {
                vessel.hit(hitPosition);
            }
        }

        return false;
    }

    public boolean isAlive() {
        for (Vessel vessel : this.vessels)
            if (vessel.isAlive())
                return true;

        return false;
    }

    public boolean sonar(Vector2 position) {
        for (Vessel vessel : this.vessels) {
            if (vessel.isInCross(position))
                return true;
        }
        return false;
    }

    public void printVessels() {
        System.out.println(vessels);
    }

    public Vector2 generateComputerShotPosition() {
        var rng = context.getRng();
        List<Vector2> availableShoots = new ArrayList<>();

        for (int col = 0; col < 10; col++) {
            for (int row = 0; row < 10; row++) {
                Vector2 position = new Vector2(col, row);
                if (this.getShotAt(position) == null)
                    availableShoots.add(position);
            }
        }

        return availableShoots.get(rng.nextInt(availableShoots.size()));

    }

    public ShotType getAvailableShotType() {
        var rng = context.getRng();

        List<ShotType> availableShoots = new ArrayList<>();
        for (Vessel vessel : this.vessels) {
            if (vessel.isAlive())
                availableShoots.add(vessel.getType().getShotType());
        }

        if (availableShoots.isEmpty())
            return null;

        return availableShoots.get(rng.nextInt(availableShoots.size()));
    }

    public ShotType getNewShot() {
        var rng = context.getRng();
        if (this.vessels.size() == 1 && this.vessels.get(0).getType() == VesselTypes.PORTA_AVIAO) {
            return this.vessels.get(0).getType().getShotType();
        } else {
            List<ShotType> availableShoots = new ArrayList<>();
            for (Vessel vessel : this.vessels) {
                if (vessel.isAlive() && vessel.getType() != VesselTypes.PORTA_AVIAO)
                    availableShoots.add(vessel.getType().getShotType());
            }

            if (availableShoots.isEmpty())
                return null;
            return availableShoots.get(rng.nextInt(availableShoots.size()));
        }

    }

    public List<ShotType> getAliveVessels() {

        List<ShotType> availableVessels = new ArrayList<>();
        for (Vessel vessel : this.vessels) {
            if (vessel.isAlive())
                availableVessels.add(vessel.getType().getShotType());
        }

        if (availableVessels.isEmpty())
            return null;
        return availableVessels;
    }

    public void resetPlayer() {
        for (Vessel vessel : vessels) {
            vessel.resetLife();
        }
        this.shots.clear();
    }

}