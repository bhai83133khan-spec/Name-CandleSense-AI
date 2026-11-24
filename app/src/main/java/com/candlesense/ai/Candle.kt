package com.candlesense.ai

import kotlin.math.roundToInt

data class Candle(
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double
)

enum class CandleDirection { BULLISH, BEARISH, NEUTRAL }

enum class CandlePattern { DOJI, HAMMER, SHOOTING_STAR, SPINNING_TOP, MARUBOZU, BULLISH_BODY, BEARISH_BODY }

object CandleAnalysis {
    /** Maps a chart tap to the nearest candle using the same center spacing as CandleChart. */
    fun nearestIndexForTap(tapX: Float, chartWidth: Float, candleCount: Int): Int? {
        if (candleCount <= 0 || !tapX.isFinite() || !chartWidth.isFinite() || chartWidth <= 0f) return null
        val spacing = chartWidth / (candleCount + 1)
        if (!spacing.isFinite() || spacing <= 0f) return null
        return (tapX / spacing).roundToInt().minus(1).coerceIn(0, candleCount - 1)
    }


    fun direction(candle: Candle): CandleDirection = when {
        candle.close > candle.open -> CandleDirection.BULLISH
        candle.close < candle.open -> CandleDirection.BEARISH
        else -> CandleDirection.NEUTRAL
    }

    /** Lightweight single-candle pattern hints; use surrounding trend for confirmation. */
    fun pattern(candle: Candle): CandlePattern {
        val range = (candle.high - candle.low).coerceAtLeast(0.0)
        if (range <= 0.0) return CandlePattern.DOJI
        val body = kotlin.math.abs(candle.close - candle.open)
        val upper = upperWick(candle)
        val lower = lowerWick(candle)
        val ratio = body / range

        if (ratio <= 0.10) return CandlePattern.DOJI
        if (ratio < 0.35 && lower >= body * 2.0 && upper <= body * 0.5) return CandlePattern.HAMMER
        if (ratio < 0.35 && upper >= body * 2.0 && lower <= body * 0.5) return CandlePattern.SHOOTING_STAR
        if (ratio <= 0.30 && upper > body && lower > body) return CandlePattern.SPINNING_TOP
        if (ratio >= 0.85) return CandlePattern.MARUBOZU
        return when (direction(candle)) {
            CandleDirection.BULLISH -> CandlePattern.BULLISH_BODY
            CandleDirection.BEARISH -> CandlePattern.BEARISH_BODY
            CandleDirection.NEUTRAL -> CandlePattern.DOJI
        }
    }

    fun patternNameUrdu(candle: Candle): String = when (pattern(candle)) {
        CandlePattern.DOJI -> "Doji (دوجی)"
        CandlePattern.HAMMER -> "Hammer (ہتھوڑا)"
        CandlePattern.SHOOTING_STAR -> "Shooting Star"
        CandlePattern.SPINNING_TOP -> "Spinning Top"
        CandlePattern.MARUBOZU -> "Marubozu"
        CandlePattern.BULLISH_BODY -> "Bullish Body"
        CandlePattern.BEARISH_BODY -> "Bearish Body"
    }

    fun patternExplanationUrdu(candle: Candle): String = when (pattern(candle)) {
        CandlePattern.DOJI -> "اوپن اور کلوز قریب ہیں؛ مارکیٹ میں غیر فیصلہ کن کیفیت ہو سکتی ہے۔ اگلی candles سے تصدیق کریں۔"
        CandlePattern.HAMMER -> "نیچے لمبی wick قیمت کے نیچے ردّ ہونے کا اشارہ دے سکتی ہے؛ downtrend کے بعد ممکنہ reversal، مگر ضمانت نہیں۔"
        CandlePattern.SHOOTING_STAR -> "اوپر لمبی wick اونچی قیمت ردّ ہونے کا اشارہ دے سکتی ہے؛ اوپر کے رجحان میں احتیاط اور اگلی candle سے تصدیق کریں۔"
        CandlePattern.SPINNING_TOP -> "چھوٹا body اور دونوں طرف wicks بتاتی ہیں کہ دونوں سمت حرکت ہوئی مگر فیصلہ کمزور رہا۔"
        CandlePattern.MARUBOZU -> "body بڑی اور wicks چھوٹی ہیں؛ اس دورانیے میں directional momentum مضبوط تھا، مگر اکیلی candle کافی نہیں۔"
        CandlePattern.BULLISH_BODY -> "کلوز اوپن سے اوپر ہے؛ buyers نسبتاً مضبوط رہے۔ اسے support، trend اور volume کے ساتھ دیکھیں۔"
        CandlePattern.BEARISH_BODY -> "کلوز اوپن سے نیچے ہے؛ sellers نسبتاً مضبوط رہے۔ اسے support، trend اور volume کے ساتھ دیکھیں۔"
    }

    fun bodyRatio(candle: Candle): Double {
        val range = (candle.high - candle.low).coerceAtLeast(0.0)
        if (range <= 0.0) return 0.0
        return (kotlin.math.abs(candle.close - candle.open) / range).coerceIn(0.0, 1.0)
    }

    fun upperWick(candle: Candle): Double =
        (candle.high - maxOf(candle.open, candle.close)).coerceAtLeast(0.0)

    fun lowerWick(candle: Candle): Double =
        (minOf(candle.open, candle.close) - candle.low).coerceAtLeast(0.0)

    fun recentHigh(candles: List<Candle>, index: Int, lookback: Int = 5): Double? {
        if (candles.isEmpty() || index !in candles.indices) return null
        val start = (index - lookback + 1).coerceAtLeast(0)
        return candles.subList(start, index + 1).maxOfOrNull { it.high }
    }

    fun recentLow(candles: List<Candle>, index: Int, lookback: Int = 5): Double? {
        if (candles.isEmpty() || index !in candles.indices) return null
        val start = (index - lookback + 1).coerceAtLeast(0)
        return candles.subList(start, index + 1).minOfOrNull { it.low }
    }

    fun buyerSellerSummary(candle: Candle): String = when (direction(candle)) {
        CandleDirection.BULLISH -> "اس مدت میں خریدار نسبتاً مضبوط رہے، مگر ایک candle مستقبل کی ضمانت نہیں۔"
        CandleDirection.BEARISH -> "اس مدت میں فروخت کنندگان نسبتاً مضبوط رہے، مگر ایک candle مستقبل کی ضمانت نہیں۔"
        CandleDirection.NEUTRAL -> "Open اور Close تقریباً برابر ہیں؛ دونوں طرف کی قوت کا واضح فیصلہ صرف اس candle سے نہیں ہوتا۔"
    }
}
