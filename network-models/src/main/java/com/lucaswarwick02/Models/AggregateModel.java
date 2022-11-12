package com.lucaswarwick02.models;

import java.util.ArrayList;
import java.util.List;

import com.lucaswarwick02.models.states.AggregateModelState;
import com.lucaswarwick02.models.states.ModelState;

public class AggregateModel {

    public ModelState[][] modelStatesList;

    private final int iterations;

    public AggregateModel(int numberOfModels, int iterations) {
        modelStatesList = new ModelState[numberOfModels][iterations];
        this.iterations = iterations;
    }

    public AggregateModelState[] aggregateResults() {
        AggregateModelState[] aggregateModelStates = new AggregateModelState[iterations];

        for (int i = 0; i < iterations; i++) {
            List<Float> susceptibleValues = new ArrayList<>();
            List<Float> infectedValues = new ArrayList<>();
            List<Float> recoveredValues = new ArrayList<>();
            List<Float> vaccinatedValues = new ArrayList<>();

            for (ModelState[] modelStates : modelStatesList) {
                susceptibleValues.add(modelStates[i].susceptible());
                infectedValues.add(modelStates[i].infected());
                recoveredValues.add(modelStates[i].recovered());
                vaccinatedValues.add(modelStates[i].vaccinated());
            }

            aggregateModelStates[i] = new AggregateModelState(
                i,
                calculateMean(susceptibleValues),
                calculateStandardDeviation(susceptibleValues),
                calculateMean(infectedValues),
                calculateStandardDeviation(infectedValues),
                calculateMean(recoveredValues),
                calculateStandardDeviation(recoveredValues),
                calculateMean(vaccinatedValues),
                calculateStandardDeviation(vaccinatedValues)
            );
        }

        return aggregateModelStates;
    }

    private float calculateMean (List<Float> list) {
        float sum = 0;
        for (float val : list) sum += val;
        return sum / list.size();
    }

    private float calculateStandardDeviation (List<Float> list) {
        float mean = calculateMean(list);
        float diffSum = 0;
        for (float val : list) diffSum += Math.pow(val - mean, 2);
        return (float) Math.sqrt(diffSum / list.size());
    }
}
