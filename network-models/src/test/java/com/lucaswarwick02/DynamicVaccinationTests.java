package com.lucaswarwick02;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.lucaswarwick02.components.Epidemic;
import com.lucaswarwick02.networks.NetworkFactory.NetworkType;
import com.lucaswarwick02.vaccination.AbstractStrategy;
import com.lucaswarwick02.vaccination.ThresholdDependent;
import com.lucaswarwick02.vaccination.TimeDependent;

public class DynamicVaccinationTests {

    private static Epidemic epidemic = Epidemic.loadFromResources("./stochastic.xml");

    /**
     * Test that the section 6 (Time-Dependent) strategy vaccinates the correct
     * proportions.
     */
    @ParameterizedTest
    @CsvSource({ "0.25, 0, 0", "0.25, 30, 30", "0.25, 30, 60" })
    public void TimeDependent_Test(float rho, int t1, int t2) {
        AbstractStrategy strategy = new TimeDependent(rho, t1, t2);
        Map<String, double[]> states = HelperFunctions.stochasticSimulationStates(NetworkType.BARABASI_ALBERT, strategy,
                epidemic, true);

        float expected = rho;
        float actual = (float) states.get("Vaccinated")[states.get("Vaccinated").length - 1];
        float difference = Math.abs(actual - expected);

        assertTrue(String.format("Actual=%f, Expected=%f", actual, expected), difference < 0.01);
    }

    /**
     * Test that the section 7 (Threshold-Dependent) strategy vaccinates the correct
     * proportions.
     */
    @ParameterizedTest
    @CsvSource({ "0.0, 0.0" })
    public void ThresholdDependent_Test(float rho, float threshold) {
        Map<String, double[]> states = HelperFunctions.stochasticSimulationThresholdStates(NetworkType.BARABASI_ALBERT,
                rho, threshold,
                epidemic, true);

        float expected = rho;
        float actual = (float) states.get("Dead")[states.get("Dead").length - 1];
        float difference = Math.abs(actual - expected);
        assertTrue("" + actual, false);
        // assertTrue(String.format("Actual=%f, Expected=%f", actual, expected),
        // difference < 0.01);
    }
}
