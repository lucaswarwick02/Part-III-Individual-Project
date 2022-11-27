package com.lucaswarwick02.models;

import com.lucaswarwick02.networks.AbstractNetwork;
import com.lucaswarwick02.components.Node;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Each iteration, use probability instead of ODEs
 */
public class StochasticModel {

    Random r = new Random(); // Used for getting random numbers

    AbstractNetwork underlyingNetwork; // Network used in the model

    final float rateOfInfection; // Probability of an infected node spreading
    final float rateOfRecovery; // Probability of an infected node recovering
    final float hospitalisationRate; // Probabiliy of an infected node being hospitalised
    final float mortalityRate; // Probability of a hospitalised node 'dying'

    public HashMap<String, int[]> states = new HashMap<>();

    VaccinationStrategy vaccinationStrategy; // Strategy used in the simulation

    /**
     * Setup the Stochastic Model
     * 
     * @param vaccinationStrategy Strategy used in the simulation
     * @param rateOfInfection     Probability of an infected node spreading
     * @param rateOfRecovery      Probability of an infected node recovering
     * @param rateOfVaccination   Probability of a susceptible node being vaccinated
     */
    public StochasticModel(VaccinationStrategy vaccinationStrategy) {
        this.vaccinationStrategy = vaccinationStrategy;

        this.rateOfInfection = 0.075f;
        this.rateOfRecovery = 0.04f;
        this.hospitalisationRate = 0.05f;
        this.mortalityRate = 0.1f;
    }

    /**
     * Percolate the virus throughout the network
     * 
     * @param iterations      Number of time steps
     * @param initialInfected Number of infected individuals at t=0
     */
    public void runSimulation(int iterations, int initialInfected) {
        // Setup the storing of model states
        states.put("Time", new int[iterations]);
        states.put("Susceptible", new int[iterations]);
        states.put("Infected", new int[iterations]);
        states.put("Recovered", new int[iterations]);
        states.put("Vaccinated", new int[iterations]);
        states.put("Hospitalised", new int[iterations]);
        states.put("Dead", new int[iterations]);

        // set initialInfected nodes to Infected
        List<Node> initialInfectedNodes = pickRandomNodes(underlyingNetwork.getNodes(), initialInfected);
        initialInfectedNodes.forEach(node -> node.state = Node.State.INFECTED);

        // Store the initial model state
        saveModelState(0);

        for (int t = 1; t < iterations; t++) {
            performIteration(t);
        }
    }

    void performIteration(int iterationNumber) {
        // Setup lists to store information from this iteration
        List<Node> nodesToInfect = new ArrayList<>();
        List<Node> nodesToHospitalise = new ArrayList<>();
        List<Node> nodesToRecover = new ArrayList<>();
        List<Node> nodesToKill = new ArrayList<>();

        // For each infected Node...
        for (Node infectedNode : underlyingNetwork.getNodesFromState(Node.State.INFECTED)) {
            // ... get a list of the Nodes they are going to infect
            infectedNode.neighbours.forEach(neighbour -> {
                if ((neighbour.state == Node.State.SUSCEPTIBLE) && (r.nextFloat() <= rateOfInfection))
                    nodesToInfect.add(neighbour);
            });

            // ... maybe recover the Node
            if (r.nextFloat() <= rateOfRecovery) {
                nodesToRecover.add(infectedNode);
            } else if (r.nextFloat() <= hospitalisationRate) {
                nodesToHospitalise.add(infectedNode);
            }
        }

        for (Node hospitalisedNode : underlyingNetwork.getNodesFromState(Node.State.HOSPITALISED)) {
            if (r.nextFloat() <= mortalityRate) {
                nodesToKill.add(hospitalisedNode);
            }
        }

        // Infect nodes
        nodesToInfect.forEach(node -> node.state = Node.State.INFECTED);
        // Recover nodes
        nodesToRecover.forEach(node -> node.state = Node.State.RECOVERED);
        // Hospitalise nodes
        nodesToHospitalise.forEach(node -> node.state = Node.State.HOSPITALISED);
        // Kill nodes
        nodesToKill.forEach(node -> node.state = Node.State.DEAD);

        // Run vacciantion strategy
        switch (vaccinationStrategy) {
            case GLOBAL:
                globalVaccinationStrategy();
                break;
            default:
                break;
        }

        saveModelState(iterationNumber); // Store the model state
    }

    // region Vaccination Strategies

    /**
     * Generated from VaccinationStrategy.GLOBAL
     */
    void globalVaccinationStrategy() {
        // Information used in this strategy:
        float rateOfVaccination = 0.0075f;

        // With the leftover nodes, vaccinate them
        for (Node susceptibleNode : underlyingNetwork.getNodesFromState(Node.State.SUSCEPTIBLE)) {
            if (r.nextFloat() <= rateOfVaccination)
                susceptibleNode.state = Node.State.VACCINATED;
        }
    }

    // endregion

    // region Setup Functions

    /**
     * Store the current state of the model as record
     * 
     * @param t Time step
     * @return ModelState
     */
    void saveModelState(int t) {
        this.states.get("Time")[t] = t;
        for (Node.State state : Node.getAllStates()) {
            this.states.get(Node.StateToString(state))[t] = this.underlyingNetwork.getNodesFromState(state).size();
        }
    }

    /**
     * Randomly pick N nodes
     * 
     * @param list List of Nodes to pick from
     * @param n    Number of Nodes to pick
     * @return List of Nodes of length N
     */
    List<Node> pickRandomNodes(List<Node> list, int n) {
        return r.ints(n, 0, list.size()).mapToObj(list::get).collect(Collectors.toList());
    }

    /**
     * Set the underlying network
     * 
     * @param underlyingNetwork Network used in the model
     */
    public void setUnderlyingNetwork(AbstractNetwork underlyingNetwork) {
        this.underlyingNetwork = underlyingNetwork;
    }

    // endregion

    // region Mathematical Functions

    /**
     * Calculate the mean of a lists
     * 
     * @param list List of floats
     * @return
     */
    public static double calculateMean(double[] list) {
        double sum = 0;
        for (double val : list)
            sum += val;

        return sum / list.length;
    }

    /**
     * Calculate the standard deviation of a list
     * 
     * @param list List of floats
     * @return
     */
    public static double calculateStandardDeviation(double[] list) {
        double length = list.length;
        double mean = calculateMean(list);
        double diffSum = 0;
        for (double val : list)
            diffSum += Math.pow(val - mean, 2);
        return (double) Math.sqrt(diffSum / length);
    }

    // endregion

    // region Static Functions

    /**
     * Use each model's ModelStates and calcualte the mean and standard deviation
     * for each time step
     * 
     * @return AggregateModelState[]
     */
    public static HashMap<String, double[]> aggregateResults(StochasticModel[] models, int iterations) {
        HashMap<String, double[]> states = new HashMap<>();

        states.put("Time", new double[iterations]);
        for (Node.State state : Node.getAllStates()) {
            String stateName = Node.StateToString(state);
            states.put(stateName, new double[iterations]);
            states.put(stateName + "_STD", new double[iterations]);
        }

        for (int i = 0; i < iterations; i++) {
            states.get("Time")[i] = i;
            for (Node.State state : Node.getAllStates()) {
                double[] values = new double[models.length];
                String stateName = Node.StateToString(state);

                for (int m = 0; m < models.length; m++) {
                    values[m] = models[m].states.get(stateName)[i];
                }

                double mean = calculateMean(values);
                double standardDeviation = calculateStandardDeviation(values);

                states.get(stateName)[i] = mean;
                states.get(stateName + "_STD")[i] = standardDeviation / 2;
            }
        }

        return states;
    }

    public static void SaveToCSV(HashMap<String, double[]> states, File dataFolder, String fileName) {

        File file = new File(dataFolder, fileName);

        int iterations = states.get("Time").length;

        String header = String.join(",", states.keySet());

        try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
            writer.write(header + "\n");
            for (int i = 0; i < iterations; i++) {
                List<String> row = new ArrayList<>();
                for (String string : states.keySet()) {
                    row.add(Double.toString(states.get(string)[i]));
                }
                writer.write(String.join(",", row) + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // endregion
}
