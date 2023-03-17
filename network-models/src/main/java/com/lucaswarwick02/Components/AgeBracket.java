package com.lucaswarwick02.components;

public enum AgeBracket {
    NONE(-1, 0f),
    BELOW_NINE(0, 0.1124f), // 0 - 9
    TEN_TO_NINETEEN(1, 0.1153f), // 10 - 19
    TWENTY_TO_THIRTYFOUR(2, 0.2043f), // 20 - 34
    THIRTYFIVE_TO_FOURTYNINE(3, 0.1930f), // 35 - 49
    FIFTY_TO_SIXTYFOUR(4, 0.1933f), // 50 - 64
    SIXTYFIVE_TO_SEVENTYNINE(5, 0.1343f), // 65 - 79
    ABOVE_EIGHTY(6, 0.0474f); // 80+

    public final int ageOrder;
    public final float percentageOfPopulation;

    AgeBracket(int ageOrder, float percentageOfPopulation) {
        this.ageOrder = ageOrder;
        this.percentageOfPopulation = percentageOfPopulation;
    }

    public static final AgeBracket[] degreeOrder = {
            ABOVE_EIGHTY,
            BELOW_NINE,
            SIXTYFIVE_TO_SEVENTYNINE,
            FIFTY_TO_SIXTYFOUR,
            TEN_TO_NINETEEN,
            THIRTYFIVE_TO_FOURTYNINE,
            TWENTY_TO_THIRTYFOUR
    };
}
