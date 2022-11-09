package com.lucaswarwick02.Networks;

import com.lucaswarwick02.Components.Node;

public class FullyMixedNetwork extends AbstractNetwork {

    /**
     * Create an empty underlying network
     */
    public FullyMixedNetwork() {
        super();
    }

    /**
     * Generate the Nodes and Edges for the underlying fully-mixed network
     * @param numberOfNodes Number of Nodes in the network
     */
    @Override
    public void generateNetwork(int numberOfNodes) {
        for (int n = 0; n < numberOfNodes; n++) {
            // Create the Node
            Node newNode = new Node(n);

            this.nodes.forEach(networkNode -> networkNode.neighbours.add(newNode));

            // Add node to the list of nodes
            this.nodes.add(newNode);
        }
    }
}