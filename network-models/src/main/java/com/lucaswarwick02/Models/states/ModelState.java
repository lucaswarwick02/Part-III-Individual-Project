package com.lucaswarwick02.models.states;

import java.io.File;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * Stores information about a Model at a specific time
 */
public record ModelState(int time, float susceptible, float infected, float recovered, float vaccinated) {

    public static String CSV_HEADER = "Time,Susceptible,Infected,Recovered,Vaccinated"; // Header for the CSV file

    /**
     * Check each attribute is not null
     * @param time Time step
     * @param susceptible Number of SUSCEPTIBLE Nodes
     * @param infected Number of INFECTED Nodes
     * @param recovered Number of RECOVERED Nodes
     * @param vaccinated Number of VACCINATED Nodes
     */
    public ModelState {
        Objects.requireNonNull(time);
        Objects.requireNonNull(susceptible);
        Objects.requireNonNull(infected);
        Objects.requireNonNull(recovered);
        Objects.requireNonNull(vaccinated);
    }

    /**
     * Get the value of the State as a String
     * @return String CSV row
     */
    public String getCSVRow () {
        return time + "," + susceptible + "," + infected + "," + recovered + "," + vaccinated;
    }

    /**
     * Save a list of states to a CSV file
     * @param modelStates List of states
     * @param dataFolder Folder where the file will be stored
     * @param fileName Name of the file
     */
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
