package com.lucaswarwick02.mains;

import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.networks.AbstractNetwork;
import com.lucaswarwick02.networks.NetworkFactory;
import com.lucaswarwick02.networks.NetworkFactory.NetworkType;

public class TestMain {
    public static void main(String[] args) {
        AbstractNetwork ba = NetworkFactory.getNetwork(NetworkType.BARABASI_ALBERT);
        ba.generateNetwork();

        System.out.println("Top N: ");
        for (Node node : ba.getHighestDegreeNodes(10)) {
            System.out.printf("... Node %d: <k> = %d%n", node.ID, node.getDegree());
        }

        System.out.println("Bottom N: ");
        for (Node node : ba.getLowestDegreeNodes(10)) {
            System.out.printf("... Node %d: <k> = %d%n", node.ID, node.getDegree());
        }
    }
}
