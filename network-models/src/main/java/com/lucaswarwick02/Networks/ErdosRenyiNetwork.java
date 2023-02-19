package com.lucaswarwick02.networks;

import java.util.ArrayList;
import java.util.Random;

import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.models.StochasticModel;

public class ErdosRenyiNetwork extends AbstractNetwork {

    Random r = new Random();

    float p;

    public ErdosRenyiNetwork(float p) {
        super();
        this.p = p;
    }

    @Override
    public void generateNetwork() {
        this.nodes = new ArrayList<>();

        for (int n = 0; n < StochasticModel.NUMBER_OF_NODES; n++) {
            Node newNode = new Node(n);
            for (Node existingNode : this.nodes) {
                if (r.nextFloat() < this.p) {
                    existingNode.neighbours.add(newNode);
                    newNode.neighbours.add(existingNode);
                }
            }
            this.nodes.add(newNode);
        }

        if (this.calculateNumberOfComponents() != 1) {
            generateNetwork();
        }
    }
}
