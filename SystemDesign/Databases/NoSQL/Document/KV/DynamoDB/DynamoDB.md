Durable, EventuallyConsistent, HighlyAvailable, Partitioned Key-Value store.
Dynamo is a Distributed Hash Table (DHT) that is replicated across the cluster for high availability and fault tolerance

## NFR
**Scalable**: Highly
**Decentralized**: 
    To avoid single points of failure and performance bottlenecks, 
    there should not be any central/leader process.
**Eventually Consistent**: 
    Data can be optimistically replicated to
    become eventually consistent. This means that instead of incurring
    write-time costs to ensure data correctness throughout the system (i.e.,
    strong consistency), inconsistencies can be resolved at some other time
    (e.g., during reads). Eventual consistency is used to achieve high
    availability.

## Characteristics
Decentralized
Distributed
Durable
Eventually Consistency
Fault Tolerence
Highly Available
Scalable
Tunable Consistency

# CAP
AP

## PartitionKey
Unique (when no sort key)

## SortKey
Optional (with unique PartitionKey)

## Indexes
### LocalSecondaryIndex
Same PartitionKey but SortKey can be different

### GlobalSecondaryIndex
Different PartitionKey & SortKey

## API
### **get(key)** 
- Returns either a single object or a list of objects with conflicting versions along with a context.
- The context contains encoded metadata about the object that is meaningless to the caller and includes information such as the version
  of the object (more on this below).
- Dynamo’s get() request will go through the following steps:
  1. The coordinator requests the data version from N − 1 highest-ranked
     healthy nodes from the preference list.
  2. Waits until R − 1 replies.
  3. Coordinator handles causal data versions through a vector clock.
  4. Returns all relevant data versions to the caller.

### **put(key, context, object)**
- The context is always stored along with the object and is used like a cookie to verify the validity of the object supplied in the put request.
- Dynamo’s put() request will go through the following steps:
  1. The coordinator generates a new data version and vector clock
     component.
  2. Saves new data locally.
  3. Sends the write request to N − 1 highest-ranked healthy nodes from
     the preference list.
  4. The put() operation is considered successful after receiving W − 1
     confirmation.

## 
- is designed for high **availability** and **partition tolerance** at the expense of **strong consistency**.
- Dynamo treats both the object and the key as an arbitrary array of bytes (typically less than 1 MB)
- Uses **MD5** Algo to generate a 128bit identifier to determine storage needs.
- Uses **SystemDesign.ConsistentHashing**
- Replicated Data to **SloppyQuorum**
- Use **GossipProtocol** to keep track of cluster state
- **"Always Writable"** or (High Write Availability) with **HintedHandoffs**
- Conflict resolution & handling permanent failures 
      - Use **VectorClocks** to keep track of value history & reconcile divergent histories **at read time**.
      - Use an anti-entropy mechanism like merkle trees to handle permanent failures.
- On read request if it sees the **same key with diff. versions**, but does not know which is newer. \
  It returns both & **tells client to figure out the version & write newer version back**. (Client reconcile - semantic reconcile)
- _Last write wins_ for _auto-conflict resolution_.

# Preference List
- The list of nodes responsible for storing a particular key is called the
  preference list. Dynamo is designed so that every node in the system can
  determine which nodes should be in this list for any specific key (discussed
  later). This list contains more than N nodes to account for failure and skip
  virtual nodes on the ring so that the list only contains distinct physical nodes.

## Req handling through State Machine
- Syntactic reconciliation at coordinator.
- Read Replica
- put() are coordinated by anyone of TopN nodes in coordinator.
- Since each write usually follows a read operation, the coordinator for read operation is chosen to be the node 
  that replied fastest to the read operation, which is stored in required context info. This enables to pick node 
  that has the data that has the data that was read by preceding read operation, 
  thereby increasing chances of getting "_read_your_writes_" consistency
- a read operation implements the following state
  machine:
  1. Send read requests to the nodes.
  2. Wait for the minimum number of required responses.
  3. If too few replies were received within a given time limit, fail the
     request.
  4. Otherwise, gather all the data versions and determine the ones to be
     returned.
  5. If versioning is enabled, perform syntactic reconciliation and generate
     an opaque write context that contains the vector clock that subsumes all
     the remaining versions.

## Linearizabilty
- Although Nonlinearizable due to **Sloppy quorums**.
- **Linearizable** with **ReadRepairs** and anti-entropy at the cost of performance.

## MerkleTrees
- Minimizes the amount to be exchanged in case replica falls behind others.
- Reduces number of disks read during anti-entropy process.
- Many tree ranges can change when a node joins or leaves the trees need to be recalculated.

## GossipProtocol
- Seed Nodes are fully functional nodes that can be obtained from static config or config service.
- All nodes are aware about seed nodes.
- Each node communicates with seed node through Gossip to reconcile membership, 
  thereby logical partitioning are highly unlikely.

## Criticism
- Each ode contains entire routing table.
- Because of seed nodes all nodes dfo not follow symmetry and same set of rules.
- Sloppy quorum during write can cause inconsistency.

## ConflictResolution
### VectorClocks
### LastWriteWins
- It is based on the wall-clock timestamp.
- LWW can easily end up losing data.

## Coordinator Node
### Strategies for choosing
- Clients can route their requests through a generic load balancer.
  - the client is unaware of the Dynamo ring, which helps
    scalability and makes Dynamo’s architecture loosely coupled. However, in
    this case, since the load balancer can forward the request to any node in the
    ring, it is possible that the node it selects is not part of the preference list.
    This will result in an extra hop, as the request will then be forwarded to one
    of the nodes in the preference list by the intermediate node.
- Clients can use a partition-aware client library that routes the requests to the appropriate coordinator nodes with lower latency.
  - The second strategy helps in achieving lower latency, as in this case, the
    client maintains a copy of the ring and forwards the request to an
    appropriate node from the preference list. Because of this option, Dynamo is
    also called a **zero-hop DHT**, as the client can directly contact the node that
    holds the required data. However, in this case, Dynamo does not have much
    control over the load distribution and request handling.

# Streams

# Accelerator (DAX)
    Caching

## WCU (Write Capacity Unit) 
## RCU (Read Capacity Unit)

# Scan
    Full table scan

# Query
    On partition / range key

# page_size
    pagination


# When NOT to use ?
1. **Complex Query Patterns**: not optimized for complex joins & queries
2. **Multi-table transactions**: has limit pn n. of items
3. **Data Model Complexity**: lots of GSI & LSIs