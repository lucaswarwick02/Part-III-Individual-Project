package com.lucaswarwick02.vaccination;

public class StrategyFactory {

    public enum StrategyType {
        RANDOM_ONEOFF,
        HIGHEST_ONEOFF,
        LOWEST_ONEOFF,
        OLDEST_ONEOFF,
        YOUNGEST_ONEOFF
    }

    private StrategyFactory() {
    }

    public static AbstractStrategy getStrategy(StrategyType strategyType, int timeDelay, float rho) {
        switch (strategyType) {
            case RANDOM_ONEOFF:
                return new RandomOneOff(timeDelay, rho);
            case HIGHEST_ONEOFF:
                return new HighestOneOff(timeDelay, rho);
            case LOWEST_ONEOFF:
                return new LowestOneOff(timeDelay, rho);
            case OLDEST_ONEOFF:
                return new OldestOneOff(timeDelay, rho);
            case YOUNGEST_ONEOFF:
                return new YoungestOneOff(timeDelay, rho);
            default:
                return null;
        }
    }
}
