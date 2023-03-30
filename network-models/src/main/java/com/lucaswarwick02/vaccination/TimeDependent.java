package com.lucaswarwick02.vaccination;

import java.util.List;

import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.components.Node.State;
import com.lucaswarwick02.models.StochasticModel;
import com.lucaswarwick02.vaccination.StrategyFactory.StrategyType;

public class TimeDependent extends AbstractStrategy {

    private int t1;
    private int t2;

    public TimeDependent(float rho, int t1, int t2) {
        super(rho);
        this.t1 = t1;
        this.t2 = t2;
    }

    @Override
    public void performStrategy(StochasticModel model) {
        if (model.getCurrentTime() == this.t1) {
            // Vaccinate first half
            int numberOfNodes = (int) Math.floor(model.getUnderlyingNetwork().getNodes().size() * this.rho);
            List<Node> nodesToVaccinate = model.getUnderlyingNetwork().getNodeByAge(numberOfNodes, true);

            List<Node> nodesSublist = nodesToVaccinate.subList(nodesToVaccinate.size() / 2, nodesToVaccinate.size());
            nodesSublist.forEach(node -> node.setState(State.VACCINATED));

        }
        if (model.getCurrentTime() == this.t2) {
            // Vaccinate second half
            int numberOfNodes = (int) Math.floor(model.getUnderlyingNetwork().getNodes().size() * this.rho);
            List<Node> nodesToVaccinate = model.getUnderlyingNetwork().getNodeByAge(numberOfNodes, true);

            List<Node> nodesSublist = nodesToVaccinate.subList(0, nodesToVaccinate.size() / 2);
            nodesSublist.forEach(node -> node.setState(State.VACCINATED));
        }
    }

    @Override
    public String getStrategyType() {
        return String.format("%s_T1=%d_T2=%d", StrategyType.TIME_DEPENDENT.toString(), this.t1, this.t2);
    }

}
