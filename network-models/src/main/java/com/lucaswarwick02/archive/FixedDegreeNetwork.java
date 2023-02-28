package com.lucaswarwick02.archive;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.lucaswarwick02.HelperFunctions;
import com.lucaswarwick02.components.ModelParameters;
import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.networks.AbstractNetwork;

public class FixedDegreeNetwork extends AbstractNetwork {
    Random r = new Random();

    int degree;

    public FixedDegreeNetwork(int degree) {
        this.degree = degree;
    }

    @Override
    public void generateNetwork() {
        this.nodes = new ArrayList<>();
        for (int n = 0; n < ModelParameters.NUMBER_OF_NODES; n++) {
            Node newNode = new Node(n);
            newNode.stubs = this.degree;
            this.nodes.add(newNode);
        }

        while (numberOfStubs(nodes) > 0) {
            List<Node> nodesWithStubs = nodes.stream().filter(node -> node.stubs > 0).collect(Collectors.toList());
            List<Node> randomNodes = HelperFunctions.pickRandomNodes(nodesWithStubs, 2);
            randomNodes.get(0).neighbours.add(randomNodes.get(1));
            randomNodes.get(0).stubs--;
            randomNodes.get(1).neighbours.add(randomNodes.get(0));
            randomNodes.get(1).stubs--;
        }
    }

    /***
     * Calculate the number of 'stubs' in the distribution sequence
     * 
     * @param nodes
     * @return
     */
    private int numberOfStubs(List<Node> nodes) {
        return nodes.stream().mapToInt(node -> node.stubs).sum();
    }
}