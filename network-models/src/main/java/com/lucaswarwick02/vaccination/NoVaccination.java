package com.lucaswarwick02.vaccination;

import com.lucaswarwick02.models.StochasticModel;

public class NoVaccination implements AbstractStrategy {

    @Override
    public void performVaccination(StochasticModel stochasticModel) {
        // Do nothing, don't vaccinate anyone
    }
    
}
