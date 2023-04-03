package com.lucaswarwick02.vaccination;

import java.util.List;

import com.lucaswarwick02.components.ModelParameters;
import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.models.StochasticModel;
import com.lucaswarwick02.vaccination.StrategyFactory.StrategyType;

public class ThresholdDependent extends AbstractStrategy {

    private float threshold;

    public ThresholdDependent(float rho, int threshold) {
        super(rho);
        this.threshold = threshold;
    }

    @Override
    public void performStrategy(StochasticModel model) {
        if (model.getCurrentTime() == 0)
            return;

        float infectedPercentage = model.totals.get("Infected")[model.getCurrentTime() - 1]
                / (float) ModelParameters.NUMBER_OF_NODES;

        if (infectedPercentage >= this.threshold) {
            int numberOfNodes = (int) Math.floor(model.getUnderlyingNetwork().getNodes().size() * this.rho);
            List<Node> nodesToVaccinate = model.getUnderlyingNetwork().getNodeByAge(numberOfNodes, true);
            nodesToVaccinate.forEach(node -> node.setState(Node.State.VACCINATED));
        }
    }

    @Override
    public String getStrategyType() {
        return String.format("%s_T=%f", StrategyType.THRESHOLD_DEPENDENT.toString(), this.threshold);
    }
}
