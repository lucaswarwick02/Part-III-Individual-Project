package com.lucaswarwick02;

import static org.junit.Assert.assertTrue;

import java.util.EnumMap;

import org.junit.Test;

import com.lucaswarwick02.components.AgeBracket;
import com.lucaswarwick02.components.ModelParameters;
import com.lucaswarwick02.networks.AbstractNetwork;
import com.lucaswarwick02.networks.NetworkFactory;
import com.lucaswarwick02.networks.NetworkFactory.NetworkType;

public class NetworkTests {

    /**
     * Test that the average degree of the Erdos-Reyni network is close to the
     * target degree.
     */
    @Test
    public void ErdosReyni_AverageDegree_Test() {
        AbstractNetwork network = NetworkFactory.getNetwork(NetworkType.ERDOS_REYNI);
        network.generateNetwork();

        double targetDegree = 10;
        double difference = Math.abs(network.getAverageDegree() - targetDegree);

        assertTrue(String.format("<k> = %f", network.getAverageDegree()), difference < 0.1);
    }

    /**
     * Test that the age distribution of the Erdos-Reyni network is correct.
     */
    @Test
    public void ErdosReyni_AgeDistribution_Test() {
        AbstractNetwork network = NetworkFactory.getNetwork(NetworkType.ERDOS_REYNI);
        network.generateNetwork();
        network.assignAgeBrackets();

        EnumMap<AgeBracket, Integer> ageDistribution = network.getAgeDistribution();

        EnumMap<AgeBracket, Float> expectedDistribution = new EnumMap<>(AgeBracket.class);
        for (AgeBracket ageBracket : AgeBracket.degreeOrder) {
            expectedDistribution.put(ageBracket, ageBracket.proportion);
        }

        boolean distributionCorrect = true;

        for (AgeBracket ageBracket : AgeBracket.degreeOrder) {
            float expected = expectedDistribution.get(ageBracket);
            float actual = (float) ageDistribution.get(ageBracket) / ModelParameters.NUMBER_OF_NODES;
            float difference = Math.abs(expected - actual);
            if (difference > 0.01) {
                distributionCorrect = false;
                break;
            }
        }

        assertTrue(distributionCorrect);
    }

    /**
     * Test that the Erdos-Reyni network has only one component.
     */
    @Test
    public void ErdosReyni_Components_Test() {
        boolean flag = true;

        for (int i = 0; i < 25; i++) {
            AbstractNetwork network = NetworkFactory.getNetwork(NetworkType.ERDOS_REYNI);
            network.generateNetwork();

            int numberOfComponents = network.calculateNumberOfComponents();
            if (numberOfComponents != 1) {
                flag = false;
                break;
            }
        }

        assertTrue(flag);
    }

    /**
     * Test that the average degree of the Barabasi-Albert network is close to the
     * target degree.
     */
    @Test
    public void BarabasiAlbert_AverageDegree_Test() {
        AbstractNetwork network = NetworkFactory.getNetwork(NetworkType.BARABASI_ALBERT);
        network.generateNetwork();

        double targetDegree = 10;
        double difference = Math.abs(network.getAverageDegree() - targetDegree);

        assertTrue(String.format("<k> = %f", network.getAverageDegree()), difference < 0.1);
    }

    /**
     * Test that the age distribution of the Barabasi-Albert network is correct.
     */
    @Test
    public void BarabasiAlbert_AgeDistribution_Test() {
        AbstractNetwork network = NetworkFactory.getNetwork(NetworkType.ERDOS_REYNI);
        network.generateNetwork();
        network.assignAgeBrackets();

        EnumMap<AgeBracket, Integer> ageDistribution = network.getAgeDistribution();

        EnumMap<AgeBracket, Float> expectedDistribution = new EnumMap<>(AgeBracket.class);
        for (AgeBracket ageBracket : AgeBracket.degreeOrder) {
            expectedDistribution.put(ageBracket, ageBracket.proportion);
        }

        boolean distributionCorrect = true;

        for (AgeBracket ageBracket : AgeBracket.degreeOrder) {
            float expected = expectedDistribution.get(ageBracket);
            float actual = (float) ageDistribution.get(ageBracket) / ModelParameters.NUMBER_OF_NODES;
            float difference = Math.abs(expected - actual);
            if (difference > 0.01) {
                distributionCorrect = false;
                break;
            }
        }

        assertTrue(distributionCorrect);
    }

    /**
     * Test that the Barabasi-Albert network has only one component.
     */
    @Test
    public void BarabasiAlbert_Components_Test() {
        boolean flag = true;

        for (int i = 0; i < 25; i++) {
            AbstractNetwork network = NetworkFactory.getNetwork(NetworkType.BARABASI_ALBERT);
            network.generateNetwork();

            int numberOfComponents = network.calculateNumberOfComponents();
            if (numberOfComponents != 1) {
                flag = false;
                break;
            }
        }

        assertTrue(flag);
    }
}
