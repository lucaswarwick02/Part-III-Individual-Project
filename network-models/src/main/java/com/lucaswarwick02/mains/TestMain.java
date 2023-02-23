package com.lucaswarwick02.mains;

import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.networks.AbstractNetwork;
import com.lucaswarwick02.networks.NetworkFactory;
import com.lucaswarwick02.networks.NetworkFactory.NetworkType;

public class TestMain {
    public static void main(String[] args) {
        AbstractNetwork network;


        network = NetworkFactory.getNetwork(NetworkType.ERDOS_REYNI);
        network.generateNetwork();

        System.out.println("Top N: ");
        for (Node node : network.getHighestDegreeNodes(5)) {
            System.out.printf("... Node %d: <k> = %d%n", node.ID, node.getDegree());
        }

        System.out.println("Bottom N: ");
        for (Node node : network.getLowestDegreeNodes(5)) {
            System.out.printf("... Node %d: <k> = %d%n", node.ID, node.getDegree());
        }

        System.out.println("Average Degree <k> = " + network.getAverageDegree());


        network = NetworkFactory.getNetwork(NetworkType.BARABASI_ALBERT);
        network.generateNetwork();

        System.out.println("Top N: ");
        for (Node node : network.getHighestDegreeNodes(5)) {
            System.out.printf("... Node %d: <k> = %d%n", node.ID, node.getDegree());
        }

        System.out.println("Bottom N: ");
        for (Node node : network.getLowestDegreeNodes(5)) {
            System.out.printf("... Node %d: <k> = %d%n", node.ID, node.getDegree());
        }

        System.out.println("Average Degree <k> = " + network.getAverageDegree());
    }
}
