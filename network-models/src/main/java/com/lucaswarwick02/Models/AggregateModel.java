package com.lucaswarwick02.models;

import com.lucaswarwick02.models.states.AggregateModelState;
import com.lucaswarwick02.models.states.ModelState;
import java.util.ArrayList;
import java.util.List;

/**
 * Aggregate model for Averaging a series of Models together
 */
public class AggregateModel {

    public ModelState[][] modelStatesList; // List of each Model's ModelStates

    private final int iterations; // Number of time steps each model performed

    /**
     * 
     * @param numberOfModels Number of Models
     * @param iterations Number fo time steps each model performed
     */
    public AggregateModel(int numberOfModels, int iterations) {
        modelStatesList = new ModelState[numberOfModels][iterations];
        this.iterations = iterations;
    }

    /**
     * Use each model's ModelStates and calcualte the mean and standard deviation for each time step
     * @return AggregateModelState[]
     */
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

    /**
     * Calculate the mean of a lists
     * @param list List of floats
     * @return
     */
    private float calculateMean (List<Float> list) {
        float sum = 0;
        for (float val : list) sum += val;
        return sum / list.size();
    }

    /**
     * Calculate the standard deviation of a list
     * @param list List of floats
     * @return 
     */
    private float calculateStandardDeviation (List<Float> list) {
        float mean = calculateMean(list);
        float diffSum = 0;
        for (float val : list) diffSum += Math.pow(val - mean, 2);
        return (float) Math.sqrt(diffSum / list.size());
    }
}
