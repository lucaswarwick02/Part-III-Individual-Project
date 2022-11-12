package com.lucaswarwick02.components;

import java.util.ArrayList;
import java.util.List;

public class Node {
    /**
     * Compartmental model states
     */
    public enum State {
        Susceptible,
        Infected,
        Recovered,
        Vaccinated
    }

    public State state; // Compartmental model state
    public final int ID; // Unique identifier

    public List<Node> neighbours;

    /**
     * Create a Susceptible Node
     * @param ID Unique identifier
     */
    public Node (int ID) {
        this.ID = ID;
        this.state = State.Susceptible;
        neighbours = new ArrayList<>();
    }

    public int getDegree () {
        return this.neighbours.size();
    }
}
