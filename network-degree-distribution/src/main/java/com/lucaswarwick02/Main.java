package com.lucaswarwick02;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main (String[] args) throws IOException {
        String ROOT_PATH = args[0];
        String FOLDER_NAME = args[1];

        for (int i = 0; i < 5; i++) {
            Model model = new Model(0.0004f, 0.035f, 1000);
            model.runSimulation(100, 3);

            Path path = Path.of(ROOT_PATH, "data", FOLDER_NAME, "model_" + i + ".csv");
            FileWriter fileWriter = new FileWriter(path.toString());

            fileWriter.write(ModelState.getCSVHeaders() + "\n");
            for (ModelState modelState : model.getModelStates()) {
                fileWriter.write(modelState.getCSVRow() + "\n");
            }

            fileWriter.close();
        }
    }
}
