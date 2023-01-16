package com.lucaswarwick02.vaccination;

import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.models.StochasticModel;

public class GlobalVaccination implements AbstractStrategy {

    @Override
    public void performVaccination(StochasticModel stochasticModel) {
        float vaccinationRate = 0.0075f;

        // With the leftover nodes, vaccinate them
        for (Node susceptibleNode : stochasticModel.getUnderlyingNetwork().getNodesFromState(Node.State.SUSCEPTIBLE)) {
            if (stochasticModel.getRandom().nextFloat() <= vaccinationRate)
                susceptibleNode.state = Node.State.VACCINATED;
        }
    }
    
}
