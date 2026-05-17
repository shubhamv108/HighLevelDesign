Pagination: Load as you scroll
Lazy loading; Loading resources asynchronously

if interviewer asks if web app server has utilized all threads on server. then can i say i'll add event control loop implementation to make requests wait in queue or will there be a way/path to reach this answer
yes, to have healthy communication

# SuddenSpike
p95 x -> 2x
p99 y -> 3y

## Reasons:
1. Downstream service responding slow
2. Too many retries to downstream service
3. high connect/read timeout to downstream service
4. Surge in traffic
5. Long running garbage collection cycle

# Horizontal Scaling
# Vertical Scaling

# Horizontal partitioning
# vertical partitioning
# Sharding
Spliting data into mutually exclusive data and distributing it into multiple servers.


# CAP
Whenever there is network failure you should get either consistency or availability
## Consistency
whenever reading you should get value of most recent write
## Availability
- Success response to each request
- achieved through replication
## Partition Tolerance
- Despite network fault the system should operate.

# SystemDesign.LoadBalance
- Responsibility: distribute load uniformly
- redirect to healthy instance
- segregate the request

# Application Gateway
- redirects the traffic along various layers
- TrafficManager/APIGateway
- Rate Limiting
  - DDoS Attack
- Authentication
- Authorization


# CDN
- Cache (LRU)
- Reverse Proxy
- Either Cache Hit or on Cache Miss request will go to Application Gateway/DFS

# Cache
- Leads to reduction in latency and n/w traffic
## Client Side Cache
- Cookies
- SystemDesign.DNS entries cache in os, browser
- reduce access to n/w traffic & resources
## Server Side Cache
- cache freq access data on server
- to reduce processing time
- decrease server load
- decrease load into backend system

# HLD Template
## Functional Requirements
- Use Cases
- Scope - A part of problem or full. If part then what
## Non-Functional Requirements
- How system behaves ? Highly Available, Reliability, Fault Tolerent, p95, p99
## Estimations
- Size of data in x years is required based on traffic or system
## Design
## Dig Deeper
- Considerations
- Replicattion
- DB Writes
- Tightly Couple handling
