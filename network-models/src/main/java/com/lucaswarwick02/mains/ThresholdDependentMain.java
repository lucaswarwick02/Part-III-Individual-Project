package com.lucaswarwick02.mains;

import java.io.File;

import com.lucaswarwick02.HelperFunctions;
import com.lucaswarwick02.components.Epidemic;
import com.lucaswarwick02.networks.NetworkFactory.NetworkType;

public class ThresholdDependentMain {

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

        runThresholdDependent(runFolder);
    }

    private static void runThresholdDependent(File runFolder) {
        float[] thresholds = HelperFunctions.createIntervals(0.0f, 1.0f, 0.1f);
        float[] rhos = HelperFunctions.createIntervals(0.000f, 1.000f, 0.1f);

        Epidemic epidemic = Epidemic.loadFromResources("/stochastic.xml");

        for (float threshold : thresholds) {
            for (float rho : rhos) {
                HelperFunctions.LOGGER.info(String.format("... threshold=%.03f, rho=%.03f", threshold, rho));
                HelperFunctions.stochasticSimulationReducedThreshold(NetworkType.BARABASI_ALBERT, rho, threshold,
                        runFolder,
                        epidemic,
                        true);
            }
        }
    }

}
