package com.lucaswarwick02.standalone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.models.HelperFunctions;

public class MathematicalComparison {
    final int numberOfNodes = 2500;
    final int initialInfected = 3;

    final float infectionRate = 0.000125f;
    final float recoveryRate = 0.016f;
    final float vaccinatedRate = 0.04f;
    final float hospitalisationRate = 0.015f;
    final float mortalityRate = 0.025f;

    final int iterations = 150;
    final int simulations = 100;

    Random r = new Random();

    public MathematicalComparison () {}

    public Map<String, double[]> runMathematicalSimulation () {
        Map<String, double[]> states = new HashMap<>();
        states.put("Time", new double[iterations]);

        for (Node.State state : Node.getAllStates()) {
            states.put(Node.stateToString(state), new double[iterations]);
        }

        states.get("Time")[0] = 0;
        states.get("Susceptible")[0] = numberOfNodes - (double) initialInfected;
        states.get("Infected")[0] = initialInfected;
        states.get("Recovered")[0] = 0;
        states.get("Hospitalised")[0] = 0;
        states.get("Dead")[0] = 0;

        for (int i = 1; i < iterations; i++) {
            states.get("Time")[i] = i;

            double newInfected = states.get("Infected")[i - 1] * states.get("Susceptible")[i - 1] * infectionRate;
            double newRecovered = states.get("Infected")[i - 1] * recoveryRate;
            double newHospitalised = states.get("Infected")[i - 1] * hospitalisationRate;
            double newDead = states.get("Hospitalised")[i - 1] * mortalityRate;

            states.get("Susceptible")[i] = states.get("Susceptible")[i - 1] - newInfected;
            states.get("Infected")[i] = states.get("Infected")[i - 1] + newInfected - newRecovered - newHospitalised;
            states.get("Recovered")[i] = states.get("Recovered")[i - 1] + newRecovered;
            states.get("Hospitalised")[i] = states.get("Hospitalised")[i - 1] + newHospitalised - newDead;
            states.get("Dead")[i] = states.get("Dead")[i - 1] + newDead;
        }

        return states;
    }

    public Map<String, double[]> runStochasticSimulations () {

        Map<String, double[]>[] allStates = new HashMap[simulations];

        for (int s = 0; s < simulations; s++) {
            Map<String, double[]> states = new HashMap<>();
            states.put("Time", new double[iterations]);

            for (Node.State state : Node.getAllStates()) {
                states.put(Node.stateToString(state), new double[iterations]);
            }

            List<Node> nodes = new ArrayList<>();
            for (int i = 0; i < numberOfNodes; i++) {
                nodes.add(new Node(i));
            }

            List<Node> initialInfectedNodes = HelperFunctions.pickRandomNodes(nodes, initialInfected);
            initialInfectedNodes.forEach(node -> node.state = Node.State.INFECTED);
            saveState(states, nodes, 0);

            for (int i = 1; i < iterations; i++) {
                performIteration(states, nodes, i);
            }

            allStates[s] = states;
        }
        
        return aggregateTotals(allStates);
    }

    void performIteration (Map<String, double[]> states, List<Node> nodes, int iterationNumber) {
        // Setup lists to store information from this iteration
        List<Node> nodesToInfect = new ArrayList<>();
        List<Node> nodesToHospitalise = new ArrayList<>();
        List<Node> nodesToRecover = new ArrayList<>();
        List<Node> nodesToKill = new ArrayList<>();

        // For each infected Node...
        for (Node infectedNode : getNodesFromState(nodes, Node.State.INFECTED)) {
            // ... get a list of the Nodes they are going to infect
            nodes.forEach(neighbour -> {
                if ((neighbour.state == Node.State.SUSCEPTIBLE) && (r.nextFloat() <= infectionRate))
                    nodesToInfect.add(neighbour);
            });

            // ... maybe recover the Node
            if (r.nextFloat() <= recoveryRate) {
                nodesToRecover.add(infectedNode);
            } else if (r.nextFloat() <= hospitalisationRate) {
                nodesToHospitalise.add(infectedNode);
            }
        }

        // For each hospitalised Node...
        for (Node hospitalisedNode : getNodesFromState(nodes, Node.State.HOSPITALISED)) {
            // ... maybe recover the node
            if (r.nextFloat() <= recoveryRate) {
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

        saveState(states, nodes, iterationNumber);
    }

    void saveState(Map<String, double[]> states, List<Node> nodes, int t) {
        states.get("Time")[t] = t;
        for (Node.State state : Node.getAllStates()) {
            states.get(Node.stateToString(state))[t] = getNodesFromState(nodes, state).size();
        }
    }

    List<Node> getNodesFromState (List<Node> nodes, Node.State state) {
        return nodes.stream().filter(node -> node.state == state).collect(Collectors.toList());
    }

    Map<String, double[]> aggregateTotals(Map<String, double[]>[] allStates) {
        HashMap<String, double[]> totals = new HashMap<>();

        Set<String> keys = allStates[0].keySet();
        for (String key : keys) {
            totals.put(key, new double[iterations]);
            totals.put(key + "_STD", new double[iterations]);
        }

        for (int i = 0; i < iterations; i++) {
            totals.get("Time")[i] = i;
            for (String key : keys) {
                double[] values = new double[allStates.length];

                for (int m = 0; m < allStates.length; m++) {
                    values[m] = allStates[m].get(key)[i];
                }

                double mean = HelperFunctions.calculateMean(values);
                double standardDeviation = HelperFunctions.calculateStandardDeviation(values) / 2;

                totals.get(key)[i] = mean;
                totals.get(key + "_STD")[i] = standardDeviation;
            }
        }

        return totals;
    }
}
