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

        runMixed(runFolder, 1.00f);
        runMixed(runFolder, 0.75f);
        runMixed(runFolder, 0.50f);
        runMixed(runFolder, 0.25f);
    }

    private static void runMixed(File runFolder, float alpha) {
        File alphaFolder = new File(runFolder, String.format("alpha=%.03f", alpha));
        alphaFolder.mkdir();

        float[] rhos = HelperFunctions.createIntervals(0, 1, 0.05f);
        Epidemic epidemic = Epidemic.loadFromResources("/stochastic.xml");

        for (float rho : rhos) {
            File rhoFolder = new File(alphaFolder, String.format("rho=%.03f", rho));
            rhoFolder.mkdir();

            AbstractStrategy strategy = new MixedStrategy(alpha, rho);

            HelperFunctions.stochasticSimulationReduced(NetworkType.BARABASI_ALBERT, strategy, rhoFolder, epidemic);
        }
    }
}
