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
            default:
                return new FullyMixedNetwork();
        }
    } 
}
