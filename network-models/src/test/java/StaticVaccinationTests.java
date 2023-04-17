import static org.junit.Assert.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.lucaswarwick02.components.Epidemic;
import com.lucaswarwick02.components.ModelParameters;
import com.lucaswarwick02.components.Node.State;
import com.lucaswarwick02.models.StochasticModel;
import com.lucaswarwick02.networks.NetworkFactory.NetworkType;
import com.lucaswarwick02.vaccination.AbstractStrategy;
import com.lucaswarwick02.vaccination.AgeStrategy;
import com.lucaswarwick02.vaccination.DegreeStrategy;
import com.lucaswarwick02.vaccination.HeatmapStrategy;
import com.lucaswarwick02.vaccination.MixedStrategy;
import com.lucaswarwick02.vaccination.RandomStrategy;

public class StaticVaccinationTests {

    private static Epidemic epidemic = Epidemic.loadFromResources("./stochastic.xml");

    /**
     * Test that the section 1 (Random) strategy vaccinates the correct proportions.
     * 
     * @param rho The proportion of the population to vaccinate.
     */
    @ParameterizedTest
    @ValueSource(floats = { 0.25f, 0.5f, 0.75f })
    public void Section1_Random_Test(float rho) {
        AbstractStrategy strategy = new RandomStrategy(rho);

        // Setup the network.
        StochasticModel model = new StochasticModel(epidemic, NetworkType.BARABASI_ALBERT, strategy, true);
        model.createNetwork();
        model.getUnderlyingNetwork().generateNetwork();
        model.getUnderlyingNetwork().assignAgeBrackets();

        // Pefrom the vacciantion.
        model.getAbstractStrategy().performStrategy(model);

        // Calulcate the difference between the expected and actual number of nodes
        double expected = ModelParameters.NUMBER_OF_NODES * rho;
        double actual = model.getUnderlyingNetwork().getNodesFromState(State.VACCINATED).size();
        double difference = Math.abs(actual - expected);

        // Assert that the difference is less than 10.
        assertTrue(difference < 10);
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
        AbstractStrategy strategy = new DegreeStrategy(rho, false);

        // Setup the network.
        StochasticModel model = new StochasticModel(epidemic, NetworkType.BARABASI_ALBERT, strategy, true);
        model.createNetwork();
        model.getUnderlyingNetwork().generateNetwork();
        model.getUnderlyingNetwork().assignAgeBrackets();

        // Pefrom the vacciantion.
        model.getAbstractStrategy().performStrategy(model);

        // Calulcate the difference between the expected and actual number of nodes
        double expected = ModelParameters.NUMBER_OF_NODES * rho;
        double actual = model.getUnderlyingNetwork().getNodesFromState(State.VACCINATED).size();
        double difference = Math.abs(actual - expected);

        // Assert that the difference is less than 10.
        assertTrue(difference < 10);
    }

    /**
     * Test that the section 2 (Degree-Highest) strategy vaccinates the correct
     * 
     * @param rho The proportion of the population to vaccinate.
     */
    @ParameterizedTest
    @ValueSource(floats = { 0.25f, 0.5f, 0.75f })
    public void Section2_HighestDegree_Test(float rho) {
        AbstractStrategy strategy = new DegreeStrategy(rho, true);

        // Setup the network.
        StochasticModel model = new StochasticModel(epidemic, NetworkType.BARABASI_ALBERT, strategy, true);
        model.createNetwork();
        model.getUnderlyingNetwork().generateNetwork();
        model.getUnderlyingNetwork().assignAgeBrackets();

        // Pefrom the vacciantion.
        model.getAbstractStrategy().performStrategy(model);

        // Calulcate the difference between the expected and actual number of nodes
        double expected = ModelParameters.NUMBER_OF_NODES * rho;
        double actual = model.getUnderlyingNetwork().getNodesFromState(State.VACCINATED).size();
        double difference = Math.abs(actual - expected);

        // Assert that the difference is less than 10.
        assertTrue(difference < 10);
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
        AbstractStrategy strategy = new AgeStrategy(rho, true);

        // Setup the network.
        StochasticModel model = new StochasticModel(epidemic, NetworkType.BARABASI_ALBERT, strategy, true);
        model.createNetwork();
        model.getUnderlyingNetwork().generateNetwork();
        model.getUnderlyingNetwork().assignAgeBrackets();

        // Pefrom the vacciantion.
        model.getAbstractStrategy().performStrategy(model);

        // Calulcate the difference between the expected and actual number of nodes
        double expected = ModelParameters.NUMBER_OF_NODES * rho;
        double actual = model.getUnderlyingNetwork().getNodesFromState(State.VACCINATED).size();
        double difference = Math.abs(actual - expected);

        // Assert that the difference is less than 10.
        assertTrue(difference < 10);
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
        AbstractStrategy strategy = new AgeStrategy(rho, false);

        // Setup the network.
        StochasticModel model = new StochasticModel(epidemic, NetworkType.BARABASI_ALBERT, strategy, true);
        model.createNetwork();
        model.getUnderlyingNetwork().generateNetwork();
        model.getUnderlyingNetwork().assignAgeBrackets();

        // Pefrom the vacciantion.
        model.getAbstractStrategy().performStrategy(model);

        // Calulcate the difference between the expected and actual number of nodes
        double expected = ModelParameters.NUMBER_OF_NODES * rho;
        double actual = model.getUnderlyingNetwork().getNodesFromState(State.VACCINATED).size();
        double difference = Math.abs(actual - expected);

        // Assert that the difference is less than 10.
        assertTrue(difference < 10);
    }

    /**
     * Test that the section 4 (Heatmap) strategy vaccinates the correct
     * 
     * @param rho The proportion of the population to vaccinate.
     * @param r   The proportion of the population to ignore.
     */
    @ParameterizedTest
    @CsvSource({ "0.25, 0.25", "0.5, 0.5", "0.75, 0.75" })
    public void Section4_Test(float rho, float r) {
        AbstractStrategy strategy = new HeatmapStrategy(r, rho);

        // Setup the network.
        StochasticModel model = new StochasticModel(epidemic, NetworkType.BARABASI_ALBERT, strategy, true);
        model.createNetwork();
        model.getUnderlyingNetwork().generateNetwork();
        model.getUnderlyingNetwork().assignAgeBrackets();

        // Pefrom the vacciantion.
        model.getAbstractStrategy().performStrategy(model);

        // If r + rho > 1, then we need to reduce rho to ensure that the total number of
        // vaccinated nodes is less than the total number of nodes.
        if (r + rho > 1) {
            float extra = r + rho - 1;
            rho -= extra;
        }

        // Calulcate the difference between the expected and actual number of nodes
        double expected = ModelParameters.NUMBER_OF_NODES * rho;
        double actual = model.getUnderlyingNetwork().getNodesFromState(State.VACCINATED).size();
        double difference = Math.abs(actual - expected);

        // Assert that the difference is less than 10.
        assertTrue(String.format("Actual=%f, Expected=%f", actual, expected), difference < 10);
    }

    /**
     * Test that the section 5 (Mixed) strategy vaccinates the correct
     * 
     * @param rho   The proportion of the population to vaccinate.
     * @param alpha The proportion of the population to vaccinate using the degree
     *              strategy.
     */
    @ParameterizedTest
    @CsvSource({ "0.25, 0.25", "0.5, 0.5", "0.75, 0.75" })
    public void Section5_Test(float rho, float alpha) {
        AbstractStrategy strategy = new MixedStrategy(alpha, rho);

        // Setup the network.
        StochasticModel model = new StochasticModel(epidemic, NetworkType.BARABASI_ALBERT, strategy, true);
        model.createNetwork();
        model.getUnderlyingNetwork().generateNetwork();
        model.getUnderlyingNetwork().assignAgeBrackets();

        // Pefrom the vacciantion.
        model.getAbstractStrategy().performStrategy(model);

        // Calulcate the difference between the expected and actual number of nodes
        // (Mixed strategy ignores nodes which are already vaccinated in the case that
        // the age-brackets overlap, so we don't need any additional calculations.)
        double expected = ModelParameters.NUMBER_OF_NODES * rho;
        double actual = model.getUnderlyingNetwork().getNodesFromState(State.VACCINATED).size();
        double difference = Math.abs(actual - expected);

        // Assert that the difference is less than 10.
        assertTrue(String.format("Actual=%f, Expected=%f", actual, expected), difference < 10);
    }
}
