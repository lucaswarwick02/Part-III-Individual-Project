package com.lucaswarwick02.standalone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.lucaswarwick02.HelperFunctions;
import com.lucaswarwick02.Main;
import com.lucaswarwick02.components.Epidemic;
import com.lucaswarwick02.components.Node;

public class MathematicalComparison {
    static final int numberOfNodes = 2500;
    static final int initialInfected = 3;

    Epidemic epidemic = Epidemic.loadFromResources("/mathematical.xml");

    static final int iterations = 150;
    static final int simulations = 100;

    Random r = new Random();

    public MathematicalComparison() {
        Main.LOGGER.info("Number of Nodes: " + numberOfNodes);
        Main.LOGGER.info("Running " + simulations + " Simulations, with " + iterations + " Iterations each");
        epidemic.logInformation();
    }

    public Map<String, double[]> runMathematicalSimulation() {
        Map<String, double[]> states = new HashMap<>();
        states.put("Time", new double[iterations]);

        for (Node.State state : Node.getAllStates()) {
            states.put(Node.stateToString(state), new double[iterations]);
        }

        states.get("Time")[0] = 0;
        states.get("Susceptible")[0] = (numberOfNodes - (double) initialInfected);
        states.get("Infected")[0] = (initialInfected);
        states.get("Recovered")[0] = 0;
        states.get("Hospitalised")[0] = 0;
        states.get("Dead")[0] = 0;

        for (int i = 1; i < iterations; i++) {
            states.get("Time")[i] = i;

            double arg1 = states.get("Infected")[i - 1] * states.get("Susceptible")[i - 1] * epidemic.infectionRate;
            double arg2 = states.get("Infected")[i - 1] * epidemic.recoveryRate;
            double arg3 = (states.get("Infected")[i - 1] - arg2) * epidemic.hospitalisationRate;
            double arg4 = states.get("Hospitalised")[i - 1] * epidemic.recoveryRate;
            double arg5 = (states.get("Hospitalised")[i - 1] - arg4) * epidemic.mortalityRate;

            states.get("Susceptible")[i] = states.get("Susceptible")[i - 1] - arg1;
            states.get("Infected")[i] = states.get("Infected")[i - 1] + arg1 - arg2 - arg3;
            states.get("Recovered")[i] = states.get("Recovered")[i - 1] + arg2 + arg4;
            states.get("Hospitalised")[i] = states.get("Hospitalised")[i - 1] + arg3 - arg4 - arg5;
            states.get("Dead")[i] = states.get("Dead")[i - 1] + arg5;
        }

        return normalizeResults(states);
    }

    public Map<String, double[]> runStochasticSimulations() {

        Map<String, double[]>[] allStates = new HashMap[simulations];

        for (int s = 0; s < simulations; s++) {
            System.out.println("Running Simulation #" + s);
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

        for (int s = 0; s < allStates.length; s++) {
            allStates[s] = normalizeResults(allStates[s]);
        }

        return aggregateTotals(allStates);
    }

    void performIteration(Map<String, double[]> states, List<Node> nodes, int iterationNumber) {
        // Setup lists to store information from this iteration
        List<Node> nodesToInfect = new ArrayList<>();
        List<Node> nodesToHospitalise = new ArrayList<>();
        List<Node> nodesToRecover = new ArrayList<>();
        List<Node> nodesToKill = new ArrayList<>();

        // For each infected Node...
        for (Node infectedNode : getNodesFromState(nodes, Node.State.INFECTED)) {
            // ... get a list of the Nodes they are going to infect
            nodes.forEach(neighbour -> {
                if ((neighbour.state == Node.State.SUSCEPTIBLE) && (r.nextFloat() <= epidemic.infectionRate))
                    nodesToInfect.add(neighbour);
            });

            // ... maybe recover the Node
            if (r.nextFloat() <= epidemic.recoveryRate) {
                nodesToRecover.add(infectedNode);
            }
        }

        for (Node infectedNode : getNodesFromState(nodes, Node.State.INFECTED)) {
            if (nodesToRecover.contains(infectedNode))
                continue;

            if (r.nextFloat() <= epidemic.hospitalisationRate) {
                nodesToHospitalise.add(infectedNode);
            }
        }

        // For each hospitalised Node...
        for (Node hospitalisedNode : getNodesFromState(nodes, Node.State.HOSPITALISED)) {
            // ... maybe recover the node
            if (r.nextFloat() <= epidemic.recoveryRate) {
                nodesToRecover.add(hospitalisedNode);
            }
        }

        // For each hospitalised Node...
        for (Node hospitalisedNode : getNodesFromState(nodes, Node.State.HOSPITALISED)) {
            if (nodesToRecover.contains(hospitalisedNode))
                continue;

            if (r.nextFloat() <= epidemic.mortalityRate) {
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

    List<Node> getNodesFromState(List<Node> nodes, Node.State state) {
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
                double standardDeviation = HelperFunctions.calculateStandardDeviation(values);

                totals.get(key)[i] = mean;
                totals.get(key + "_STD")[i] = standardDeviation;
            }
        }

        return totals;
    }

    private Map<String, double[]> normalizeResults(Map<String, double[]> results) {
        for (String key : results.keySet()) {
            if (key.equals("Time"))
                continue;
            for (int i = 0; i < results.get(key).length; i++) {
                results.get(key)[i] /= numberOfNodes;
            }
        }
        return results;
    }
}
