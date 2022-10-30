package com.lucaswarwick02;

public class ModelState {
    private final int time;

    private final int susceptible;
    private final int infected;
    private final int removed;

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

    public static String getCSVHeaders () {
        return "Time|Susceptible|Infected|Removed";
    }

    public String getCSVRow () {
        return time + "|" + susceptible + "|" + infected + "|" + removed;
    }
}
