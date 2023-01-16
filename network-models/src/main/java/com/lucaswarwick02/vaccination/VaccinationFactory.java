package com.lucaswarwick02.vaccination;

public class VaccinationFactory {

    public enum VaccinationType {
        NONE, // No vaccination occurs
        GLOBAL // All susceptible nodes are vaccinated at the same rate
    }

    private VaccinationFactory () {}

    public static AbstractStrategy getVaccinationStrategy (VaccinationType vaccinationType) {
        switch (vaccinationType) {
            case NONE:
                return new NoVaccination();
            case GLOBAL:
                return new GlobalVaccination();
            default:
                return new NoVaccination();
        }
    }
}
