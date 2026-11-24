# CandleSense AI — Live Data Integration Acceptance Plan

This is a planning/acceptance artifact. No live data integration is implemented by this document.

## Provider adapter contract
- Keep the chart/inspector/replay UI independent of the provider.
- Adapter returns normalized OHLC candles with timestamp, open, high, low, close, volume (optional).
- Reject non-finite values, non-positive prices, inverted high/low bounds, and inconsistent OHLC bounds.
- Preserve symbol, quote currency, and source/provider metadata.
- Request limits: page size and lookback are bounded; avoid unbounded network requests.
- UI states: Loading, Success, Empty, Offline/Stale, Rate Limited, Error. Show last-updated time for cached results.

## Integration stages
1. Select a provider with terms/licensing that permit intended usage; do not hard-code provider credentials.
2. Implement provider interface and mock adapter first; unit tests cover normalized data and malformed responses.
3. Add network adapter with timeout, cancellation, rate-limit handling, and clear error mapping.
4. Cache normalized candles; stale cache must be labeled stale, never silently presented as live.
5. Add symbol lookup without changing existing offline demo mode.
6. Validate the same chart/inspector/replay behavior on live and demo sources.
7. Build in CI, install on phone, verify portrait/landscape, touch selection, refresh, offline fallback, and accessibility.

## Release acceptance checks
- [ ] Provider source and license documented
- [ ] Secrets absent from repository
- [ ] Unit tests pass for valid/invalid OHLC
- [ ] Adapter tests cover empty, timeout, rate limit, malformed payload
- [ ] Cache expiry and stale label verified
- [ ] Offline demo mode still works
- [ ] CI build artifact verified
- [ ] Device QA recorded

## User safety
Educational explanations remain non-predictive. Live data availability does not turn pattern/context hints into trade signals.
