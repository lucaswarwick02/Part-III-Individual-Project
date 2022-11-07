package com.lucaswarwick02.Components;

public class Node {
    /**
     * Compartmental model states
     */
    enum State {
        Susceptible,
        Infected,
        Recovered
    }

    private State state; // Compartmental model state
    private final int ID; // Unique identifier
    private int degree; // Number of adjacent neighbours

    /**
     * Create a Susceptible Node
     * @param ID Unique identifier
     */
    public Node (int ID) {
        this.ID = ID;
        this.state = State.Susceptible;
    }

    /**
     * Get the ID of the Node
     * @return Node's ID
     */
    public int getID () {
        return this.ID;
    }

    /**
     * Get the state of the Node
     * @return Node's state
     */
    public State getState () {
        return this.state;
    }

    /**
     * Get the degree of the Node (number of neighbours)
     * @return Node's degree
     */
    public int getDegree () {
        return this.degree;
    }

    /**
     * Set the state of the Node (compartmental)
     * @param newState Compartment state
     */
    public void setState (State newState) {
        this.state = newState;
    }

    /**
     * Set the degree of the Node
     * @param newDegree
     */
    public void setDegree (int newDegree) {
        this.degree = newDegree;
    }
}
