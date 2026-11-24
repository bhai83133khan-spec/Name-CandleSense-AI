package com.candlesense.ai

enum class CandlePairPattern {
    NONE,
    BULLISH_ENGULFING,
    BEARISH_ENGULFING,
    BULLISH_HARAMI,
    BEARISH_HARAMI
}

/**
 * Educational two-candle pattern hints.
 * These are shape-based observations, not trade signals; trend and volume context are still required.
 */
object CandlePairAnalysis {
    fun classify(previous: Candle, current: Candle): CandlePairPattern {
        if (!isValid(previous) || !isValid(current)) return CandlePairPattern.NONE

        val previousBodyLow = minOf(previous.open, previous.close)
        val previousBodyHigh = maxOf(previous.open, previous.close)
        val currentBodyLow = minOf(current.open, current.close)
        val currentBodyHigh = maxOf(current.open, current.close)
        val previousBody = previousBodyHigh - previousBodyLow
        val currentBody = currentBodyHigh - currentBodyLow
        if (previousBody <= 0.0 || currentBody <= 0.0) return CandlePairPattern.NONE

        val prevBearish = previous.close < previous.open
        val prevBullish = previous.close > previous.open
        val currBullish = current.close > current.open
        val currBearish = current.close < current.open

        if (prevBearish && currBullish &&
            current.open <= previous.close && current.close >= previous.open &&
            (current.open < previous.close || current.close > previous.open)
        ) return CandlePairPattern.BULLISH_ENGULFING

        if (prevBullish && currBearish &&
            current.open >= previous.close && current.close <= previous.open &&
            (current.open > previous.close || current.close < previous.open)
        ) return CandlePairPattern.BEARISH_ENGULFING

        if (prevBearish && currBullish &&
            currentBodyLow >= previousBodyLow && currentBodyHigh <= previousBodyHigh &&
            currentBody < previousBody
        ) return CandlePairPattern.BULLISH_HARAMI

        if (prevBullish && currBearish &&
            currentBodyLow >= previousBodyLow && currentBodyHigh <= previousBodyHigh &&
            currentBody < previousBody
        ) return CandlePairPattern.BEARISH_HARAMI

        return CandlePairPattern.NONE
    }

    fun nameUrdu(pattern: CandlePairPattern): String = when (pattern) {
        CandlePairPattern.NONE -> "کوئی واضح two-candle pattern نہیں"
        CandlePairPattern.BULLISH_ENGULFING -> "Bullish Engulfing"
        CandlePairPattern.BEARISH_ENGULFING -> "Bearish Engulfing"
        CandlePairPattern.BULLISH_HARAMI -> "Bullish Harami"
        CandlePairPattern.BEARISH_HARAMI -> "Bearish Harami"
    }

    fun explanationUrdu(pattern: CandlePairPattern): String = when (pattern) {
        CandlePairPattern.NONE -> "دو candles کی شکل سے کوئی واضح pattern نہیں بنا؛ trend اور volume بھی دیکھیں۔"
        CandlePairPattern.BULLISH_ENGULFING -> "دوسری bullish body پہلی bearish body کو گھیرتی ہے؛ خریداروں کی قوت بڑھنے کا ممکنہ اشارہ، مگر تصدیق ضروری ہے۔"
        CandlePairPattern.BEARISH_ENGULFING -> "دوسری bearish body پہلی bullish body کو گھیرتی ہے؛ فروخت کا دباؤ بڑھنے کا ممکنہ اشارہ، مگر تصدیق ضروری ہے۔"
        CandlePairPattern.BULLISH_HARAMI -> "چھوٹی bullish body پہلی bearish body کے اندر ہے؛ رفتار کم یا بدلتی ہوئی ہو سکتی ہے، اکیلا reversal signal نہیں۔"
        CandlePairPattern.BEARISH_HARAMI -> "چھوٹی bearish body پہلی bullish body کے اندر ہے؛ رفتار کم یا بدلتی ہوئی ہو سکتی ہے، اکیلا reversal signal نہیں۔"
    }

    private fun isValid(candle: Candle): Boolean =
        candle.open.isFinite() && candle.high.isFinite() && candle.low.isFinite() && candle.close.isFinite() &&
            candle.high >= maxOf(candle.open, candle.close) && candle.low <= minOf(candle.open, candle.close) &&
            candle.high >= candle.low
}
