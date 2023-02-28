package com.lucaswarwick02;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import com.lucaswarwick02.components.Epidemic;
import com.lucaswarwick02.components.ModelParameters;
import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.models.StochasticModel;
import com.lucaswarwick02.networks.NetworkFactory;
import com.lucaswarwick02.vaccination.AbstractStrategy;

public class HelperFunctions {

    public static final Logger LOGGER = Logger.getLogger(HelperFunctions.class.getName());

    static Random r = new Random();

    private HelperFunctions() {
    }

    /**
     * Calculate the mean of a lists
     * 
     * @param list List of floats
     * @return
     */
    public static double calculateMean(double[] list) {
        double sum = 0;
        for (double val : list)
            sum += val;

        return sum / list.length;
    }

    /**
     * Calculate the standard deviation of a list
     * 
     * @param list List of floats
     * @return
     */
    public static double calculateStandardDeviation(double[] list) {
        double length = list.length;
        double mean = calculateMean(list);
        double diffSum = 0;
        for (double val : list)
            diffSum += Math.pow(val - mean, 2);
        return Math.sqrt(diffSum / length);
    }

    /**
     * Aggregate together a list of states
     * 
     * @param models List of models
     * @return Aggregate states
     */
    public static Map<String, double[]> aggregateStates(StochasticModel[] models) {
        HashMap<String, double[]> states = new HashMap<>();

        states.put("Time", new double[ModelParameters.ITERATIONS]);
        for (Node.State state : Node.getAllStates()) {
            String stateName = Node.stateToString(state);
            states.put(stateName, new double[ModelParameters.ITERATIONS]);
            states.put(stateName + "_STD", new double[ModelParameters.ITERATIONS]);
        }

        for (int i = 0; i < ModelParameters.ITERATIONS; i++) {
            states.get("Time")[i] = i;
            for (Node.State state : Node.getAllStates()) {
                double[] values = new double[models.length];
                String stateName = Node.stateToString(state);

                for (int m = 0; m < models.length; m++) {
                    values[m] = models[m].states.get(stateName)[i];
                    if (!stateName.equals("Time"))
                        values[m] /= ModelParameters.NUMBER_OF_NODES;
                }

                double mean = calculateMean(values);
                double standardDeviation = calculateStandardDeviation(values);

                states.get(stateName)[i] = mean;
                states.get(stateName + "_STD")[i] = standardDeviation / 2;
            }
        }

        return states;
    }

    /**
     * Aggregate together a list of totals
     * 
     * @param models List of models
     * @return Aggregated totals
     */
    public static Map<String, double[]> aggregateTotals(StochasticModel[] models) {
        HashMap<String, double[]> totals = new HashMap<>();

        Set<String> keys = models[0].totals.keySet();
        for (String key : keys) {
            totals.put(key, new double[ModelParameters.ITERATIONS]);
            totals.put(key + "_STD", new double[ModelParameters.ITERATIONS]);
        }

        for (int i = 0; i < ModelParameters.ITERATIONS; i++) {
            totals.get("Time")[i] = i;
            for (String key : keys) {
                double[] values = new double[models.length];

                for (int m = 0; m < models.length; m++) {
                    values[m] = models[m].totals.get(key)[i];
                    if (!key.equals("Time"))
                        values[m] /= ModelParameters.NUMBER_OF_NODES;
                }

                double mean = calculateMean(values);
                double standardDeviation = calculateStandardDeviation(values) / 2;

                totals.get(key)[i] = mean;
                totals.get(key + "_STD")[i] = standardDeviation;
            }
        }

        return totals;
    }

    /**
     * Save a mapping table to a file
     * 
     * @param states Aggregate states
     * @param file   Aggregate totals
     */
    public static void saveToCSV(Map<String, double[]> states, File file) {
        String header = String.join(",", states.keySet());

        try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
            writer.write(header + "\n");
            for (int i = 0; i < ModelParameters.ITERATIONS; i++) {
                List<String> row = new ArrayList<>();
                for (String string : states.keySet()) {
                    row.add(Double.toString(states.get(string)[i]));
                }
                writer.write(String.join(",", row) + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Randomly pick N items from a list
     * 
     * @param list List to pick from
     * @param n    Number to pick
     * @return List of length N
     */
    public static <T> List<T> pickRandomNodes(List<T> list, int n) {
        // return r.ints(n, 0, list.size()).mapToObj(list::get).collect(Collectors.toList());

        if (list.size() < n) {
            return new ArrayList<>();
        }

        List<T> picked = new ArrayList<>();
        while (picked.size() < n) {
            int randomIndex = r.nextInt(list.size());
            if (!picked.contains(list.get(randomIndex))) {
                picked.add(list.get(randomIndex));
            }
        }

        return picked;
    }

    /**
     * Log the evaluation metrics to the console
     * 
     * @param states Aggregated states
     * @param totals Aggregated totals
     */
    public static void evaluateAggregateModel(Map<String, double[]> states, Map<String, double[]> totals) {
        int length = states.get("Time").length;

        HelperFunctions.LOGGER
                .info(String.format("... Total Infected = %.2f%%", (totals.get("Infected")[length - 1] * 100)));
        HelperFunctions.LOGGER
                .info(String.format("... Total Hospitalised = %.2f%%", (totals.get("Hospitalised")[length - 1] * 100)));
        HelperFunctions.LOGGER.info(String.format("... Total Dead = %.2f%%", (totals.get("Dead")[length - 1] * 100)));
    }

    public static void logPeakInfected(Map<String, double[]> states) {
        IntStream.range(0, states.get("Time").length).reduce((a, b) -> states.get("Infected")[a] < states.get("Infected")[b] ? b : a).ifPresent(ix -> HelperFunctions.LOGGER.info("Peak Infected = " + states.get("Time")[ix]));
    }

    public static float[] createIntervals (float min, float max, float step) {
        int size = (int)Math.ceil((max - min) / step) + 1;
        float[] intervals = new float[size];

        for (int i = 0; i < size; i++) {
            intervals[i] = min + (i * step);
        }

        return intervals;
    }

    /**
     * Variation of the same function in StochasticMain, but with minimal logging
     * 
     * @param networkType
     * @param runFolder
     */
    public static void stochasticSimulationReduced(NetworkFactory.NetworkType networkType, AbstractStrategy abstractStrategy,
            File runFolder, Epidemic epidemic) {

        StochasticModel[] models = new StochasticModel[ModelParameters.SIMULATIONS];

        // Setup the thread groups for multithreading
        int np = Runtime.getRuntime().availableProcessors();

        ExecutorService executor = Executors.newFixedThreadPool(np);

        for (int i = 0; i < ModelParameters.SIMULATIONS; i++) {
            models[i] = new StochasticModel(epidemic, networkType, abstractStrategy);
            executor.execute(models[i]);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait until the executor has finished the simulations
        }

        // Aggregate together all of the simulations
        Map<String, double[]> aggregateStates = HelperFunctions.aggregateStates(models);
        Map<String, double[]> aggregateTotals = HelperFunctions.aggregateTotals(models);

        // Log key information on the simulations statistics
        HelperFunctions.evaluateAggregateModel(aggregateStates, aggregateTotals);

        // Save both the states and totals to the out folder
        HelperFunctions.saveToCSV(aggregateStates, new File(runFolder, "states.csv"));
        HelperFunctions.saveToCSV(aggregateTotals, new File(runFolder, "totals.csv"));

        HelperFunctions.LOGGER.info("... Completed");
    }
}
