package com.lucaswarwick02.networks;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class ScaleFreeNetwork extends AbstractNetwork {

    int gamma;
    int kappa;

    /**
     * Create an empty underlying network
     */
    public ScaleFreeNetwork(int gamma, int kappa) {
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
    public void generateNetwork(int numberOfNodes) {
        // ! EXPERIMENTAL
    }

    public int[] generateDegreeSequence(int numberOfNodes) {
        double[] probabilities = new double[kappa - 1]; // Ignore case: degree = 0
        int[] nodesPerDegree = new int[kappa - 1];
        double polylogarithmValue = polylogarithm(gamma, Math.exp(-1d / (double) kappa), 100);

        for (int k = 1; k < kappa; k++) {
            probabilities[k - 1] = powerLawDegreeProbability(k, gamma, kappa, polylogarithmValue);
            nodesPerDegree[k - 1] = (int) Math.ceil(probabilities[k - 1] * numberOfNodes);
        }

        int nodesLeft = numberOfNodes - Arrays.stream(nodesPerDegree).sum();

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
     * @param k
     * @param gamma
     * @param kappa
     * @param polylogarithmValue
     * @return Probability between 0-1
     */
    private double powerLawDegreeProbability(int k, int gamma, int kappa, double polylogarithmValue) {
        BigDecimal top1 = BigDecimal.valueOf(Math.pow(k, -gamma));
        BigDecimal top2 = BigDecimal.valueOf(Math.exp((double) -k / (double) kappa));
        double bottom = polylogarithmValue;

        return top1.multiply(top2).divide(BigDecimal.valueOf(bottom), 6, RoundingMode.HALF_UP).floatValue();
    }

    /**
     * Calculate the polylogarithm using a power series in z
     * @param s
     * @param z
     * @param precision Number of times the series is applied, greater = more accurate
     * @return Polylogarithm using s and z
     */
    private double polylogarithm(int s, double z, int precision) {
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

}
