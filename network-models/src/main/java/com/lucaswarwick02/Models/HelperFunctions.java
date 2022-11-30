package com.lucaswarwick02.models;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.lucaswarwick02.Main;
import com.lucaswarwick02.components.Node;

public class HelperFunctions {

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

        states.put("Time", new double[Main.ITERATIONS]);
        for (Node.State state : Node.getAllStates()) {
            String stateName = Node.stateToString(state);
            states.put(stateName, new double[Main.ITERATIONS]);
            states.put(stateName + "_STD", new double[Main.ITERATIONS]);
        }

        for (int i = 0; i < Main.ITERATIONS; i++) {
            states.get("Time")[i] = i;
            for (Node.State state : Node.getAllStates()) {
                double[] values = new double[models.length];
                String stateName = Node.stateToString(state);

                for (int m = 0; m < models.length; m++) {
                    values[m] = models[m].states.get(stateName)[i];
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
            totals.put(key, new double[Main.ITERATIONS]);
            totals.put(key + "_STD", new double[Main.ITERATIONS]);
        }

        for (int i = 0; i < Main.ITERATIONS; i++) {
            totals.get("Time")[i] = i;
            for (String key : keys) {
                double[] values = new double[models.length];

                for (int m = 0; m < models.length; m++) {
                    values[m] = models[m].totals.get(key)[i];
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
            for (int i = 0; i < Main.ITERATIONS; i++) {
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
        return r.ints(n, 0, list.size()).mapToObj(list::get).collect(Collectors.toList());
    }

    /**
     * Log the evaluation metrics to the console
     * 
     * @param states Aggregated states
     * @param totals Aggregated totals
     */
    public static void evaluateAggregateModel(Map<String, double[]> states, Map<String, double[]> totals) {
        int length = states.get("Time").length;

        Main.LOGGER.info("Evaluation: ");
        Main.LOGGER.info("... Total Infected = " + totals.get("Infected")[length - 1]);
        Main.LOGGER.info("... Total Hospitalised = " + totals.get("Hospitalised")[length - 1]);
        Main.LOGGER.info("... Total Dead = " + totals.get("Dead")[length - 1]);
    }
}
