package com.lucaswarwick02;

public class AggregateModelState {

    private final int susceptibleMin;
    private final int susceptibleMax;
    private final double susceptibleMean;

    private final int infectedMin;
    private final int infectedMax;
    private final double infectedMean;

    private final int removedMin;
    private final int removedMax;
    private final double removedMean;

    private final int time;

    public AggregateModelState (int time, int susceptibleMin, int susceptibleMax, double susceptibleMean, int infectedMin, int infectedMax, double infectedMean, int removedMin, int removedMax, double removedMean) {
        this.susceptibleMin = susceptibleMin;
        this.susceptibleMax = susceptibleMax;
        this.susceptibleMean = susceptibleMean;
        this.infectedMin = infectedMin;

        this.infectedMax = infectedMax;
        this.infectedMean = infectedMean;
        this.removedMin = removedMin;
        this.removedMax = removedMax;
        this.removedMean = removedMean;

        this.time = time;
    }

    public static String getCSVHeaders () {
        return "Time,SusceptibleMin,SusceptibleMax,SusceptibleMean,InfectedMin,InfectedMax,InfectedMean,RemovedMin,RemovedMax,RemovedMean";
    }

    public String getCSVRow () {
        return
                time + "," +
                susceptibleMin + "," + susceptibleMax + "," + susceptibleMean + "," +
                infectedMin + "," + infectedMax + "," + infectedMean + "," +
                removedMin + "," + removedMax + "," + removedMean;
    }
}
