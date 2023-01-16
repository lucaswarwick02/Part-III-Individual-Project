package com.lucaswarwick02.networks;

import com.lucaswarwick02.Main;
import com.lucaswarwick02.archive.FixedDegreeNetwork;
import com.lucaswarwick02.archive.FullyMixedNetwork;
import com.lucaswarwick02.archive.ScaleFreeNetwork;

/**
 * Factory class for generating AbstractNetwork objects from a NetworkType enum.
 * Contains only one static function: getNetwork(NetworkType networkType)
 */
public class NetworkFactory {

    /**
     * Enum used in the NetworkFactory function getNetwork
     */
    public enum NetworkType {
        FULLY_MIXED, // All nodes are connected to eachother (homogeneous)
        FIXED_DEGREE, // All nodes have the same degree
        POISSON, // Degree sequence generated from a Poisson Distribution
        SCALE_FREE, // Degree sequence generated from a Power-Law Distribution
        BARABASI_ALBERT, // Scale-Free, generated from the Barabasi-Albert Model
    }

    static final int FIXED_DEGREE = 4;

    static final float Z = 8;
    static final int MAX_DEGREE = 25;

    static final float GAMMA = 2f;
    static final int KAPPA = 20;

    static final int M = 4;

    /**
     * Restrict use of the constructor
     */
    private NetworkFactory() {}

    /**
     * 
     * @param networkType Network type Enum
     * @return AbstractNetwork
     */
    public static AbstractNetwork getNetwork(NetworkType networkType) {
        switch (networkType) {
            case FULLY_MIXED:
                return new FullyMixedNetwork();
            case FIXED_DEGREE:
                return new FixedDegreeNetwork(FIXED_DEGREE);
            case POISSON:
                return new PoissonNetwork(Z, MAX_DEGREE);
            case SCALE_FREE:
                return new ScaleFreeNetwork(GAMMA, KAPPA);
            case BARABASI_ALBERT:
                return new BarabasiAlbertNetwork(M);
            default:
                return new FullyMixedNetwork();
        }
    }

    public static void logNetworkInfo(NetworkType networkType) {
        Main.LOGGER.info("### Network Parameters ###");
        switch (networkType) {
            case FULLY_MIXED:
                Main.LOGGER.info("... FULLY_MIXED: No Parameters");
                break;
            case FIXED_DEGREE:
                Main.LOGGER.info("... FIXED_DEGREE: k=" + FIXED_DEGREE);
                break;
            case POISSON:
                Main.LOGGER.info("... POISSON: z=" + Z + ", maxDegree=" + MAX_DEGREE);
                break;
            case SCALE_FREE:
                Main.LOGGER.info("... SCALE_FREE: gamma=" + GAMMA + ", kappa=" + KAPPA);
                break;
            case BARABASI_ALBERT:
                Main.LOGGER.info("... BARABASI_ALBERT: m=" + M);
                break;
            default:
                break;
        }
    }
}