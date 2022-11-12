package com.lucaswarwick02.models;

import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.models.states.ModelState;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Each iteration, use ODEs instead of random probabilities
 */
public class MathematicalModel {

    Random r = new Random(); // Used for getting random numbers

    final float rateOfInfection; // Probability of an infected node spreading
    final float rateOfRecovery; // Probability of an infected node recovering
    final float rateOfVaccination; // Probability of a susceptible node being vaccinated

    final int populationSize; // Size of the population

    ModelState[] modelStates; // Stores the state of the model at each time step

    /**
     * 
     * @param populationSize Size of the population
     * @param rateOfInfection Probability of an infected node spreading
     * @param rateOfRecovery Probability of an infected node recovering
     * @param rateOfVaccination Probability of a susceptible node being vaccinated
     */
    public MathematicalModel ( int populationSize, float rateOfInfection, float rateOfRecovery, float rateOfVaccination ) {
        this.rateOfInfection = rateOfInfection;
        this.rateOfRecovery = rateOfRecovery;
        this.rateOfVaccination = rateOfVaccination;
        this.populationSize = populationSize;
    }

    /**
     * Percolate the virus throughout the network
     * @param iterations Number of time steps
     * @param initialInfected Number of infected individuals at t=0
     */
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

    /**
     * Randomly pick N nodes
     * @param list List of Nodes to pick from
     * @param n Number of Nodes to pick
     * @return List of Nodes of length N
     */
    List<Node> pickRandomNodes( List<Node> list, int n ) {
        return r.ints(n, 0, list.size()).mapToObj(list::get).collect( Collectors.toList());
    }

    /**
     * Get the Model States
     * @return ModelState[]
     */
    public ModelState[] getModelStates () {
        return this.modelStates;
    }
}
