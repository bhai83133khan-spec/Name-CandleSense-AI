# CandleSense AI requirements and progress

## Learning experience
- [x] Offline demo chart and sample assets
- [x] Tap candle selection (nearest rendered-candle hit testing and safe edge clamping)
- [x] OHLC inspector
- [x] Body/wick measurements
- [x] Buyers/sellers interpretation (educational, non-predictive)
- [x] Previous candle context
- [x] Rough rolling high/low for selected candles
- [x] Rolling-level calculation ignores invalid OHLC data and rejects non-positive lookback
- [x] Single-candle OHLC validation (finite values and high/low enclosing the body) before classification
- [x] Replay stepping controls
- [ ] Live market data search/provider
- [ ] Real historical chart and timeframes
- [x] Single-candle pattern classification with Urdu explanations
- [x] Two-candle Bullish/Bearish Engulfing and Harami recognition with Urdu explanations and unit tests
- [x] Three-candle Soldiers/Crows classification with Urdu explanations and unit tests
- [ ] Expanded candlestick pattern library with visual examples
- [ ] Fundamentals, tokenomics, and asset background
- [ ] Support/resistance derived from real history
- [ ] Bull/bear scenario explanations with uncertainty
- [ ] Forex, gold and stocks (provider licensing/coverage check)
- [ ] Offline cache and refresh/error states

## Android and quality
- [x] Compose app scaffold
- [x] High-contrast vector launcher icon
- [x] CI unit tests and debug build
- [ ] Accessibility, screen-size and rotation QA
- [ ] Live API integration security review
- [ ] Release build and device verification

## Current limitation
Demo data is hard-coded and labeled. No exchange/broker connection or trade execution exists.

## Three-candle pattern milestone
- [x] Three White Soldiers / Three Black Crows shape classification with Urdu learning explanations and unit tests
- [x] Integrate three-candle classifications into the selected-candle inspector
- [ ] Add visual examples for three-candle patterns
