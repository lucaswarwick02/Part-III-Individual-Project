package com.lucaswarwick02.models.states;

import java.io.File;
import java.io.PrintWriter;
import java.util.Objects;

public record ModelState(int time, float susceptible, float infected, float recovered, float vaccinated) {

    public static String CSV_HEADER = "Time,Susceptible,Infected,Recovered,Vaccinated";

    public ModelState {
        Objects.requireNonNull(time);
        Objects.requireNonNull(susceptible);
        Objects.requireNonNull(infected);
        Objects.requireNonNull(recovered);
        Objects.requireNonNull(vaccinated);
    }

    public String getCSVRow () {
        return time + "," + susceptible + "," + infected + "," + recovered + "," + vaccinated;
    }

    public static void saveArrayToCSV ( ModelState[] modelStates, File dataFolder, String fileName ) {
        File file = new File(dataFolder, fileName);

        try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
            writer.write(ModelState.CSV_HEADER + "\n");
            for (ModelState modelState : modelStates) {
                writer.write(modelState.getCSVRow() + "\n");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
