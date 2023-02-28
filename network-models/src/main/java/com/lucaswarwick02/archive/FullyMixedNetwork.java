package com.lucaswarwick02.archive;

import com.lucaswarwick02.components.ModelParameters;
import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.networks.AbstractNetwork;

import java.util.ArrayList;

/**
 * Fully homogeneous network. All nodes are connected to eachother
 */
public class FullyMixedNetwork extends AbstractNetwork {

    /**
     * Create an empty underlying network
     */
    public FullyMixedNetwork() {
        super();
    }

    /**
     * Generate the Nodes and Edges for the underlying fully-mixed network
     * 
     * @param numberOfNodes Number of Nodes in the network
     */
    @Override
    public void generateNetwork() {
        this.nodes = new ArrayList<>();

        for (int n = 0; n < ModelParameters.NUMBER_OF_NODES; n++) {
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
