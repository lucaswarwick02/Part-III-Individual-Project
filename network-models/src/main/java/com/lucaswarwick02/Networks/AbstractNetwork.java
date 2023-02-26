package com.lucaswarwick02.networks;

import com.lucaswarwick02.HelperFunctions;
import com.lucaswarwick02.components.AgeBracket;
import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.models.StochasticModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class AbstractNetwork {

    private Random random = new Random();

    /**
     * Nodes contained in the network
     */
    public List<Node> nodes;

    /**
     * Create an empty underlying network
     */
    protected AbstractNetwork() {
        this.nodes = new ArrayList<>();
    }

    /**
     * Generate the Nodes and Edges for the underlying network
     * 
     * @param numberOfNodes Number of Nodes in the network
     */
    public abstract void generateNetwork();

    public void assignAgeBrackets() {
        List<Node> allNodes = new ArrayList<>(this.getNodes());

        // Order nodes from lowest degree -> highest degree
        Collections.sort(allNodes, degreeComparator);

        // For each node...
        for (AgeBracket ageBracket : AgeBracket.degreeOrder) {
            // Calculate the total number of nodes for this age bracket
            int numberOfNodes = (int) Math.floor(ageBracket.percentageOfPopulation * StochasticModel.NUMBER_OF_NODES);

            // Get a list of the first N nodes
            List<Node> nodesForAgeBracket = allNodes.stream().limit(numberOfNodes).collect(Collectors.toList());

            // Assign each of those nodes this age bracket
            nodesForAgeBracket.forEach(node -> node.ageBracket = ageBracket);

            // Remove them from the list of nodes
            allNodes.removeAll(nodesForAgeBracket);
        }

        // For the remaining few nodes without an age bracket (due to Math.floor)
        for (Node node : allNodes) {
            // Assign them a random age bracket to avoid a bias
            node.ageBracket = AgeBracket.degreeOrder[random.nextInt(AgeBracket.degreeOrder.length)];
        }
    }

    /**
     * Set a subsection of the nodes within the network which match the state
     * 
     * @param state Compartmental model state
     * @return Subset of the networks nodes
     */
    public List<Node> getNodesFromState(Node.State state) {
        return this.getNodes().stream().filter(node -> node.getState() == state).collect(Collectors.toList());
    }

    /**
     * Get the Nodes within the underlying network
     * 
     * @return List of Nodes
     */
    public List<Node> getNodes() {
        return this.nodes;
    }

    /**
     * Calculate the average degree of all the nodes
     * 
     * @return Average degree
     */
    public double getAverageDegree() {
        return this.nodes.stream().mapToDouble(Node::getDegree).average().orElse(Double.NaN);
    }

    public void logDegreeDistribution() {
        HashMap<Integer, Integer> nodesPerDegree = new HashMap<>();
        for (Node node : this.nodes) {
            if (nodesPerDegree.containsKey(node.getDegree())) {
                nodesPerDegree.put(node.getDegree(), nodesPerDegree.get(node.getDegree()) + 1);
            } else {
                nodesPerDegree.put(node.getDegree(), 1);
            }
        }
        HelperFunctions.LOGGER.info("Degree Distribution: ");
        nodesPerDegree.forEach((key, value) -> {
            HelperFunctions.LOGGER
                    .info(key + ": " + value + " (" + (((float) value / (float) this.nodes.size()) * 100) + "%)");
        });
    }

    public void logAgeDistribution() {
        EnumMap<AgeBracket, Integer> ageDistribution = new EnumMap<>(AgeBracket.class);

        for (Node node : this.nodes) {
            ageDistribution.put(node.ageBracket, ageDistribution.getOrDefault(node.ageBracket, 0) + 1);
        }

        for (AgeBracket ageBracket : AgeBracket.values()) {
            HelperFunctions.LOGGER.info(
                    ageBracket + ": " + ((ageDistribution.get(ageBracket) / (float) this.nodes.size()) * 100) + "%");
        }
    }

    public void logStateDistribution() {
        EnumMap<Node.State, Integer> stateDistribution = new EnumMap<>(Node.State.class);

        for (Node node : this.nodes) {
            stateDistribution.put(node.getState(), stateDistribution.getOrDefault(node.getState(), 0) + 1);
        }

        for (Node.State nodeState : Node.State.values()) {
            HelperFunctions.LOGGER.info(
                    nodeState + ": " + stateDistribution.get(nodeState));
        }
    }

    public int calculateNumberOfComponents() {
        List<Node> nodesToVisit = new ArrayList<>(this.nodes);
        List<List<Node>> components = new ArrayList<>();

        while (nodesToVisit.size() > 0) {
            List<Node> component = new ArrayList<>();

            visitNode(nodesToVisit.get(0), component, nodesToVisit);

            components.add(component);
        }

        return components.size();
    }

    private void visitNode(Node node, List<Node> component, List<Node> nodesToVisit) {
        if (!nodesToVisit.contains(node))
            return;

        nodesToVisit.remove(node);

        component.add(node);

        for (Node neighbour : node.neighbours) {
            visitNode(neighbour, component, nodesToVisit);
        }

    }

    private Comparator<Node> degreeComparator = (Node n1, Node n2) -> {
        if (n1.getDegree() == n2.getDegree()) {
            return 0;
        } else if (n1.getDegree() < n2.getDegree()) {
            return -1;
        } else {
            return 1;
        }
    };

    public List<Node> getHighestDegreeNodes(int n) {
        List<Node> allNodes = new ArrayList<>(this.getNodes());

        Collections.sort(allNodes, degreeComparator);
        Collections.reverse(allNodes);

        return allNodes.stream().limit(n).collect(Collectors.toList());
    }

    public List<Node> getLowestDegreeNodes(int n) {
        List<Node> allNodes = new ArrayList<>(this.getNodes());

        Collections.sort(allNodes, degreeComparator);

        return allNodes.stream().limit(n).collect(Collectors.toList());
    }

    private Comparator<Node> ageComparator = (Node n1, Node n2) -> {
        if (n1.ageBracket.ageOrder == n2.ageBracket.ageOrder) {
            return 0;
        } else if (n1.ageBracket.ageOrder < n2.ageBracket.ageOrder) {
            return -1;
        } else {
            return 1;
        }
    };

    public List<Node> getOldestNodes(int n) {
        List<Node> allNodes = new ArrayList<>(this.getNodes());

        Collections.sort(allNodes, ageComparator);
        Collections.reverse(allNodes);

        return allNodes.stream().limit(n).collect(Collectors.toList());
    }

    public List<Node> getYoungestNodes(int n) {
        List<Node> allNodes = new ArrayList<>(this.getNodes());

        Collections.sort(allNodes, ageComparator);

        return allNodes.stream().limit(n).collect(Collectors.toList());
    }
}
