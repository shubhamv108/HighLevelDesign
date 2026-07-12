# SystemDesign.ConsistentHashing



## Node
Server (functionality to other services)

## HashFunc
For a arbitary size input it returns a fixed size output
f(arbitarySize) = fixedSizeOutput

## DataPartitioning

## DataReplication
Multiple copies of same data to ensure availability (into multiple nodes)

## HotSpot Node
Degraded Node / overwhelmed node
not capable to store or load multiple request


# Problem - Website getting popular
1. Load will increase
2. Data replication of cache server
3. existing Nodes x dynamic load
4. Cache misses
5. origin server

clinet -> LB -> K1v1
            \>  K1V1 (Cache Miss)
            \>  Origin Server  -> DB

# FR
Horizontally scale cache nodes to ensure
1. data uniformity
2. lower hotspot problem / in the network
3. able to handle dynamic load

# NFR
1. Scalable
2. Available (Data replication)
3. Low Latency


Server1     Server2     Server3     Server4

### HashFunc
Fixed
MD5
SHA1

## Load distribution across multiple node
1. %

## Assign data to server
1. Random Assignment
2. Range Based Partitioning 
   - leads to hotspot nodes
   - no uniformity gauranted
3. Consistent hashing
    - Consistent hashing minimizes the no of keys to be remapped when total no of nodes changes
    - when no of servers are changed only k/n keys are remapped
    - k: keys, N: servers
    - too many replica nodes in ring will cause more cache misses 
    - add virtual node and monitor it
    Disadvantage
      - Complexity
      - redistribution