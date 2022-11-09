package com.lucaswarwick02;

import com.lucaswarwick02.Components.Node;
import com.lucaswarwick02.Networks.AbstractNetwork;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.LinePlot;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Layout;
import tech.tablesaw.plotly.traces.ScatterTrace;
import tech.tablesaw.plotly.traces.Trace;

import java.util.*;
import java.util.stream.Collectors;

public class SIRModel {
    AbstractNetwork underlyingNetwork;

    final float RATE_OF_INFECTION;
    final float RATE_OF_RECOVERY;

    Table results;

    public SIRModel (AbstractNetwork underlyingNetwork, float rateOfInfection, float rateOfRecovery) {
        this.underlyingNetwork = underlyingNetwork;

        this.RATE_OF_INFECTION = rateOfInfection;
        this.RATE_OF_RECOVERY = rateOfRecovery;
    }

    public void runSimulation (int iterations, int initialInfected) {
        int[] timeCount = new int[iterations];
        int[] susceptibleCount = new int[iterations];
        int[] infectedCount = new int[iterations];
        int[] recoveredCount = new int[iterations];

        Random r = new Random();

        // Initialise the model at time = 0

        // set initialInfected nodes to Infected
        List<Node> initialInfectedNodes = pickRandom(underlyingNetwork.getNodes(), initialInfected);
        initialInfectedNodes.forEach(node -> node.state = Node.State.Infected);

        timeCount[0] = 0;
        susceptibleCount[0] = this.underlyingNetwork.getNodesFromState(Node.State.Susceptible).size();
        infectedCount[0] = this.underlyingNetwork.getNodesFromState(Node.State.Infected).size();
        recoveredCount[0] = this.underlyingNetwork.getNodesFromState(Node.State.Recovered).size();

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
        }

        results = Table.create("SIR Model Results")
                .addColumns(
                        IntColumn.create("Time", timeCount),
                        IntColumn.create("Susceptible", susceptibleCount),
                        IntColumn.create("Infected", infectedCount),
                        IntColumn.create("Recovered", recoveredCount)
                );
    }

    public void viewResults () {
        Layout layout = Layout.builder()
                .title("My Title")
                .height(500)
                .width(650)
                .build();

        ScatterTrace susceptibleTrace = ScatterTrace.builder(
                results.column(0), results.column(1))
                .mode(ScatterTrace.Mode.LINE)
                .name("Susceptible")
                .build();

        ScatterTrace infectedTrace = ScatterTrace.builder(
                        results.column(0), results.column(2))
                .mode(ScatterTrace.Mode.LINE)
                .name("Infected")
                .build();

        ScatterTrace recoveredTrace = ScatterTrace.builder(
                        results.column(0), results.column(3))
                .mode(ScatterTrace.Mode.LINE)
                .name("Recovered")
                .build();

        Plot.show(new Figure(layout, susceptibleTrace, infectedTrace, recoveredTrace));
    }

    private static <E> List<E> pickRandom(List<E> list, int n) {
        return new Random().ints(n, 0, list.size()).mapToObj(list::get).collect(Collectors.toList());
    }
}
