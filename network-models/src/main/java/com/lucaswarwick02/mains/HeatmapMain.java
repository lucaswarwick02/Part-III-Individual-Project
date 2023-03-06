package com.lucaswarwick02.mains;

import java.io.File;
import com.lucaswarwick02.HelperFunctions;
import com.lucaswarwick02.components.Epidemic;
import com.lucaswarwick02.networks.NetworkFactory.NetworkType;
import com.lucaswarwick02.vaccination.AbstractStrategy;
import com.lucaswarwick02.vaccination.HeatmapStrategy;

public class HeatmapMain {
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

        // Iterate through rhos
        // Iterate through rs
        float[] rhos = HelperFunctions.createIntervals(0.125f, 1, 0.125f);
        float[] rs = HelperFunctions.createIntervals(0, 1, 0.125f);

        for (float rho : rhos) {
            for (float r : rs) {
                HelperFunctions.LOGGER.info("r = " + r + ", rho = " + rho);

                File strategyFolder = new File(runFolder, String.format("r=%.03f_rho=%.03f", r, rho));
                strategyFolder.mkdir();

                AbstractStrategy strategy = new HeatmapStrategy(r, rho);
                Epidemic epidemic = Epidemic.loadFromResources("/stochastic.xml");

                HelperFunctions.stochasticSimulationReduced(NetworkType.BARABASI_ALBERT, strategy, strategyFolder,
                        epidemic);
            }
        }
    }
}
