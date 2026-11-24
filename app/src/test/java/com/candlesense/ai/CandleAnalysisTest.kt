package com.candlesense.ai

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CandleAnalysisTest {
    @Test
    fun bullishAndBearishCandlesAreClassified() {
        assertEquals(CandleDirection.BULLISH, CandleAnalysis.direction(Candle(10.0, 12.0, 9.0, 11.0)))
        assertEquals(CandleDirection.BEARISH, CandleAnalysis.direction(Candle(10.0, 11.0, 8.0, 9.0)))
        assertEquals(CandleDirection.NEUTRAL, CandleAnalysis.direction(Candle(10.0, 12.0, 8.0, 10.0)))
    }

    @Test
    fun wickAndBodyRatiosAreCalculatedWithinExpectedBounds() {
        val candle = Candle(10.0, 13.0, 8.0, 12.0)
        assertEquals(1.0, CandleAnalysis.upperWick(candle), 0.0001)
        assertEquals(2.0, CandleAnalysis.lowerWick(candle), 0.0001)
        assertEquals(0.4, CandleAnalysis.bodyRatio(candle), 0.0001)
        assertTrue(CandleAnalysis.bodyRatio(Candle(2.0, 2.0, 2.0, 2.0)) == 0.0)
    }

    @Test
    fun recentLevelsUseOnlySelectedCandleAndLookbackWindow() {
        val candles = listOf(
            Candle(5.0, 7.0, 4.0, 6.0),
            Candle(6.0, 9.0, 5.0, 8.0),
            Candle(8.0, 10.0, 6.0, 7.0),
            Candle(7.0, 8.0, 3.0, 4.0),
            Candle(4.0, 6.0, 2.0, 5.0)
        )
        assertEquals(10.0, CandleAnalysis.recentHigh(candles, 2, 2)!!, 0.0001)
        assertEquals(3.0, CandleAnalysis.recentLow(candles, 3, 2)!!, 0.0001)
        assertEquals(null, CandleAnalysis.recentHigh(candles, 9, 3))
    }
}
