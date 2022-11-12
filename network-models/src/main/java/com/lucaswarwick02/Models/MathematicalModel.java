package com.lucaswarwick02.models;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.models.states.ModelState;

public class MathematicalModel {
    Random r = new Random();

    final float rateOfInfection;
    final float rateOfRecovery;
    final float rateOfVaccination;

    final int populationSize;

    ModelState[] modelStates;

    public MathematicalModel ( int populationSize, float rateOfInfection, float rateOfRecovery, float rateOfVaccination ) {
        this.rateOfInfection = rateOfInfection;
        this.rateOfRecovery = rateOfRecovery;
        this.rateOfVaccination = rateOfVaccination;
        this.populationSize = populationSize;
    }

    public void runSimulation(int iterations, int initialInfected) {
        modelStates = new ModelState[iterations];

        modelStates[0] = new ModelState(0, populationSize - initialInfected, initialInfected, 0, 0);

        for (int t = 1; t < iterations; t++) {
            float changeInS = -1f * (this.rateOfInfection * modelStates[t - 1].infected() * modelStates[t - 1].susceptible())
                    - (this.rateOfVaccination * modelStates[t - 1].susceptible());
            float changeInI = (this.rateOfInfection * modelStates[t - 1].infected() * modelStates[t - 1].susceptible())
                    - (this.rateOfRecovery * modelStates[t - 1].infected());
            float changeInR = (this.rateOfRecovery * modelStates[t - 1].infected());
            float changeInV = (this.rateOfVaccination * modelStates[t - 1].susceptible());

            modelStates[t] = new ModelState(
                t, 
                modelStates[t - 1].susceptible() + changeInS,
                modelStates[t - 1].infected() + changeInI,
                modelStates[t - 1].recovered() + changeInR,
                modelStates[t - 1].vaccinated() + changeInV
            );
        }
    }

    List<Node> pickRandomNodes( List<Node> list, int n ) {
        return r.ints(n, 0, list.size()).mapToObj(list::get).collect( Collectors.toList());
    }

    public ModelState[] getModelStates () {
        return this.modelStates;
    }
}
