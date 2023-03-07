package com.lucaswarwick02.vaccination;

import com.lucaswarwick02.components.ModelParameters;
import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.components.Node.State;
import com.lucaswarwick02.models.StochasticModel;

public class MixedStrategy extends AbstractStrategy {

    private float alpha;

    public MixedStrategy(float alpha) {
        this.alpha = alpha;
    }

    @Override
    public void performStrategy(StochasticModel model) {
        if (model.getCurrentTime() != 0)
            return;

        int partSize = (int) Math.floor(ModelParameters.NUMBER_OF_NODES * this.alpha);

        // Vacciante the highest N * alpha degree nodes
        for (Node node : model.getUnderlyingNetwork().getHighestDegreeNodes(partSize)) {
            node.setState(State.VACCINATED);
        }

        // Vaccinate the oldest N * alpha degree nodes
        for (Node node : model.getUnderlyingNetwork().getOldestNodes(partSize)) {
            node.setState(State.VACCINATED);
        }
    }

}
