package com.lucaswarwick02;

import org.jfree.ui.RefineryUtilities;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main (String[] args) throws IOException {
        Model model = new Model(0.0004f, 0.035f, 1000);
        model.runSimulation(100, 3);

        FileWriter fileWriter = new FileWriter("C:\\Users\\Lucas\\Documents\\Projects\\Part-III-Individual-Project\\out\\test.csv");

        fileWriter.write(ModelState.getCSVHeaders() + "\n");
        for (ModelState modelState : model.getModelStates()) {
            fileWriter.write(modelState.getCSVRow() + "\n");
        }

        fileWriter.close();
    }
}
