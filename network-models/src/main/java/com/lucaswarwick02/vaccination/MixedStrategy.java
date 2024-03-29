package com.lucaswarwick02.vaccination;

import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.components.Node.State;
import com.lucaswarwick02.models.StochasticModel;
import com.lucaswarwick02.networks.AbstractNetwork;
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

        int allToVaccinate = this.numberOfNodesToVaccinate();

        int degreeSize = (int) Math.floor(allToVaccinate * this.alpha);
        int ageSize = (int) Math.floor(allToVaccinate * (1 - this.alpha));

        for (Node node : AbstractNetwork.getNodesByDegree(
                model.getUnderlyingNetwork().getNodesFromState(State.SUSCEPTIBLE), degreeSize, true)) {
            node.setState(State.VACCINATED);
        }

        for (Node node : AbstractNetwork.getNodeByAge(model.getUnderlyingNetwork().getNodesFromState(State.SUSCEPTIBLE),
                ageSize, true)) {
            node.setState(State.VACCINATED);
        }
    }

    @Override
    public String getStrategyType() {
        return String.format("%s_ALPHA=%.03f", StrategyType.MIXED.toString(), this.alpha);
    }
}
