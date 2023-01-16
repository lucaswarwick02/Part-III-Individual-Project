package com.lucaswarwick02.vaccination;

import com.lucaswarwick02.models.StochasticModel;

public interface AbstractStrategy {
    public void performVaccination(StochasticModel stochasticModel);
}
