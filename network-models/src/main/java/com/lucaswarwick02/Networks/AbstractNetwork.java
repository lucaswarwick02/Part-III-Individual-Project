package com.lucaswarwick02.networks;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.lucaswarwick02.components.Node;

public abstract class AbstractNetwork {
    List<Node> nodes;

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
    public abstract void generateNetwork (int numberOfNodes);

    public List<Node> getNodesFromState (Node.State state) {
        return this.getNodes().stream().filter(node -> node.state == state).collect(Collectors.toList());
    }

    /**
     * Get the Nodes within the underlying network
     * @return
     */
    public List<Node> getNodes () {
        return this.nodes;
    }

    public double getAverageDegree () {
        return this.nodes.stream().mapToDouble(node -> node.getDegree()).average().orElse(Double.NaN);
    }
}
