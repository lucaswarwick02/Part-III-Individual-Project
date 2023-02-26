package com.lucaswarwick02.mains;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import com.lucaswarwick02.HelperFunctions;
import com.lucaswarwick02.networks.NetworkFactory.NetworkType;
import com.lucaswarwick02.vaccination.AbstractStrategy;
import com.lucaswarwick02.vaccination.StrategyFactory;
import com.lucaswarwick02.vaccination.StrategyFactory.StrategyType;

public class ResultsPart1Main {
    public static void main (String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-4s] %5$s %n");

        // Store arguments from command line
        String rootFolder = args[0];

        float[] rhos = HelperFunctions.createIntervals(0, 1, 0.05f);

        // * Section 1a
        // runSection(rootFolder, "section_1a", NetworkType.ERDOS_REYNI, StrategyType.RANDOM_ONEOFF, 0, rhos);

        // * Section 1b
        // runSection(rootFolder, "section_1b", NetworkType.BARABASI_ALBERT, StrategyType.RANDOM_ONEOFF, 0, rhos);

        // * Section 2a
        // runSection(rootFolder, "section_2a", NetworkType.BARABASI_ALBERT, StrategyType.HIGHEST_ONEOFF, 0, rhos);

        // * Section 2b
        // runSection(rootFolder, "section_2b", NetworkType.BARABASI_ALBERT, StrategyType.LOWEST_ONEOFF, 0, rhos);

        // * Section 3a
        // runSection(rootFolder, "section_3a", NetworkType.BARABASI_ALBERT, StrategyType.OLDEST_ONEOFF, 0, rhos);

        // * Section 3b
        runSection(rootFolder, "section_3b", NetworkType.BARABASI_ALBERT, StrategyType.YOUNGEST_ONEOFF, 0, rhos);
    }

    private static void runSection (String rootFolder, String run, NetworkType networkType, StrategyType strategyType, int timeDelay, float[] rhos) {
        HelperFunctions.LOGGER.info("### " + run + " ###");

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

        for (float rho : rhos) {
            AbstractStrategy strategy = StrategyFactory.getStrategy(strategyType, timeDelay, rho);

            HelperFunctions.LOGGER.info("Running: " + strategy.toString());

            File strategyFolder = new File(runFolder, strategy.toString());
            strategyFolder.mkdir();

            HelperFunctions.stochasticSimulationReduced(networkType, strategy, strategyFolder);
        }
    }
}
