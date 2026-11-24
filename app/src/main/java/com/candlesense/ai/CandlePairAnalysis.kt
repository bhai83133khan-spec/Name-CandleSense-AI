package com.candlesense.ai

enum class CandlePairPattern {
    NONE,
    BULLISH_ENGULFING,
    BEARISH_ENGULFING,
    BULLISH_HARAMI,
    BEARISH_HARAMI
}

enum class CandlePairBias { BULLISH, BEARISH, NEUTRAL }

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

    /** Educational directional context only; explicitly avoids presenting patterns as trade signals. */
    fun bias(pattern: CandlePairPattern): CandlePairBias = when (pattern) {
        CandlePairPattern.BULLISH_ENGULFING, CandlePairPattern.BULLISH_HARAMI -> CandlePairBias.BULLISH
        CandlePairPattern.BEARISH_ENGULFING, CandlePairPattern.BEARISH_HARAMI -> CandlePairBias.BEARISH
        CandlePairPattern.NONE -> CandlePairBias.NEUTRAL
    }

    fun biasLabelUrdu(pattern: CandlePairPattern): String = when (bias(pattern)) {
        CandlePairBias.BULLISH -> "ممکنہ bullish bias — یہ trade signal نہیں؛ trend، volume اور اگلی candle کی تصدیق کریں۔"
        CandlePairBias.BEARISH -> "ممکنہ bearish bias — یہ trade signal نہیں؛ trend، volume اور اگلی candle کی تصدیق کریں۔"
        CandlePairBias.NEUTRAL -> "Neutral bias — واضح دو-candle سمت نہیں؛ market context دیکھیں۔"
    }

    /** Practical confirmation checklist for learners; never presents a pattern as a standalone trade signal. */
    fun confirmationChecklistUrdu(pattern: CandlePairPattern): String = when (pattern) {
        CandlePairPattern.NONE -> "تصدیق: trend، support/resistance، volume اور اگلی candle دیکھیں۔"
        CandlePairPattern.BULLISH_ENGULFING -> "تصدیق: downtrend یا support، volume میں اضافہ، اور اگلی candle کا bullish follow-through دیکھیں۔"
        CandlePairPattern.BEARISH_ENGULFING -> "تصدیق: uptrend یا resistance، volume میں اضافہ، اور اگلی candle کا bearish follow-through دیکھیں۔"
        CandlePairPattern.BULLISH_HARAMI -> "تصدیق: support، volume، اور bullish follow-through candle کے بغیر اسے reversal نہ سمجھیں۔"
        CandlePairPattern.BEARISH_HARAMI -> "تصدیق: resistance، volume، اور bearish follow-through candle کے بغیر اسے reversal نہ سمجھیں۔"
    }

    fun explanationUrdu(pattern: CandlePairPattern): String = when (pattern) {
        CandlePairPattern.NONE -> "دو candles کی شکل سے کوئی واضح pattern نہیں بنا؛ trend اور volume بھی دیکھیں۔"
        CandlePairPattern.BULLISH_ENGULFING -> "دوسری bullish body پہلی bearish body کو گھیرتی ہے؛ خریداروں کی قوت بڑھنے کا ممکنہ اشارہ، مگر تصدیق ضروری ہے۔"
        CandlePairPattern.BEARISH_ENGULFING -> "دوسری bearish body پہلی bullish body کو گھیرتی ہے؛ فروخت کا دباؤ بڑھنے کا ممکنہ اشارہ، مگر تصدیق ضروری ہے۔"
        CandlePairPattern.BULLISH_HARAMI -> "چھوٹی bullish body پہلی bearish body کے اندر ہے؛ رفتار کم یا بدلتی ہوئی ہو سکتی ہے، اکیلا reversal signal نہیں۔"
        CandlePairPattern.BEARISH_HARAMI -> "چھوٹی bearish body پہلی bullish body کے اندر ہے؛ رفتار کم یا بدلتی ہوئی ہو سکتی ہے، اکیلا reversal signal نہیں۔"
    }

    // Reuse the shared validator so two-candle patterns also reject finite endpoints
    // whose derived range overflows, instead of drifting from single-candle rules.
    private fun isValid(candle: Candle): Boolean = CandleAnalysis.isValid(candle)
}
