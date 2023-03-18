package com.lucaswarwick02.vaccination;

import com.lucaswarwick02.components.ModelParameters;
import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.components.Node.State;
import com.lucaswarwick02.models.StochasticModel;
import com.lucaswarwick02.vaccination.StrategyFactory.StrategyType;

public class MixedStrategy extends AbstractStrategy {

    private float alpha;

    public MixedStrategy(float alpha, float rho) {
        super(rho);
        this.alpha = alpha;
    }

    @Override
    public void performStrategy(StochasticModel model) {
        if (model.getCurrentTime() != 0)
            return;

        int allToVaccinate = (int) Math.floor(ModelParameters.NUMBER_OF_NODES * this.rho);

        int degreeSize = (int) Math.floor(allToVaccinate * this.alpha);
        int ageSize = (int) Math.floor(allToVaccinate * (1 - this.alpha));

        for (Node node : model.getUnderlyingNetwork().getNodesByDegree(degreeSize, true)) {
            node.setState(State.VACCINATED);
        }

        for (Node node : model.getUnderlyingNetwork().getNodeByAge(ageSize, true)) {
            node.setState(State.VACCINATED);
        }
    }

    @Override
    public String getStrategyType() {
        return String.format("%s_ALPHA=%.03f", StrategyType.MIXED.toString(), this.alpha);
    }
}
