package com.lucaswarwick02.networks;

import java.math.BigDecimal;
import java.math.BigInteger;
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
        BigDecimal[] probabilities = new BigDecimal[kappa - 1]; // We don't want any nodes to have a degree of 0, so
                                                                // we
        // ignore
        int[] nodesPerDegree = new int[kappa - 1];
        for (int k = 1; k < kappa; k++) {
            probabilities[k - 1] = powerLawDegreeProbability(k, gamma, kappa);
            nodesPerDegree[k - 1] = (probabilities[k - 1].multiply(BigDecimal.valueOf(numberOfNodes)))
                    .setScale(0, RoundingMode.UP).intValue();
        }

        int nodesLeft = numberOfNodes - Arrays.stream(nodesPerDegree).sum();

        float averageDegree = 0f;
        for (int k = 1; k < kappa; k++) {
            averageDegree += probabilities[k - 1].multiply(BigDecimal.valueOf(k)).floatValue();
        }
        System.out.println("Average Degree = " + averageDegree);

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

        for (int k = 1; k < kappa; k++) {
            System.out.printf("%d (%.5f): %d", k, probabilities[k - 1], nodesPerDegree[k - 1]);
        }

        return nodesPerDegree;
    }

    private BigDecimal powerLawDegreeProbability(int k, int gamma, int kappa) {
        BigDecimal top = BigDecimal.valueOf(Math.pow(k, -gamma) * Math.exp(-k / kappa));
        BigDecimal bottom = polylogarithm(gamma, Math.exp(-1 / kappa), 100);

        return top.divide(bottom);
    }

    private BigDecimal polylogarithm(int s, double z, int precision) {
        BigDecimal power_sum = BigDecimal.ZERO;
        for (int k = 1; k < precision; k++) {
            BigDecimal top = BigDecimal.valueOf(Math.pow(s, z)).setScale(0, RoundingMode.HALF_UP);
            BigDecimal bottom = BigDecimal.valueOf(Math.pow(k, s)).setScale(0, RoundingMode.HALF_UP);
            power_sum.add(top.divide(bottom).setScale(0, RoundingMode.HALF_UP));
        }
        return power_sum;
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
