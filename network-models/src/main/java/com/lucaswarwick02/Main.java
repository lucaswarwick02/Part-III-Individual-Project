package com.lucaswarwick02;

import com.lucaswarwick02.networks.AbstractNetwork;
import com.lucaswarwick02.networks.NetworkFactory;
import com.lucaswarwick02.networks.NetworkType;
import com.lucaswarwick02.models.HelperFunctions;
import com.lucaswarwick02.models.StochasticModel;
import com.lucaswarwick02.models.VaccinationStrategy;

import java.io.File;
import java.util.Map;

public class Main {

    static final int ITERATIONS = 150;
    static final int INITIAL_INFECTED = 3;
    static int NODES;
    static int SIMULATIONS;
    static String ROOT_FOLDER;
    static File DATA_FOLDER;

    public static void main(String[] args) {
        // Store arguments from command line
        ROOT_FOLDER = args[0];
        NODES = Integer.parseInt(args[1]);
        SIMULATIONS = Integer.parseInt(args[2]);

        // Create a new data folder for this current run-through
        DATA_FOLDER = new File(ROOT_FOLDER, "data");
        DATA_FOLDER.mkdir();

        stochasticSimulation(NetworkType.BARABASI_ALBERT, VaccinationStrategy.NONE, "no_vaccination.csv");

        stochasticSimulation(NetworkType.BARABASI_ALBERT, VaccinationStrategy.GLOBAL, "global_vaccination.csv");
    }

    /**
     * Run, aggreggate and save multiple stochastic simulations to the data folder
     */
    public static void stochasticSimulation(NetworkType networkType, VaccinationStrategy vaccinationStrategy,
            String outputFileName) {

        System.out.println("##################################################");
        System.out.println("Network: " + networkType + ", Vaccination Strategy: " + vaccinationStrategy);
        System.out.print("... Running Simulation #00");

        StochasticModel[] models = new StochasticModel[SIMULATIONS];

        for (int s = 0; s < SIMULATIONS; s++) {
            AbstractNetwork network = NetworkFactory.getNetwork(networkType);
            StochasticModel model = new StochasticModel(vaccinationStrategy);

            System.out.print("\b\b");
            System.out.printf("%02d", (s + 1));
            network.generateNetwork(NODES);

            model.setUnderlyingNetwork(network);
            model.runSimulation(ITERATIONS, 3);

            models[s] = model;
        }
        System.out.println("\nSimulations Complete");

        Map<String, double[]> aggregateStates = HelperFunctions.aggregateStates(models, ITERATIONS);
        Map<String, double[]> aggregateTotals = HelperFunctions.aggregateTotals(models, ITERATIONS);

        HelperFunctions.evaluateAggregateModel(aggregateStates, aggregateTotals);



        HelperFunctions.saveToCSV(aggregateStates, DATA_FOLDER, outputFileName);
    }
}
