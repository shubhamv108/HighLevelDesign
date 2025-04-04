Framework and distributed processing engine for stateful computations over unbounded and bounded data streams.

1. **Unbounded Streams**
- have a start but no defined end.
- Processing unbounded data often requires that events are ingested in a specific order, such as the order in which events occurred, 
  to be able to reason about result completeness.

2. **Bounded streams**
- Have a defined start and end.
- Bounded streams can be processed by ingesting all data before performing any computations.
- Ordered ingestion is not required to process bounded streams because a bounded data set can always be sorted.
- Processing of bounded streams is also known as batch processing.

- Parallel (group by A)
- Forward
- Repartition (Shuffle) (group by B)
- Rebalance
  
# Flink SQL

# Table API (dynamic tables)
declarative DSL
- Changelog stream 

Short Name | Long Name | Semantics
-----| --------- | -------
+I | Insertion | Default
-U | Update Before | Retract an earlier result
+U | Update After | Update an earlier result
-D | Delete | Delete an earlier result

# DataStream APi (streams, windows)
stream processing & analytics

# Process Functions (events, state, time)
low level stateful stream processing


# SQl operations
### Stateless
- SELECT
- WHERE
### Materializing (dangerousl stateful)
- GROUP BY
- regular JOINs
### Temporal (safely stateful)
- time-windowed aggregations
- interval-joins
- time versioned joins
- MATCH_RECOGNIZE (pattern matching)

# Checkpoint
Algorithm:  Chandy-Lamport Distributed Snapshot Algorithm, globally consistent snapshots
