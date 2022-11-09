package com.lucaswarwick02.Networks;

import com.lucaswarwick02.Components.Node;

import java.util.ArrayList;
import java.util.stream.Collectors;

public abstract class AbstractNetwork {
    ArrayList<Node> nodes;

    /**
     * Create an empty underlying network
     */
    public AbstractNetwork() {
        this.nodes = new ArrayList<>();
    }

    /**
     * Generate the Nodes and Edges for the underlying network
     * @param numberOfNodes Number of Nodes in the network
     */
    public abstract void generateNetwork (int numberOfNodes);

    public ArrayList<Node> getNodesFromState (Node.State state) {
        return (ArrayList<Node>) this.getNodes().stream().filter(node -> node.state == state).collect(Collectors.toList());
    }

    /**
     * Get the Nodes within the underlying network
     * @return
     */
    public ArrayList<Node> getNodes () {
        return this.nodes;
    }

    public double getAverageDegree () {
        return this.nodes.stream().mapToDouble(node -> node.getDegree()).average().orElse(Double.NaN);
    }
}
