package com.lucaswarwick02.vaccination;

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
        // TODO Check the total number of infected individuals
        // TODO Convert to percentage of population
        // TODO If the percentage is greater than or equal to the threshold, vaccinate
        // TODO ... using oldest first
    }

    @Override
    public String getStrategyType() {
        return String.format("%s_T=%f", StrategyType.THRESHOLD_DEPENDENT.toString(), this.threshold);
    }
}
