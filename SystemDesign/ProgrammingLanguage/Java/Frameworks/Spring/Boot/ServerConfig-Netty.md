```shell
reactor.netty.ioWorkerCount=16 # Controls the number of worker threads handling connections, reducing request delays.
reactor.netty.ioSelectCount=8 # Controls how many threads handle accepting new connections.
reactor.netty.pool.max-connections=10000 # Controls the Maximum Number of Concurrent Connections

server.netty.connection-backlog=1024 # This backlog queue stores incoming connections waiting to be accepted when all worker threads are busy.
```

- I/O-bound APIs (DB calls, external APIs) → max-connections = 10 × CPU cores
- CPU-heavy workloads → max-connections = 2 × CPU cores

Traffic Load | 	connection-backlog | 	max-connections       |	vCPUs |	RAM
------------ |---------------------|------------------------| ------- | ---
Low (1K req/sec) | 	512 |	5000                   |	2-4 |	4-8GB
Medium (5K req/sec) | 	1024               | 	10,000 |	8-16 |	16-32GB   
High (10K+ req/sec) | 	2048               | 	20,000 |	16-32 |	32-64GB  
Extreme (50K+ req/sec) | 	4096               | 	100,000 |	32-64 |	64-128GB 


Traffic Load |	Best EC2 Instance Type |	vCPUs |	RAM |	Max Connections
------------ | ----------------------- | -------- | --- | -----------------
Low (1K req/sec) |	t3.large / t4g.large |	2 |	8GB |	10,000+
Medium (5K req/sec) |	m6i.large / c6i.large |	4 |	16GB |	50,000+
High (10K+ req/sec) |	c6i.2xlarge / m6i.2xlarge |	8 |	32GB |	100,000+
Very High (50K+ req/sec) |	c6i.4xlarge / c6in.4xlarge |	16 |	64GB |	250,000+
Extreme Scale (100K+ req/sec) |	c6in.8xlarge / r6i.8xlarge |	32 |	128GB |	500,000+

