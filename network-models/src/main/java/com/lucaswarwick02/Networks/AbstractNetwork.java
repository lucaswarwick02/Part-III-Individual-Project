package com.lucaswarwick02.networks;

import com.lucaswarwick02.Main;
import com.lucaswarwick02.components.Node;
import java.util.ArrayList;
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
     * @param numberOfNodes Number of Nodes in the network
     */
    public abstract void generateNetwork ();

    public void assignAgeBrackets () {
        for (Node node : this.nodes) {
            node.assignAgeBracket();
        }
    }

    /**
     * Set a subsection of the nodes within the network which match the state
     * @param state Compartmental model state
     * @return Subset of the networks nodes
     */
    public List<Node> getNodesFromState (Node.State state) {
        return this.getNodes().stream().filter(node -> node.state == state).collect(Collectors.toList());
    }

    /**
     * Get the Nodes within the underlying network
     * @return List of Nodes
     */
    public List<Node> getNodes () {
        return this.nodes;
    }

    /**
     * Calculate the average degree of all the nodes
     * @return Average degree
     */
    public double getAverageDegree () {
        return this.nodes.stream().mapToDouble(Node::getDegree).average().orElse(Double.NaN);
    }

    public void logDegreeDistribution () {
        HashMap<Integer, Integer> nodesPerDegree = new HashMap<>();
        for (Node node : this.nodes) {
            if (nodesPerDegree.containsKey(node.getDegree())) {
                nodesPerDegree.put(node.getDegree(), nodesPerDegree.get(node.getDegree()) + 1);
            }
            else {
                nodesPerDegree.put(node.getDegree(), 1);
            }
        }
        Main.LOGGER.info("Degree Distribution: ");
        nodesPerDegree.forEach((key, value) -> {
            Main.LOGGER.info(key + ": " + value + " (" + (((float)value / (float)this.nodes.size()) * 100) + "%)");
        });
    }

    public void logAgeDistribution () {
        EnumMap<Node.AgeBracket, Integer> ageDistribution = new EnumMap<>(Node.AgeBracket.class);

        for (Node node : this.nodes) {
            ageDistribution.put(node.ageBracket, ageDistribution.getOrDefault(node.ageBracket, 0) + 1);
        }

        for (Node.AgeBracket ageBracket : Node.AgeBracket.values()) {
            Main.LOGGER.info(ageBracket + ": " + ((ageDistribution.get(ageBracket) / (float)this.nodes.size()) * 100) + "%");
        }
    }
}
