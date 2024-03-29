package com.lucaswarwick02.models;

import com.lucaswarwick02.networks.AbstractNetwork;
import com.lucaswarwick02.networks.NetworkFactory;
import com.lucaswarwick02.networks.NetworkFactory.NetworkType;
import com.lucaswarwick02.vaccination.AbstractStrategy;
import com.lucaswarwick02.HelperFunctions;
import com.lucaswarwick02.components.Epidemic;
import com.lucaswarwick02.components.ModelParameters;
import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.components.Node.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Each iteration, use probability instead of ODEs
 */
public class StochasticModel implements Runnable {

    Random r = new Random(); // Used for getting random numbers

    boolean includeAge;

    NetworkType networkType;
    AbstractNetwork underlyingNetwork; // Network used in the model

    Epidemic epidemic;
    AbstractStrategy abstractStrategy;

    int currentTime = 0;

    public Map<String, int[]> states = new HashMap<>();
    public Map<String, int[]> totals = new HashMap<>();

    /**
     * Setup the Stochastic Model
     * 
     * @param vaccinationStrategy Strategy used in the simulation
     */
    public StochasticModel(Epidemic epidemic, NetworkType networkType, AbstractStrategy abstractStrategy,
            boolean includeAge) {
        this.epidemic = epidemic;
        this.networkType = networkType;
        this.abstractStrategy = abstractStrategy;
        this.includeAge = includeAge;

        this.abstractStrategy.initialiseStrategy();
    }

    @Override
    public void run() {
        underlyingNetwork = NetworkFactory.getNetwork(networkType);

        underlyingNetwork.generateNetwork();
        if (this.includeAge) {
            underlyingNetwork.assignAgeBrackets();
        }

        runSimulation();
    }

    /**
     * Percolate the virus throughout the network
     */
    public void runSimulation() {
        // Setup the storing of model states
        states.put("Time", new int[ModelParameters.ITERATIONS]);
        states.put("Susceptible", new int[ModelParameters.ITERATIONS]);
        states.put("Infected", new int[ModelParameters.ITERATIONS]);
        states.put("Recovered", new int[ModelParameters.ITERATIONS]);
        states.put("Vaccinated", new int[ModelParameters.ITERATIONS]);
        states.put("Hospitalised", new int[ModelParameters.ITERATIONS]);
        states.put("Dead", new int[ModelParameters.ITERATIONS]);

        totals.put("Time", new int[ModelParameters.ITERATIONS]);
        totals.put("Hospitalised", new int[ModelParameters.ITERATIONS]);
        totals.put("Infected", new int[ModelParameters.ITERATIONS]);
        totals.put("Dead", new int[ModelParameters.ITERATIONS]);

        abstractStrategy.performStrategy(this);

        // set initialInfected nodes to Infected
        List<Node> initialInfectedNodes = HelperFunctions.pickRandomNodes(
                underlyingNetwork.getNodesFromState(State.SUSCEPTIBLE),
                ModelParameters.INITIAL_INFECTED);
        initialInfectedNodes.forEach(node -> node.setState(Node.State.INFECTED));

        // Store the initial model state
        saveModelState(0);
        saveTotals(0, initialInfectedNodes.size(), 0, 0);

        for (int t = 1; t < ModelParameters.ITERATIONS; t++) {
            performIteration(t);
        }
    }

    void performIteration(int iterationNumber) {
        // Update the internal currentTime
        this.currentTime = iterationNumber;

        // Setup lists to store information from this iteration
        List<Node> nodesToInfect = new ArrayList<>();
        List<Node> nodesToHospitalise = new ArrayList<>();
        List<Node> nodesToRecover = new ArrayList<>();
        List<Node> nodesToKill = new ArrayList<>();

        // For each infected Node...
        for (Node infectedNode : underlyingNetwork.getNodesFromState(Node.State.INFECTED)) {
            // ... get a list of the Nodes they are going to infect
            infectedNode.neighbours.forEach(neighbour -> {
                if ((neighbour.getState() == Node.State.SUSCEPTIBLE) && (r.nextFloat() <= epidemic.infectionRate))
                    nodesToInfect.add(neighbour);
            });

            // ... maybe recover the Node
            if (r.nextFloat() <= epidemic.recoveryRate) {
                nodesToRecover.add(infectedNode);
            }
        }

        for (Node infectedNode : underlyingNetwork.getNodesFromState(Node.State.INFECTED)) {
            if (nodesToRecover.contains(infectedNode))
                continue;

            if (r.nextFloat() <= epidemic.hospitalisationRate * infectedNode.ageBracket.h) {
                nodesToHospitalise.add(infectedNode);
            }
        }

        // For each hospitalised Node...
        for (Node hospitalisedNode : underlyingNetwork.getNodesFromState(Node.State.HOSPITALISED)) {
            // ... maybe recover the node
            if (r.nextFloat() <= epidemic.recoveryRate) {
                nodesToRecover.add(hospitalisedNode);
            }
        }

        for (Node hospitalisedNode : underlyingNetwork.getNodesFromState(Node.State.HOSPITALISED)) {
            if (nodesToRecover.contains(hospitalisedNode))
                continue;

            if (r.nextFloat() <= epidemic.mortalityRate * hospitalisedNode.ageBracket.d) {
                nodesToKill.add(hospitalisedNode);
            }
        }

        // Infect nodes
        nodesToInfect.forEach(node -> node.setState(Node.State.INFECTED));
        // Recover nodes
        nodesToRecover.forEach(node -> node.setState(Node.State.RECOVERED));
        // Hospitalise nodes
        nodesToHospitalise.forEach(node -> node.setState(Node.State.HOSPITALISED));
        // Kill nodes
        nodesToKill.forEach(node -> node.setState(Node.State.DEAD));

        // Perform vaccination strategy at the end of the iteration (time step)
        abstractStrategy.performStrategy(this);

        calculateAndSaveTotals(iterationNumber, nodesToInfect.size(), nodesToHospitalise.size(), nodesToKill.size());

        saveModelState(iterationNumber); // Store the model state
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
            this.states.get(Node.stateToString(state))[t] = this.underlyingNetwork.getNodesFromState(state).size();
        }
    }

    void saveTotals(int t, int infected, int hospitalised, int dead) {
        totals.get("Time")[t] = t;
        totals.get("Infected")[t] = infected;
        totals.get("Hospitalised")[t] = hospitalised;
        totals.get("Dead")[t] = dead;
    }

    void calculateAndSaveTotals(int t, int newlyInfected, int newlyHospitalised, int newlyDead) {
        saveTotals(t,
                totals.get("Infected")[t - 1] + newlyInfected,
                totals.get("Hospitalised")[t - 1] + newlyHospitalised,
                totals.get("Dead")[t - 1] + newlyDead);
    }

    public AbstractNetwork getUnderlyingNetwork() {
        return this.underlyingNetwork;
    }

    public int getCurrentTime() {
        return this.currentTime;
    }

    public Random getRandom() {
        return this.r;
    }

    public AbstractStrategy getAbstractStrategy() {
        return this.abstractStrategy;
    }

    public void createNetwork() {
        this.underlyingNetwork = NetworkFactory.getNetwork(this.networkType);
    }
}
