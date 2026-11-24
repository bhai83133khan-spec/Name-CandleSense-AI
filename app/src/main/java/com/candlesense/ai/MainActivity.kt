package com.candlesense.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CandleSenseApp() }
    }
}

private val demoCoins = listOf("BTC/USD", "ETH/USD", "SOL/USD")

private val demoData = mapOf(
    "BTC/USD" to listOf(
        Candle(65000.0, 65500.0, 64500.0, 65200.0), Candle(65200.0, 66000.0, 65000.0, 65800.0),
        Candle(65800.0, 66200.0, 65100.0, 65400.0), Candle(65400.0, 65700.0, 64600.0, 64900.0),
        Candle(64900.0, 65500.0, 64700.0, 65300.0), Candle(65300.0, 66700.0, 65100.0, 66400.0),
        Candle(66400.0, 67000.0, 66000.0, 66800.0), Candle(66800.0, 67200.0, 66200.0, 66500.0),
        Candle(66500.0, 66800.0, 65700.0, 65900.0), Candle(65900.0, 66600.0, 65600.0, 66400.0),
        Candle(66400.0, 67000.0, 66200.0, 66900.0), Candle(66900.0, 67500.0, 66500.0, 67200.0)
    ),
    "ETH/USD" to listOf(
        Candle(3400.0, 3440.0, 3340.0, 3420.0), Candle(3420.0, 3500.0, 3400.0, 3480.0),
        Candle(3480.0, 3520.0, 3410.0, 3450.0), Candle(3450.0, 3470.0, 3360.0, 3390.0),
        Candle(3390.0, 3450.0, 3370.0, 3430.0), Candle(3430.0, 3550.0, 3410.0, 3520.0),
        Candle(3520.0, 3580.0, 3480.0, 3560.0), Candle(3560.0, 3600.0, 3500.0, 3530.0),
        Candle(3530.0, 3560.0, 3470.0, 3490.0), Candle(3490.0, 3550.0, 3460.0, 3540.0),
        Candle(3540.0, 3590.0, 3520.0, 3570.0), Candle(3570.0, 3620.0, 3530.0, 3600.0)
    ),
    "SOL/USD" to listOf(
        Candle(150.0, 154.0, 147.0, 152.0), Candle(152.0, 158.0, 151.0, 157.0),
        Candle(157.0, 159.0, 153.0, 154.0), Candle(154.0, 156.0, 149.0, 151.0),
        Candle(151.0, 155.0, 150.0, 154.0), Candle(154.0, 161.0, 153.0, 159.0),
        Candle(159.0, 164.0, 157.0, 162.0), Candle(162.0, 165.0, 158.0, 160.0),
        Candle(160.0, 162.0, 155.0, 157.0), Candle(157.0, 161.0, 156.0, 160.0),
        Candle(160.0, 163.0, 159.0, 162.0), Candle(162.0, 166.0, 160.0, 164.0)
    )
)

@Composable
private fun CandleSenseApp() {
    var selectedCoin by remember { mutableStateOf("BTC/USD") }
    var search by remember { mutableStateOf("") }
    var selectedIndex by remember { mutableIntStateOf(0) }
    var replayMode by remember { mutableStateOf(false) }
    val candles = demoData[selectedCoin] ?: demoData.getValue("BTC/USD")
    val candle = candles[selectedIndex.coerceIn(0, candles.lastIndex)]
    val previous = candles.getOrNull(selectedIndex - 1)
    val recentHigh = CandleAnalysis.recentHigh(candles, selectedIndex)
    val recentLow = CandleAnalysis.recentLow(candles, selectedIndex)
    val colorScheme = lightColorScheme(
        primary = Color(0xFF173D70), secondary = Color(0xFF168C62),
        background = Color(0xFFF1F5FA), surface = Color.White
    )

    MaterialTheme(colorScheme = colorScheme) {
        Column(
            Modifier.fillMaxSize().background(Color(0xFFF1F5FA)).padding(14.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("CANDLESENSE AI", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black,
                color = Color(0xFF16334F))
            Text("Tap a candle • Learn one step at a time", color = Color(0xFF40556B))
            OutlinedTextField(
                value = search, onValueChange = { search = it }, modifier = Modifier.fillMaxWidth(),
                label = { Text("Search demo coin") }, singleLine = true
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                demoCoins.filter { it.contains(search.trim(), ignoreCase = true) }.forEach { symbol ->
                    FilterChip(
                        selected = selectedCoin == symbol,
                onClick = { selectedCoin = symbol; selectedIndex = 0 },
                label = { Text(symbol) }
                    )
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF101A2C))
            ) {
                Column(Modifier.padding(10.dp)) {
                    Text("${selectedCoin} • Sample chart", color = Color.White, fontWeight = FontWeight.Bold)
                    CandleChart(candles, selectedIndex, { selectedIndex = it })
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Body (Open–Close)", color = Color(0xFF42D99B))
                        Text("Wick (High–Low)", color = Color(0xFFB5C6DA))
                    }
                    Text("Demo candles only — live market data is not connected.", color = Color(0xFFB5C6DA))
                }
            }
            Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Text("CANDLE ${selectedIndex + 1} / ${candles.size}", color = Color(0xFF16334F), fontWeight = FontWeight.Bold)
                    val direction = CandleAnalysis.direction(candle)
                    val green = direction == CandleDirection.BULLISH
                    Text(if (green) "Bullish candle (green)" else if (direction == CandleDirection.BEARISH) "Bearish candle (red)" else "Neutral candle",
                        color = if (green) Color(0xFF138456) else Color(0xFFD34454), fontWeight = FontWeight.Bold)
                    Text("Pattern: ${CandleAnalysis.patternNameUrdu(candle)}", color = Color(0xFF176B57), fontWeight = FontWeight.Bold)
                    Text(CandleAnalysis.patternExplanationUrdu(candle), color = Color(0xFF40556B))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        PriceCard("Open", candle.open, Modifier.weight(1f))
                        PriceCard("High", candle.high, Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        PriceCard("Low", candle.low, Modifier.weight(1f))
                        PriceCard("Close", candle.close, Modifier.weight(1f))
                    }
                    Text("Body: ${(CandleAnalysis.bodyRatio(candle) * 100).toInt()}% of candle range")
                    Text("Upper wick: ${CandleAnalysis.upperWick(candle)} • Lower wick: ${CandleAnalysis.lowerWick(candle)}")
                    Text(CandleAnalysis.buyerSellerSummary(candle), color = Color(0xFF273F57))
                    Text(
                        if (previous == null) "Previous candle: none" else "Previous close: ${formatPrice(previous.close)} • Current close is ${if (candle.close >= previous.close) "above" else "below"} it",
                        color = Color(0xFF40556B)
                    )
                    if (previous != null) {
                        val pairPattern = CandlePairAnalysis.classify(previous, candle)
                        Text("Two-candle pattern: ${CandlePairAnalysis.nameUrdu(pairPattern)}",
                            color = Color(0xFF176B57), fontWeight = FontWeight.Bold)
                        Text(CandlePairAnalysis.explanationUrdu(pairPattern), color = Color(0xFF40556B))
                    }
                    if (selectedIndex >= 2) {
                        val threePattern = ThreeCandleAnalysis.classify(
                            candles[selectedIndex - 2], candles[selectedIndex - 1], candle
                        )
                        Text("Three-candle pattern: ${ThreeCandleAnalysis.nameUrdu(threePattern)}",
                            color = Color(0xFF176B57), fontWeight = FontWeight.Bold)
                        Text(ThreeCandleAnalysis.explanationUrdu(threePattern), color = Color(0xFF40556B))
                    }
                    if (recentHigh != null && recentLow != null) {
                        Text("Recent 5 candles: rough high ${formatPrice(recentHigh)} / low ${formatPrice(recentLow)}",
                            color = Color(0xFF40556B))
                    }
                    Text("Common mistake: صرف candle کے رنگ پر buy/sell فیصلہ نہ کریں۔", color = Color(0xFF9B4D24))
                }
            }
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Text("Replay learning mode")
                Spacer(Modifier.weight(1f))
                Switch(checked = replayMode, onCheckedChange = { replayMode = it; if (it) selectedIndex = 0 })
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { selectedIndex = (selectedIndex - 1).coerceAtLeast(0) },
                    enabled = selectedIndex > 0, modifier = Modifier.weight(1f)) { Text("Previous") }
                Button(onClick = { selectedIndex = (selectedIndex + 1).coerceAtMost(candles.lastIndex) },
                    enabled = selectedIndex < candles.lastIndex, modifier = Modifier.weight(1f)) { Text("Next candle") }
            }
            if (replayMode) Text("Replay: use Next to reveal each candle sequentially.", color = Color(0xFF40556B))
        }
    }
}

@Composable
private fun PriceCard(label: String, value: Double, modifier: Modifier = Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F7FB))) {
        Column(Modifier.padding(10.dp)) {
            Text(label, color = Color(0xFF40556B))
            Text(formatPrice(value), fontWeight = FontWeight.Bold, color = Color(0xFF16334F))
        }
    }
}

private fun formatPrice(value: Double): String = String.format(Locale.US, "%.2f", value)
