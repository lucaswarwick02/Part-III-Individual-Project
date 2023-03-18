package com.lucaswarwick02.vaccination;

import java.util.List;

import com.lucaswarwick02.HelperFunctions;
import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.components.Node.State;
import com.lucaswarwick02.models.StochasticModel;
import com.lucaswarwick02.vaccination.StrategyFactory.StrategyType;

public class RandomStrategy extends AbstractStrategy {

    public RandomStrategy(float rho) {
        super(rho);
    }

    @Override
    public void performStrategy(StochasticModel model) {
        if (model.getCurrentTime() != 0)
            return;

        int numberOfNodes = (int) Math.floor(model.getUnderlyingNetwork().getNodes().size() * rho);
        List<Node> nodesToVaccinate = HelperFunctions.pickRandomNodes(
                model.getUnderlyingNetwork().getNodesFromState(State.SUSCEPTIBLE),
                numberOfNodes);

        nodesToVaccinate.forEach(node -> node.setState(State.VACCINATED));
    }

    @Override
    public String getStrategyType() {
        return StrategyType.RANDOM.toString();
    }
}
