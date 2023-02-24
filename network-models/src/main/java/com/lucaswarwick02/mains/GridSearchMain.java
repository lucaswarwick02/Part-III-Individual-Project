package com.lucaswarwick02.mains;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import com.lucaswarwick02.HelperFunctions;
import com.lucaswarwick02.networks.NetworkFactory.NetworkType;
import com.lucaswarwick02.vaccination.AbstractStrategy;
import com.lucaswarwick02.vaccination.LowestOneOff;

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
        float[] rhos = HelperFunctions.createIntervals(0, 1, 0.05f);

        for (int timeDelay : timeDelays) {
            for (float rho : rhos) {
                AbstractStrategy strategy = new LowestOneOff(timeDelay, rho);
                HelperFunctions.LOGGER.info("Running: " + strategy.toString());

                File strategyFolder = new File(runFolder, strategy.toString());
                strategyFolder.mkdir();

                HelperFunctions.stochasticSimulationReduced(NetworkType.BARABASI_ALBERT, strategy, strategyFolder);
            }
        }
    }
}
