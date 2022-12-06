package com.lucaswarwick02;

import com.lucaswarwick02.networks.AbstractNetwork;
import com.lucaswarwick02.networks.NetworkFactory;
import com.lucaswarwick02.standalone.MathematicalComparison;
import com.lucaswarwick02.components.Epidemic;
import com.lucaswarwick02.models.StochasticModel;
import com.lucaswarwick02.models.VaccinationStrategy;

import java.io.File;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {

    public static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static final int ITERATIONS = 150;
    public static final int INITIAL_INFECTED = 3;
    public static final int NUMBER_OF_NODES = 10000;
    public static final int SIMULATIONS = 100;

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-4s] %5$s %n");

        // Store arguments from command line
        String rootFolder = args[0];
        String run = args[1];

        // Create a new data folder for this current run-through
        File dataFolder = new File(rootFolder, "out");
        dataFolder.mkdir();
        File runFolder = new File(dataFolder, run);
        runFolder.mkdir();

        try {
            FileHandler fileHandler = new FileHandler((new File(runFolder, "output.log")).getAbsolutePath(), true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        stochasticSimulation(NetworkFactory.NetworkType.POISSON, VaccinationStrategy.NONE, runFolder);

        // MathematicalComparison mathematicalComparison = new MathematicalComparison();
        // HelperFunctions.saveToCSV(mathematicalComparison.runMathematicalSimulation(),
        // new File(runFolder, "equations.csv"));
        // HelperFunctions.saveToCSV(mathematicalComparison.runStochasticSimulations(),
        // new File(runFolder, "simulations.csv"));
    }

    /**
     * Run, aggreggate and save multiple stochastic simulations to the data folder
     */
    public static void stochasticSimulation(NetworkFactory.NetworkType networkType,
            VaccinationStrategy vaccinationStrategy,
            File runFolder) {

        NetworkFactory.logNetworkInfo(networkType);
        LOGGER.info("Vaccination Strategy: " + vaccinationStrategy);

        LOGGER.info("Running " + SIMULATIONS + " Simulations, with " + ITERATIONS + " Iterations each");
        LOGGER.info("Number of Nodes = " + NUMBER_OF_NODES);

        StochasticModel[] models = new StochasticModel[SIMULATIONS];

        for (int s = 0; s < SIMULATIONS; s++) {
            AbstractNetwork network = NetworkFactory.getNetwork(networkType);
            StochasticModel model = new StochasticModel(vaccinationStrategy);

            network.generateNetwork();
            if (s == SIMULATIONS - 1) {
                LOGGER.info("Average Degree <k> = " + network.getAverageDegree());
                model.epidemic.logInformation();
            }
            System.out.println("Simulation #" + s);

            model.setUnderlyingNetwork(network);
            model.runSimulation();

            models[s] = model;
        }

        LOGGER.info("Simulations Complete");

        Map<String, double[]> aggregateStates = HelperFunctions.aggregateStates(models);
        Map<String, double[]> aggregateTotals = HelperFunctions.aggregateTotals(models);

        HelperFunctions.evaluateAggregateModel(aggregateStates, aggregateTotals);

        HelperFunctions.saveToCSV(aggregateStates, new File(runFolder, "states.csv"));
        HelperFunctions.saveToCSV(aggregateTotals, new File(runFolder, "totals.csv"));
    }
}
