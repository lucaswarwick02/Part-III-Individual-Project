package com.lucaswarwick02;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.lucaswarwick02.components.Epidemic;
import com.lucaswarwick02.components.ModelParameters;
import com.lucaswarwick02.networks.NetworkFactory.NetworkType;
import com.lucaswarwick02.vaccination.AbstractStrategy;
import com.lucaswarwick02.vaccination.TimeDependent;

public class DynamicVaccinationTests {

    private static Epidemic epidemic = Epidemic.loadFromResources("./stochastic.xml");

    /**
     * Test that the section 6 (Time-Dependent) strategy vaccinates the correct
     * proportions.
     */
    @ParameterizedTest
    @CsvSource({ "0.25, 0, 30", "0.25, 30, 30", "0.25, 30, 60" })
    public void TimeDependent_Test(float rho, int t1, int t2) {
        AbstractStrategy strategy = new TimeDependent(rho, t1, t2);
        Map<String, double[]> totals = HelperFunctions.stochasticSimulationTotals(NetworkType.BARABASI_ALBERT, strategy,
                epidemic, true);

        float expected = ModelParameters.NUMBER_OF_NODES * rho;
        float actual = totals.get("Vaccinated").length;
        float difference = Math.abs(actual - expected);

        assertTrue(String.format("Actual=%f, Expected=%f", actual, expected), difference < 10);
    }
}
