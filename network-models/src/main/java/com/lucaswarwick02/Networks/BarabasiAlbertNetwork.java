package com.lucaswarwick02.networks;

import java.util.ArrayList;
import java.util.List;

import com.lucaswarwick02.Main;
import com.lucaswarwick02.components.Node;

public class BarabasiAlbertNetwork extends AbstractNetwork {

    int m;

    public BarabasiAlbertNetwork(int m) {
        super();
        this.m = m;
    }

    @Override
    public void generateNetwork() {
        this.nodes = new ArrayList<>();

        // Create the first m nodes, all connected
        for (int n = 0; n < this.m; n++) {
            Node newNode = new Node(n);
            for (Node existingNode : this.nodes) {
                existingNode.neighbours.add(newNode);
                newNode.neighbours.add(existingNode);
            }
            this.nodes.add(newNode);
        }

        // For each of the remaining (numberOfNodes - m) nodes...
        for (int n = this.m; n < Main.NUMBER_OF_NODES; n++) {
            // ... Create the new Node
            Node newNode = new Node(n);

            double sumOfDegrees = this.nodes.stream().mapToInt(Node::getDegree).sum();

            List<Node> nodesToJoin = new ArrayList<>();
            for (int i = 0; i < this.m; i++) {
                Node node = null;
                do {
                    node = getWeightedNode(sumOfDegrees);
                } while (nodesToJoin.contains(node));
                nodesToJoin.add(node);
            }

            // ... For each existing node...
            for (Node existingNode : nodesToJoin) {
                existingNode.neighbours.add(newNode);
                newNode.neighbours.add(existingNode);
            }

            this.nodes.add(newNode);
        }
    }

    private Node getWeightedNode(double sumOfDegrees) {
        int idx = 0;
        for (double r = Math.random(); idx < this.nodes.size(); ++idx) {
            r -= this.nodes.get(idx).getDegree() / sumOfDegrees;
            if (r <= 0.0)
                break;
        }
        return this.nodes.get(idx);
    }
}