package com.lucaswarwick02.mains;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import com.lucaswarwick02.HelperFunctions;
import com.lucaswarwick02.components.Epidemic;
import com.lucaswarwick02.models.StochasticModel;
import com.lucaswarwick02.networks.NetworkFactory;
import com.lucaswarwick02.networks.NetworkFactory.NetworkType;
import com.lucaswarwick02.vaccination.AbstractStrategy;
import com.lucaswarwick02.vaccination.OneOffStrategy;

public class GridSearchMain {
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
            HelperFunctions.LOGGER.addHandler(fileHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // * OneOff-Strategy
        int[] timeDelays = { 0 };
        double[] percentagesOfPopulation = HelperFunctions.createIntervals(0, 1, 0.05d);

        for (int timeDelay : timeDelays) {
            for (double percentageOfPopulation : percentagesOfPopulation) {
                OneOffStrategy strategy = new OneOffStrategy(timeDelay, percentageOfPopulation);
                HelperFunctions.LOGGER.info("Running: " + strategy.toString());

                File strategyFolder = new File(runFolder, strategy.toString());
                strategyFolder.mkdir();

                stochasticSimulation(NetworkType.BARABASI_ALBERT, strategy, strategyFolder);
            }
        }
    }

    /**
     * Variation of the same function in StochasticMain, but with minimal logging
     * 
     * @param networkType
     * @param runFolder
     */
    public static void stochasticSimulation(NetworkFactory.NetworkType networkType, AbstractStrategy abstractStrategy,
            File runFolder) {

        StochasticModel[] models = new StochasticModel[StochasticModel.SIMULATIONS];
        Epidemic epidemic = Epidemic.loadFromResources("/stochastic.xml");

        // Setup the thread groups for multithreading
        int np = Runtime.getRuntime().availableProcessors();

        ExecutorService executor = Executors.newFixedThreadPool(np);

        for (int i = 0; i < StochasticModel.SIMULATIONS; i++) {
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
