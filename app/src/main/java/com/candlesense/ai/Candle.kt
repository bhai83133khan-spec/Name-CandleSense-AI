package com.candlesense.ai

import kotlin.math.roundToInt

data class Candle(
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double
)

enum class CandleDirection { BULLISH, BEARISH, NEUTRAL }

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
