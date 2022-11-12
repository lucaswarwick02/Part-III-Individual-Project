package com.lucaswarwick02.networks;

public class NetworkFactory {
    public static AbstractNetwork getNetwork (NetworkType networkType) {
        switch (networkType) {
            case FULLY_MIXED:
                return new FullyMixedNetwork();
            default:
                return new FullyMixedNetwork();
        }
    } 
}
