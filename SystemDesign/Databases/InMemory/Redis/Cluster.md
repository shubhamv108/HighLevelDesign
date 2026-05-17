# Communicate Cluster State
via Gossip protocol

# Keyspace (Slots)
- Hash the key
- Find the slot by hash

# the entire shard is gone (primary + all its replicas)
    Then No automatic recovery

1. By default Redis has:

   cluster-require-full-coverage yes

   👉 Result:

   Entire cluster enters error state
   Any request (even for healthy slots) may fail with:
   CLUSTERDOWN Hash slot not served

2. when ```cluster-require-full-coverage no```
    👉 Then:

    Requests for healthy slots → work ✅
    Requests for missing slots → fail ❌

    Example:

    GET key_in_slot_6000 → works
    GET key_in_slot_1000 → fails