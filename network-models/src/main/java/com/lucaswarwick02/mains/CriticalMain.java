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

public class CriticalMain {
    public static void main (String[] args) {
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

        determineCriticalPoint(runFolder, NetworkType.BARABASI_ALBERT, StrategyType.RANDOM_ONEOFF, 0, 0.6f);
    }

    private static void determineCriticalPoint (File runFolder, NetworkType networkType, StrategyType strategyType, int timeDelay, float rho) {
        float infectionRateLowerBount = 0f;
        float infectionRateUpperBound = 0.03f;
        float infectionStepSize = 0.0025f;

        float recoveryRate = 0.06f;
        float hospitalisationRate = 0.04f;
        float mortalityRate = 0.1f;

        for (float infectionRate : HelperFunctions.createIntervals(infectionRateLowerBount, infectionRateUpperBound, infectionStepSize)) {
            AbstractStrategy strategy = StrategyFactory.getStrategy(strategyType, timeDelay, rho);

            Epidemic epidemic = new Epidemic();
            epidemic.infectionRate = infectionRate;
            epidemic.recoveryRate = recoveryRate;
            epidemic.hospitalisationRate = hospitalisationRate;
            epidemic.mortalityRate = mortalityRate;

            String runName = String.format("RandomOneOff(%d, %.03f, %.04f)", timeDelay, rho, infectionRate);

            HelperFunctions.LOGGER.info("Running: " + runName);

            File specificRunFolder = new File(runFolder, runName);
            specificRunFolder.mkdir();

            HelperFunctions.stochasticSimulationReduced(networkType, strategy, specificRunFolder, epidemic);
        }
    }
}
