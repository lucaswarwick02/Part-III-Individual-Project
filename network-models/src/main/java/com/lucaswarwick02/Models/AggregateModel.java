package com.lucaswarwick02.Models;

import tech.tablesaw.api.Table;

public class AggregateModel {

    public Table[] results;

    public AggregateModel (int numberOfModels) {
        results = new Table[numberOfModels];
    }
}
