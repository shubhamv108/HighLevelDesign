# Definition
    **Make a system appear as if there is only one copy of data & all operations on it are atomic.**
- Maintaining the illusion of a single copy of data means guaranteeing that the value read is the most recent, up-to-date value, and doesn't come from stale cache or replica.
- Linearizability is a recency guarantee.
## AlsoKnownAs 
    **AtomicConsistency**, 
    **StrongConsistency**, 
    **ImmediateConsistency**, 
    **ExternalConsistency**

# RelyingOnLinearizability
## Locking & LeaderElection
- Acquire the lock to become leader in single-leader replication.
- Consensus algorithms to implement linearizable operations in a fault tolerant way.
## Constraints & UniquenessGuarantees
- UniqueConstraints are common in databases.  
## Cross-Channel Timing Dependencies
- A race condition b/w two communication channels.

# Implementing
### Single-leader replication (potentially linearizable)
## Consensus Algorithms (linearizable)
- compareAndSet operation can only be performed using consensus algorithm
## Multi-leader replication (not linearizable)
- Can produce conflicting writes
## Leaderless replication (probably not linearizable)
- "Last write wins" conflict resolution methods based on time-of-day clocks are certainly linearizable
- Sloppy Quorums also ruin any chance of linearizability
## Quorums
- Strict quorum are not linearizable due to n/w delays.
- DynamoDB is **Linearizable** with **ReadRepairs** and anti-entropy at the cost of performance.
- Cassandra does wait for ReadRepair to complete on quorum reads, but it loses linearizability if there are multiple concurrent writes to the same key,
  due to its use of **last-write-wins conflict resolution**. 

