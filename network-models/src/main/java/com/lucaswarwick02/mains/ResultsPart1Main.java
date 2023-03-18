package com.lucaswarwick02.mains;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import com.lucaswarwick02.HelperFunctions;
import com.lucaswarwick02.components.Epidemic;
import com.lucaswarwick02.networks.NetworkFactory.NetworkType;
import com.lucaswarwick02.vaccination.AbstractStrategy;
import com.lucaswarwick02.vaccination.StrategyFactory;
import com.lucaswarwick02.vaccination.StrategyFactory.StrategyType;

public class ResultsPart1Main {
    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-4s] %5$s %n");

        // Store arguments from command line
        String rootFolder = args[0];

        // * Full Scale (rho: 0-1)
        float[] rhos = HelperFunctions.createIntervals(0, 1, 0.05f);

        runSection(rootFolder, "section_1a", NetworkType.ERDOS_REYNI, StrategyType.RANDOM, rhos);
        runSection(rootFolder, "section_1b", NetworkType.BARABASI_ALBERT, StrategyType.RANDOM, rhos);
        runSection(rootFolder, "section_2a", NetworkType.BARABASI_ALBERT, StrategyType.HIGHEST, rhos);
        runSection(rootFolder, "section_2b", NetworkType.BARABASI_ALBERT, StrategyType.LOWEST, rhos);
        runSection(rootFolder, "section_3a", NetworkType.BARABASI_ALBERT, StrategyType.OLDEST, rhos);
        runSection(rootFolder, "section_3b", NetworkType.BARABASI_ALBERT, StrategyType.YOUNGEST, rhos);

        // * Reduced Scale (rho: 0-0.2)
        // float[] rhos = HelperFunctions.createIntervals(0, 0.2f, 0.0008f);

        // runSection(rootFolder, "section_1a_reduced", NetworkType.ERDOS_REYNI,
        // StrategyType.RANDOM, rhos);
        // runSection(rootFolder, "section_1b_reduced", NetworkType.BARABASI_ALBERT,
        // StrategyType.RANDOM, rhos);
        // runSection(rootFolder, "section_2a_reduced", NetworkType.BARABASI_ALBERT,
        // StrategyType.HIGHEST, rhos);
        // runSection(rootFolder, "section_2b_reduced", NetworkType.BARABASI_ALBERT,
        // StrategyType.LOWEST, rhos);
        // runSection(rootFolder, "section_3a_reduced", NetworkType.BARABASI_ALBERT,
        // StrategyType.OLDEST, rhos);
        // runSection(rootFolder, "section_3b_reduced", NetworkType.BARABASI_ALBERT,
        // StrategyType.YOUNGEST, rhos);
    }

    private static void runSection(String rootFolder, String run, NetworkType networkType, StrategyType strategyType,
            float[] rhos) {
        HelperFunctions.LOGGER.info("### " + run + " ###");

        Epidemic epidemic = Epidemic.loadFromResources("/stochastic.xml");

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
            AbstractStrategy strategy = StrategyFactory.getStrategy(strategyType, rho);
            HelperFunctions.LOGGER.info(String.format("Running: %.03f", rho));
            HelperFunctions.stochasticSimulationReduced(networkType, strategy, runFolder, epidemic, true);
        }
    }
}
