```
server.tomcat.accept-count=100  # Max queue size before rejecting requests
server.tomcat.max-threads=200   # Max worker threads to process requests
server.tomcat.max-connections=5000  # Limit max concurrent connections
```

- OS backlog queue size
    - Increase net.core.somaxconn on Linux to match a higher accept-count.
        ```
        sysctl net.core.somaxconn
        ```
        ```
        sysctl -w net.core.somaxconn=1024
        ```

- For CPU-heavy apps → max-threads ≈ 2 × CPU cores
- For I/O-heavy apps → max-threads ≈ 10–50 × CPU cores


Threads (max-threads) |	Recommended CPU Cores |	Recommended RAM
--------------------- | --------------------- | ---------------
500  |                  4–8 cores |	            4–8 GB
2000 |	                8–16 cores |            16–32 GB
5000 |	                32–64 cores |           64–128 GB

<br/>

Instance | Family     |    Best For
-------- |------------| ------------ 
T-Series | (t3, t4g)  |	Small, burstable apps (low-cost, auto CPU burst)
M-Series | (m6i, m5)  | 	Balanced workloads (CPU + memory balanced)               
C-Series | (c6i, c5)  | 	CPU-heavy workloads (faster request processing)          
R-Series |  (r6i, r5) | 	Memory-intensive apps (good for caching, large JVM heap) 

<br/>

Use Case | Instance
------------------------ | -----------------------------------
Small Apps (Low Traffic) |	        t3.large (2 vCPUs, 8GB RAM)
Medium Workload	         |          m6i.large (4 vCPUs, 16GB RAM)
High Concurrency (5K+ req/sec) |	c6i.4xlarge (16 vCPUs, 32GB RAM)
Extreme Load (10K+ req/sec) |	    c6i.8xlarge (32 vCPUs, 64GB RAM)