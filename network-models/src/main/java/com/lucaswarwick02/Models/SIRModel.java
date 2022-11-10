package com.lucaswarwick02.Models;

import com.lucaswarwick02.Components.Node;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.Table;
import java.util.*;

public class SIRModel extends AbstractModel {

    public SIRModel ( float rateOfInfection, float rateOfRecovery ) {
        super( rateOfInfection, rateOfRecovery );
    }

    @Override
    public void runSimulation (int iterations, int initialInfected) {
        int[] timeCount = new int[iterations];
        int[] susceptibleCount = new int[iterations];
        int[] infectedCount = new int[iterations];
        int[] recoveredCount = new int[iterations];

        int[] cumulativeInfected = new int[iterations];

        Random r = new Random();

        // Initialise the model at time = 0

        // set initialInfected nodes to Infected
        List<Node> initialInfectedNodes = pickRandom(underlyingNetwork.getNodes(), initialInfected);
        initialInfectedNodes.forEach(node -> node.state = Node.State.Infected);

        timeCount[0] = 0;
        susceptibleCount[0] = this.underlyingNetwork.getNodesFromState(Node.State.Susceptible).size();
        infectedCount[0] = this.underlyingNetwork.getNodesFromState(Node.State.Infected).size();
        recoveredCount[0] = this.underlyingNetwork.getNodesFromState(Node.State.Recovered).size();

        cumulativeInfected[0] = this.underlyingNetwork.getNodesFromState(Node.State.Infected).size();

        for (int t = 1; t < iterations; t++) {

            List<Node> nodesToInfect = new ArrayList<>();
            List<Node> nodesToRecover = new ArrayList<>();

            // For each infected Node...
            for (Node infectedNode : underlyingNetwork.getNodesFromState(Node.State.Infected)) {
                // ... get a list of the Nodes they are going to infect
                infectedNode.neighbours.forEach(neighbour -> {
                    if (neighbour.state == Node.State.Susceptible) {
                        if (r.nextFloat() <= RATE_OF_INFECTION) nodesToInfect.add(neighbour);
                    }
                });

                // ... maybe recover the Node
                if (r.nextFloat() <= RATE_OF_RECOVERY) nodesToRecover.add(infectedNode);
            }

            // Infect the nodes
            nodesToInfect.forEach(node -> node.state = Node.State.Infected);
            // Recover the nodes
            nodesToRecover.forEach(node -> node.state = Node.State.Recovered);

            timeCount[t] = t;
            susceptibleCount[t] = this.underlyingNetwork.getNodesFromState(Node.State.Susceptible).size();
            infectedCount[t] = this.underlyingNetwork.getNodesFromState(Node.State.Infected).size();
            recoveredCount[t] = this.underlyingNetwork.getNodesFromState(Node.State.Recovered).size();

            cumulativeInfected[t] = cumulativeInfected[t - 1] + nodesToInfect.size();
        }

        results = Table.create("SIR Model Results")
                .addColumns(
                        IntColumn.create("Time", timeCount),
                        IntColumn.create("Susceptible", susceptibleCount),
                        IntColumn.create("Infected", infectedCount),
                        IntColumn.create("Recovered", recoveredCount),
                        IntColumn.create("CumulativeInfected", cumulativeInfected)
                );
    }
}
