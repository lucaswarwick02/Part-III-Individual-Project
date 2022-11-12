package com.lucaswarwick02.models.states;

import java.io.File;
import java.io.PrintWriter;
import java.util.Objects;

public record AggregateModelState(int time, double susceptible, double susceptibleStd, double infected, double infectedStd, double recovered, double recoveredStd, double vaccinated, double vaccinatedStd) {

    public static String CSV_HEADER = "Time,Susceptible,Infected,Recovered,Vaccinated,Susceptible_STD,Infected_STD,Recovered_STD,Vaccinated_STD";

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

    public String getCSVRow () {
        return time + "," + 
            susceptible + "," + infected + "," + recovered + "," + vaccinated + "," + 
            susceptibleStd + "," + infectedStd + "," + recoveredStd + "," + vaccinatedStd;
    }

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
