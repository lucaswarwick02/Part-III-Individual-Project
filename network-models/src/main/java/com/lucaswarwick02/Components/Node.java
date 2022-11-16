package com.lucaswarwick02.components;

import java.util.ArrayList;
import java.util.List;

/**
 * Information on an individual in a network
 */
public class Node {

    /**
     * Compartmental model states
     */
    public enum State {
        SUSCEPTIBLE,
        INFECTED,
        RECOVERED,
        VACCINATED
    }

    public State state; // Compartmental model state
    public final int ID; // Unique identifier

    public List<Node> neighbours;

    public int stubs;

    /**
     * Create a Susceptible Node
     * @param ID Unique identifier
     */
    public Node (int ID) {
        this.ID = ID;
        this.state = State.SUSCEPTIBLE;
        neighbours = new ArrayList<>();
    }

    /**
     * Get the number of connections the Node has
     * @return Number of connections
     */
    public int getDegree () {
        return this.neighbours.size();
    }
}
