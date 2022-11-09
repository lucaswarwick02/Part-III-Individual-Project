package com.lucaswarwick02;

import com.lucaswarwick02.Networks.FullyMixedNetwork;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;

public class Main {
    public static void main (String[] args) {

        // * Fully-Mixed Network
        FullyMixedNetwork fullyMixedNetwork = new FullyMixedNetwork();
        fullyMixedNetwork.generateNetwork(1000);

        // * SIR Model
        SIRModel model = new SIRModel(fullyMixedNetwork, 0.0004f, 0.04f);
        model.runSimulation(100, 3);

        model.viewResults();
    }
}
