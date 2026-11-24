package com.candlesense.ai

import org.junit.Assert.assertEquals
import org.junit.Test

class DemoSymbolFilterTest {
    private val symbols = listOf("BTC/USD", "ETH/USD", "SOL/USD")

    @Test
    fun emptyQueryReturnsAllLocalDemoSymbols() {
        assertEquals(symbols, filterDemoSymbols("  ", symbols))
    }

    @Test
    fun queryIsTrimmedAndCaseInsensitive() {
        assertEquals(listOf("BTC/USD"), filterDemoSymbols(" btc ", symbols))
        assertEquals(listOf("ETH/USD"), filterDemoSymbols("eth", symbols))
    }

    @Test
    fun unknownQueryReturnsEmptyList() {
        assertEquals(emptyList<String>(), filterDemoSymbols("XRP", symbols))
    }
}
