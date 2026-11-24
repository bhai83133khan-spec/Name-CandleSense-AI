package com.candlesense.ai

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ThreeCandleAnalysisTest {
    @Test
    fun recognizesThreeWhiteSoldiers() {
        val result = ThreeCandleAnalysis.classify(
            Candle(10.0, 12.0, 9.5, 11.0),
            Candle(11.0, 13.0, 10.5, 12.0),
            Candle(12.0, 14.0, 11.5, 13.0)
        )
        assertEquals(ThreeCandlePattern.THREE_WHITE_SOLDIERS, result)
        assertTrue(ThreeCandleAnalysis.explanationUrdu(result).isNotBlank())
    }

    @Test
    fun recognizesThreeBlackCrows() {
        val result = ThreeCandleAnalysis.classify(
            Candle(13.0, 13.5, 12.0, 12.0),
            Candle(12.5, 13.0, 11.0, 11.5),
            Candle(12.0, 12.5, 10.5, 11.0)
        )
        assertEquals(ThreeCandlePattern.THREE_BLACK_CROWS, result)
    }

    @Test
    fun rejectsWrongDirectionAndInvalidOhlc() {
        assertEquals(
            ThreeCandlePattern.NONE,
            ThreeCandleAnalysis.classify(
                Candle(10.0, 12.0, 9.0, 11.0),
                Candle(11.0, 13.0, 10.0, 12.0),
                Candle(12.0, 14.0, 11.0, 13.0)
            )
        )
        assertEquals(
            ThreeCandlePattern.NONE,
            ThreeCandleAnalysis.classify(
                Candle(10.0, 9.0, 8.0, 9.5),
                Candle(9.5, 10.0, 8.5, 9.0),
                Candle(9.0, 9.5, 8.0, 8.5)
            )
        )
    }
}
