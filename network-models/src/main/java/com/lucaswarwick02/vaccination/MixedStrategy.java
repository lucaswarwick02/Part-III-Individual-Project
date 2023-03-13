package com.lucaswarwick02.vaccination;

import com.lucaswarwick02.HelperFunctions;
import com.lucaswarwick02.components.ModelParameters;
import com.lucaswarwick02.models.StochasticModel;

public class MixedStrategy extends AbstractStrategy {

    private float alpha;
    private float rho;

    public MixedStrategy(float alpha, float rho) {
        this.alpha = alpha;
        this.rho = rho;
    }

    @Override
    public void performStrategy(StochasticModel model) {
        if (model.getCurrentTime() != 0)
            return;

        int allToVaccinate = (int) Math.floor(ModelParameters.NUMBER_OF_NODES * this.rho);

        int degreeSize = (int) Math.floor(allToVaccinate * this.alpha);
        int ageSize = (int) Math.floor(allToVaccinate * (1 - this.alpha));

        HelperFunctions.LOGGER.info("alpha = " + this.alpha + ", rho = " + this.rho);
        HelperFunctions.LOGGER.info("... allToVaccinate = " + allToVaccinate);
        HelperFunctions.LOGGER.info("... degreeSize = " + degreeSize);
        HelperFunctions.LOGGER.info("... ageSize = " + ageSize);
    }

}
