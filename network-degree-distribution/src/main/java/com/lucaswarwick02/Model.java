package com.lucaswarwick02;

import java.sql.PseudoColumnUsage;
import java.util.Random;

public class Model {
    private ModelState[] modelStates = null;

    private final float RATE_OF_INFECTION;
    private final float RATE_OF_RECOVERY;

    private final int POPULATION_SIZE;

    private int time;

    public Model (float rateOfInfection, float rateOfRecovery, int populationSize) {
        this.RATE_OF_INFECTION = rateOfInfection;
        this.RATE_OF_RECOVERY = rateOfRecovery;

        this.POPULATION_SIZE = populationSize;
    }

    public void runSimulation (int iterations, int initialInfected) {
        Random r = new Random();

        modelStates = new ModelState[iterations];

        // Initialise the model at t=0
        time = 0;
        modelStates[0] = new ModelState(time, POPULATION_SIZE - initialInfected, initialInfected, 0);

        for (int time = 1; time < iterations; time++) {
            int newlyInfected = 0;
            for (int i = 0; i < modelStates[time - 1].getSusceptible() * modelStates[time - 1].getInfected(); i++) {
                if (r.nextFloat() <= RATE_OF_INFECTION) {
                    newlyInfected++;
                }
            }

            int newlyRemoved = 0;
            for (int i = 0; i < modelStates[time - 1].getInfected(); i++) {
                if (r.nextFloat() <= RATE_OF_RECOVERY) {
                    newlyRemoved++;
                }
            }

            modelStates[time] = new ModelState(time, modelStates[time - 1].getSusceptible() - newlyInfected, modelStates[time - 1].getInfected() + newlyInfected - newlyRemoved, modelStates[time - 1].getRemoved() + newlyRemoved);
        }
    }

    public void printSimulation () {
        for (ModelState modelState : modelStates) {
            if (modelState != null) System.out.println(modelState.toString());
        }
    }
}
