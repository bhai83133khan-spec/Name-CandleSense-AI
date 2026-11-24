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
    fun singleCandlePatternsAreClassifiedAndExplained() {
        assertEquals(CandlePattern.DOJI, CandleAnalysis.pattern(Candle(10.0, 11.0, 9.0, 10.05)))
        assertEquals(CandlePattern.HAMMER, CandleAnalysis.pattern(Candle(100.0, 103.0, 90.0, 102.0)))
        assertEquals(CandlePattern.SHOOTING_STAR, CandleAnalysis.pattern(Candle(102.0, 112.0, 99.0, 100.0)))
        assertEquals(CandlePattern.SPINNING_TOP, CandleAnalysis.pattern(Candle(100.0, 104.0, 97.0, 101.0)))
        assertEquals(CandlePattern.MARUBOZU, CandleAnalysis.pattern(Candle(100.0, 109.0, 100.0, 109.0)))
        assertEquals(CandlePattern.BULLISH_BODY, CandleAnalysis.pattern(Candle(100.0, 104.0, 98.0, 102.0)))
        assertEquals(CandlePattern.BEARISH_BODY, CandleAnalysis.pattern(Candle(102.0, 104.0, 98.0, 100.0)))
        assertTrue(CandleAnalysis.patternExplanationUrdu(Candle(100.0, 103.0, 90.0, 102.0)).isNotBlank())
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
    fun tapSelectionMatchesRenderedCandleCenters() {
        val width = 1200f
        val count = 5
        val centers = listOf(200f, 400f, 600f, 800f, 1000f)
        centers.forEachIndexed { index, center ->
            assertEquals(index, CandleAnalysis.nearestIndexForTap(center, width, count))
        }
        assertEquals(2, CandleAnalysis.nearestIndexForTap(550f, width, count))
    }

    @Test
    fun tapSelectionClampsEdgesAndRejectsInvalidChartSize() {
        assertEquals(0, CandleAnalysis.nearestIndexForTap(0f, 600f, 4))
        assertEquals(3, CandleAnalysis.nearestIndexForTap(600f, 600f, 4))
        assertEquals(null, CandleAnalysis.nearestIndexForTap(100f, 600f, 0))
        assertEquals(null, CandleAnalysis.nearestIndexForTap(100f, 0f, 4))
        assertEquals(null, CandleAnalysis.nearestIndexForTap(Float.NaN, 600f, 4))
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
