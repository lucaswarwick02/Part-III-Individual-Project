package com.lucaswarwick02.vaccination;

import java.util.List;

import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.components.Node.State;
import com.lucaswarwick02.models.StochasticModel;
import com.lucaswarwick02.networks.AbstractNetwork;
import com.lucaswarwick02.vaccination.StrategyFactory.StrategyType;

public class DegreeStrategy extends AbstractStrategy {

    private boolean reverse;

    public DegreeStrategy(float rho, boolean reverse) {
        super(rho);
        this.reverse = reverse;
    }

    @Override
    public void performStrategy(StochasticModel model) {
        if (model.getCurrentTime() != 0)
            return;

        int numberOfNodes = this.numberOfNodesToVaccinate();

        List<Node> nodesToVaccinate = AbstractNetwork.getNodesByDegree(
                model.getUnderlyingNetwork().getNodesFromState(State.SUSCEPTIBLE), numberOfNodes, this.reverse);

        nodesToVaccinate.forEach(node -> node.setState(State.VACCINATED));
    }

    @Override
    public String getStrategyType() {
        if (this.reverse) {
            return StrategyType.HIGHEST.toString();
        } else {
            return StrategyType.LOWEST.toString();
        }
    }
}
