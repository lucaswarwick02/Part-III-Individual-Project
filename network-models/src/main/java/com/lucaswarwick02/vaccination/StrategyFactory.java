package com.lucaswarwick02.vaccination;

public class StrategyFactory {

    public enum StrategyType {
        RANDOM,
        HIGHEST,
        LOWEST,
        OLDEST,
        YOUNGEST,
        MIXED,
        HEATMAP
    }

    private StrategyFactory() {
    }

    public static AbstractStrategy getStrategy(StrategyType strategyType, float rho) {
        switch (strategyType) {
            case RANDOM:
                return new RandomStrategy(rho);
            case HIGHEST:
                return new DegreeStrategy(rho, true);
            case LOWEST:
                return new DegreeStrategy(rho, false);
            case OLDEST:
                return new AgeStrategy(rho, true);
            case YOUNGEST:
                return new AgeStrategy(rho, false);
            default:
                return null;
        }
    }
}
