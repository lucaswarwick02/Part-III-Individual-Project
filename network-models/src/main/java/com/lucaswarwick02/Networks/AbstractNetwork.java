package com.lucaswarwick02.networks;

import com.lucaswarwick02.HelperFunctions;
import com.lucaswarwick02.components.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractNetwork {

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
        for (Node node : this.nodes) {
            node.assignAgeBracket();
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
        EnumMap<Node.AgeBracket, Integer> ageDistribution = new EnumMap<>(Node.AgeBracket.class);

        for (Node node : this.nodes) {
            ageDistribution.put(node.ageBracket, ageDistribution.getOrDefault(node.ageBracket, 0) + 1);
        }

        for (Node.AgeBracket ageBracket : Node.AgeBracket.values()) {
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

    public List<Node> getHighestDegreeNodes (int n) {
        List<Node> allNodes = new ArrayList<>(this.getNodes());
        
        Collections.sort(allNodes);
        Collections.reverse(allNodes);

        return allNodes.stream().limit(n).collect(Collectors.toList());
    }

    public List<Node> getLowestDegreeNodes (int n) {
        List<Node> allNodes = new ArrayList<>(this.getNodes());
        
        Collections.sort(allNodes);

        return allNodes.stream().limit(n).collect(Collectors.toList());
    }
}
