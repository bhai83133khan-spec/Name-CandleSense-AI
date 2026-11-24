# CandleSense AI — interactive candle-learning app

CandleSense AI is an Android-first learning tool for understanding candlesticks, markets, and context in plain Urdu.

## Working milestone (CI-verified)
- Offline Compose app with demo BTC/USD, ETH/USD and SOL/USD charts
- Tap-to-select nearest rendered candle (hit regions aligned to candle centers, with safe edge clamping) and OHLC inspector
- Invalid/non-finite OHLC values are rejected by single-candle classification and excluded from rolling high/low levels; non-positive lookback is safely rejected
- Three-candle pattern hints (Three White Soldiers / Three Black Crows) surfaced in the candle inspector with Urdu explanations
- High-contrast bullish/bearish candle bodies and wicks
- Body/wick measurements and recent five-candle high/low context
- Buyer/seller interpretation and previous-close comparison
- Sequential replay controls (tap Previous/Next)
- Search/filter among local demo symbols
- Phone-friendly scroll layout
- Vector launcher icon
- Unit tests and GitHub Actions debug APK build succeeded

## Not yet connected
- Live coin search, live prices, or full historical data
- Licensed data provider for crypto, forex, gold, and stocks
- Asset fundamentals/tokenomics/background profiles
- Historical support/resistance and advanced trend analysis
- Bull/bear scenario generation with uncertainty
- Offline market-data cache and refresh/error handling
- Device QA for accessibility, rotation, and touch accuracy

## Data disclaimer
The chart is illustrative sample data only. It is not an exchange feed and must not be used to make trading decisions.

## Build
GitHub Actions builds the debug APK on push to `main` or by manual dispatch. APK artifacts are not distributed until all projects are complete and verified.
