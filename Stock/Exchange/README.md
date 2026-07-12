## Fn
1. Order Entry
2. Order Matching
3. ExecutionReporting
4. Market Data Dessemination
5. Clearing & Settlement

## NonFn
1. Ultra low latency (<50ms)
2. High Throughput (1M+ messages per sec), especailly during market open close burst
3. Fairness (Strict FIFO)
4. Fault Tolerance


## DataModel
1. In-Memory State (The order Book)
- Active State of market lives entirely in RAM
- Utilizing custom data structures optimized for L1/L2 CPU cache lines to avoid expensive main-memory fetches.

2. Event Journal (Aeron / Highly Tuned Kafka)
- Aeron - Microsecond Latency, Cluster (Fault Tolerance)
- Immutable, append-only distributed log
- it records every incoming order before it hits the matching engine.

3. Relational Database
- The "Cold Path" persistance layer
- used for eod clearing, storing participant account balances, reference data, & regularity reporting.

## Protocol
- FIX (Financial Information Exchange) BSE API Doc
    - Universal Standardization
    - Built-in reliability (session management, hearbeat pings, retries)
    - Efficiency (SBE - Simple Binary Encoding)


## API
1. Order Entry (FIX)
- NewOrderSingle (MsgType=D)
    * Submits new order containing Symbol, Side (Buy/Sell), OrderQty, price, ClOrdID (Client Order ID)
- OrderCancelRequest (MsgType=F)
    * Requests cancellation of an active order.

2. Execution Reports (FIX)
- ExecutionReport (MsgType=8)
    * Sent back to the client ACK an order receipt, fill or cancel.

3. Market Data (Binary/SBE over UDP)
- Exchanges use custom binary protocol encoded with SBE distributed via UDP Multicast to provide the lowest possible wire-size & latency.Exchange
- Thread Pinning
- Lock-Free Ring Buffers
- Mechanical Sympathy (prevents False sharing)
- Zero Garbage Collection (Zero dynamic memory allocation)

### UDP Multicast
1. Hardware Duplication
2. O(1) Sever Load
3. Absolute Fairness
4. Zero Handshakes

## HLD
### Matching Engine
1. GlobalHashMap
2. DoublyLinkedList


## References
https://www.youtube.com/watch?v=3KzWb8bwTv8
