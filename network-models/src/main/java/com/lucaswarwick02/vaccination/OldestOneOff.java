package com.lucaswarwick02.vaccination;

import java.util.List;

import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.components.Node.State;
import com.lucaswarwick02.models.StochasticModel;

public class OldestOneOff implements AbstractStrategy {

    private int timeDelay;
    private float rho;

    public OldestOneOff (int timeDelay, float rho) {
        this.timeDelay = timeDelay;
        this.rho = rho;
    }

    @Override
    public void performStrategy (StochasticModel model) {
        if (model.getCurrentTime() == timeDelay) {
            int numberOfNodes = (int) Math.floor(model.getUnderlyingNetwork().getNodes().size() * rho);
            List<Node> nodesToVaccinate = model.getUnderlyingNetwork().getOldestNodes(numberOfNodes);

            nodesToVaccinate.forEach(node -> node.setState(State.VACCINATED));
        }
    }

    @Override
    public String toString () {
        return String.format("OldestOneOff(%d, %.03f)", this.timeDelay, this.rho);
    }
}