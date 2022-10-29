package com.lucaswarwick02;

public class ModelState {
    private int time;

    private int susceptible;
    private int infected;
    private int removed;

    public ModelState (int time, int susceptible, int infected, int removed) {
        this.time = time;
        this.susceptible = susceptible;
        this.infected = infected;
        this.removed = removed;
    }

    public int getSusceptible() {
        return susceptible;
    }

    public int getInfected() {
        return infected;
    }

    public int getRemoved() {
        return removed;
    }

    @Override
    public String toString() {
        return "ModelState(susceptible=" + susceptible + ", infected=" + infected + ", removed=" + removed + ")";
    }
}
