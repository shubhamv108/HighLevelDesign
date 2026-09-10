# Click-Bound Wait
    Wait to cover the uncertainty in time across cluster nodes before reading & writing 
    values so that values can be correctly ordered across cluster nodes. 

    - While reading or writing, cluster nodes wait until the clock values on every node in the cluster are guaranteed to be above the timestamp assigned to the value.
    - maxClockOffsetAcrossCluster=10ms
    - To guarantee that every other cluster node has its clock past time t, the cluster node that handles any write operation will have to wait until t + 10 ms before storing the value.

# [ConsistentCore](https://martinfowler.com/articles/patterns-of-distributed-systems/consistent-core.html)
    Maintain a smaller cluster providing stronger consistency to allow the large data cluster to coordinate server activities without implementing quorum-based algorithms.
    - When a cluster needs to handle a lot of data, it uses more and more servers.
    - Common requirements
        - selecting a server to be the master for a particular task
        - managing group membership information
        - mapping data partitions to servers etc...
        - This functionality requires a strong consistency guarantee of linearizability..
        - Use a fault-tolerant consensus algorithm based on Majority Quorum
    - Implement a smaller cluster of 3 to 5 nodes which provides linearizability guarantee as well as fault tolerance.
    - A separate data cluster can use the small consistent cluster to manage metadata and for taking cluster-wide decisions with primitives like **Lease**.
    - eg.
        1. Google spanner
            - GlobalConsistency with TrueTime API
            - Consensus Core with Paxos
            - ACID transactions
        2. Insurance claim calculation 
            - Financial Calc
            - Policyholder Data
            - Claims Approval

# EmergentLeader
    - Order cluster nodes based on their age within the cluster to allow nodes to select a leader without running an explicit election.
    - One of the common techniques used in peer-to-peer systems is to order cluster nodes according to their age in the cluster. 
    - The oldest member of the cluster plays the role of the coordinator. 
    - The coordinator is responsible for deciding on membership changes and making cluster-wide decisions, such as ensuring the distribution of Fixed Partitions across cluster nodes.

# FixedPartitions
    - Keep the number of partitions fixed to keep the mapping of data to partition unchanged when the size of a cluster changes.
    - One of the most commonly used solution is to map data to logical partitions. 
    - **Logical partitions** are mapped to the cluster nodes.
    - So the way data is mapped to partitions using a hash of the key remains the same.
    - When partitions are moved to new nodes, it should be relatively quick with only a smaller portion of the data being moved.

# FollowerReads
    - Serve read requests from followers to achieve better throughput and lower latency
    - It's possible that the leader may get overloaded if too many requests are sent to it.
    - While the write requests need to go to the leader to maintain consistency, the read-only requests can instead go to the nearest follower. 
    - This is particularly useful when clients are mostly read-only.

# GenerationClock
    - A monotonically increasing number indicating the generation of the server.
    - there is a possibility of the leader being temporarily disconnected from the followers. 
    - In this case the leader process is still running, and after the pause or the network disruption is over, it will try sending replication requests to the followers. 
    - It is important for the rest of the cluster to detect any requests from the old leader.
    - he old leader itself should also be able to detect that it was temporarily disconnected from the cluster and take necessary corrective action to step down from leadership.
    - Maintain a monotonically increasing number indicating the generation of the server.
    - Every time a new leader election happens, it should be marked by incrementing the generation. 
    - The generation needs to be available beyond a server reboot, so it is stored with every entry in the Write-Ahead Log. As discussed in High-Water Mark, followers use this information to find conflicting entries in their log.
    - If a follower gets a message from a deposed leader, the follower can tell because its generation is too low. The follower then replies with a failure response.

# GossipDissemination
    - Use a random selection of nodes to pass on information to ensure it reaches all the nodes in the cluster without flooding the network.
    - In a large cluster, if all servers communicate with all the other servers, a lot of network bandwidth can be consumed.
    - Cluster nodes use gossip-like communication to propagate state updates. Each node selects a random node to pass the information it has. 
    - This is done at a regular interval, say every 1 second. Each time, a random node is selected to pass on the information.

# HeartBeat
    - Show a server is available by periodically sending a message to all the other servers.
    - Select the request interval to be more than the network round trip time between the servers.
    - All the listening servers wait for the timeout interval, which is a multiple of the request interval.

# High-Water Mark (Commit index)
    - An index in the write-ahead log showing the last successful replication.
    - It becomes important for each follower to know what part of the log is safe to be made available to the clients.
    - The high-water mark is an index into the log file that records the last log entry known to have successfully replicated to a Majority Quorum of followers.
    - All servers in the cluster should only transmit to clients the data that reflects updates below the high-water mark.

# HybridClock
    - Use a combination of system timestamp and logical timestamp to have versions as date and time, which can be ordered.
    - Hybrid logical clock provides a way to have a version which is monotonically increasing just like a simple integer, but also has relation to the actual date and time.
    - Used in Db like MongoDB, CockroachDB
    - Every server holds an instance of a hybrid clock.

# IdempotentReceiver
    - Identify requests from clients uniquely so you can ignore duplicate requests when client retries.
    - Identify a client uniquely by assigning a unique ID to each client.
    
# Key-Range Partitions
    - Partition data in sorted key ranges to efficiently handle range queries.
    - Create logical partitions for keys ranged in a sorted order.
    - To query a range of data, the client can get all partitions that contain keys from a given range and query only those specific partitions to get the values required.
    
# LamportClock
    - Use logical timestamps as a version for a value to allow ordering of values across servers.
    - Every cluster node maintains an instance of a Lamport clock.
    - Whenever a server carries out any write operation, it should advance the Lamport clock using the `tick()` method.
    - The server can be sure that the write is sequenced after the request and after any other action the server has carried out since the request was initiated by the client.
    - The server returns the timestamp that was used for writing the value to the client.
    - The requesting client then uses this timestamp to issue any further writes to other set of servers. This way, the causal chain of requests is maintained.

# Leader & Followers
    - Have a single server to coordinate replication across a set of servers.

# Lease
    - Use time-bound leases for cluster nodes to coordinate their activities.
    - Cluster nodes need exclusive access to certain resources. But nodes can crash, be temporarily disconnected, or experience a process pause. Under these error scenarios, they should not keep the access to a resource indefinitely.
    - A cluster node can ask for a lease for a limited period of time, after which it expires.
    - The node can renew the lease before it expires if it wants to extend the access.
    - Implement the lease mechanism with Consistent Core to provide fault tolerance and consistency.
    - Cluster nodes can create keys in a consistent core with a lease attached to it.
    - Have a time-to-live value associated with the lease.
    - It's the responsibility of the node that owns the lease to periodically refresh it.
    - HeartBeat is used by clients to refresh the time-to-live value in the consistent core.

# Low-Water mark
    - An index in the write-ahead log showing which portion of the log can be discarded.
    - It can grow indefinitely over time.
    - Segmented Log ensures smaller files, but the total disk storage can grow indefinitely if not checked.
    - The mechanism gives the lowest offset, or low-water mark, before which the logs can be discarded.

# Majority Quorum
    - Avoid two groups of servers making independent decisions by requiring majority for taking every decision.
    - For a cluster of n nodes, the quorum is n / 2 + 1.
    - In general, if we want to tolerate f failures we need a cluster size of 2f + 1.

# Paxos
    - Use two consensus building phases to reach safe consensus even when nodes disconnect
    - Paxos works in three phases to make sure multiple nodes agree on the same value in spite of partial network or node failures. 
    - The first two phases act to build consensus around a value and the last phase then communicates that consensus to the remaining replicas.
        - Prepare phase: Establish the latest Generation Clock and gather any already accepted values.
        - Accept phase: Propose a value for this generation for replicas to accept.
        - Commit phase: Let all the replicas know that a value has been chosen.

# Replicated Log
    - Keep the state of multiple nodes synchronized by using a write-ahead log that is replicated to all the cluster nodes.
    - Each replica also needs to execute requests in the same order, otherwise different replicas can get into a different final state, even if they have consensus on an individual request.
    - Cluster nodes maintain a **Write-Ahead Log**. Each log entry stores the state required for consensus along with the user request. 
    - They coordinate to build consensus over the log entries, so that all cluster nodes have exactly the same write-ahead log.
    - The requests are then executed sequentially as per the log.
    - This ensures that all the cluster nodes share the same state.

# Request Batch
    - Combine multiple requests to optimally utilise the network.
    - If a lot of requests are sent to cluster nodes with a small amount of data, network latency and the request processing time (including serialization and deserialization of the request on the server side) can add significant overhead.
    - Combine multiple requests together into a single request batch.
    - with each request processed in exactly the same manner as an individual request,
    - The node will then respond with a batch of responses.

# Request Pipeline
    - Improve latency by sending multiple requests on the connection without waiting for the response of the previous requests.
    - Communicating between servers within a cluster using Single-Socket Channel can cause performance issues if requests need to wait for responses to previous requests.
    - Nodes send requests to other nodes without waiting for responses from previous requests.
    - This is achieved by creating two separate threads, one for sending requests over a network channel and the other for receiving responses from the network channel.

# Request Waiting List
    - Track client requests which require responses after the criteria to respond is met based on responses from other cluster nodes.
    - A cluster node needs to communicate with other cluster nodes to replicate data while processing a client request. A response from all other cluster nodes or a Majority Quorum is needed before responding to clients.
    - So the cluster node receives and processes responses from multiple other cluster nodes asynchronously. It then needs to correlate them to check if the Majority Quorum for a particular client request is reached.
    - The cluster node maintains a waiting list which maps a key and a callback function.
    - The key is chosen depending on the specific criteria to invoke the callback..
    - if it needs to be invoked whenever a message from other cluster node is received, it can be the correlation ID of the message..
    - In the case of Replicated Log, it is the High-Water Mark. 
    - The callback handles the response and decides if the client request can be fulfilled.

# Segmented Log
    - Split log into multiple smaller files instead of a single large file for easier operations.
    - Single log is split into multiple segments. 
    - Log files are rolled after a specified size limit.

# Single-Socket Channel
    - Maintain the order of the requests sent to a server by using a single TCP connection.
    - When using Leader and Followers, we need to ensure that messages between the leader and each follower are kept in order, with a retry mechanism for any lost messages.
    - We need to do this while keeping the cost of new connections low, so that opening connections doesn't increase the system's latency.
    - TCP protocol provides all these necessary characteristics.
    - The follower then serializes the updates from the leader using a Singular Update Queue.

# Singular Update Queue
    - Use a single thread to process requests asynchronously to maintain order without blocking the caller.
    - Implement a work queue and a single thread working off the queue.
    - Multiple concurrent clients can submit state changes to the queue—but a single thread works on state changes. 
    - This can be naturally implemented with goroutines and channels in languages like Golang.

# State Watch
    - Notify clients when specific values change on the server.
    - Allow clients to register their interest with the server for specific state changes.
    - The server notifies the interested clients when state changes happen.
    - The client maintains a Single-Socket Channel with the server. The server sends state change notifications on this channel. 
    - So clients can use a Request Pipeline.

# Two-Phase Commit
    - Update resources on multiple nodes in one atomic operation.
    - When data needs to be atomically stored on multiple cluster nodes, nodes cannot make the data accessible to clients until the decision of other cluster nodes is known. Each node needs to know if other nodes successfully stored the data or if they failed.
    - it carries out an update in two phases:
        - The prepare phase asks each node if it can promise to carry out the update.
        - The commit phase actually carries it out.
    - It is crucial for each participant to ensure the durability of their decisions using pattern like Write-Ahead Log. This means that even if a node crashes and subsequently restarts, it should be capable of completing the protocol without any issues.

# Version Vector
    - Maintain a list of counters, one per cluster node, to detect concurrent updates.
    - If multiple servers allow the same key to be updated, it's important to detect when the values are concurrently updated across a set of replicas.
    - Each key value is associated with a version vector that maintains a number for each cluster node.
    - In essence, a version vector is a set of counters, one for each node. 
    - eg. A version vector for three nodes (blue, green, black) would look something like [blue: 43, green: 54, black: 12]. Each time a node has an internal update, it updates its own counter, so an update in the green node would change the vector to [blue: 43, green: 55, black: 12].
    - Whenever two nodes communicate, they synchronize their vector stamps, allowing them to detect any simultaneous updates.

# Version Value
    - Store every update to a value with a new version, to allow reading historical values.
    - In a distributed system, nodes need to be able to tell which value for a key is the most recent. Sometimes they need to know past values so they can react properly to changes in a value.
    - --> This allows every update to be converted to a new write without blocking a read. <--
    - --> Clients can read historical values at a specific version number. <--

# Write-Ahead Log (for **Durability**)
    - Provide **durability** guarantee without the storage data structures to be flushed to disk, by persisting every state change as a command to the append only log.
    - Strong durability guarantee is needed even in the case of the server machines storing data failing.
    - Once a server agrees to perform an action, it should do so even if it fails and restarts losing all of its in-memory state.
    - Store each state change as a command in a file on a hard disk.
