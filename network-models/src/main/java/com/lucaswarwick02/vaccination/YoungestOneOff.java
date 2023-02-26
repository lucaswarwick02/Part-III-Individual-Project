package com.lucaswarwick02.vaccination;

import java.util.List;

import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.components.Node.State;
import com.lucaswarwick02.models.StochasticModel;

public class YoungestOneOff implements AbstractStrategy {

    private int timeDelay;
    private float rho;

    public YoungestOneOff (int timeDelay, float rho) {
        this.timeDelay = timeDelay;
        this.rho = rho;
    }

    @Override
    public void performStrategy (StochasticModel model) {
        if (model.getCurrentTime() == timeDelay) {
            int numberOfNodes = (int) Math.floor(model.getUnderlyingNetwork().getNodes().size() * rho);
            List<Node> nodesToVaccinate = model.getUnderlyingNetwork().getYoungestNodes(numberOfNodes);

            nodesToVaccinate.forEach(node -> node.setState(State.VACCINATED));
        }
    }

    @Override
    public String toString () {
        return String.format("YoungestOneOff(%d, %.03f)", this.timeDelay, this.rho);
    }
}
