package com.lucaswarwick02.vaccination;

import java.util.List;

import com.lucaswarwick02.HelperFunctions;
import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.components.Node.State;
import com.lucaswarwick02.models.StochasticModel;

public class OneOffStrategy implements AbstractStrategy {

    public OneOffStrategy(int timeDelay, double percentageOfPopulation) {
        this.timeDelay = timeDelay;
        this.percentageOfPopulation = percentageOfPopulation;
    }

    private int timeDelay;

    private double percentageOfPopulation;

    @Override
    public void performStrategy(StochasticModel model) {
        // Check if the current time is at the time delay
        if (model.getCurrentTime() == timeDelay) {
            // ... Vaccinate a percentage of the population instantly
            int numberOfNodes = (int) Math.floor(model.getUnderlyingNetwork().nodes.size() * percentageOfPopulation);
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
        return String.format("oneoff_(%d, %.02f)", this.timeDelay, this.percentageOfPopulation);
    }

}
