package com.lucaswarwick02.Networks;

import com.lucaswarwick02.Components.Edge;
import com.lucaswarwick02.Components.Node;
import java.util.HashSet;

public abstract class AbstractNetwork {
    HashSet<Node> nodes;
    HashSet<Edge> edges;

    /**
     * Create an empty underlying network
     */
    public AbstractNetwork() {
        this.nodes = new HashSet<>();
        this.edges = new HashSet<>();
    }

    /**
     * Generate the Nodes and Edges for the underlying network
     * @param numberOfNodes Number of Nodes in the network
     */
    public abstract void generateNetwork (int numberOfNodes);

    /**
     * Calculate the average degree of all the Nodes within the underlying network
     * @return Average Degree
     */
    public double getAverageDegree () {
        return this.getNodes().stream().mapToDouble(Node::getDegree).average().orElse(Double.NaN);
    }

    /**
     * Get the Nodes within the underlying network
     * @return
     */
    public HashSet<Node> getNodes () {
        return this.nodes;
    }

    /**
     * Get the Edges within the underlying network
     * @return
     */
    public HashSet<Edge> getEdges () {
        return this.edges;
    }
}
