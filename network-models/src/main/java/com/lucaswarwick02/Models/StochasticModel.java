package com.lucaswarwick02.models;

import com.lucaswarwick02.networks.AbstractNetwork;
import com.lucaswarwick02.components.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Each iteration, use probability instead of ODEs
 */
public class StochasticModel {

    Random r = new Random(); // Used for getting random numbers

    AbstractNetwork underlyingNetwork; // Network used in the model

    final float rateOfInfection; // Probability of an infected node spreading
    final float rateOfRecovery; // Probability of an infected node recovering
    final float hospitalisationRate; // Probabiliy of an infected node being hospitalised
    final float mortalityRate; // Probability of a hospitalised node 'dying'

    public Map<String, int[]> states = new HashMap<>();

    public Map<String, int[]> totals = new HashMap<>();

    VaccinationStrategy vaccinationStrategy; // Strategy used in the simulation

    /**
     * Setup the Stochastic Model
     * 
     * @param vaccinationStrategy Strategy used in the simulation
     * @param rateOfInfection     Probability of an infected node spreading
     * @param rateOfRecovery      Probability of an infected node recovering
     * @param rateOfVaccination   Probability of a susceptible node being vaccinated
     */
    public StochasticModel(VaccinationStrategy vaccinationStrategy) {
        this.vaccinationStrategy = vaccinationStrategy;

        this.rateOfInfection = 0.075f;
        this.rateOfRecovery = 0.06f;
        this.hospitalisationRate = 0.04f;
        this.mortalityRate = 0.1f;
    }

    /**
     * Percolate the virus throughout the network
     * 
     * @param iterations      Number of time steps
     * @param initialInfected Number of infected individuals at t=0
     */
    public void runSimulation(int iterations, int initialInfected) {
        // Setup the storing of model states
        states.put("Time", new int[iterations]);
        states.put("Susceptible", new int[iterations]);
        states.put("Infected", new int[iterations]);
        states.put("Recovered", new int[iterations]);
        states.put("Vaccinated", new int[iterations]);
        states.put("Hospitalised", new int[iterations]);
        states.put("Dead", new int[iterations]);

        totals.put("Time", new int[iterations]);
        totals.put("Hospitalised", new int[iterations]);
        totals.put("Infected", new int[iterations]);
        totals.put("Dead", new int[iterations]);

        // set initialInfected nodes to Infected
        List<Node> initialInfectedNodes = HelperFunctions.pickRandomNodes(underlyingNetwork.getNodes(), initialInfected);
        initialInfectedNodes.forEach(node -> node.state = Node.State.INFECTED);

        // Store the initial model state
        saveModelState(0);
        saveTotals(0, 3, 0, 0);

        for (int t = 1; t < iterations; t++) {
            performIteration(t);
        }
    }

    void performIteration(int iterationNumber) {
        // Setup lists to store information from this iteration
        List<Node> nodesToInfect = new ArrayList<>();
        List<Node> nodesToHospitalise = new ArrayList<>();
        List<Node> nodesToRecover = new ArrayList<>();
        List<Node> nodesToKill = new ArrayList<>();

        // For each infected Node...
        for (Node infectedNode : underlyingNetwork.getNodesFromState(Node.State.INFECTED)) {
            // ... get a list of the Nodes they are going to infect
            infectedNode.neighbours.forEach(neighbour -> {
                if ((neighbour.state == Node.State.SUSCEPTIBLE) && (r.nextFloat() <= rateOfInfection))
                    nodesToInfect.add(neighbour);
            });

            // ... maybe recover the Node
            if (r.nextFloat() <= rateOfRecovery) {
                nodesToRecover.add(infectedNode);
            } else if (r.nextFloat() <= hospitalisationRate) {
                nodesToHospitalise.add(infectedNode);
            }
        }

        // For each hospitalised Node...
        for (Node hospitalisedNode : underlyingNetwork.getNodesFromState(Node.State.HOSPITALISED)) {
            // ... maybe recover the node
            if (r.nextFloat() <= rateOfRecovery) {
                nodesToRecover.add(hospitalisedNode);
            } else if (r.nextFloat() <= mortalityRate) {
                nodesToKill.add(hospitalisedNode);
            }
        }

        // Infect nodes
        nodesToInfect.forEach(node -> node.state = Node.State.INFECTED);
        // Recover nodes
        nodesToRecover.forEach(node -> node.state = Node.State.RECOVERED);
        // Hospitalise nodes
        nodesToHospitalise.forEach(node -> node.state = Node.State.HOSPITALISED);
        // Kill nodes
        nodesToKill.forEach(node -> node.state = Node.State.DEAD);

        // Run vacciantion strategy
        switch (vaccinationStrategy) {
            case GLOBAL:
                globalVaccinationStrategy();
                break;
            default:
                break;
        }

        calculateAndSaveTotals(iterationNumber, nodesToInfect.size(), nodesToHospitalise.size(), nodesToKill.size());

        saveModelState(iterationNumber); // Store the model state
    }

    /**
     * Generated from VaccinationStrategy.GLOBAL
     */
    void globalVaccinationStrategy() {
        // Information used in this strategy:
        float rateOfVaccination = 0.0075f;

        // With the leftover nodes, vaccinate them
        for (Node susceptibleNode : underlyingNetwork.getNodesFromState(Node.State.SUSCEPTIBLE)) {
            if (r.nextFloat() <= rateOfVaccination)
                susceptibleNode.state = Node.State.VACCINATED;
        }
    }

    /**
     * Store the current state of the model as record
     * 
     * @param t Time step
     * @return ModelState
     */
    void saveModelState(int t) {
        this.states.get("Time")[t] = t;
        for (Node.State state : Node.getAllStates()) {
            this.states.get(Node.StateToString(state))[t] = this.underlyingNetwork.getNodesFromState(state).size();
        }
    }

    void saveTotals (int t, int infected, int hospitalised, int dead) {
        totals.get("Time")[t] = t;
        totals.get("Infected")[t] = infected;
        totals.get("Hospitalised")[t] = hospitalised;
        totals.get("Dead")[t] = dead;
    }

    void calculateAndSaveTotals(int t, int newlyInfected, int newlyHospitalised, int newlyDead) {
        saveTotals(t, 
            totals.get("Infected")[t - 1] + newlyInfected,
            totals.get("Hospitalised")[t - 1] + newlyHospitalised,
            totals.get("Dead")[t - 1] + newlyDead
        );
    }

    /**
     * Set the underlying network
     * 
     * @param underlyingNetwork Network used in the model
     */
    public void setUnderlyingNetwork(AbstractNetwork underlyingNetwork) {
        this.underlyingNetwork = underlyingNetwork;
    }
}
