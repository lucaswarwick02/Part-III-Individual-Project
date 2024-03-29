package com.lucaswarwick02.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Information on an individual in a network
 */
public class Node {

    Random random = new Random();

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

    private State state; // Compartmental model state

    public AgeBracket ageBracket = AgeBracket.NONE;

    public final int ID; // Unique identifier

    public List<Node> neighbours; // List of the connected Nodes

    public int stubs; // Used for generating the network using the Configuration Model

    /**
     * Get the state of the Node
     * 
     * @return State of the Node
     */
    public State getState() {
        return this.state;
    }

    /**
     * Set the state of the Node
     */
    public void setState(State state) {
        this.state = state;
    }

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

    /**
     * Convert a State to it's associated column name
     * 
     * @param state State
     * @return Column name for the State
     */
    public static String stateToString(Node.State state) {
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

    /**
     * Get a list of all the State's
     * 
     * @return List of States
     */
    public static Node.State[] getAllStates() {
        return new Node.State[] { Node.State.SUSCEPTIBLE, Node.State.INFECTED, Node.State.RECOVERED,
                Node.State.VACCINATED, Node.State.HOSPITALISED, Node.State.DEAD };
    }
}
