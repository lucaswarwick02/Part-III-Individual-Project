package com.lucaswarwick02.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.lucaswarwick02.networks.AbstractNetwork;
import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.models.states.ModelState;

public class StochasticModel {
    Random r = new Random();

    AbstractNetwork underlyingNetwork;

    final float rateOfInfection;
    final float rateOfRecovery;
    final float rateOfVaccination;

    public ModelState[] modelStates;

    VaccinationStrategy vaccinationStrategy;

    public StochasticModel ( VaccinationStrategy vaccinationStrategy, float rateOfInfection, float rateOfRecovery, float rateOfVaccination ) {
        this.vaccinationStrategy = vaccinationStrategy;
        this.rateOfInfection = rateOfInfection;
        this.rateOfRecovery = rateOfRecovery;
        this.rateOfVaccination = rateOfVaccination;
    }

    public void runSimulation ( int iterations, int initialInfected ) {
        // Setup the storing of model states
        modelStates = new ModelState[iterations];

        // set initialInfected nodes to Infected
        List<Node> initialInfectedNodes = pickRandomNodes(underlyingNetwork.getNodes(), initialInfected);
        initialInfectedNodes.forEach(node -> node.state = Node.State.Infected);

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

    void globalVaccinationStrategy () {
        // Setup lists to store information from this iteration
        List<Node> nodesToInfect = new ArrayList<>();
        List<Node> nodesToRecover = new ArrayList<>();

        // For each infected Node...
        for (Node infectedNode : underlyingNetwork.getNodesFromState(Node.State.Infected)) {
            // ... get a list of the Nodes they are going to infect
            infectedNode.neighbours.forEach(neighbour -> {
                if ((neighbour.state == Node.State.Susceptible) && (r.nextFloat() <= rateOfInfection))
                    nodesToInfect.add(neighbour);
            });

            // ... maybe recover the Node
            if (r.nextFloat() <= rateOfRecovery)
                nodesToRecover.add(infectedNode);
        }

        // Infect the nodes
        nodesToInfect.forEach(node -> node.state = Node.State.Infected);
        // Recover the nodes
        nodesToRecover.forEach(node -> node.state = Node.State.Recovered);

        // With the leftover nodes, vaccinate them
        for (Node susceptibleNode : underlyingNetwork.getNodesFromState(Node.State.Susceptible)) {
            if (r.nextFloat() <= rateOfVaccination)
                susceptibleNode.state = Node.State.Vaccinated;
        }
    }

    ModelState generateModelState (int t) {
        return new ModelState( t, 
            this.underlyingNetwork.getNodesFromState(Node.State.Susceptible).size(),
            this.underlyingNetwork.getNodesFromState(Node.State.Infected).size(),
            this.underlyingNetwork.getNodesFromState(Node.State.Recovered).size(), 
            this.underlyingNetwork.getNodesFromState(Node.State.Vaccinated).size()
        );
    }

    List<Node> pickRandomNodes( List<Node> list, int n ) {
        return r.ints(n, 0, list.size()).mapToObj(list::get).collect( Collectors.toList());
    }
    
    public void setUnderlyingNetwork ( AbstractNetwork underlyingNetwork ) {
        this.underlyingNetwork = underlyingNetwork;
    }
}
