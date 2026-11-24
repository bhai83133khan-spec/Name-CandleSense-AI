package com.candlesense.ai

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CandleAnalysisValidationTest {
    @Test
    fun malformedOhlcIsNotClassifiedAsMarketPattern() {
        val invalid = Candle(open = 10.0, high = 9.0, low = 11.0, close = 12.0)
        assertEquals(false, CandleAnalysis.isValid(invalid))
        assertEquals(CandleDirection.NEUTRAL, CandleAnalysis.direction(invalid))
        assertEquals(CandlePattern.INVALID, CandleAnalysis.pattern(invalid))
        assertEquals(0.0, CandleAnalysis.bodyRatio(invalid), 0.0001)
        assertEquals(0.0, CandleAnalysis.upperWick(invalid), 0.0001)
        assertEquals(0.0, CandleAnalysis.lowerWick(invalid), 0.0001)
        assertEquals("Invalid OHLC data", CandleAnalysis.patternNameUrdu(invalid))
    }

    @Test
    fun rollingLevelsIgnoreInvalidCandlesAndRejectNonPositiveLookback() {
        val candles = listOf(
            Candle(5.0, 7.0, 4.0, 6.0),
            Candle(10.0, 9.0, 11.0, 12.0),
            Candle(6.0, 9.0, 3.0, 8.0)
        )
        assertNull(CandleAnalysis.recentHigh(candles, 2, 0))
        assertNull(CandleAnalysis.recentLow(candles, 2, -1))
        assertEquals(9.0, CandleAnalysis.recentHigh(candles, 2, 2)!!, 0.0001)
        assertEquals(3.0, CandleAnalysis.recentLow(candles, 2, 2)!!, 0.0001)
    }
    @Test
    fun finiteEndpointsWithOverflowingRangeAreRejected() {
        val extreme = Candle(
            open = -Double.MAX_VALUE,
            high = Double.MAX_VALUE,
            low = -Double.MAX_VALUE,
            close = Double.MAX_VALUE
        )
        assertEquals(false, CandleAnalysis.isValid(extreme))
        assertEquals(CandlePattern.INVALID, CandleAnalysis.pattern(extreme))
        assertEquals(0.0, CandleAnalysis.bodyRatio(extreme), 0.0001)
    }

    @Test
    fun tapHitTestingRejectsInvalidGeometryAndClampsEdges() {
        assertNull(CandleAnalysis.nearestIndexForTap(10f, 0f, 3))
        assertNull(CandleAnalysis.nearestIndexForTap(Float.NaN, 100f, 3))
        assertNull(CandleAnalysis.nearestIndexForTap(10f, 100f, 0))
        assertEquals(0, CandleAnalysis.nearestIndexForTap(-10f, 100f, 3))
        assertEquals(0, CandleAnalysis.nearestIndexForTap(25f, 100f, 3))
        assertEquals(1, CandleAnalysis.nearestIndexForTap(50f, 100f, 3))
        assertEquals(2, CandleAnalysis.nearestIndexForTap(75f, 100f, 3))
        assertEquals(2, CandleAnalysis.nearestIndexForTap(1000f, 100f, 3))
    }

}
