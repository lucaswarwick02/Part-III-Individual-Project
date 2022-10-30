package com.lucaswarwick02;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main (String[] args) throws IOException {
        for (String arg : args) {
            System.out.println("Arg: " + arg);
        }

        Path path = Paths.get(args[0], "data", args[1]);

        Model model = new Model(0.0004f, 0.035f, 1000);
        model.runSimulation(100, 3);

        FileWriter fileWriter = new FileWriter(path.toString());

        fileWriter.write(ModelState.getCSVHeaders() + "\n");
        for (ModelState modelState : model.getModelStates()) {
            fileWriter.write(modelState.getCSVRow() + "\n");
        }

        fileWriter.close();
    }
}
