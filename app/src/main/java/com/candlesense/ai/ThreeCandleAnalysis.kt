package com.candlesense.ai

enum class ThreeCandlePattern {
    NONE,
    THREE_WHITE_SOLDIERS,
    THREE_BLACK_CROWS
}

/**
 * Simple educational three-candle formations. These shape checks do not predict
 * price direction; confirm any pattern with trend, volume, and market context.
 */
object ThreeCandleAnalysis {
    fun classify(first: Candle, second: Candle, third: Candle): ThreeCandlePattern {
        if (!listOf(first, second, third).all(::isValid)) return ThreeCandlePattern.NONE
        val candles = listOf(first, second, third)
        val bodies = candles.map { kotlin.math.abs(it.close - it.open) }
        if (bodies.any { !it.isFinite() || it <= 0.0 }) return ThreeCandlePattern.NONE

        val whiteSoldiers = candles.all { it.close > it.open } &&
            second.open in minOf(first.open, first.close)..maxOf(first.open, first.close) &&
            third.open in minOf(second.open, second.close)..maxOf(second.open, second.close) &&
            second.close > first.close && third.close > second.close &&
            bodies[1] >= bodies[0] * 0.5 && bodies[2] >= bodies[1] * 0.5

        if (whiteSoldiers) return ThreeCandlePattern.THREE_WHITE_SOLDIERS

        val blackCrows = candles.all { it.close < it.open } &&
            second.open in minOf(first.open, first.close)..maxOf(first.open, first.close) &&
            third.open in minOf(second.open, second.close)..maxOf(second.open, second.close) &&
            second.close < first.close && third.close < second.close &&
            bodies[1] >= bodies[0] * 0.5 && bodies[2] >= bodies[1] * 0.5

        return if (blackCrows) ThreeCandlePattern.THREE_BLACK_CROWS else ThreeCandlePattern.NONE
    }

    fun nameUrdu(pattern: ThreeCandlePattern): String = when (pattern) {
        ThreeCandlePattern.NONE -> "کوئی واضح three-candle pattern نہیں"
        ThreeCandlePattern.THREE_WHITE_SOLDIERS -> "Three White Soldiers"
        ThreeCandlePattern.THREE_BLACK_CROWS -> "Three Black Crows"
    }

    fun explanationUrdu(pattern: ThreeCandlePattern): String = when (pattern) {
        ThreeCandlePattern.NONE -> "تین candles سے واضح soldiers/crows pattern نہیں بنا؛ trend، volume اور price context دیکھیں۔"
        ThreeCandlePattern.THREE_WHITE_SOLDIERS -> "تین bullish candles کے closes مسلسل اوپر ہیں اور اگلی openings پچھلی body کے اندر ہیں؛ buyers کی مضبوطی کا ممکنہ اشارہ، یقینی پیش گوئی نہیں۔"
        ThreeCandlePattern.THREE_BLACK_CROWS -> "تین bearish candles کے closes مسلسل نیچے ہیں اور اگلی openings پچھلی body کے اندر ہیں؛ selling pressure کا ممکنہ اشارہ، یقینی پیش گوئی نہیں۔"
    }

    /** Use the same OHLC/range guard as single- and two-candle analysis. */
    private fun isValid(candle: Candle): Boolean = CandleAnalysis.isValid(candle)
}
