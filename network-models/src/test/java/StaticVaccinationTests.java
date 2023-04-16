import static org.junit.Assert.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class StaticVaccinationTests {

    /**
     * Test that the section 1 (Random) strategy vaccinates the correct proportions.
     * 
     * @param rho The proportion of the population to vaccinate.
     */
    @ParameterizedTest
    @ValueSource(floats = { 0.25f, 0.5f, 0.75f })
    public void Section1_Random_Test(float rho) {
        assertTrue("TEST NOT WRITTEN", false);
    }

    /**
     * Test that the section 2 (Degree-Lowest) strategy vaccinates the correct
     * proportions.
     * 
     * @param rho The proportion of the population to vaccinate.
     */
    @ParameterizedTest
    @ValueSource(floats = { 0.25f, 0.5f, 0.75f })
    public void Section2_LowestDegree_Test(float rho) {
        assertTrue("TEST NOT WRITTEN", false);
    }

    /**
     * Test that the section 2 (Degree-Highest) strategy vaccinates the correct
     * 
     * @param rho The proportion of the population to vaccinate.
     */
    @ParameterizedTest
    @ValueSource(floats = { 0.25f, 0.5f, 0.75f })
    public void Section2_HighestDegree_Test(float rho) {
        assertTrue("TEST NOT WRITTEN", false);
    }

    /**
     * Test that the section 3 (Age-Oldest) strategy vaccinates the correct
     * proportions.
     * 
     * @param rho The proportion of the population to vaccinate.
     */
    @ParameterizedTest
    @ValueSource(floats = { 0.25f, 0.5f, 0.75f })
    public void Section3_OldestAge_Test(float rho) {
        assertTrue("TEST NOT WRITTEN", false);
    }

    /**
     * Test that the section 3 (Age-Youngest) strategy vaccinates the correct
     * proportions.
     * 
     * @param rho The proportion of the population to vaccinate.
     */
    @ParameterizedTest
    @ValueSource(floats = { 0.25f, 0.5f, 0.75f })
    public void Section3_YoungestAge_Test(float rho) {
        assertTrue("TEST NOT WRITTEN", false);
    }

    @ParameterizedTest
    @CsvSource({ "0.25, 0.25", "0.5, 0.5", "0.75, 0.75" })
    public void Section4_Test(float rho, float r) {
        assertTrue("TEST NOT WRITTEN", false);
    }

    @ParameterizedTest
    @CsvSource({ "0.25, 0.25", "0.5, 0.5", "0.75, 0.75" })
    public void Section5_Test(float rho, float alpha) {
        assertTrue("TEST NOT WRITTEN", false);
    }
}
