package com.lucaswarwick02.mains;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lucaswarwick02.HelperFunctions;
import com.lucaswarwick02.components.Epidemic;
import com.lucaswarwick02.components.ModelParameters;
import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.networks.NetworkFactory.NetworkType;
import com.lucaswarwick02.vaccination.RandomOneOff;

public class MathematicalMain {
    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-4s] %5$s %n");

        // Store arguments from command line
        String rootName = args[0];
        String runName = args[1];

        File dataFolder = new File(rootName, "out");
        dataFolder.mkdir();
        File runFolder = new File(dataFolder, runName);
        runFolder.mkdir();

        Epidemic epidemic = Epidemic.loadFromResources("/mathematical.xml");

        // * Mathematical Section
        HelperFunctions.LOGGER.info("Running mathematical simualtion...");
        Map<String, double[]> mathematicalResults = runMathematicalSimulation(epidemic);
        saveMathematicalToCSV(mathematicalResults, new File(runFolder, "mathematical.csv"));
        HelperFunctions.LOGGER.info("... Done");

        // * Stochastic Section
        HelperFunctions.LOGGER.info("Running " + ModelParameters.SIMULATIONS + " stochastic simulations...");
        HelperFunctions.stochasticSimulationReduced(NetworkType.FULLY_MIXED, new RandomOneOff(0, 0), runFolder,
                epidemic, false);
        HelperFunctions.LOGGER.info("... Done");
    }

    private static Map<String, double[]> runMathematicalSimulation(Epidemic epidemic) {
        Map<String, double[]> states = new HashMap<>();
        states.put("Time", new double[ModelParameters.ITERATIONS]);

        for (Node.State state : Node.getAllStates()) {
            states.put(Node.stateToString(state), new double[ModelParameters.ITERATIONS]);
        }

        states.get("Time")[0] = 0;
        states.get("Susceptible")[0] = (ModelParameters.NUMBER_OF_NODES - (double) ModelParameters.INITIAL_INFECTED);
        states.get("Infected")[0] = (ModelParameters.INITIAL_INFECTED);
        states.get("Recovered")[0] = 0;
        states.get("Hospitalised")[0] = 0;
        states.get("Dead")[0] = 0;

        for (int i = 1; i < ModelParameters.ITERATIONS; i++) {
            states.get("Time")[i] = i;

            // Newly infected
            double arg1 = states.get("Infected")[i - 1] * states.get("Susceptible")[i - 1] * epidemic.infectionRate;

            // Newly recovered
            double arg2 = states.get("Infected")[i - 1] * epidemic.recoveryRate;

            // Nodes that didnt recover, hospitalised
            double arg3 = (states.get("Infected")[i - 1] - arg2) * epidemic.hospitalisationRate;

            // Nodes that were hospitalised, recovered
            double arg4 = states.get("Hospitalised")[i - 1] * epidemic.recoveryRate;

            // Nodes that didnt recover from H, dead
            double arg5 = (states.get("Hospitalised")[i - 1] - arg4) * epidemic.mortalityRate;

            states.get("Susceptible")[i] = states.get("Susceptible")[i - 1] - arg1;
            states.get("Infected")[i] = states.get("Infected")[i - 1] + arg1 - arg2 - arg3;
            states.get("Recovered")[i] = states.get("Recovered")[i - 1] + arg2 + arg4;
            states.get("Hospitalised")[i] = states.get("Hospitalised")[i - 1] + arg3 - arg4 - arg5;
            states.get("Dead")[i] = states.get("Dead")[i - 1] + arg5;
        }

        return normalizeResults(states);
    }

    private static Map<String, double[]> normalizeResults(Map<String, double[]> results) {
        for (String key : results.keySet()) {
            if (key.equals("Time"))
                continue;
            for (int i = 0; i < results.get(key).length; i++) {
                results.get(key)[i] /= ModelParameters.NUMBER_OF_NODES;
            }
        }
        return results;
    }

    private static void saveMathematicalToCSV(Map<String, double[]> states, File file) {
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
}
