package com.lucaswarwick02.Models;

import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.Table;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AggregateModel {

    public Table[] results;

    public AggregateModel(int numberOfModels) {
        results = new Table[numberOfModels];
    }

    public Table aggregateResults() {

        int numberOfColumns = results[0].columnCount();
        int numberOfRows = results[0].rowCount();
        List<String> columnNames = results[0].columnNames();
        IntColumn timeColumn = IntColumn.create("Time");

        HashMap<String, DoubleColumn> averageColumns = new HashMap<>();
        HashMap<String, DoubleColumn> stdColumns = new HashMap<>();
        DoubleColumn cumulativeInfected = DoubleColumn.create("CumulativeInfected");

        // For each column ( excluding 0 ) ...
        for (int c = 1; c < numberOfColumns; c++) {
            String columnName = columnNames.get(c);
            // ... Create Min, Max and Mean columns
            averageColumns.put(columnName, DoubleColumn.create(columnName));
            stdColumns.put(columnName + "_STD", DoubleColumn.create(columnName + "_STD"));
        }

        for (int r = 0; r < numberOfRows; r++) {
            for (int c = 1; c < numberOfColumns; c++) {
                int finalR = r;
                int finalC = c;
                String columnName = columnNames.get(c);
                List<Double> values = Arrays.stream(results)
                        .mapToDouble(resultsTable -> resultsTable.row(finalR).getInt(finalC)).boxed().toList();
                double mean = values.stream().mapToDouble(a -> a).average().orElse(Double.NaN);
                double std = CalculateStandardDeviation(values, mean);
                averageColumns.get(columnName).append(mean);
                stdColumns.get(columnName + "_STD").append(std);

                // Calculate the newlyInfected;
            }
            timeColumn.append(r);
        }

        Table table = Table.create("Aggregate " + results[0].name()).addColumns(timeColumn);
        averageColumns.values().forEach(table::addColumns);
        stdColumns.values().forEach(table::addColumns);
        return table;
    }

    public static double CalculateStandardDeviation(List<Double> input, double mean) {
        return Math.sqrt(input.stream().mapToDouble(x -> Math.pow(x - mean, 2)).sum() / input.size());
    }
}
