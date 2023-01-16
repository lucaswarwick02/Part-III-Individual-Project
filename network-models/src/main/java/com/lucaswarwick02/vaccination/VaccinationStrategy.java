package com.lucaswarwick02.vaccination;

/**
 * Used in creating the epidemic model
 */
public enum VaccinationStrategy {
    NONE, // No vaccination occurs
    GLOBAL // All susceptible nodes are vaccinated at the same rate
}
