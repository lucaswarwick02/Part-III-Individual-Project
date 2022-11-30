package com.lucaswarwick02;

import com.lucaswarwick02.networks.AbstractNetwork;
import com.lucaswarwick02.networks.NetworkFactory;
import com.lucaswarwick02.networks.NetworkType;
import com.lucaswarwick02.models.HelperFunctions;
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
    public static final int NUMBER_OF_NODES = 2500;
    public static final int SIMULATIONS = 100;

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-4s] %5$s %n");

        // Store arguments from command line
        String rootFolder = args[0];
        String run = args[1];

        // Create a new data folder for this current run-through
        File dataFolder = new File(rootFolder, "data");
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

        stochasticSimulation(NetworkType.SCALE_FREE, VaccinationStrategy.NONE, runFolder);
    }

    /**
     * Run, aggreggate and save multiple stochastic simulations to the data folder
     */
    public static void stochasticSimulation(NetworkType networkType, VaccinationStrategy vaccinationStrategy,
            File runFolder) {

        NetworkFactory.logNetworkInfo(networkType);
        LOGGER.info("Vaccination Strategy: " + vaccinationStrategy);
        LOGGER.info("Parameters: ");
        LOGGER.info("... INFECTION_RATE: " + StochasticModel.INFECTION_RATE);
        LOGGER.info("... RECOVERY_RATE: " + StochasticModel.RECOVERY_RATE);
        LOGGER.info("... HOSPITALISATION_RATE: " + StochasticModel.HOSPITALISATION_RATE);
        LOGGER.info("... MORTALITY_RATE: " + StochasticModel.MORTALITY_RATE);

        LOGGER.info("Running " + SIMULATIONS + " Simulations, with " + ITERATIONS + " Iterations each");

        StochasticModel[] models = new StochasticModel[SIMULATIONS];

        for (int s = 0; s < SIMULATIONS; s++) {
            AbstractNetwork network = NetworkFactory.getNetwork(networkType);
            StochasticModel model = new StochasticModel(vaccinationStrategy);

            network.generateNetwork();

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
