package com.lucaswarwick02.vaccination;

import java.util.ArrayList;
import java.util.List;

import com.lucaswarwick02.components.ModelParameters;
import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.components.Node.State;
import com.lucaswarwick02.models.StochasticModel;
import com.lucaswarwick02.networks.AbstractNetwork;
import com.lucaswarwick02.vaccination.StrategyFactory.StrategyType;

public class HeatmapStrategy extends AbstractStrategy {

    private float r;

    public HeatmapStrategy(float r, float rho) {
        super(rho);
        this.r = r;
    }

    @Override
    public void performStrategy(StochasticModel model) {
        if (model.getCurrentTime() != 0)
            return;

        int numberToIgnore = (int) Math.floor(r * ModelParameters.NUMBER_OF_NODES);

        int numberToVaccinate = Math.min(ModelParameters.NUMBER_OF_NODES - numberToIgnore,
                this.numberOfNodesToVaccinate());

        List<Node> orderedNodes = AbstractNetwork.getNodeByAge(
                model.getUnderlyingNetwork().getNodesFromState(State.SUSCEPTIBLE), ModelParameters.NUMBER_OF_NODES,
                true);

        // Remove the first numberToIgnore from the list
        List<Node> filteredNodes = new ArrayList<>();
        for (int i = 0; i < orderedNodes.size(); i++) {
            if (i < numberToIgnore) {
                continue;
            }

            filteredNodes.add(orderedNodes.get(i));
        }

        // Vaccinate the first numberToVaccinate
        for (int i = 0; i < filteredNodes.size(); i++) {
            if (i < numberToVaccinate) {
                filteredNodes.get(i).setState(State.VACCINATED);
            }
        }

        this.logVaccinationDistribution(model.getUnderlyingNetwork());
    }

    @Override
    public String getStrategyType() {
        return StrategyType.HEATMAP.toString();
    }

}
