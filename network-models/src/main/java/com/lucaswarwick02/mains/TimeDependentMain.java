package com.lucaswarwick02.mains;

import java.io.File;

import com.lucaswarwick02.HelperFunctions;
import com.lucaswarwick02.components.Epidemic;
import com.lucaswarwick02.networks.NetworkFactory.NetworkType;
import com.lucaswarwick02.vaccination.AbstractStrategy;
import com.lucaswarwick02.vaccination.TimeDependent;

public class TimeDependentMain {
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

        runTimeDependent(runFolder, 0.25f);
        runTimeDependent(runFolder, 0.1f);
    }

    private static void runTimeDependent(File runFolder, float rho) {
        HelperFunctions.LOGGER.info(String.format("rho=%.03f", rho));

        File rhoFolder = new File(runFolder, String.format("rho=%.03f", rho));
        rhoFolder.mkdir();

        int[] t1s = HelperFunctions.createIntervals(0, 50, 5);
        int[] t2s = HelperFunctions.createIntervals(50, 100, 5);

        Epidemic epidemic = Epidemic.loadFromResources("/stochastic.xml");

        for (int t1 : t1s) {
            for (int t2 : t2s) {
                HelperFunctions.LOGGER.info(String.format("... t1=%d, t2=%d", t1, t2));
                AbstractStrategy strategy = new TimeDependent(rho, t1, t2);
                HelperFunctions.stochasticSimulationReduced(NetworkType.BARABASI_ALBERT, strategy, rhoFolder, epidemic,
                        true);
            }
        }

    }
}
