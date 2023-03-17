package com.lucaswarwick02.vaccination;

import java.util.List;
import java.util.stream.Collectors;

import com.lucaswarwick02.HelperFunctions;
import com.lucaswarwick02.components.AgeBracket;
import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.components.Node.State;
import com.lucaswarwick02.models.StochasticModel;
import com.lucaswarwick02.networks.AbstractNetwork;
import com.lucaswarwick02.vaccination.StrategyFactory.StrategyType;

public abstract class AbstractStrategy {

    public float rho;

    protected AbstractStrategy(float rho) {
        this.rho = rho;
    }

    public abstract void performStrategy(StochasticModel model);

    public abstract StrategyType getStrategyType();

    public void logVaccinationDistribution(AbstractNetwork network) {
        for (AgeBracket ageBracket : AgeBracket.values()) {
            List<Node> nodes = network.getNodesFromAgeBracket(ageBracket);
            float nodesVaccinated = nodes.stream().filter(node -> node.getState() == State.VACCINATED)
                    .collect(Collectors.toList()).size();

            HelperFunctions.LOGGER.info("... " + ageBracket.toString() + " = " + (nodesVaccinated / nodes.size()));
        }
    }
}
