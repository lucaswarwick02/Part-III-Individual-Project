package com.lucaswarwick02.vaccination;

import java.util.List;

import com.lucaswarwick02.components.ModelParameters;
import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.components.Node.State;
import com.lucaswarwick02.models.StochasticModel;
import com.lucaswarwick02.networks.AbstractNetwork;
import com.lucaswarwick02.vaccination.StrategyFactory.StrategyType;

public class ThresholdDependent extends AbstractStrategy {

    private float threshold;

    private boolean hasVaccinationOccured;

    public ThresholdDependent(float rho, float threshold) {
        super(rho);
        this.threshold = threshold;
    }

    @Override
    public void initialiseStrategy() {
        this.hasVaccinationOccured = false;
    }

    @Override
    public void performStrategy(StochasticModel model) {
        if (model.getCurrentTime() == 0 || hasVaccinationOccured) {
            return;
        }
        // Percentage of infected required for vaccianation
        float infectedPercentage = model.totals.get("Infected")[model.getCurrentTime() - 1]
                / (float) ModelParameters.NUMBER_OF_NODES;

        // If the percentage of people infected is greater than the threshold, vaccinate
        if (infectedPercentage >= this.threshold) {
            // Vaccinate a percentage of the population
            int numberOfNodes = numberOfNodesToVaccinate();

            // Get a random sample of nodes to vaccinate
            List<Node> nodesToVaccinate = AbstractNetwork.getNodeByAge(
                    model.getUnderlyingNetwork().getNodesFromState(State.SUSCEPTIBLE), numberOfNodes, false);

            // Vaccinate the nodes
            nodesToVaccinate.forEach(node -> node.setState(Node.State.VACCINATED));

            // Update flag
            hasVaccinationOccured = true;
        }
    }

    @Override
    public String getStrategyType() {
        return String.format("%s_T=%f", StrategyType.THRESHOLD_DEPENDENT.toString(), this.threshold);
    }
}
