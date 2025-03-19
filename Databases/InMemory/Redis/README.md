# [Persistence](https://redis.io/docs/latest/operate/oss_and_stack/management/persistence/)
1. RDB (Redis DB)
Advantages
   - RDB persistence performs point-in-time snapshots of your dataset at specified intervals.
   - For instance you may want to archive your RDB files every hour for the latest 24 hours, and to save an RDB snapshot every day for 30 days. This allows you to easily restore different versions of the data set in case of disasters.
   - RDB is very good for disaster recovery
   - RDB maximizes Redis performances since the only work the Redis parent process needs to do in order to persist is forking a child
   - RDB allows faster restarts with big datasets compared to AOF.
   - On replicas, RDB supports partial resynchronizations after restarts and failovers.
Disadvantages
    - RDB is NOT good if you need to minimize the chance of data loss in case Redis stops working (for example after a power outage).
    - fork() can be time consuming if the dataset is big, 
Snapshotting
    - By default Redis saves snapshots of the dataset on disk, in a binary file called dump.rdb
    - For example, this configuration will make Redis automatically dump the dataset to disk every 60 seconds if at least 1000 keys changed:
      ```
          save 60 1000
      ```


2. AOF
Advantages
    - Using AOF Redis is much more durable
    - have different fsync policies
      a. no fsync at all
      b. fsync every second
      c. fsync at every query
    - fsync is performed using a background thread and the main thread will try hard to perform writes when no fsync is in progress, 
      so you can only lose one second worth of writes.
   -

Disadvantages

    - AOF files are usually bigger than the equivalent RDB files for the same dataset.
    - AOF can be slower than RDB depending on the exact fsync policy.
    - fsync set to every second performance is still very high,
    - sync disabled it should be exactly as fast as RDB even under high load. Still RDB is able to provide more guarantees about the maximum latency even in the case of a huge write load.

- fully-durable strategy ```appendonly yes```
- ```appendonly always```
- ```appendfsync everysec```
- ```appendfsync no```