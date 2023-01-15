package com.lucaswarwick02;

import com.lucaswarwick02.networks.NetworkFactory;
import com.lucaswarwick02.networks.NetworkFactory.NetworkType;
import com.lucaswarwick02.threading.ThreadedModel;
import com.lucaswarwick02.models.StochasticModel;
import com.lucaswarwick02.models.VaccinationStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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

        // Setup the Logger to output all logs to a file
        try {
            FileHandler fileHandler = new FileHandler((new File(runFolder, "output.log")).getAbsolutePath(), true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        stochasticSimulation(NetworkType.BARABASI_ALBERT, VaccinationStrategy.NONE, runFolder);
    }

    /**
     * Run, aggreggate and save multiple stochastic simulations to the data folder
     */
    public static void stochasticSimulation(NetworkFactory.NetworkType networkType,
            VaccinationStrategy vaccinationStrategy,
            File runFolder) {

        // Log the information for the network and other key attributes
        NetworkFactory.logNetworkInfo(networkType);
        LOGGER.info("Vaccination Strategy: " + vaccinationStrategy);
        LOGGER.info("Running " + SIMULATIONS + " Simulations, with " + ITERATIONS + " Iterations each");
        LOGGER.info("Number of Nodes = " + NUMBER_OF_NODES);

        // Setup the thread groups for multithreading
        ThreadGroup tg = new ThreadGroup("main");
        int np = Runtime.getRuntime().availableProcessors();

        List<ThreadedModel> threadedModels = new ArrayList<>();

        long start = System.nanoTime();

        for (int i = 0; i < SIMULATIONS; i++) {
            threadedModels.add(new ThreadedModel(
                    NetworkFactory.getNetwork(networkType),
                    new StochasticModel(vaccinationStrategy),
                    "Simulation #" + i,
                    tg)
                );
        }

        int i = 0;
        // Create and start all the simulation threads
        while (i < threadedModels.size()) {
            if (tg.activeCount() < np) {
                ThreadedModel threadedModel = threadedModels.get(i);
                threadedModel.start();
                i++;
            }
            else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }

        // Wait until all the simulation threads are complete
        while (tg.activeCount() > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) { e.printStackTrace(); }
        }

        LOGGER.info("Simulations Complete");

        // Convert the list of simulation threads back into just the models
        StochasticModel[] models = new StochasticModel[SIMULATIONS];
        for (i = 0; i < SIMULATIONS; i++) {
            models[i] = threadedModels.get(i).getModel();
        }

        // Aggregate together all of the simulations
        Map<String, double[]> aggregateStates = HelperFunctions.aggregateStates(models);
        Map<String, double[]> aggregateTotals = HelperFunctions.aggregateTotals(models);

        // Log key information on the simulations statistics
        HelperFunctions.evaluateAggregateModel(aggregateStates, aggregateTotals);

        // Save both the states and totals to the out folder
        HelperFunctions.saveToCSV(aggregateStates, new File(runFolder, "states.csv"));
        HelperFunctions.saveToCSV(aggregateTotals, new File(runFolder, "totals.csv"));

        long end = System.nanoTime();
        LOGGER.info(SIMULATIONS + " simulations took " + ((end - start) / 1e9) + "s");
    }
}
