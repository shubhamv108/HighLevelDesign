## Algorithms
1. Count Based
2. Time Based

## State
1. CLOSED
    Normal operation. Requests flow through. Failure rate is being tracked in a sliding window. If failure rate exceeds the threshold, it transitions to OPEN.
2. OPEN
    Circuit is tripped. All requests are rejected immediately (fail-fast) without calling the downstream service. A CallNotPermittedException is thrown. After a configured wait duration, it moves to HALF_OPEN.
3. HALF_OPEN
    Allows a limited number of probe requests through to test if the downstream has recovered. If those succeed → back to CLOSED. If they fail → back to OPEN.

Special States:
4. DISABLED
    Circuit breaker is turned off completely. All requests pass through, no state tracking, no metrics. Has to be manually triggered.
5. FORCED_OPEN
    Manually forced open. All requests rejected regardless of actual failure rate. Useful for maintenance or manual intervention.