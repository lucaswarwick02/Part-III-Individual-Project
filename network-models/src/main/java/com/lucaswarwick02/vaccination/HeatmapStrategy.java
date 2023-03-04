package com.lucaswarwick02.vaccination;

import java.util.ArrayList;
import java.util.List;

import com.lucaswarwick02.HelperFunctions;
import com.lucaswarwick02.components.ModelParameters;
import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.components.Node.State;
import com.lucaswarwick02.models.StochasticModel;

public class HeatmapStrategy implements AbstractStrategy {

    private float r;
    private float rho;

    public HeatmapStrategy(float r, float rho) {
        this.r = r;
        this.rho = rho;
    }

    @Override
    public void performStrategy(StochasticModel model) {
        if (model.getCurrentTime() != 0)
            return;

        int numberToIgnore = (int) Math.floor(r * ModelParameters.NUMBER_OF_NODES);

        int numberToVaccinate = Math.min(ModelParameters.NUMBER_OF_NODES - numberToIgnore,
                (int) Math.floor(rho * ModelParameters.NUMBER_OF_NODES));

        List<Node> orderedNodes = model.getUnderlyingNetwork().getOldestNodes(ModelParameters.NUMBER_OF_NODES);

        // Remove the first numberToIgnore from the list
        List<Node> filteredNodes = new ArrayList<>();
        for (int i = 0; i < orderedNodes.size(); i++) {
            if (i < numberToIgnore)
                continue;

            filteredNodes.add(orderedNodes.get(i));
        }

        // Vaccinate the first numberToVaccinate
        for (int i = 0; i < filteredNodes.size(); i++) {
            if (i < numberToVaccinate) {
                orderedNodes.get(i).setState(State.VACCINATED);
            }
        }

        HelperFunctions.LOGGER.info("Ignored = " + (ModelParameters.NUMBER_OF_NODES - filteredNodes.size())
                + ", Vaccinated = " + model.getUnderlyingNetwork().getNodesFromState(State.VACCINATED).size());
    }

}
