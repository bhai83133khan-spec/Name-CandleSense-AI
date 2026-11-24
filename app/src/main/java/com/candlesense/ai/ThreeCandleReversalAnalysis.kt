package com.candlesense.ai

enum class ThreeCandleReversalPattern {
    NONE,
    MORNING_STAR,
    EVENING_STAR
}

/**
 * Three-candle reversal-pattern hints based on body geometry.
 * These are educational heuristics only; trend, volume, gaps and market context are not evaluated.
 */
object ThreeCandleReversalAnalysis {
    fun classify(first: Candle, middle: Candle, last: Candle): ThreeCandleReversalPattern {
        if (!listOf(first, middle, last).all(::isValid)) return ThreeCandleReversalPattern.NONE
        val firstBody = kotlin.math.abs(first.close - first.open)
        val middleBody = kotlin.math.abs(middle.close - middle.open)
        if (firstBody <= 0.0 || middleBody <= 0.0) return ThreeCandleReversalPattern.NONE

        val firstMidpoint = (first.open + first.close) / 2.0
        val smallMiddle = middleBody <= firstBody * 0.35
        val morningStar = first.close < first.open &&
            smallMiddle &&
            last.close > last.open &&
            last.close > firstMidpoint

        if (morningStar) return ThreeCandleReversalPattern.MORNING_STAR

        val eveningStar = first.close > first.open &&
            smallMiddle &&
            last.close < last.open &&
            last.close < firstMidpoint

        return if (eveningStar) ThreeCandleReversalPattern.EVENING_STAR else ThreeCandleReversalPattern.NONE
    }

    fun nameUrdu(pattern: ThreeCandleReversalPattern): String = when (pattern) {
        ThreeCandleReversalPattern.NONE -> "کوئی واضح Star reversal pattern نہیں"
        ThreeCandleReversalPattern.MORNING_STAR -> "Morning Star (مارننگ اسٹار)"
        ThreeCandleReversalPattern.EVENING_STAR -> "Evening Star (ایوننگ اسٹار)"
    }

    fun explanationUrdu(pattern: ThreeCandleReversalPattern): String = when (pattern) {
        ThreeCandleReversalPattern.NONE -> "تین candles کی body شکل واضح Morning/Evening Star نہیں بناتی؛ trend اور volume بھی دیکھیں۔"
        ThreeCandleReversalPattern.MORNING_STAR -> "پہلی bearish candle، درمیان میں چھوٹی body، اور آخری bullish candle پہلی body کے midpoint سے اوپر close ہوئی۔ یہ ممکنہ reversal hint ہے، ضمانت نہیں۔"
        ThreeCandleReversalPattern.EVENING_STAR -> "پہلی bullish candle، درمیان میں چھوٹی body، اور آخری bearish candle پہلی body کے midpoint سے نیچے close ہوئی۔ یہ ممکنہ reversal hint ہے، ضمانت نہیں۔"
    }

    private fun isValid(candle: Candle): Boolean =
        candle.open.isFinite() && candle.high.isFinite() && candle.low.isFinite() && candle.close.isFinite() &&
            candle.high >= maxOf(candle.open, candle.close) &&
            candle.low <= minOf(candle.open, candle.close) &&
            candle.high >= candle.low
}
