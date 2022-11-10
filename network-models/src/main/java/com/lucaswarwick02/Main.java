package com.lucaswarwick02;

import com.lucaswarwick02.Models.AbstractModel;
import com.lucaswarwick02.Models.AggregateModel;
import com.lucaswarwick02.Models.SIRModel;
import com.lucaswarwick02.Models.SIRVModel;
import com.lucaswarwick02.Networks.AbstractNetwork;
import com.lucaswarwick02.Networks.FullyMixedNetwork;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarStyle;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main {

    public static void main (String[] args) {

        // Store arguments from command line
        final String DATA_FOLDER = args[0];
        final int numberOfNodes = Integer.parseInt(args[1]);
        final int numberOfSimulations = Integer.parseInt(args[2]);

        // Create a new data folder for this current run-through
        final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        final File outputFolder = new File(DATA_FOLDER, timeStamp);
        outputFolder.mkdir();

        runSimulations( outputFolder, numberOfNodes, numberOfSimulations, new FullyMixedNetwork(), new SIRModel( 0.0004f, 0.04f ) );
        // runSimulations( outputFolder, numberOfNodes, numberOfSimulations, new FullyMixedNetwork(), new SIRVModel( 0.0004f, 0.04f, 0.04f ) );
    }

    public static void runSimulations ( File outputFolder, int numberOfNodes, int numberOfSimulations, AbstractNetwork network, AbstractModel model ) {
        AggregateModel aggregateModel = new AggregateModel( numberOfSimulations );

        ProgressBar pb = new ProgressBar(network.getClass().getSimpleName() + " - " + model.getClass().getSimpleName(), numberOfSimulations, ProgressBarStyle.UNICODE_BLOCK);

        pb.start();
        for (int s = 0; s < numberOfSimulations; s++) {

            network.generateNetwork( numberOfNodes );

            model.setUnderlyingNetwork( network );
            model.runSimulation( 100, 3 );

            aggregateModel.results[s] = model.results;
            pb.step();
        }
        pb.stop();

        // System.out.println(aggregateModel.aggregateResults().first(10));
        aggregateModel.viewResults(aggregateModel.aggregateResults());
        aggregateModel.viewResults(aggregateModel.results[0]);
    }
}
