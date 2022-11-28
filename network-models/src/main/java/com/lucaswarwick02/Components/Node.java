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
        HOSPITALISED,
        RECOVERED,
        VACCINATED,
        DEAD
    }

    public State state; // Compartmental model state
    public final int ID; // Unique identifier

    public List<Node> neighbours;

    public int stubs;

    /**
     * Create a Susceptible Node
     * 
     * @param ID Unique identifier
     */
    public Node(int ID) {
        this.ID = ID;
        this.state = State.SUSCEPTIBLE;
        neighbours = new ArrayList<>();
    }

    /**
     * Get the number of connections the Node has
     * 
     * @return Number of connections
     */
    public int getDegree() {
        return this.neighbours.size();
    }

    public static String StateToString(Node.State state) {
        switch (state) {
            case SUSCEPTIBLE:
                return "Susceptible";
            case INFECTED:
                return "Infected";
            case RECOVERED:
                return "Recovered";
            case VACCINATED:
                return "Vaccinated";
            case HOSPITALISED:
                return "Hospitalised";
            case DEAD:
                return "Dead";
            default:
                return "INVALID_STATE";
        }
    }

    public static Node.State[] getAllStates() {
        return new Node.State[] { Node.State.SUSCEPTIBLE, Node.State.INFECTED, Node.State.RECOVERED,
                Node.State.VACCINATED, Node.State.HOSPITALISED, Node.State.DEAD };
    }
}
