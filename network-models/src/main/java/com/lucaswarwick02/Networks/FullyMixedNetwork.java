package com.lucaswarwick02.Networks;

import com.lucaswarwick02.Components.Edge;
import com.lucaswarwick02.Components.Node;

public class FullyMixedNetwork extends AbstractNetwork {

    public FullyMixedNetwork() {
        super();
    }

    @Override
    public void generateNetwork(int numberOfNodes) {
        for (int n = 0; n < numberOfNodes; n++) {
            // Create the Node
            Node node1 = new Node(n);
            // For each node in the graph...
            int k = 0;
            for (Node node2 : this.nodes) {
                // ... Create an edge with this new node
                this.edges.add(new Edge(node1, node2));
                node1.setDegree( node1.getDegree() + 1 );
                node2.setDegree( node2.getDegree() + 1 );
            }
            // Add node to the list of nodes
            this.nodes.add(node1);
        }
    }
}
