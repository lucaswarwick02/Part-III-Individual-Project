package com.lucaswarwick02.Models;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.lucaswarwick02.Networks.AbstractNetwork;
import org.apache.commons.math3.analysis.function.Abs;
import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Layout;
import tech.tablesaw.plotly.traces.ScatterTrace;
import tech.tablesaw.plotly.traces.Trace;

public abstract class AbstractModel
{
    AbstractNetwork underlyingNetwork;

    final float RATE_OF_INFECTION;
    final float RATE_OF_RECOVERY;

    public Table results;

    public AbstractModel ( float rateOfInfection, float rateOfRecovery ) {
        this.RATE_OF_INFECTION = rateOfInfection;
        this.RATE_OF_RECOVERY = rateOfRecovery;
    }

    static <E> List<E> pickRandom( List<E> list, int n) {
        return new Random().ints(n, 0, list.size()).mapToObj(list::get).collect( Collectors.toList());
    }

    public void setUnderlyingNetwork (AbstractNetwork underlyingNetwork) {
        this.underlyingNetwork = underlyingNetwork;
    }

    public abstract void runSimulation (int iterations, int initialInfected);

    public void saveResults (File outputFolder) {
        String fileName = this.results.name().replaceAll("\\s+", "_").toLowerCase() + ".csv";
        File outputFile = new File(outputFolder, fileName);
        this.results.write().csv(outputFile);
    }

    public static void ViewResults (Table results) {
        Trace[] traces = new ScatterTrace[  results.columnCount() - 1 ];

        for ( int i = 1; i < results.columnCount(); i++ ) {
            ScatterTrace columnTrace = ScatterTrace
                    .builder( results.column( 0 ), results.column( i ) )
                    .mode( ScatterTrace.Mode.LINE )
                    .name( results.column( i ).name() )
                    .build();
            traces[ i - 1 ] = columnTrace;
        }

        Layout layout = Layout.builder().title( results.name() ).height( 500 ).width( 650 ).build();
        Plot.show( new Figure( layout, traces ) );
    }
}