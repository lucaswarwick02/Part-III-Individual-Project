package com.lucaswarwick02.models;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import com.lucaswarwick02.components.Node;

public class HelperFunctions {

    static Random r = new Random();

    private HelperFunctions () {}

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
     * Use each model's ModelStates and calcualte the mean and standard deviation
     * for each time step
     * 
     * @return AggregateModelState[]
     */
    public static Map<String, double[]> aggregateResults(StochasticModel[] models, int iterations) {
        HashMap<String, double[]> states = new HashMap<>();

        states.put("Time", new double[iterations]);
        for (Node.State state : Node.getAllStates()) {
            String stateName = Node.StateToString(state);
            states.put(stateName, new double[iterations]);
            states.put(stateName + "_STD", new double[iterations]);
        }

        for (int i = 0; i < iterations; i++) {
            states.get("Time")[i] = i;
            for (Node.State state : Node.getAllStates()) {
                double[] values = new double[models.length];
                String stateName = Node.StateToString(state);

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

    public static void saveToCSV(Map<String, double[]> states, File dataFolder, String fileName) {

        File file = new File(dataFolder, fileName);

        int iterations = states.get("Time").length;

        String header = String.join(",", states.keySet());

        try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
            writer.write(header + "\n");
            for (int i = 0; i < iterations; i++) {
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
     * Randomly pick N nodes
     * 
     * @param list List of Nodes to pick from
     * @param n    Number of Nodes to pick
     * @return List of Nodes of length N
     */
    public static <T> List<T> pickRandomNodes(List<T> list, int n) {
        return r.ints(n, 0, list.size()).mapToObj(list::get).collect(Collectors.toList());
    }

    public static void evaluateAggregateModel (Map<String, double[]> model) {
        int length = model.get("Time").length;

        double finalInfected = model.get("Infected")[length - 1];
        double finalRecovered = model.get("Recovered")[length - 1];
        double finalHospitalised = model.get("Hospitalised")[length - 1];
        double finalDead = model.get("Dead")[length - 1];

        System.out.println("Evaluation: ");
        System.out.println("... Total Infected = " + (finalInfected + finalRecovered + finalHospitalised + finalDead));
        System.out.println("... Total Hospitalised = " + (finalHospitalised + finalDead));
        System.out.println("... Total Dead = " + (finalDead));

        System.out.println("... Total Infected = " + cumulativeInfected(model));
        System.out.println("... Total Hospitalised = " + 0);
        System.out.println("... Total Dead = " + cumulativeDead(model));
    }

    private static double cumulativeInfected (Map<String, double[]> model) {
        int length = model.get("Time").length;

        double initalPopulation = model.get("Susceptible")[0] + model.get("Infected")[0];
        double finalSusceptible = model.get("Susceptible")[length - 1];
        double finalVaccinanted = model.get("Vaccinated")[length - 1];
        
        return initalPopulation - finalSusceptible - finalVaccinanted;
    }

    private static double cumulativeDead (Map<String, double[]> model) {
        int length = model.get("Time").length;
        return model.get("Dead")[length - 1];
    }
}
