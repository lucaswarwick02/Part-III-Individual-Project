package com.lucaswarwick02;

import com.lucaswarwick02.networks.AbstractNetwork;
import com.lucaswarwick02.networks.NetworkFactory;
import com.lucaswarwick02.networks.NetworkType;
import com.lucaswarwick02.models.StochasticModel;
import com.lucaswarwick02.models.VaccinationStrategy;
import com.lucaswarwick02.models.states.AggregateModelState;
import com.lucaswarwick02.models.states.ModelState;
import com.lucaswarwick02.models.MathematicalModel;
import com.lucaswarwick02.models.AggregateModel;

import java.io.File;

public class Main {

    static final int ITERATIONS = 100;
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

        AbstractNetwork network = NetworkFactory.getNetwork(networkType);
        StochasticModel model = new StochasticModel(vaccinationStrategy);

        AggregateModel aggregateModel = new AggregateModel(SIMULATIONS, ITERATIONS);

        System.out.println("##################################################");
        System.out.println("Network: " + networkType + ", Vaccination Strategy: " + vaccinationStrategy);
        System.out.print("... Running Simulation #00");

        for (int s = 0; s < SIMULATIONS; s++) {
            System.out.print("\b\b");
            System.out.printf("%02d", (s + 1));
            network.generateNetwork(NODES);

            model.setUnderlyingNetwork(network);
            model.runSimulation(ITERATIONS, 3);

            aggregateModel.modelStatesList[s] = model.modelStates;
        }
        System.out.println("\nSimulations Complete");

        AggregateModelState.saveArrayToCSV(aggregateModel.aggregateResults(), DATA_FOLDER, outputFileName);
    }

    /**
     * Run and save a mathematical simulation to the data folder
     */
    public static void mathematicalSimumation(float rateOfInfection, float rateOfRecovery, float rateOfVaccination,
            String outputFileName) {
        MathematicalModel mathematicalModel = new MathematicalModel(NODES, rateOfInfection, rateOfRecovery,
                rateOfVaccination);
        mathematicalModel.runSimulation(100, 3);

        ModelState.saveArrayToCSV(mathematicalModel.getModelStates(), DATA_FOLDER, outputFileName);
    }
}
