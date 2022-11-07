package com.lucaswarwick02.Components;

public class Node {
    enum State {
        Susceptible,
        Infected,
        Recovered
    }

    private State state;
    private final int ID;
    private int degree;

    public Node (int ID) {
        this.ID = ID;
        this.state = State.Susceptible;
    }

    public int getID () {
        return this.ID;
    }

    public State getState () {
        return this.state;
    }

    public int getDegree () {
        return this.degree;
    }

    public void setState (State newState) {
        this.state = newState;
    }

    public void setDegree (int newDegree) {
        this.degree = newDegree;
    }
}
