package com.lucaswarwick02.vaccination;

import java.util.List;

import com.lucaswarwick02.HelperFunctions;
import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.components.Node.State;
import com.lucaswarwick02.models.StochasticModel;

public class RandomOneOff extends AbstractStrategy {

    private int timeDelay;
    private float rho;

    public RandomOneOff(int timeDelay, float rho) {
        this.timeDelay = timeDelay;
        this.rho = rho;
    }

    @Override
    public void performStrategy(StochasticModel model) {
        // Check if the current time is at the time delay
        if (model.getCurrentTime() == timeDelay) {
            // ... Vaccinate a percentage of the population instantly
            int numberOfNodes = (int) Math.floor(model.getUnderlyingNetwork().getNodes().size() * rho);
            List<Node> nodesToVaccinate = HelperFunctions.pickRandomNodes(
                    model.getUnderlyingNetwork().getNodesFromState(State.SUSCEPTIBLE),
                    numberOfNodes);

            for (Node nodeToVaccinate : nodesToVaccinate) {
                nodeToVaccinate.setState(State.VACCINATED);
            }
        }
    }

    @Override
    public String toString() {
        return String.format("RandomOneOff(%d, %.03f)", this.timeDelay, this.rho);
    }

}
