# CandleSense AI — Fixed Progress Baseline v1.0

Date established: 2026-11-25

Progress uses fixed milestone weights totaling 100. Only verified completed milestones count; in-progress work does not. This is a methodology correction from earlier informal estimates, not a code regression.

| Milestone | Weight | Status | Evidence / remaining |
|---|---:|---|---|
| Offline sample charts and OHLC inspector | 15 | Complete | README describes demo BTC/USD, ETH/USD, SOL/USD charts and tap inspector |
| Pattern and context analysis (3-candle hints, pair bias, body/wick, 5-candle high/low context) | 20 | Complete | README + recent analysis/UI/test commits |
| Replay and local symbol search/navigation | 10 | Complete | README lists Previous/Next replay and local demo symbol search |
| Urdu educational explanations and non-predictive labels | 5 | Complete | README + pair-bias test commit |
| Live market data and licensed provider integration | 15 | Not complete | No live feed connected |
| Asset fundamentals/tokenomics profiles | 5 | Not complete | Not connected |
| Advanced trend and support/resistance analysis | 10 | Not complete | Not implemented |
| Bull/bear scenarios with uncertainty | 5 | Not complete | Not implemented |
| Offline data cache, refresh and error handling | 5 | Not complete | Not implemented |
| Accessibility, rotation, touch accuracy and device QA | 5 | Not complete | Requires Android device verification |
| Release build, artifact and release-readiness checks | 5 | Not complete | CI/build results not currently visible from status endpoint |

**Verified milestone total: 50 / 100 = 50%.**

The prior 42% was a rough estimate and is not comparable. The latest commit's combined status endpoint returned no individual status entries; this must not be interpreted as CI success.
