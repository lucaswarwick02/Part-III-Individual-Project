package com.lucaswarwick02.vaccination;

import java.util.List;

import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.components.Node.State;
import com.lucaswarwick02.models.StochasticModel;

public class DegreeStrategy extends AbstractStrategy {

    private float rho;
    private boolean reverse;

    public DegreeStrategy(float rho, boolean reverse) {
        this.rho = rho;
        this.reverse = reverse;
    }

    @Override
    public void performStrategy(StochasticModel model) {
        if (model.getCurrentTime() != 0)
            return;

        int numberOfNodes = (int) Math.floor(model.getUnderlyingNetwork().getNodes().size() * rho);
        List<Node> nodesToVaccinate = model.getUnderlyingNetwork().getNodesByDegree(numberOfNodes, this.reverse);

        nodesToVaccinate.forEach(node -> node.setState(State.VACCINATED));
    }
}
