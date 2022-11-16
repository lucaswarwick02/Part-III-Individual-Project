package com.lucaswarwick02.networks;

/**
 * Factory class for generating AbstractNetwork objects from a NetworkType enum.
 * Contains only one static function: getNetwork(NetworkType networkType)
 */
public class NetworkFactory {
    
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
                return new PoissonNetwork(4, 15);
            case SCALE_FREE:
                return new ScaleFreeNetwork(2, 15);
            default:
                return new FullyMixedNetwork();
        }
    } 
}
