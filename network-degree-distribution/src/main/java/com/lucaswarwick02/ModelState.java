package com.lucaswarwick02;

public class ModelState {
    private final int time;

    private final int susceptible;
    private final int infected;
    private final int removed;

    private final int cumulativeInfected;

    public ModelState(int time, int susceptible, int infected, int removed, int cumulativeInfected) {
        this.time = time;
        this.susceptible = susceptible;
        this.infected = infected;
        this.removed = removed;
        this.cumulativeInfected = cumulativeInfected;
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

    public int getCumulativeInfected() {
        return cumulativeInfected;
    }

    public static String getCSVHeaders() {
        return "Time,Susceptible,Infected,Removed,CumulativeInfected";
    }

    public String getCSVRow() {
        return time + "," + susceptible + "," + infected + "," + removed + ", " + cumulativeInfected;
    }
}
