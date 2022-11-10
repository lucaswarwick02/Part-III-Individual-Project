package com.lucaswarwick02.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.lucaswarwick02.Components.Node;
import com.lucaswarwick02.Networks.AbstractNetwork;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Layout;
import tech.tablesaw.plotly.traces.ScatterTrace;
import tech.tablesaw.plotly.traces.Trace;

public class SIRVModel extends AbstractModel
{
    final float RATE_OF_VACCINATION;

    public SIRVModel ( float rateOfInfection, float rateOfRecovery, float rateOfVaccination ) {
        super( rateOfInfection, rateOfRecovery );
        this.RATE_OF_VACCINATION = rateOfVaccination;
    }

    @Override
    public void runSimulation( int iterations, int initialInfected )
    {
        int[] timeCount = new int[iterations];
        int[] susceptibleCount = new int[iterations];
        int[] infectedCount = new int[iterations];
        int[] recoveredCount = new int[iterations];
        int[] vaccinatedCount = new int[iterations];

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

            // With the leftover nodes, vaccinate them
            for (Node susceptibleNode : underlyingNetwork.getNodesFromState( Node.State.Susceptible )) {
                if (r.nextFloat() <= RATE_OF_VACCINATION) susceptibleNode.state = Node.State.Vaccinated;
            }

            timeCount[t] = t;
            susceptibleCount[t] = this.underlyingNetwork.getNodesFromState(Node.State.Susceptible).size();
            infectedCount[t] = this.underlyingNetwork.getNodesFromState(Node.State.Infected).size();
            recoveredCount[t] = this.underlyingNetwork.getNodesFromState(Node.State.Recovered).size();
            vaccinatedCount[t] = this.underlyingNetwork.getNodesFromState(Node.State.Vaccinated).size();
        }

        results = Table.create( "SIRV Model Results" )
                .addColumns(
                        IntColumn.create( "Time", timeCount ),
                        IntColumn.create( "Susceptible", susceptibleCount ),
                        IntColumn.create( "Infected", infectedCount ),
                        IntColumn.create( "Recovered", recoveredCount ),
                        IntColumn.create( "Vaccinated", vaccinatedCount )
                );
    }
}
