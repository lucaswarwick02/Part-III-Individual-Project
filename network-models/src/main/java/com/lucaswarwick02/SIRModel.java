package com.lucaswarwick02;

import com.lucaswarwick02.Components.Node;
import com.lucaswarwick02.Networks.AbstractNetwork;

import java.util.*;
import java.util.stream.Collectors;

public class SIRModel {
    AbstractNetwork underlyingNetwork;

    final float RATE_OF_INFECTION;
    final float RATE_OF_RECOVERY;

    int time;

    public SIRModel (AbstractNetwork underlyingNetwork, float rateOfInfection, float rateOfRecovery) {
        this.underlyingNetwork = underlyingNetwork;

        this.RATE_OF_INFECTION = rateOfInfection;
        this.RATE_OF_RECOVERY = rateOfRecovery;
    }

    public void runSimulation (int iterations, int initialInfected) {
        Random r = new Random();

        // Initialise the model at time = 0
        time = 0;

        // set initialInfected nodes to Infected
        List<Node> initialInfectedNodes = pickRandom(underlyingNetwork.getNodes(), initialInfected);
        initialInfectedNodes.forEach(node -> node.state = Node.State.Infected);

        printModelState();

        for (int t = 1; t < iterations; t++) {
            time = t;

            List<Node> nodesToInfect = new ArrayList<>();
            List<Node> nodesToRecover = new ArrayList<>();

            // For each infected Node...
            for (Node infectedNode : underlyingNetwork.getNodesFromState(Node.State.Infected)) {
                // ... get a list of the Nodes they are going to infect
                infectedNode.neighbours.forEach(node -> {if (r.nextFloat() <= RATE_OF_INFECTION) nodesToInfect.add(node); });

                // ... maybe recover the Node
                if (r.nextFloat() <= RATE_OF_RECOVERY) nodesToRecover.add(infectedNode);
            }

            // Infect the nodes
            nodesToInfect.forEach(node -> node.state = Node.State.Infected);
            // Recover the nodes
            nodesToRecover.forEach(node -> node.state = Node.State.Recovered);

            printModelState();
        }
    }

    private static <E> List<E> pickRandom(List<E> list, int n) {
        return new Random().ints(n, 0, list.size()).mapToObj(list::get).collect(Collectors.toList());
    }

    public void printModelState () {
//        System.out.println("--------------------------------------------------");
//        // Print time of the Model
//        System.out.println("Time: " + this.time);
//        // Print number of Susceptible Nodes
//        System.out.println("Susceptible: " + underlyingNetwork.getNodesFromState(Node.State.Susceptible).size());
//        // Print number of Infected Nodes
//        System.out.println("Infected: " + underlyingNetwork.getNodesFromState(Node.State.Infected).size());
//        // Print number of Recovered Nodes
//        System.out.println("Recovered: " + underlyingNetwork.getNodesFromState(Node.State.Recovered).size());

        System.out.println(
                time + "," +
                        underlyingNetwork.getNodesFromState(Node.State.Susceptible).size() + "," +
                        underlyingNetwork.getNodesFromState(Node.State.Infected).size() + "," +
                        underlyingNetwork.getNodesFromState(Node.State.Recovered).size());
    }
}
