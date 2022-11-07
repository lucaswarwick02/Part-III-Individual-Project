package com.lucaswarwick02.Components;

public class Edge {
    private final Node node1; // First Node
    private final Node node2; // Second Node

    /**
     * Create a connection between 2 Nodes
     * @param node1 First Node
     * @param node2 Second Node
     */
    public Edge (Node node1, Node node2) {
        this.node1 = node1;
        this.node2 = node2;
    }

    /**
     * Get the first Node, forming the edge
     * @return First Node
     */
    public Node getNode1 () {
        return this.node1;
    }

    /**
     * Get the second Node, forming the edge
     * @return Second Node
     */
    public Node getNode2 () {
        return this.node2;
    }
}
