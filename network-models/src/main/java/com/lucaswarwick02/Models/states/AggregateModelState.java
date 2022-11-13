package com.lucaswarwick02.models.states;

import java.io.File;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * Stores information about the average of some models at a specific time
 */
public record AggregateModelState(int time, double susceptible, double susceptibleStd, double infected, double infectedStd, double recovered, double recoveredStd, double vaccinated, double vaccinatedStd) {

    public static String CSV_HEADER = "Time,Susceptible,Infected,Recovered,Vaccinated,Susceptible_STD,Infected_STD,Recovered_STD,Vaccinated_STD"; // Header for the CSV file

    /**
     * Check each attribute is not null
     * @param time Time step
     * @param susceptible Average number of SUSCEPTIBLE Nodes
     * @param susceptibleStd Standard deviation of SUSCEPTIBLE Nodes
     * @param infected Average number of INFECTED Nodes
     * @param infectedStd Standard deviation of INFECTED Nodes
     * @param recovered Average number of RECOVERED Nodes
     * @param recoveredStd Standard deviation of RECOVERED Nodes
     * @param vaccinated Average number of VACCINATED Nodes
     * @param vaccinatedStd Standard deviation of VACCINATED Nodes
     */
    public AggregateModelState {
        Objects.requireNonNull(time);
        Objects.requireNonNull(susceptible);
        Objects.requireNonNull(susceptibleStd);
        Objects.requireNonNull(infected);
        Objects.requireNonNull(infectedStd);
        Objects.requireNonNull(recovered);
        Objects.requireNonNull(recoveredStd);
        Objects.requireNonNull(vaccinated);
        Objects.requireNonNull(vaccinatedStd);
    }

    /**
     * Get the value of the State as a String
     * @return String CSV row
     */
    public String getCSVRow () {
        return time + "," + 
            susceptible + "," + infected + "," + recovered + "," + vaccinated + "," + 
            susceptibleStd + "," + infectedStd + "," + recoveredStd + "," + vaccinatedStd;
    }

    /**
     * Save a list of states to a CSV file
     * @param aggregateModelStates List of states
     * @param dataFolder Folder where the file will be stored
     * @param fileName Name of the file
     */
    public static void saveArrayToCSV ( AggregateModelState[] aggregateModelStates, File dataFolder, String fileName ) {
        File file = new File(dataFolder, fileName);

        try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
            writer.write(AggregateModelState.CSV_HEADER + "\n");
            for (AggregateModelState aggregateModelState : aggregateModelStates) {
                writer.write(aggregateModelState.getCSVRow() + "\n");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
