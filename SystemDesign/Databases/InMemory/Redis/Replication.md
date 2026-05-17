    1. A single node can have replicas explicitly configured with it.
    2. Each node will also have a replication backlog buffer. 3. whenever a write is made it is appended to the replication backlog buffer with offset for each entry when replicas are configured for the master.
    4. another single thread then sends them over TCP to replicas. in this manner replicas are asynchronously updated.
    5. if a new replica is added, it will do a full sync with primary and primary will respond with full snapshot.
    6. if replica disconnects and reconnects it will do a partial sync with primary from it's current offset. (if replica promoted to primary but it will have two replication id, one new ad second old when it was replica which will allow it to partial sync with old replicationId when asked by a replica)
    7. Primary & replicas talk to each other using gossip protocol. This helps to discover disconnection of node or the primary.
    8. When each replica detects that primary is down . Raft election is initiated amongst the most updated and connected replicas. one is chosen as primary.
    9. Client will discover new primary by fixing it's topology. it will send write request to old primary and it will find it to be dead. then it will choose another replica for write if it is not primary it will respond with MOVED (301). till pne replies successfully.
    10. Client can opt-in to read from replicas by using READONLY mode.
    11. We can have replicas in Multi AZ to achieve high Availability.