package com.candlesense.ai

/**
 * Filters only the local demo symbols. Trimming and case-insensitive matching
 * keep the UI search forgiving without implying that live market lookup exists.
 */
internal fun filterDemoSymbols(search: String, symbols: List<String>): List<String> {
    val query = search.trim()
    return symbols.filter { it.contains(query, ignoreCase = true) }
}
