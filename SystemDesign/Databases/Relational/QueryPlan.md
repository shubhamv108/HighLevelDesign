```
SQL Query
   ↓
Parser
   ↓
Query transformations
   ↓
Optimizer
   ↓
Generate possible execution plans
   ↓
Estimate rows + cost of plans
   ↓
Choose estimated cheapest plan
   ↓
Execute
```

    MySQL's cost-based optimizer generates candidate execution strategies by considering table access methods, indexes, join order, join algorithms, filtering, sorting and other operations.
    It uses table/index statistics and cardinality estimates to estimate the cost of those strategies and chooses the estimated lowest-cost execution plan.
    We can inspect the estimated plan with EXPLAIN and compare estimates with actual execution using EXPLAIN ANALYZE.
    ```
    Statistics
        ↓
    Cardinality / selectivity estimation
        ↓
    Access paths
        ↓
    Join order
        ↓
    Join strategy
        ↓
    Cost estimation
        ↓
    Lowest estimated-cost plan
        ↓
    Execution
    ```