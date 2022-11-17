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
                return new PoissonNetwork(3, 20);
            case SCALE_FREE:
                return new ScaleFreeNetwork(1.615f, 20);
            default:
                return new FullyMixedNetwork();
        }
    } 
}
