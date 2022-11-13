package com.lucaswarwick02.models;

import com.lucaswarwick02.networks.AbstractNetwork;
import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.models.states.ModelState;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Each iteration, use probability instead of ODEs
 */
public class StochasticModel {

    Random r = new Random(); // Used for getting random numbers

    AbstractNetwork underlyingNetwork; // Network used in the model

    final float rateOfInfection; // Probability of an infected node spreading
    final float rateOfRecovery; // Probability of an infected node recovering
    final float rateOfVaccination; // Probability of a susceptible node being vaccinated

    public ModelState[] modelStates; // Stores the state of the model at each time step

    VaccinationStrategy vaccinationStrategy; // Strategy used in the simulation

    /**
     * Setup the Stochastic Model
     * @param vaccinationStrategy Strategy used in the simulation
     * @param rateOfInfection Probability of an infected node spreading
     * @param rateOfRecovery Probability of an infected node recovering
     * @param rateOfVaccination Probability of a susceptible node being vaccinated
     */
    public StochasticModel ( VaccinationStrategy vaccinationStrategy, float rateOfInfection, float rateOfRecovery, float rateOfVaccination ) {
        this.vaccinationStrategy = vaccinationStrategy;
        this.rateOfInfection = rateOfInfection;
        this.rateOfRecovery = rateOfRecovery;
        this.rateOfVaccination = rateOfVaccination;
    }

    /**
     * Percolate the virus throughout the network
     * @param iterations Number of time steps
     * @param initialInfected Number of infected individuals at t=0
     */
    public void runSimulation ( int iterations, int initialInfected ) {
        // Setup the storing of model states
        modelStates = new ModelState[iterations];

        // set initialInfected nodes to Infected
        List<Node> initialInfectedNodes = pickRandomNodes(underlyingNetwork.getNodes(), initialInfected);
        initialInfectedNodes.forEach(node -> node.state = Node.State.INFECTED);

        // Store the initial model state
        modelStates[0] = generateModelState(0);

        for (int t = 1; t < iterations; t++) {

            // Based on the vaccination strategy, run the interval
            switch (vaccinationStrategy) {
                case GLOBAL:
                    globalVaccinationStrategy();
                    break;
                default:
                    globalVaccinationStrategy();
                    break;
            }

            // Store the model state
            modelStates[t] = generateModelState(t);
        }
    }

    /**
     * Generated from VaccinationStrategy.GLOBAL
     */
    void globalVaccinationStrategy () {
        // Setup lists to store information from this iteration
        List<Node> nodesToInfect = new ArrayList<>();
        List<Node> nodesToRecover = new ArrayList<>();

        // For each infected Node...
        for (Node infectedNode : underlyingNetwork.getNodesFromState(Node.State.INFECTED)) {
            // ... get a list of the Nodes they are going to infect
            infectedNode.neighbours.forEach(neighbour -> {
                if ((neighbour.state == Node.State.SUSCEPTIBLE) && (r.nextFloat() <= rateOfInfection))
                    nodesToInfect.add(neighbour);
            });

            // ... maybe recover the Node
            if (r.nextFloat() <= rateOfRecovery)
                nodesToRecover.add(infectedNode);
        }

        // Infect the nodes
        nodesToInfect.forEach(node -> node.state = Node.State.INFECTED);
        // Recover the nodes
        nodesToRecover.forEach(node -> node.state = Node.State.RECOVERED);

        // With the leftover nodes, vaccinate them
        for (Node susceptibleNode : underlyingNetwork.getNodesFromState(Node.State.SUSCEPTIBLE)) {
            if (r.nextFloat() <= rateOfVaccination)
                susceptibleNode.state = Node.State.VACCINATED;
        }
    }

    /**
     * Store the current state of the model as record
     * @param t Time step
     * @return ModelState
     */
    ModelState generateModelState (int t) {
        return new ModelState( t, 
            this.underlyingNetwork.getNodesFromState(Node.State.SUSCEPTIBLE).size(),
            this.underlyingNetwork.getNodesFromState(Node.State.INFECTED).size(),
            this.underlyingNetwork.getNodesFromState(Node.State.RECOVERED).size(), 
            this.underlyingNetwork.getNodesFromState(Node.State.VACCINATED).size()
        );
    }

    /**
     * Randomly pick N nodes
     * @param list List of Nodes to pick from
     * @param n Number of Nodes to pick
     * @return List of Nodes of length N
     */
    List<Node> pickRandomNodes( List<Node> list, int n ) {
        return r.ints(n, 0, list.size()).mapToObj(list::get).collect( Collectors.toList());
    }
    
    /**
     * Set the underlying network
     * @param underlyingNetwork Network used in the model
    */
    public void setUnderlyingNetwork ( AbstractNetwork underlyingNetwork ) {
        this.underlyingNetwork = underlyingNetwork;
    }
}
