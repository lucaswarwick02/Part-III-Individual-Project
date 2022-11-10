package com.lucaswarwick02.Models;

import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Layout;
import tech.tablesaw.plotly.traces.ScatterTrace;
import tech.tablesaw.plotly.traces.Trace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class AggregateModel {

    public Table[] results;

    public AggregateModel (int numberOfModels) {
        results = new Table[numberOfModels];
    }

    public Table aggregateResults () {

        int numberOfColumns = results[0].columnCount();
        int numberOfRows = results[0].rowCount();

        HashMap<String, DoubleColumn> newColumns = new HashMap<>();

        IntColumn timeColumn = IntColumn.create("Time");

        // For each column ( excluding 0 ) ...
        for (int columnNumber = 1; columnNumber < numberOfColumns; columnNumber++) {
            // ... Create Min, Max and Mean columns
            String columnName = results[0].column( columnNumber ).name();
            String[] newColumnNames = { columnName + "_Min", columnName + "_Max", columnName + "_Mean" };
            for (String newColumnName : newColumnNames) {
                newColumns.put(newColumnName, DoubleColumn.create(newColumnName));
            }
        }

        for (int r = 0; r < numberOfRows; r++) {
            for (int c = 1; c < numberOfColumns; c++) {
                String columnName = results[0].column(c).name();
                String[] newColumnNames = { columnName + "_Min", columnName + "_Max", columnName + "_Mean" };
                int finalR = r;
                int finalC = c;
                List<Double> values = Arrays.stream( results )
                        .mapToDouble( resultsTable -> resultsTable.row( finalR ).getInt( finalC ) ).boxed().toList();
                double average = values.stream().mapToDouble(a -> a).average().orElse(Double.NaN);
                double min = values.stream().mapToDouble(a -> a).min().orElse(Double.NaN);
                double max = values.stream().mapToDouble(a -> a).max().orElse(Double.NaN);
                newColumns.get(newColumnNames[0]).append(min);
                newColumns.get(newColumnNames[1]).append(max);
                newColumns.get(newColumnNames[2]).append(average);
            }
            timeColumn.append( r );
        }

        Table table = Table.create("Aggregate " + results[0].name()).addColumns( timeColumn );
        newColumns.values().forEach(table::addColumns);
        return table;
    }

    public void viewResults (Table results) {
        Trace[] traces = new ScatterTrace[ results.columnCount() - 1 ];

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
