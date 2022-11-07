package com.lucaswarwick02;

import com.lucaswarwick02.Networks.FullyMixedNetwork;

public class Main {
    public static void main (String[] args) {
        // * Fully-Mixed Network
        FullyMixedNetwork fullyMixedNetwork = new FullyMixedNetwork();

        fullyMixedNetwork.generateNetwork(1000);

        System.out.println("Nodes: " + fullyMixedNetwork.getNodes().size());
        System.out.println("Edges: " + fullyMixedNetwork.getEdges().size());
        System.out.println("Average Degree: " + fullyMixedNetwork.getAverageDegree());
    }
}
