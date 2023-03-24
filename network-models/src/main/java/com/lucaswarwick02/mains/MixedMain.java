package com.lucaswarwick02.mains;

import java.io.File;

import com.lucaswarwick02.HelperFunctions;
import com.lucaswarwick02.components.Epidemic;
import com.lucaswarwick02.networks.NetworkFactory.NetworkType;
import com.lucaswarwick02.vaccination.AbstractStrategy;
import com.lucaswarwick02.vaccination.MixedStrategy;

public class MixedMain {
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

        runMixed(runFolder, 0.1f);
        // runMixed(runFolder, 0.25f);
        // runMixed(runFolder, 0.5f);
        // runMixed(runFolder, 0.75f);
    }

    private static void runMixed(File runFolder, float rho) {
        HelperFunctions.LOGGER.info(String.format("rho=%.03f", rho));

        File rhoFolder = new File(runFolder, String.format("rho=%.03f", rho));
        rhoFolder.mkdir();

        float[] alphas = HelperFunctions.createIntervals(0, 1, 0.05f);
        Epidemic epidemic = Epidemic.loadFromResources("/stochastic.xml");

        for (float alpha : alphas) {
            HelperFunctions.LOGGER.info(String.format("... alpha=%.03f", alpha));
            AbstractStrategy strategy = new MixedStrategy(alpha, rho);
            HelperFunctions.stochasticSimulationReduced(NetworkType.BARABASI_ALBERT, strategy, rhoFolder, epidemic,
                    true);
        }
    }
}
