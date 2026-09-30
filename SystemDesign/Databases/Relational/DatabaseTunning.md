## Plan
```
1. Identify what changed
        ↓
2. Find top queries by CPU/time/execution count
        ↓
3. EXPLAIN ANALYZE
        ↓
4. Check scans + indexes
        ↓
5. Check cardinality/statistics
        ↓
6. Check joins/sorts/disk spills
        ↓
7. Check locks + long transactions
        ↓
8. Check connection/concurrency growth
        ↓
9. Check CPU/memory/I/O/cache
        ↓
10. Optimize query/schema/index/workload
        ↓
11. Scale/partition/cache only where justified
```