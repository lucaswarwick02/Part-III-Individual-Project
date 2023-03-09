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

        // Iterate through alphas
        float[] alphas = HelperFunctions.createIntervals(0, 0.35f, 0.025f);

        for (float alpha : alphas) {
            HelperFunctions.LOGGER.info(String.format("alpha = %.03f", alpha));

            File strategyFolder = new File(runFolder, String.format("a=%.03f", alpha));
            strategyFolder.mkdir();

            AbstractStrategy strategy = new MixedStrategy(alpha);
            Epidemic epidemic = Epidemic.loadFromResources("/stochastic.xml");

            HelperFunctions.stochasticSimulationReduced(NetworkType.BARABASI_ALBERT, strategy, strategyFolder,
                    epidemic);
        }
    }
}
