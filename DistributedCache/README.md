LLD:
Node has an inmemory HashMap to keep key value. hashmap will hold value and ttl againsy unqiue key. TTL will allow invalidation.

we can also have evicton policy and use LRU eviction by default.

CachedValue:
     - value: Value
     - expiryAt: timestampz
     - CachedValue(values: Value, ttl: long): CachedValue
     + get()
     + isExpired(key: Key)
Cache:
    - store: Map<Key, CachedValue>
    - evictionPolicy: EvictionPolicy
    + set(key: Key, value: Value)
    + get(key: Key): Value // remove expired entries if cross ttl/expiryAt
    + delete(key: Key): Value
    - evict()
    # cleanup() // remove expired entries if cross ttl/expiryAt
EvictionPolicy
    + access(key: Key)
    + remove(key: Key): bool
    + evict(): Key
LRUEvictionPolicy(EvictionPolicy):
    - nodes: Map<Key, Node>
    - head: Node
    - tail: Node
Node:
    key: Key
    prev: Node
    next: Node

Asynchronous Event Loop:
Every request will get queued in event loop which will be processed by single thread one by one. since it s in memory operations will be fast. This will allow atomicity due to single thread being used.

WAL:
We can also asynchronously write to WAL file asynchronously for delayed durability. without hampering the speed cache updates. this WAL append can also be configurable so that it can happen synchronously thereby achieving immediate durability.


Replication:
1. A single node can have replicas explicitly configured with it.
2. Each node will also have a replication backlog buffer. 3. whenever a write is made it is appended to the replication backlog buffer with ofsset for each entry when replicas are configured for the master.
4. another single thread then sends them over TCP to replicas. in this manner replicas are asynchronously updated.
5. if a new replica is added, it will do a full sync with primary and primary will respond with full snapshot.
6. if replica disconnects and reconnects it will do a partial sync with primary from it's current offset.
7. Primary & replicas talk to each other using gossip protocol. This helps to discover disconnection of node or the primary.
8. When each replica detects that primary is down . Raft election is initiated amongst the most updated and connected replicas. one is chosen as primary.
9. Client will fetch new primary .Or it will discover new primary by fixing it's topology. it will send write request to old primary and it will find it to be dead. then it will choose another replica for write if it is not primary it will respond with MOVED (301). till pne replies successfully.
10. Client can opt-in to read from replicas by using READONLY mode.
11. We can have replicas in Multi AZ to achieve high Availability.


Sharding:
Client discover the shards (primary-replicas) by querying the seed nodes.
Cluster itself discover the topology of all shards using gossip protocol.

ShardingTechniques
1. Slot based CRC Hash sharding technique
- Shard our cache into 16383 slots which. suppose we have 3 shards. then 1/3rd of slots in increasing manner will get amongst 3 shards.
- in-case if a shard (primary + replica) is down then either whole cluster fails or only the shard fails (this can be configurable)

2. Consistent Hashing
- configure multiple shards. and use SystemDesign.ConsistentHashing algorithm for calculating the shard for current key.
- in case if a shard (primary + replica) is down. this will avoid failures as keys will get distributed to other shards. although cache miss for those shards will increase.
- during failure of a shard due to mny requests can create a domino effect on other shards as the load from them will be transfered to them.


Metrics:
We can collect metrics for each key being read, written to detect keys which are hot and show access pattern of more than 500 reads/writes per second. We mark them read hot/write hot in database.

Scaling Writes:
When there are too many writes for a key we can append a shardSuffix to it to distribute it's write to multiple shard. during read we can agrregate them by taking sum from all shrds. this will reduce read performance although.

Scaling Reads:
We can write to multiple shards for a key. Reads can be directed to any shard. this will increase write overhead and latency.