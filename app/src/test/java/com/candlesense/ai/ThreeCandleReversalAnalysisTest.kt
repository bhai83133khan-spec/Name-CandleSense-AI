package com.candlesense.ai

import org.junit.Assert.assertEquals
import org.junit.Test

class ThreeCandleReversalAnalysisTest {
    @Test fun recognizesMorningStarShape() {
        val first = Candle(10.0, 10.2, 8.8, 9.0)
        val middle = Candle(8.9, 9.1, 8.7, 8.85)
        val last = Candle(8.9, 10.1, 8.8, 10.0)
        assertEquals(ThreeCandleReversalPattern.MORNING_STAR,
            ThreeCandleReversalAnalysis.classify(first, middle, last))
    }

    @Test fun recognizesEveningStarShape() {
        val first = Candle(9.0, 10.2, 8.8, 10.0)
        val middle = Candle(10.05, 10.15, 9.95, 10.1)
        val last = Candle(10.0, 10.1, 8.8, 9.0)
        assertEquals(ThreeCandleReversalPattern.EVENING_STAR,
            ThreeCandleReversalAnalysis.classify(first, middle, last))
    }

    @Test fun rejectsInvalidInput() {
        val invalid = Candle(Double.NaN, 10.0, 8.0, 9.0)
        val middle = Candle(9.0, 9.1, 8.9, 9.0)
        val last = Candle(9.0, 10.0, 8.9, 9.5)
        assertEquals(ThreeCandleReversalPattern.NONE,
            ThreeCandleReversalAnalysis.classify(invalid, middle, last))
    }

    @Test fun rejectsNonStarSequence() {
        val first = Candle(10.0, 10.2, 8.8, 9.0)
        val middle = Candle(8.9, 9.5, 8.7, 9.4)
        val last = Candle(9.4, 9.5, 9.0, 9.1)
        assertEquals(ThreeCandleReversalPattern.NONE,
            ThreeCandleReversalAnalysis.classify(first, middle, last))
    }
}
