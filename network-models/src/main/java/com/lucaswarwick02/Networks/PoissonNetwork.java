package com.lucaswarwick02.networks;

import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;

import java.math.BigDecimal;

public class PoissonNetwork extends AbstractNetwork {

    float z;
    int maxDegree;

    /**
     * Create an empty underlying network
     */
    public PoissonNetwork(float z, int maxDegree) {
        super();
        this.z = z;
        this.maxDegree = maxDegree;
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
        BigDecimal[] probabilities = new BigDecimal[maxDegree - 1]; // Ignore case: degree = 0
        // ignore
        int[] nodesPerDegree = new int[maxDegree - 1];
        for (int k = 1; k < maxDegree; k++) {
            probabilities[k - 1] = poissonDegreeProbability(k, z);
            nodesPerDegree[k - 1] = (probabilities[k - 1].multiply(BigDecimal.valueOf(numberOfNodes)))
                    .setScale(0, RoundingMode.UP).intValue();
        }

        int nodesLeft = numberOfNodes - Arrays.stream(nodesPerDegree).sum();

        while (nodesLeft > 0) {
            if (nodesLeft == 1) {
                if ((numberOfStubs(nodesPerDegree) + z) % 2 == 0) {
                    nodesPerDegree[Math.round(z) - 1]++;
                } else {
                    nodesPerDegree[Math.round(z)]++;
                }
            } else {
                nodesPerDegree[Math.round(z) - 1]++;
            }
            nodesLeft--;
        }

        return nodesPerDegree;
    }

    /**
     * Generate the probability of a node having a degree of k
     * 
     * @param k
     * @param z
     * @return Probability between 0-1
     */
    private BigDecimal poissonDegreeProbability(int k, float z) {
        BigDecimal d = BigDecimal.valueOf(Math.pow(z, k) * Math.exp(-z));
        return d.divide(new BigDecimal(factorial(k)), MathContext.DECIMAL128);
    }

    /**
     * Calculate the BigInteger value for the factorial of a number
     * 
     * @param n Number
     * @return Factorial of n
     */
    private BigInteger factorial(int n) {
        BigInteger f = BigInteger.ONE;

        for (int i = n; i > 0; i--) {
            f = f.multiply(BigInteger.valueOf(i));
        }

        return f;
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
