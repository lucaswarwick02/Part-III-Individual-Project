package com.lucaswarwick02.networks;

import org.apache.commons.math3.special.Gamma;

import com.lucaswarwick02.Main;

/**
 * Factory class for generating AbstractNetwork objects from a NetworkType enum.
 * Contains only one static function: getNetwork(NetworkType networkType)
 */
public class NetworkFactory {

    static final float Z = 3;
    static final int MAX_DEGREE = 20;

    static final float GAMMA = 1.615f;
    static final int KAPPA = 20;

    static final int M = 2;
    
    /**
     * Restrict use of the constructor
     */
    private NetworkFactory () {}

    /**
     * 
     * @param networkType Network type Enum
     * @return AbstractNetwork
     */
    public static AbstractNetwork getNetwork (NetworkType networkType) {
        switch (networkType) {
            case FULLY_MIXED:
                return new FullyMixedNetwork();
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
        switch (networkType) {
            case FULLY_MIXED:
                Main.LOGGER.info("FULLY_MIXED: No Parameters");
                break;
            case POISSON:
                Main.LOGGER.info("POISSON: z=" + Z + ", maxDegree=" + MAX_DEGREE);
                break;
            case SCALE_FREE:
                Main.LOGGER.info("SCALE_FREE: gamma=" + GAMMA + ", kappa=" + KAPPA);
                break;
            case BARABASI_ALBERT:
                Main.LOGGER.info("BARABASI_ALBERT: m=" + M);
                break;
            default:
                break;
        }
    }
}
