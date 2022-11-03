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

    private final int cumulativeInfectedMin;
    private final int cumulativeInfectedMax;
    private final double cumulativeInfectedMean;

    private final int time;

    public AggregateModelState(int time, int susceptibleMin, int susceptibleMax, double susceptibleMean,
            int infectedMin, int infectedMax, double infectedMean, int removedMin, int removedMax, double removedMean,
            int cumulativeInfectedMin, int cumulativeInfectedMax, double cumulativeInfectedMean) {

        this.susceptibleMin = susceptibleMin;
        this.susceptibleMax = susceptibleMax;
        this.susceptibleMean = susceptibleMean;

        this.infectedMin = infectedMin;
        this.infectedMax = infectedMax;
        this.infectedMean = infectedMean;

        this.removedMin = removedMin;
        this.removedMax = removedMax;
        this.removedMean = removedMean;

        this.cumulativeInfectedMin = cumulativeInfectedMin;
        this.cumulativeInfectedMax = cumulativeInfectedMax;
        this.cumulativeInfectedMean = cumulativeInfectedMean;

        this.time = time;
    }

    public static String getCSVHeaders() {
        return "Time,SusceptibleMin,SusceptibleMax,SusceptibleMean,InfectedMin,InfectedMax,InfectedMean,RemovedMin,RemovedMax,RemovedMean,CumulativeInfectedMin,CumulativeInfectedMax,CumulativeInfectedMean";
    }

    public String getCSVRow() {
        return time + "," +
                susceptibleMin + "," + susceptibleMax + "," + susceptibleMean + "," +
                infectedMin + "," + infectedMax + "," + infectedMean + "," +
                removedMin + "," + removedMax + "," + removedMean + "," +
                cumulativeInfectedMin + ", " + cumulativeInfectedMax + ", " + cumulativeInfectedMean;
    }
}
