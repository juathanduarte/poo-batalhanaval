package screens;

import logic.Player;

public class PlayerController {

    private Player player;
    private GridMaker grid;

    public PlayerController(Player player, GridMaker grid) {
        this.player = player;
        this.grid = grid;
    }

    public Player getPlayer() {
        return player;
    }

    public GridMaker getGrid() {
        return grid;
    }
}