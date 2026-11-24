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
    @Test fun midpointCalculationDoesNotOverflowForLargeFinitePrices() {
        val first = Candle(1.6e308, 1.65e308, 1.15e308, 1.2e308)
        val middle = Candle(1.2e308, 1.22e308, 1.19e308, 1.21e308)
        val last = Candle(1.5e308, 1.55e308, 1.44e308, 1.45e308)
        assertEquals(ThreeCandleReversalPattern.NONE,
            ThreeCandleReversalAnalysis.classify(first, middle, last))
    }

    @Test fun overflowingBodySizesAreRejectedInsteadOfTreatedAsSmallStarBodies() {
        val first = Candle(1.7e308, 1.7e308, -1.7e308, -1.7e308)
        val middle = Candle(-1.7e308, 1.7e308, -1.7e308, 1.7e308)
        val last = Candle(-1.0e307, 1.1e307, -1.1e307, 1.0e307)
        assertEquals(ThreeCandleReversalPattern.NONE,
            ThreeCandleReversalAnalysis.classify(first, middle, last))
    }

    @Test
    fun rejectsFiniteEndpointsWithOverflowingRange() {
        val extreme = Candle(0.0, 1.7e308, -1.7e308, -0.1)
        val middle = Candle(0.0, 0.1, -0.1, 0.01)
        val last = Candle(-0.1, 0.2, -0.2, 0.1)
        assertEquals(ThreeCandleReversalPattern.NONE,
            ThreeCandleReversalAnalysis.classify(extreme, middle, last))
    }

    @Test
    fun rejectsFiniteEndpointsWithOverflowingRange() {
        val extreme = Candle(0.0, 1.7e308, -1.7e308, -0.1)
        val middle = Candle(0.0, 0.1, -0.1, 0.01)
        val last = Candle(-0.1, 0.2, -0.2, 0.1)
        assertEquals(ThreeCandleReversalPattern.NONE,
            ThreeCandleReversalAnalysis.classify(extreme, middle, last))
    }

}
