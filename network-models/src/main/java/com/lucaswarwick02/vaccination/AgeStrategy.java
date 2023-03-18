package com.lucaswarwick02.vaccination;

import java.util.List;

import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.components.Node.State;
import com.lucaswarwick02.models.StochasticModel;
import com.lucaswarwick02.vaccination.StrategyFactory.StrategyType;

public class AgeStrategy extends AbstractStrategy {

    private boolean reverse;

    /**
     * Vaccinates at time step 0 by age
     * 
     * @param rho     Percentage to vaccinate
     * @param reverse Whether to reverse the order
     */
    public AgeStrategy(float rho, boolean reverse) {
        super(rho);
        this.reverse = reverse;
    }

    @Override
    public void performStrategy(StochasticModel model) {
        if (model.getCurrentTime() != 0)
            return;

        int numberOfNodes = (int) Math.floor(model.getUnderlyingNetwork().getNodes().size() * this.rho);

        List<Node> nodesToVaccinate = model.getUnderlyingNetwork().getNodeByAge(numberOfNodes, this.reverse);

        nodesToVaccinate.forEach(node -> node.setState(State.VACCINATED));
    }

    @Override
    public String getStrategyType() {
        if (this.reverse) {
            return StrategyType.OLDEST.toString();
        } else {
            return StrategyType.YOUNGEST.toString();
        }
    }
}
