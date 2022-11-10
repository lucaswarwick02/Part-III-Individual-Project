package com.lucaswarwick02;

import com.lucaswarwick02.Models.AbstractModel;
import com.lucaswarwick02.Models.AggregateModel;
import com.lucaswarwick02.Models.MathematicalSIRModel;
import com.lucaswarwick02.Models.SIRModel;
import com.lucaswarwick02.Networks.AbstractNetwork;
import com.lucaswarwick02.Networks.FullyMixedNetwork;
import tech.tablesaw.api.Table;

import java.io.File;

public class Main {

    public static void main (String[] args) {

        // Store arguments from command line
        final String ROOT_FOLDER = args[0];
        final int numberOfNodes = Integer.parseInt(args[1]);
        final int numberOfSimulations = Integer.parseInt(args[2]);

        // Create a new data folder for this current run-through
        final File dataFolder = new File(ROOT_FOLDER, "data");
        dataFolder.mkdir();

        // * Create and save Aggregate Model
        AbstractNetwork network = new FullyMixedNetwork();
        AbstractModel model = new SIRModel( 0.0004f, 0.04f );

        AggregateModel aggregateModel = new AggregateModel( numberOfSimulations );

        for (int s = 0; s < numberOfSimulations; s++) {

            network.generateNetwork( numberOfNodes );

            model.setUnderlyingNetwork( network );
            model.runSimulation( 100, 3 );

            aggregateModel.results[s] = model.results;
        }

        saveTableToCSV(aggregateModel.aggregateResults(), dataFolder, "stochastic_model.csv");

        // * Create and save Mathematical Model
        MathematicalSIRModel mathematicalModel = new MathematicalSIRModel( 0.0004f, 0.04f, numberOfNodes );
        mathematicalModel.runSimulation( 100, 3 );

        saveTableToCSV(mathematicalModel.results, dataFolder, "mathematical_model.csv");
    }

    static void saveTableToCSV (Table table, File dataFolder, String fileName) {
        File outputFile = new File(dataFolder, fileName);
        table.write().csv(outputFile);
    }
}
