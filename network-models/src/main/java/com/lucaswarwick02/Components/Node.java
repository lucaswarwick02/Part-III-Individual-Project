package com.lucaswarwick02.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Information on an individual in a network
 */
public class Node implements Comparable<Node> {

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

    public enum AgeBracket {
        BELOW_NINE, // 0 - 9
        TEN_TO_NINETEEN, // 10 - 19
        TWENTY_TO_THIRTYFOUR, // 20 - 34
        THIRTYFIVE_TO_FOURTYNINE, // 35 - 49
        FIFTY_TO_SIXTYFOUR, // 50 - 64
        SIXTYFIVE_TO_SEVENTYNINE, // 65 - 79
        ABOVE_EIGHTY // 80+
    }
    public AgeBracket ageBracket;

    public final int ID; // Unique identifier
    
    public List<Node> neighbours; // List of the connected Nodes

    public int stubs; // Used for generating the network using the Configuration Model

    public State getState () {
        return this.state;
    }

    public void setState (State state) {
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
     * @return List of States
     */
    public static Node.State[] getAllStates() {
        return new Node.State[] { Node.State.SUSCEPTIBLE, Node.State.INFECTED, Node.State.RECOVERED,
                Node.State.VACCINATED, Node.State.HOSPITALISED, Node.State.DEAD };
    }

    public void assignAgeBracket () {
        float r = random.nextFloat();

        switch (this.getDegree()) {
            case 4:
                if (r <= 0.1483) {
                    ageBracket = AgeBracket.ABOVE_EIGHTY;
                }
                else if (r <= 0.4864) {
                    ageBracket = AgeBracket.BELOW_NINE;
                }
                else if (r <= 0.8915) {
                    ageBracket = AgeBracket.SIXTYFIVE_TO_SEVENTYNINE;
                }
                else {
                    ageBracket = AgeBracket.FIFTY_TO_SIXTYFOUR;
                }
                break;
            case 5:
                if (r <= 0.8517) {
                    ageBracket = AgeBracket.FIFTY_TO_SIXTYFOUR;
                }
                else {
                    ageBracket = AgeBracket.TEN_TO_NINETEEN;
                }
                break;
            case 6:
                if (r <= 0.7383) {
                    ageBracket = AgeBracket.TEN_TO_NINETEEN;
                }
                else {
                    ageBracket = AgeBracket.THIRTYFIVE_TO_FOURTYNINE;
                }
                break;
            case 7:
                ageBracket = AgeBracket.THIRTYFIVE_TO_FOURTYNINE;
                break;
            case 8:
                ageBracket = AgeBracket.THIRTYFIVE_TO_FOURTYNINE;
                break;
            case 9:
                if (r <= 0.6925) {
                    ageBracket = AgeBracket.THIRTYFIVE_TO_FOURTYNINE;
                }
                else {
                    ageBracket = AgeBracket.TWENTY_TO_THIRTYFOUR;
                }
                break;
            default:
                ageBracket = AgeBracket.TWENTY_TO_THIRTYFOUR;
                break;
        }
    }

    public float hospitalisationMultiplier () {
        switch (this.ageBracket) {
            case BELOW_NINE:
                return 0.6f;
            case TEN_TO_NINETEEN:
                return 0.2f;
            case TWENTY_TO_THIRTYFOUR:
                return 1;
            case THIRTYFIVE_TO_FOURTYNINE:
                return 1.7f;
            case FIFTY_TO_SIXTYFOUR:
                return 3.5f;
            case SIXTYFIVE_TO_SEVENTYNINE:
                return 6f;
            case ABOVE_EIGHTY:
                return 15f;
            default:
                return 1;
        }
    }

    public float mortalityMultiplier () {
        switch (this.ageBracket) {
            case BELOW_NINE:
                return 0.2f;
            case TEN_TO_NINETEEN:
                return 0.1f;
            case TWENTY_TO_THIRTYFOUR:
                return 1;
            case THIRTYFIVE_TO_FOURTYNINE:
                return 3f;
            case FIFTY_TO_SIXTYFOUR:
                return 30f;
            case SIXTYFIVE_TO_SEVENTYNINE:
                return 75f;
            case ABOVE_EIGHTY:
                return 350f;
            default:
                return 1;
        }
    }

    @Override
    public int compareTo(Node o) {
        if (this.getDegree() == o.getDegree()) return 0;

        if (this.getDegree() < o.getDegree()) {
            return -1;
        }
        else {
            return 1;
        }
    }
}
