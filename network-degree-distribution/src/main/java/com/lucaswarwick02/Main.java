package com.lucaswarwick02;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        String ROOT_PATH = args[0];
        String FOLDER_NAME = args[1];

        final int MODELS_TO_GENERATE = 50;

        Model[] models = new Model[MODELS_TO_GENERATE];
        for (int i = 0; i < MODELS_TO_GENERATE; i++) {
            models[i] = new Model(0.0004f, 0.035f, 1000);
            models[i].runSimulation(100, 3);

            Path path = Path.of(ROOT_PATH, "data", FOLDER_NAME, "model_" + i + ".csv");
            FileWriter fileWriter = new FileWriter(path.toString());

            fileWriter.write(ModelState.getCSVHeaders() + "\n");
            for (ModelState modelState : models[i].getModelStates()) {
                fileWriter.write(modelState.getCSVRow() + "\n");
            }

            fileWriter.close();
        }

        AggregateModel aggregateModel = new AggregateModel(models);

        Path path = Path.of(ROOT_PATH, "data", FOLDER_NAME, "model_aggregate.csv");
        FileWriter fileWriter = new FileWriter(path.toString());

        fileWriter.write(AggregateModelState.getCSVHeaders() + "\n");

        for (int i = 0; i < aggregateModel.aggregateModelStates.length; i++) {
            if (aggregateModel.aggregateModelStates[i] == null) {
                System.out.println("Aggy state is null at i=" + i);
                break;
            }
            fileWriter.write(aggregateModel.aggregateModelStates[i].getCSVRow() + "\n");
        }

        fileWriter.close();
    }
}
