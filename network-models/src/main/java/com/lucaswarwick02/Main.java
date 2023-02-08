package com.lucaswarwick02;

import com.lucaswarwick02.networks.NetworkFactory;
import com.lucaswarwick02.networks.NetworkFactory.NetworkType;
import com.lucaswarwick02.vaccination.VaccinationFactory.VaccinationType;
import com.lucaswarwick02.components.Epidemic;
import com.lucaswarwick02.models.StochasticModel;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

        // Setup the Logger to output all logs to a file
        try {
            FileHandler fileHandler = new FileHandler((new File(runFolder, "output.log")).getAbsolutePath(), true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        stochasticSimulation(NetworkType.BARABASI_ALBERT, VaccinationType.GLOBAL, runFolder);
    }

    /**
     * Run, aggreggate and save multiple stochastic simulations to the data folder
     */
    public static void stochasticSimulation(NetworkFactory.NetworkType networkType,
            VaccinationType vaccinationStrategy,
            File runFolder) {

        // Log the information for the network and other key attributes
        NetworkFactory.logNetworkInfo(networkType);

        LOGGER.info("### Simulation Parameters ###");
        LOGGER.info("... Vaccination Strategy: " + vaccinationStrategy);
        LOGGER.info("... Running " + SIMULATIONS + " Simulations, with " + ITERATIONS + " Iterations each");
        LOGGER.info("... Number of Nodes = " + NUMBER_OF_NODES);

        LOGGER.info("### Running Simulations ###");
        StochasticModel[] models = new StochasticModel[SIMULATIONS];
        Epidemic epidemic = Epidemic.loadFromResources("/stochastic.xml");

        long start = System.nanoTime();

        // Setup the thread groups for multithreading
        int np = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(np);

        for (int i = 0; i < SIMULATIONS; i++) {
            models[i] = new StochasticModel(vaccinationStrategy, epidemic, networkType);
            executor.execute(models[i]);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait until the executor has finished the simulations
        }
        long end = System.nanoTime();
        LOGGER.info("... Completed (" + ((end - start) / 1e9) + "s)");

        LOGGER.info("### Aggregating/Saving Results ###");
        start = System.nanoTime();

        // Aggregate together all of the simulations
        Map<String, double[]> aggregateStates = HelperFunctions.aggregateStates(models);
        Map<String, double[]> aggregateTotals = HelperFunctions.aggregateTotals(models);

        // Log key information on the simulations statistics
        HelperFunctions.evaluateAggregateModel(aggregateStates, aggregateTotals);

        // Save both the states and totals to the out folder
        HelperFunctions.saveToCSV(aggregateStates, new File(runFolder, "states.csv"));
        HelperFunctions.saveToCSV(aggregateTotals, new File(runFolder, "totals.csv"));

        end = System.nanoTime();
        LOGGER.info("... Completed (" + ((end - start) / 1e9) + "s)");
    }

    public static void gridSearchSimulations(NetworkFactory.NetworkType networkType) {
        //
    }
}
