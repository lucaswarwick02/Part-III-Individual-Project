package com.lucaswarwick02.networks;

/**
 * Enum used in the NetworkFactory function getNetwork
 */
public enum NetworkType {
    FULLY_MIXED, // All nodes are connected to eachother (homogeneous)
    POISSON, // Degree sequence generated from a Poisson Distribution
    SCALE_FREE, // Degree sequence generated from a Power-Law Distribution
    BARABASI_ALBERT, // Scale-Free, generated from the Barabasi-Albert Model
}
