package com.candlesense.ai

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@androidx.compose.runtime.Composable
fun CandleChart(
    candles: List<Candle>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (candles.isEmpty()) return
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(230.dp)
            .pointerInput(candles.size) {
                detectTapGestures { tap ->
                    CandleAnalysis.nearestIndexForTap(tap.x, size.width.toFloat(), candles.size)?.let(onSelect)
                }
            }
    ) {
        val minPrice = candles.minOf { it.low }
        val maxPrice = candles.maxOf { it.high }
        val range = (maxPrice - minPrice).coerceAtLeast(0.0001)
        val y = { price: Double -> size.height - ((price - minPrice) / range).toFloat() * size.height }
        val gridColor = Color(0xFF2B3B50)
        for (line in 0..4) {
            val yPos = size.height * line / 4f
            drawLine(gridColor, start = androidx.compose.ui.geometry.Offset(0f, yPos),
                end = androidx.compose.ui.geometry.Offset(size.width, yPos), strokeWidth = 1f)
        }
        val spacing = size.width / (candles.size + 1)
        val bodyWidth = (spacing * 0.52f).coerceAtLeast(5f)
        candles.forEachIndexed { index, candle ->
            val x = spacing * (index + 1)
            val rising = candle.close >= candle.open
            val candleColor = if (rising) Color(0xFF42D99B) else Color(0xFFFF6B78)
            if (index == selectedIndex) {
                drawLine(Color(0xFF7CB7FF), start = androidx.compose.ui.geometry.Offset(x, 0f),
                    end = androidx.compose.ui.geometry.Offset(x, size.height), strokeWidth = 1.5f)
            }
            drawLine(candleColor, start = androidx.compose.ui.geometry.Offset(x, y(candle.high)),
                end = androidx.compose.ui.geometry.Offset(x, y(candle.low)), strokeWidth = 2.5f, cap = StrokeCap.Round)
            val top = y(maxOf(candle.open, candle.close))
            val bottom = y(minOf(candle.open, candle.close))
            val bodyHeight = (bottom - top).coerceAtLeast(4f)
            drawRect(candleColor, topLeft = androidx.compose.ui.geometry.Offset(x - bodyWidth / 2, top),
                size = androidx.compose.ui.geometry.Size(bodyWidth, bodyHeight))
        }
    }
}
