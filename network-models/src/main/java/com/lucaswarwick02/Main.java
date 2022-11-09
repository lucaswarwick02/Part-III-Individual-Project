package com.lucaswarwick02;

import com.lucaswarwick02.Models.AbstractModel;
import com.lucaswarwick02.Models.SIRModel;
import com.lucaswarwick02.Models.SIRVModel;
import com.lucaswarwick02.Networks.AbstractNetwork;
import com.lucaswarwick02.Networks.FullyMixedNetwork;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Grid;
import tech.tablesaw.plotly.components.Layout;
import tech.tablesaw.plotly.traces.ScatterTrace;
import tech.tablesaw.plotly.traces.Trace;

public class Main {
    public static void main (String[] args) {

        AbstractNetwork network;
        AbstractModel model;

        // * Fully-Mixed Network / SIR Model
        network = new FullyMixedNetwork();
        network.generateNetwork( 1000 );

        model = new SIRModel( network, 0.0004f, 0.04f );
        model.runSimulation( 100, 3 );
        model.viewResults();

        // * Fully-Mixed Network / SIRV Model
        network = new FullyMixedNetwork();
        network.generateNetwork( 1000 );

        model = new SIRVModel( network, 0.0004f, 0.04f, 0.04f );
        model.runSimulation( 100, 3 );
        model.viewResults();

        // ! Experimental

//        ScatterTrace susceptibleTrace = ScatterTrace
//                .builder( model.results.column( 0 ), model.results.column( 1 ) )
//                .mode( ScatterTrace.Mode.LINE )
//                .name( "Susceptible" )
//                .build();
//
//        ScatterTrace infectedTrace = ScatterTrace
//                .builder( model.results.column( 0 ), model.results.column( 2 ) )
//                .mode( ScatterTrace.Mode.LINE )
//                .name( "Infected" )
//                .build();
//
//        ScatterTrace recoveredTrace = ScatterTrace
//                .builder( model.results.column( 0 ), model.results.column( 3 ) )
//                .mode( ScatterTrace.Mode.LINE )
//                .name( "Recovered" )
//                .build();
//
//        ScatterTrace vaccinatedTrace = ScatterTrace
//                .builder( model.results.column( 0 ), model.results.column( 4 ) )
//                .mode( ScatterTrace.Mode.LINE )
//                .name( "Vaccinated" )
//                .build();
//
//        Trace[] traces = new ScatterTrace[] {susceptibleTrace, infectedTrace, recoveredTrace, vaccinatedTrace};
//        Grid grid = Grid.builder().columns( 2 ).rows( 2 ).pattern( Grid.Pattern.INDEPENDENT ).build();
//        Layout layout = Layout.builder()
//                .title( "Test Suplot" )
//                .height( 700 )
//                .width( 1000 )
//                .grid( grid )
//                .build();
//        Plot.show( new Figure( layout, traces ) );
    }
}
