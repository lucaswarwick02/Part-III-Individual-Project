package com.lucaswarwick02.models.states;

import java.io.File;
import java.io.PrintWriter;

/**
 * Stores information about the average of some models at a specific time
 */
public record AggregateModelState(int time, double susceptible, double susceptibleStd, double infected,
        double infectedStd, double recovered, double recoveredStd, double vaccinated, double vaccinatedStd,
        double hospitalised, double hospitalisedStd, double dead,
        double deadStd, double cumulativeInfected) {

    public static String CSV_HEADER = "Time,Susceptible,Infected,Recovered,Vaccinated,Hospitalised,Dead,Susceptible_STD,Infected_STD,Recovered_STD,Vaccinated_STD,Hospitalised_STD,Dead_STD,CumulativeInfected"; // Header
    // for
    // the
    // CSV
    // file

    /**
     * Get the value of the State as a String
     * 
     * @return String CSV row
     */
    public String getCSVRow() {
        return time + "," +
                susceptible + "," + infected + "," + recovered + "," + vaccinated + "," + hospitalised + "," + dead
                + "," +
                susceptibleStd + "," + infectedStd + "," + recoveredStd + "," + vaccinatedStd + "," + hospitalisedStd
                + "," + deadStd + ","
                + cumulativeInfected;
    }

    /**
     * Save a list of states to a CSV file
     * 
     * @param aggregateModelStates List of states
     * @param dataFolder           Folder where the file will be stored
     * @param fileName             Name of the file
     */
    public static void saveArrayToCSV(AggregateModelState[] aggregateModelStates, File dataFolder, String fileName) {
        File file = new File(dataFolder, fileName);

        try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
            writer.write(AggregateModelState.CSV_HEADER + "\n");
            for (AggregateModelState aggregateModelState : aggregateModelStates) {
                writer.write(aggregateModelState.getCSVRow() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
