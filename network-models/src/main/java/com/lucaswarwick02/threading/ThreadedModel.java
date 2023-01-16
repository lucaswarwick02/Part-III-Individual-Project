package com.lucaswarwick02.threading;

import com.lucaswarwick02.models.StochasticModel;
import com.lucaswarwick02.networks.AbstractNetwork;

public class ThreadedModel implements Runnable {

    AbstractNetwork network;
    StochasticModel model;

    /**
     * Used to run a complete simulation in a separate thread.
     * Essentially a 'wrapper' for the StochasticModel to allow for simple
     * multithreading.
     */
    public ThreadedModel(AbstractNetwork network, StochasticModel model) {
        this.network = network;
        this.model = model;
    }

    @Override
    public void run() {
        network.generateNetwork();
        network.assignAgeBrackets();

        model.setUnderlyingNetwork(network);
        model.runSimulation();
    }

    public StochasticModel getModel() {
        return this.model;
    }
}
