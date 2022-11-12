package com.lucaswarwick02.networks;

import java.util.ArrayList;

import com.lucaswarwick02.components.Node;

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
        this.nodes = new ArrayList<>();

        for (int n = 0; n < numberOfNodes; n++) {
            // Create the Node
            Node newNode = new Node(n);

            // Let this node be a neighbour for all the others...
            this.nodes.forEach(networkNode -> networkNode.neighbours.add(newNode));

            // ... and let all the others be a neighbour of this node
            newNode.neighbours.addAll(this.nodes);

            // Add node to the list of nodes
            this.nodes.add(newNode);
        }
    }
}
