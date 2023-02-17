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
import com.lucaswarwick02.vaccination.OneOffStrategy;

public class StochasticMain {

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

        stochasticSimulation(NetworkType.BARABASI_ALBERT, runFolder);
    }

    /**
     * Run, aggreggate and save multiple stochastic simulations to the data folder
     */
    public static void stochasticSimulation(NetworkFactory.NetworkType networkType,
            File runFolder) {

        // Log the information for the network and other key attributes
        NetworkFactory.logNetworkInfo(networkType);

        HelperFunctions.LOGGER.info("### Simulation Parameters ###");

        HelperFunctions.LOGGER.info(
                "... Running " + StochasticModel.SIMULATIONS + " Simulations, with " + StochasticModel.ITERATIONS
                        + " Iterations each");
        HelperFunctions.LOGGER.info("... Number of Nodes = " + StochasticModel.NUMBER_OF_NODES);

        HelperFunctions.LOGGER.info("### Running Simulations ###");
        StochasticModel[] models = new StochasticModel[StochasticModel.SIMULATIONS];
        Epidemic epidemic = Epidemic.loadFromResources("/stochastic.xml");

        long start = System.nanoTime();

        int np = Runtime.getRuntime().availableProcessors();

        try (ExecutorService executor = Executors.newFixedThreadPool(np)) {
            for (int i = 0; i < StochasticModel.SIMULATIONS; i++) {
                models[i] = new StochasticModel(epidemic, networkType, new OneOffStrategy(0, 0));
                executor.execute(models[i]);
            }
            executor.shutdown();
            while (!executor.isTerminated()) {
                // Wait until the executor has finished the simulations
            }
        }
        long end = System.nanoTime();
        HelperFunctions.LOGGER.info("... Completed (" + ((end - start) / 1e9) + "s)");

        HelperFunctions.LOGGER.info("### Aggregating/Saving Results ###");
        start = System.nanoTime();

        // Aggregate together all of the simulations
        Map<String, double[]> aggregateStates = HelperFunctions.aggregateStates(models);
        Map<String, double[]> aggregateTotals = HelperFunctions.aggregateTotals(models);

        // Log key information on the simulations statistics
        HelperFunctions.evaluateAggregateModel(aggregateStates, aggregateTotals);
        HelperFunctions.logPeakInfected(aggregateStates);

        // Save both the states and totals to the out folder
        HelperFunctions.saveToCSV(aggregateStates, new File(runFolder, "states.csv"));
        HelperFunctions.saveToCSV(aggregateTotals, new File(runFolder, "totals.csv"));

        end = System.nanoTime();
        HelperFunctions.LOGGER.info("... Completed (" + ((end - start) / 1e9) + "s)");
    }
}
