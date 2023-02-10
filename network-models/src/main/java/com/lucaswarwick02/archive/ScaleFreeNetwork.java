package com.lucaswarwick02.archive;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.models.StochasticModel;
import com.lucaswarwick02.networks.AbstractNetwork;

public class ScaleFreeNetwork extends AbstractNetwork {

    Random r = new Random(); // Used for getting random numbers

    float gamma;
    int kappa;

    /**
     * Create an empty underlying network
     */
    public ScaleFreeNetwork(float gamma, int kappa) {
        super();
        this.gamma = gamma;
        this.kappa = kappa;
    }

    /**
     * Generate the Nodes and Edges for the underlying fully-mixed network
     * 
     * @param numberOfNodes Number of Nodes in the network
     */
    @Override
    public void generateNetwork() {
        int[] degreeSequence = generateDegreeSequence();
        this.nodes = new ArrayList<>();

        int nodeID = 0;
        for (int i = 0; i < degreeSequence.length; i++) {
            int k = i + 1;
            for (int j = 0; j < degreeSequence[i]; j++) {
                Node newNode = new Node(nodeID);
                newNode.stubs = k;
                this.nodes.add(newNode);
                nodeID++;
            }
        }

        while (numberOfStubs(nodes) > 0) {
            List<Node> nodesWithStubs = nodes.stream().filter(node -> node.stubs > 0).collect(Collectors.toList());
            List<Node> randomNodes = pickRandomNodes(nodesWithStubs, 2);
            randomNodes.get(0).neighbours.add(randomNodes.get(1));
            randomNodes.get(0).stubs--;
            randomNodes.get(1).neighbours.add(randomNodes.get(0));
            randomNodes.get(1).stubs--;
        }
    }

    public int[] generateDegreeSequence() {
        double[] probabilities = new double[kappa - 1]; // Ignore case: degree = 0
        int[] nodesPerDegree = new int[kappa - 1];
        double polylogarithmValue = polylogarithm(gamma, Math.exp(-1d / kappa), 100); // ? Might need to cast kappa to
                                                                                      // double

        for (int k = 1; k < kappa; k++) {
            probabilities[k - 1] = powerLawDegreeProbability(k, gamma, kappa, polylogarithmValue);
            nodesPerDegree[k - 1] = (int) Math.ceil(probabilities[k - 1] * StochasticModel.NUMBER_OF_NODES);
        }

        int nodesLeft = StochasticModel.NUMBER_OF_NODES - Arrays.stream(nodesPerDegree).sum();

        float averageDegree = 0f;
        for (int k = 1; k < kappa; k++) {
            averageDegree += probabilities[k - 1] * k;
        }

        while (nodesLeft > 0) {
            if (nodesLeft == 1) {
                if ((numberOfStubs(nodesPerDegree) + averageDegree) % 2 == 0) {
                    nodesPerDegree[Math.round(averageDegree) - 1]++;
                } else {
                    nodesPerDegree[Math.round(averageDegree)]++;
                }
            } else {
                nodesPerDegree[Math.round(averageDegree) - 1]++;
            }
            nodesLeft--;
        }

        return nodesPerDegree;
    }

    /**
     * Generate the probability of a node having a degree of k
     * 
     * @param k
     * @param gamma
     * @param kappa
     * @param polylogarithmValue
     * @return Probability between 0-1
     */
    private double powerLawDegreeProbability(int k, float gamma, int kappa, double polylogarithmValue) {
        BigDecimal top1 = BigDecimal.valueOf(Math.pow(k, -gamma));
        BigDecimal top2 = BigDecimal.valueOf(Math.exp((double) -k / (double) kappa));
        double bottom = polylogarithmValue;

        return top1.multiply(top2).divide(BigDecimal.valueOf(bottom), 6, RoundingMode.HALF_UP).floatValue();
    }

    /**
     * Calculate the polylogarithm using a power series in z
     * 
     * @param s
     * @param z
     * @param precision Number of times the series is applied, greater = more
     *                  accurate
     * @return Polylogarithm using s and z
     */
    private double polylogarithm(float s, double z, int precision) {
        double powerSum = 0;
        for (int k = 1; k < precision; k++) {
            powerSum += Math.pow(z, k) / Math.pow(k, s);
        }
        return powerSum;
    }

    /***
     * Calculate the number of 'stubs' in the distribution sequence
     * 
     * @param nodesPerDegree
     * @return
     */
    private int numberOfStubs(int[] nodesPerDegree) {
        int stubs = 0;
        for (int i = 0; i < nodesPerDegree.length; i++) {
            stubs += (i + 1) * nodesPerDegree[i];
        }
        return stubs;
    }

    /***
     * Calculate the number of 'stubs' in the distribution sequence
     * 
     * @param nodes
     * @return
     */
    private int numberOfStubs(List<Node> nodes) {
        return nodes.stream().mapToInt(node -> node.stubs).sum();
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

}
