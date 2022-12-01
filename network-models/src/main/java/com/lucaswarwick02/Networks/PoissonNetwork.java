package com.lucaswarwick02.networks;

import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.lucaswarwick02.Main;
import com.lucaswarwick02.components.Node;
import com.lucaswarwick02.models.HelperFunctions;

import java.math.BigDecimal;

public class PoissonNetwork extends AbstractNetwork {

    Random r = new Random(); // Used for getting random numbers

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
            List<Node> randomNodes = HelperFunctions.pickRandomNodes(nodesWithStubs, 2);
            randomNodes.get(0).neighbours.add(randomNodes.get(1));
            randomNodes.get(0).stubs--;
            randomNodes.get(1).neighbours.add(randomNodes.get(0));
            randomNodes.get(1).stubs--;
        }
    }

    public int[] generateDegreeSequence() {
        double[] probabilities = new double[maxDegree - 1]; // Ignore case: degree = 0
        // ignore
        int[] nodesPerDegree = new int[maxDegree - 1];
        for (int k = 1; k < maxDegree; k++) {
            probabilities[k - 1] = poissonDegreeProbability(k, z);
            nodesPerDegree[k - 1] = (int) Math.ceil(probabilities[k - 1] * Main.NUMBER_OF_NODES);
        }

        int nodesLeft = Main.NUMBER_OF_NODES - Arrays.stream(nodesPerDegree).sum();

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
    private double poissonDegreeProbability(int k, float z) {
        BigDecimal d = BigDecimal.valueOf(Math.pow(z, k) * Math.exp(-z));
        return d.divide(new BigDecimal(factorial(k)), MathContext.DECIMAL128).doubleValue();
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

    /***
     * Calculate the number of 'stubs' in the distribution sequence
     * 
     * @param nodes
     * @return
     */
    private int numberOfStubs(List<Node> nodes) {
        return nodes.stream().mapToInt(node -> node.stubs).sum();
    }
}
