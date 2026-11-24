package com.candlesense.ai

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CandlePairAnalysisTest {
    @Test
    fun detectsBullishEngulfing() {
        val previous = Candle(10.0, 11.0, 8.0, 9.0)
        val current = Candle(8.8, 12.0, 8.5, 11.5)
        assertEquals(CandlePairPattern.BULLISH_ENGULFING, CandlePairAnalysis.classify(previous, current))
        assertTrue(CandlePairAnalysis.explanationUrdu(CandlePairPattern.BULLISH_ENGULFING).isNotBlank())
    }

    @Test
    fun detectsBearishEngulfing() {
        val previous = Candle(9.0, 11.0, 8.0, 10.5)
        val current = Candle(10.8, 11.5, 8.5, 8.7)
        assertEquals(CandlePairPattern.BEARISH_ENGULFING, CandlePairAnalysis.classify(previous, current))
    }

    @Test
    fun detectsBullishAndBearishHarami() {
        val bullishPrevious = Candle(10.0, 12.0, 8.0, 9.0)
        val bullishCurrent = Candle(9.1, 10.0, 9.0, 9.8)
        assertEquals(CandlePairPattern.BULLISH_HARAMI, CandlePairAnalysis.classify(bullishPrevious, bullishCurrent))

        val bearishPrevious = Candle(8.0, 11.0, 7.5, 10.5)
        val bearishCurrent = Candle(10.0, 10.2, 9.0, 9.5)
        assertEquals(CandlePairPattern.BEARISH_HARAMI, CandlePairAnalysis.classify(bearishPrevious, bearishCurrent))
    }

    @Test
    fun rejectsOhlcWhoseFiniteRangeOverflowsDouble() {
        val extreme = Candle(-Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE, Double.MAX_VALUE / 2.0)
        val normal = Candle(10.0, 12.0, 8.0, 11.0)
        assertEquals(CandlePairPattern.NONE, CandlePairAnalysis.classify(extreme, normal))
        assertEquals(CandlePairPattern.NONE, CandlePairAnalysis.classify(normal, extreme))
    }

    @Test
    fun rejectsSameDirectionAndInvalidOhlc() {
        assertEquals(
            CandlePairPattern.NONE,
            CandlePairAnalysis.classify(Candle(10.0, 12.0, 9.0, 11.0), Candle(11.0, 13.0, 10.0, 12.0))
        )
        assertEquals(
            CandlePairPattern.NONE,
            CandlePairAnalysis.classify(Candle(10.0, 9.0, 8.0, 9.0), Candle(9.0, 10.0, 8.5, 9.5))
        )
    }
}
