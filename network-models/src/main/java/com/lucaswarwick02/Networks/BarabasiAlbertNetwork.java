package com.lucaswarwick02.networks;

import java.util.ArrayList;
import java.util.Random;

import com.lucaswarwick02.components.Node;

public class BarabasiAlbertNetwork extends AbstractNetwork {

    Random r = new Random();

    int m;

    public BarabasiAlbertNetwork(int m) {
        super();
        this.m = m;
    }

    @Override
    public void generateNetwork(int numberOfNodes) {
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
        for (int n = this.m; n < numberOfNodes; n++) {
            // ... Create the new Node
            Node newNode = new Node(n);

            double sumOfDegrees = this.nodes.stream().mapToInt(Node::getDegree).sum();

            // ... For each existing node...
            for (Node existingNode : this.nodes) {
                if (newNode.getDegree() > this.m) break;
                // ... ... Get probability of connecting to new node
                double probabilityOfConnecting = existingNode.getDegree() / sumOfDegrees;

                // ... ... Connect based on probability
                if (r.nextFloat() <= probabilityOfConnecting) {
                    existingNode.neighbours.add(newNode);
                    newNode.neighbours.add(existingNode);
                }
            }

            this.nodes.add(newNode);
        }
    }
}