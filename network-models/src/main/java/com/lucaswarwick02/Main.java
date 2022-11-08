package com.lucaswarwick02;

import com.lucaswarwick02.Networks.FullyMixedNetwork;

public class Main {
    public static void main (String[] args) {
        // * Fully-Mixed Network
        FullyMixedNetwork fullyMixedNetwork = new FullyMixedNetwork();
        fullyMixedNetwork.generateNetwork(1000);

        // * SIR Model
        SIRModel model = new SIRModel(fullyMixedNetwork, 0.0004f, 0.035f);
        model.runSimulation(100, 3);
    }
}
