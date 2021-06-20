package logic;

import java.util.Random;

public class Context {

    private Player user;
    private Player computer;
    private Random rng;
    public boolean turn = true;

    public Context() {
        this.rng = new Random();
        this.user = new Player("", this);
        this.computer = new Player("Computador", this);
        this.computer.setCpu(true);
    }

    public void clear() {
        this.user.clear();
        this.computer.clear();
    }

    public Player getUser() {
        return this.user;
    }

    public Player getComputer() {
        return this.computer;
    }

    public String getName() {
        return user.getName();
    }

    public void setName(String name) {
        user.setName(name);
    }

    public Random getRng() {
        return rng;
    }

}