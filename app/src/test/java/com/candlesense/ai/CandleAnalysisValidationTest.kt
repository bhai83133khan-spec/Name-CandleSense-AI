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
}
