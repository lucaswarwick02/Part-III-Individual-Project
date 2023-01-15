package com.lucaswarwick02.threading;

import java.lang.System.Logger;

import com.lucaswarwick02.Main;
import com.lucaswarwick02.models.StochasticModel;
import com.lucaswarwick02.networks.AbstractNetwork;

public class ThreadedModel extends Thread {

    AbstractNetwork network;
    StochasticModel model;

    public boolean isComplete = false;
    
    public ThreadedModel (AbstractNetwork network, StochasticModel model, String threadName, ThreadGroup tg) {
        super(tg, threadName);
        this.network = network;
        this.model = model;
    }

    @Override
    public void run () {
        Main.LOGGER.info("Running run");
        network.generateNetwork();
        network.assignAgeBrackets();

        model.setUnderlyingNetwork(network);
        model.runSimulation();

        isComplete = true;
    }

    public StochasticModel getModel () {
        return this.model;
    }
}
