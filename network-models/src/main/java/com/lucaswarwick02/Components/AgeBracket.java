package com.lucaswarwick02.components;

public enum AgeBracket {
    NONE(-1, 0f, 1f, 1f),
    BELOW_NINE(0, 0.1124f, 0.6f, 0.2f), // 0 - 9
    TEN_TO_NINETEEN(1, 0.1153f, 0.2f, 0.1f), // 10 - 19
    TWENTY_TO_THIRTYFOUR(2, 0.2043f, 1f, 1f), // 20 - 34
    THIRTYFIVE_TO_FOURTYNINE(3, 0.1930f, 5f, 3f), // 35 - 49
    FIFTY_TO_SIXTYFOUR(4, 0.1933f, 10f, 30f), // 50 - 64
    SIXTYFIVE_TO_SEVENTYNINE(5, 0.1343f, 22.5f, 75f), // 65 - 79
    ABOVE_EIGHTY(6, 0.0474f, 30f, 350f); // 80+

    public final int ageOrder;
    public final float proportion;
    public final float h;
    public final float d;

    AgeBracket(int ageOrder, float proportion, float h, float d) {
        this.ageOrder = ageOrder;
        this.proportion = proportion;
        this.h = h;
        this.d = d;
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
