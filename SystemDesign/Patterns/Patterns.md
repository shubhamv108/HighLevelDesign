# BloomFilters
- Probabilistic Data Structure
- Use Bloom filters to quickly find if an element might be present in a set.
- The Bloom filter data structure tells whether an element may be in a set, or
  definitely is not.
- The only possible errors are false positives, i.e., a search
  for a nonexistent element might give an incorrect answer.
- eg. used in BigTable, Cassandra to check whether element in BigTable
- They help reduce the number of disk accesses by predicting if an SSTable
  may contain data corresponding to a particular row or column pair.

# ConsistentHashing
- Consistent Hashing maps data to physical nodes and ensures that only a
  small set of keys move when servers are added or removed.
- Potential issues associated with a manual and fixed division of the ranges:
  - **Adding or removing nodes**: Adding or removing nodes will result in
    recomputing the tokens causing a significant administrative overhead
    for a large cluster.
  - **Hotspots**: Since each node is assigned one large range, if the data is not
    evenly distributed, some nodes can become hotspots.
  - **Node rebuilding**: Since each node’s data might be replicated (for fault-tolerance) 
    on a fixed number of other nodes, when we need to rebuild a
    node, only its replica nodes can provide the data. This puts a lot of
    pressure on the replica nodes and can lead to service degradation.
## Vnodes
  - Instead of assigning a single token
    to a node, the hash range is divided into multiple smaller ranges, and each
    348physical node is assigned several of these smaller ranges.
  - Vnodes are randomly distributed across the cluster and are
    generally non-contiguous so that no two neighboring Vnodes are assigned to
    the same physical node.
  - since there can be heterogeneous machines in the
    clusters, some servers might hold more Vnodes than others.
  - Each physical node is assigned a set of Vnodes and
    each Vnode is replicated once.
  - Advantags
    - **speeds up the rebalancing** process after adding or removing nodes.
    - Vnodes make it easier to maintain a cluster containing heterogeneous
      machines. This means, with Vnodes, **we can assign a high number of
      sub-ranges to a powerful server** and a lower number of sub-ranges to a
      less powerful server.
    - decreases the probability of **hotspots**.
  - eg. DynamoDB, Cassandra

# Quorum
- if they all have the latest copy of the data and that all clients
  see the same view of the data
- **In a distributed environment, a quorum is the minimum number of servers
  on which a distributed operation needs to be performed successfully before
  declaring the operation’s overall success.**
- Best performance (throughput/availability) when 1 < r < w < n,
  because reads are more frequent than writes in most applications.

# Leader and Follower
- Allow only a single server (called leader) to be responsible for data replication and to coordinate work.
- In Kafka, each partition has a designated leader which is responsible
  for all reads and writes for that partition.
- Within the Kafka cluster, one broker is elected as the Controller. This
  Controller is responsible for admin operations, such as creating/deleting
  a topic, adding partitions, assigning leaders to partitions, monitoring
  broker failures, etc. Furthermore, the Controller periodically checks the
  health of other brokers in the system.
- To ensure strong consistency, Paxos (hence Chubby) performs leader
  election at startup. This leader is responsible for data replication and
  coordination.

# Write-ahead Log
- To guarantee durability and data integrity, each modification to the system is
  first written to an append-only log on the disk.
- This log is known as Write-Ahead Log (WAL) or **transaction log** or **commit log**.
- Writing to the WAL guarantees that if the machine crashes, the system will be able to **recover
  and reapply the operation** if necessary.
- Each log entry should contain enough information to redo or undo the modification.
- The log can be read on every restart to recover the previous state by replaying all the log entries.
- Using WAL results in a significantly reduced number of disk
  writes, because **only the log file needs to be flushed to disk** to guarantee that
  a transaction is committed, rather than every data file changed by the
  transaction.
- Each node, in a distributed environment, maintains its own log.
- WAL is always sequentially appended.
- Each log entry is given a unique identifier; this identifier helps in implementing
  certain other operations like log segmentation(discussed later) or log purging.
- eg.
  - **Cassandra**: To ensure durability, whenever a node receives a write
    request, it immediately writes the data to a commit log which is a WAL.
    Cassandra, before writing data to a MemTable. On startup, any mutations in the commit log will be applied
    to MemTables.
  - **Kafka**: implements a distributed Commit Log to persistently store all messages it receives.
  - **Chubby**: For fault tolerance and in the event of a leader crash, all
    database transactions are stored in a transaction log which is a WAL.

# Segmented Log
- Break down the log into smaller segments for easier management.
- The system can roll the log based on a rolling
  policy - either a configurable period of time (e.g., every 4 hours) or a
  configurable maximum size (e.g., every 1GB).
- eg. Cassandra commit log, Kafka storage for partition

# High-Water Mark
- it becomes important for the
  leader and followers to know what part of the log is safe to be exposed to the
  clients.
- **Keep track of the last log entry on the leader, which has been successfully
  replicated to a quorum of followers.**
- The index of this entry in the log is
  known as the High-Water Mark index. The leader exposes data only up to the
  high-water mark index.
- his guarantees that even if the
  current leader fails and another leader is elected, the client will not see any
  data inconsistencies.
- **Kafka**: To deal with non-repeatable reads and ensure data consistency, Kafka
  brokers keep track of the high-water mark, which is the largest offset that all
  In-Sync-Replicas (ISRs) of a particular partition share. Consumers can see
  messages only until the high-water mark.

# Lease
- Use time-bound leases to grant clients rights on resources.
- One way to fulfill this requirement is through distributed
  locking. If the client fails to release the lock due to any reason, e.g., process crash, deadlock, or a
  software bug, the resource will be locked indefinitely.
- **A lease is like a lock, but it works even when the client goes away.**
- If the client wants to extend the lease, it can renew the lease before it expires.
- eg.
  - Chubby clients maintain a time-bound session lease with the leader. During
    this time interval, the leader guarantees to not terminate the session
    unilaterally.

# Heartbeat
- Each server periodically sends a heartbeat message to a **central monitoring
  server** or **other servers** in the system to show that it is still alive and
  functioning.
- servers should know if other servers are alive and working
- enables the system to take corrective actions and move the data/work to another
  healthy server and stop the environment from further deterioration.
- mechanisms for detecting failures.
- If there is no central server, all servers randomly choose a set
  of servers and send them a heartbeat message every few seconds.
- If there is no heartbeat within a configured timeout period, the system can conclude that the server is not
  alive anymore and stop sending requests to it and start working on its replacement.
- eg.
  - **GFS**: The leader periodically communicates with each ChunkServer in
    HeartBeat messages to give instructions and collect state.
  - **HDFS**: The NameNode keeps track of DataNodes through a heartbeat
    mechanism.

# Gossip Protocol
- Each node keeps track of state information about other nodes in the cluster
  and gossip (i.e., share) this information to one other random node every
  second. This way, eventually, each node gets to know about the state of every
  other node in the cluster.
- peer-to-peer communication mechanism
-  any state change will eventually propagate through the system, and all nodes quickly learn about all other
   nodes in a cluster.
- eg.
  - DynamoDB & Cassandra

# Phi Accrual Failure Detection
- Adaptive failure detection algorithm.
- This algorithm uses historical heartbeat information to make the threshold adaptive.
- A generic Accrual Failure Detector outputs the suspicion level about a server.
- If a node does not respond, its suspicion level is increased and could be declared dead later.
- Detector makes a distributed system efficient as it takes into account fluctuations in the network environment and other intermittent
  server issues before declaring a system completely dead.
- eg.
  - Cassandra uses the Phi Accrual Failure Detector algorithm to determine the state of the nodes in the cluster.

# Split Brain
- The common scenario in which a distributed system has two or more active leaders is called split-brain.
- Split-brain is solved through the use of **Generation Clock**, which is simply a monotonically increasing number to indicate a server’s generation.
- cannot truly know if the leader has stopped for good or has experienced an intermittent failure like a stop-the-world GC
  pause or a temporary network disruption.
- If the original leader had an intermittent failure, we now find ourselves with a so-called zombie leader.
- Another node has taken its place, but the zombie leader might not know that yet.
- All nodes in the system can ignore requests from the old leader and the old leader itself can detect that it is no longer the leader.
- Every time a new leader is elected, the generation number gets incremented. This means if the old leader had a generation number of ‘1’, the new one will have ‘2’.
- This generation number is included in every request that is sent from the leader to other nodes.
- The generation number should be persisted on disk, so that it remains available after a server reboot. 
  One way is to store it with every entry in the Write-Ahead Log.
- eg.
  - **Kafka**: To handle Split-brain (where we could have multiple active controller
    brokers), Kafka uses ‘Epoch number,’ which is simply a monotonically
    increasing number to indicate a server’s generation.
  - **HDFS**: ZooKeeper is used to ensure that only one NameNode is active at any
    time. An epoch number is maintained as part of every transaction ID to
    reflect the NameNode generation.
  - **Cassandra** uses generation number to distinguish a node’s state before and
    after a restart. Each node stores a generation number which is incremented
    every time a node restarts. This generation number is included in gossip
    messages exchanged between nodes and is used to distinguish the current
    state of a node from the state before a restart. The generation number
    remains the same while the node is alive and is incremented each time the
    node restarts. The node receiving the gossip message can compare the
    generation number it knows and the generation number in the gossip
    message. If the generation number in the gossip message is higher, it knows
    that the node was restarted.

# Fencing
- Put a ‘Fence’ around the **previous leader** to prevent it from doing any
  damage or causing corruption.
- **Resource fencing**: Under this scheme, the system blocks the previously
  active leader from accessing resources needed to perform essential
  tasks. For example, revoking its access to the shared storage directory
  (typically by using a vendor-specific Network File System (NFS)
  command), or disabling its network port via a remote management
  command.
  **Node fencing**: Under this scheme, the system stops the previously active
  leader from accessing all resources. A common way of doing this is to
  power off or reset the node. This is a very effective method of keeping it
  from accessing anything at all. This technique is also called STONIT or
  “**Shoot The Other Node In The Head.**”
- eg. 
  - **HDFS** uses fencing to stop the previously active NameNode from accessing
    cluster resources.

# Checksum
- Calculate a checksum and store it with data.
- **Problem**: How can a distributed system ensure data integrity, so that the
  client receives an error instead of corrupt data ?
- When a system is storing some data, it computes a checksum of the data, and
  stores the checksum with the data. When a client retrieves data, it verifies
  that the data it received from the server matches the checksum stored. If not,
  then the client can opt to retrieve that data from another replica.
- eg.
  - **HDFS** and **Chubby** store the checksum of each file with the data.

# Vector Clocks
- Use Vector clocks to keep track of value history and reconcile divergent
  histories at read time.
- **Problem**: So how can we reconcile and capture causality between different versions of
  the same object?
- A vector clock is effectively a **(node, counter)** pair.
- One vector clock is associated with every version of every object.
- Conflicts are resolved at read-time, and if the system is not able to
  reconcile an object’s state from its vector clocks, it sends it to the client
  application for reconciliation
- eg.
  - To reconcile concurrent updates on an object Amazon’s Dynamo uses Vector Clocks.

# CAP
**Consistency** ( C ): All nodes see the same data at the same time. It is
equivalent to having a single up-to-date copy of the data.
**Availability** ( A ): Every request received by a non-failing node in the system
must result in a response. Even when severe network failures occur, every
request must terminate.
**Partition tolerance** ( P ): A partition-tolerant system continues to operate
despite partial system failure or arbitrary message loss. Such a system can
sustain any network failure that does not result in a failure of the entire
network. Data is sufficiently replicated across combinations of nodes and
networks to keep the system up through intermittent outages.
- In the presence of a network partition, a distributed system must choose either Consistency or Availability.

# PACELC Theorem
The PACELC theorem states that in a system that replicates data:
- if there is a partition (‘P’), a distributed system can tradeoff between
  availability and consistency (i.e., ‘A’ and ‘C’);
- else (‘E’), when the system is running normally in the absence of
  partitions, the system can tradeoff between latency (‘L’) and consistency (‘C’).
- - eg.
  - **Dynamo and Cassandra are PA/EL systems**: They choose availability
    over consistency when a partition occurs; otherwise, they choose lower
    latency.
  - **BigTable and HBase are PC/EC systems**: They will always choose
    consistency, giving up availability and lower latency.
  - **MongoDB is PA/EC**: In case of a network partition, it chooses availability,
    but otherwise guarantees consistency.

# Hinted Handoff
For nodes that are down, the system keeps notes (or hints) of all the write
requests they have missed. Once the failing nodes recover, the write requests
are forwarded to them based on the hints stored.
- When the coordinating node discovers that a node for
  which it holds hints has recovered, it forwards the write requests for each
  hint to the target.
- eg.
  - **Cassandra** nodes use Hinted Handoff to remember the write operation
    for failing nodes.
  - **Dynamo** ensures that the system is “always-writeable” by using Hinted
    Handoff (and Sloppy Quorum).

# Read Repair
- Repair stale data during the read operation, since at that point, we can read
  data from multiple nodes to perform a comparison and find nodes that have
  stale data. This mechanism is called Read Repair. Once the node with old
  data is known, the read repair operation pushes the newer version of data to
  nodes with the older version.
- Example, for Quorum=2, the system reads data from one node and digest of
  the data from the second node. The digest is a checksum of the data and is
  used to save network bandwidth. If the digest does not match, it means some
  replicas do not have the latest version of the data. In this case, the system
  reads the data from all the replicas to find the latest data.
- The system returns the latest data to the client and initiates a Read Repair request. 
- The read repair operation pushes the latest version of data to nodes with the older version.
- The system immediately sends a response to the client when the consistency
  level is met and performs the read repair asynchronously in the background.
- **Cassandra** and **Dynamo** use ‘Read Repair’ to push the latest version of the
  data to nodes with the older versions.

# Merkle Trees
- 
- A replica can contain a lot of data. Naively splitting up the entire range to calculate checksums for comparison is not very feasible; 
  there is simply too much data to be transferred. Instead we can use Merkle trees to compare replicas of a range.
- 