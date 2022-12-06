package com.lucaswarwick02.networks;

import com.lucaswarwick02.components.Node;
import java.util.ArrayList;
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
}
