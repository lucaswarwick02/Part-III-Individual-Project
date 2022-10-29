package com.lucaswarwick02;

import org.jfree.ui.RefineryUtilities;

public class Main {
    public static void main (String[] args) {
        Model model = new Model(0.0004f, 0.035f, 1000);
        model.runSimulation(10, 3);
        model.printSimulation();

        LineChart_AWT chart = new LineChart_AWT(
                "Schools Vs Years",
                "Number of Schools vs years"
        );
        chart.pack();
        RefineryUtilities.centerFrameOnScreen( chart );
        chart.setVisible( true );
    }
}
