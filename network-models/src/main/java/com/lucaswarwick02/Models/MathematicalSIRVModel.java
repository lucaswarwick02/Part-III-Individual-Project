package com.lucaswarwick02.Models;

import tech.tablesaw.api.FloatColumn;
import tech.tablesaw.api.Table;

public class MathematicalSIRVModel extends AbstractModel {

    int populationSize;
    float rateOfVaccination;

    public MathematicalSIRVModel(float rateOfInfection, float rateOfRecovery, float rateOfVaccination,
            int populationSize) {
        super(rateOfInfection, rateOfRecovery);
        this.rateOfVaccination = rateOfVaccination;
        this.populationSize = populationSize;
    }

    @Override
    public void runSimulation(int iterations, int initialInfected) {
        float[] timeCount = new float[iterations];
        float[] susceptibleCount = new float[iterations];
        float[] infectedCount = new float[iterations];
        float[] recoveredCount = new float[iterations];
        float[] vaccinatedCount = new float[iterations];

        float[] cumulativeInfected = new float[iterations];

        timeCount[0] = 0;
        susceptibleCount[0] = populationSize - initialInfected;
        infectedCount[0] = initialInfected;
        recoveredCount[0] = 0;
        vaccinatedCount[0] = 0;
        cumulativeInfected[0] = initialInfected;

        for (int t = 1; t < iterations; t++) {
            float changeInS = -1f * (this.RATE_OF_INFECTION * infectedCount[t - 1] * susceptibleCount[t - 1])
                    - (this.rateOfVaccination * susceptibleCount[t - 1]);
            float changeInI = (this.RATE_OF_INFECTION * infectedCount[t - 1] * susceptibleCount[t - 1])
                    - (this.RATE_OF_RECOVERY * infectedCount[t - 1]);
            float changeInR = (this.RATE_OF_RECOVERY * infectedCount[t - 1]);
            float changeInV = (this.rateOfVaccination * susceptibleCount[t - 1]);
            float newlyInfected = (this.RATE_OF_INFECTION * infectedCount[t - 1] *
                    susceptibleCount[t - 1]);

            timeCount[t] = t;
            susceptibleCount[t] = susceptibleCount[t - 1] + changeInS;
            infectedCount[t] = infectedCount[t - 1] + changeInI;
            recoveredCount[t] = recoveredCount[t - 1] + changeInR;
            vaccinatedCount[t] = vaccinatedCount[t - 1] + changeInV;
            cumulativeInfected[t] = cumulativeInfected[t - 1] + newlyInfected;
        }

        results = Table.create("Mathematical SIR Model Results")
                .addColumns(
                        FloatColumn.create("Time", timeCount),
                        FloatColumn.create("Susceptible", susceptibleCount),
                        FloatColumn.create("Infected", infectedCount),
                        FloatColumn.create("Recovered", recoveredCount),
                        FloatColumn.create("Vaccinated", vaccinatedCount),
                        FloatColumn.create("CumulativeInfected", cumulativeInfected));
    }
}
