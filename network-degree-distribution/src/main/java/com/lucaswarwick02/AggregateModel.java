package com.lucaswarwick02;

import java.util.Arrays;
import java.util.stream.Stream;

public class AggregateModel {

    public AggregateModelState[] aggregateModelStates;

    public AggregateModel (Model[] models) {
        aggregateModels( models );
    }

    private void aggregateModels (Model[] models) {
        int iterations = models[0].getModelStates().length;


        aggregateModelStates = new AggregateModelState[iterations];

        for (int time = 0; time < iterations; time++) {
            int[] susceptibleArray = new int[models.length];
            int[] infectedArray = new int[models.length];
            int[] removedArray = new int[models.length];

            for (int m = 0; m < models.length; m++) {
                susceptibleArray[m] = models[m].getModelStates()[time].getSusceptible();
                infectedArray[m] = models[m].getModelStates()[time].getInfected();
                removedArray[m] = models[m].getModelStates()[time].getRemoved();
            }

            aggregateModelStates[time] = new AggregateModelState(
                    time,
                    Arrays.stream(susceptibleArray).min().getAsInt(),
                    Arrays.stream(susceptibleArray).max().getAsInt(),
                    Arrays.stream(susceptibleArray).average().orElse(Double.NaN),
                    Arrays.stream(infectedArray).min().getAsInt(),
                    Arrays.stream(infectedArray).max().getAsInt(),
                    Arrays.stream(infectedArray).average().orElse(Double.NaN),
                    Arrays.stream(removedArray).min().getAsInt(),
                    Arrays.stream(removedArray).max().getAsInt(),
                    Arrays.stream(removedArray).average().orElse(Double.NaN)

            );
        }
    }
}
