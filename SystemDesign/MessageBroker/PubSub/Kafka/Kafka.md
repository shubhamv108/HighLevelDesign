# Event
- Something that has happened.
- immutable, durable append only logs.

# Low-Water mark
    - Similar to Log start offset, which indicates the earliest offset of a partition that is retained in disk.
    - In Kafka, the low-water mark refers to the earliest offset in a topic partition that is still available on disk. 
    - It is determined by Kafka's log retention policies and consumer progress.
    - The minimum offset across all active consumers in a partition is effectively the low-water mark.
    How it Works
        Consumers and Offsets:
            Each consumer in a consumer group tracks its progress by maintaining the latest offset it has processed for a specific partition.
            The minimum offset across all active consumers in a partition is effectively the low-water mark.
        
        Retention Policies:
            Kafka brokers automatically delete or compact old messages based on:
            Time-based retention (log.retention.hours).
            Size-based retention (log.retention.bytes).
            The low-water mark ensures messages are not deleted prematurely while still being processed by active consumers.
        
        Log Cleanup:
            Messages with offsets less than the low-water mark are eligible for deletion, as all consumers have confirmed processing them.
            Example:
                Assume a topic partition has the following offsets: [0, 1, 2, 3, 4, 5].
                Consumer A has processed up to offset 3.
                Consumer B has processed up to offset 2.
                The low-water mark is set to offset 2.
                Messages with offsets < 2 are eligible for deletion based on retention policies.

# Broker
```retention.ms=7days```
```retention.bytes=1GB```

# Producer
### Idempotent
Each producer is assigned a ProducerID. 
```producer.enable.idempotence=true``` on producer
```producer.aks=all```

# Rebalance


### Batch
```batch.size={bytes}```
```linger.ms={ms}```
```compression.type=gzip/snapppy/lz4```

# [Listeners](https://www.confluent.io/blog/kafka-listeners-explained/)

# Connectors
[Hub](https://www.confluent.io/hub/)
[Connectors](https://www.confluent.io/product/connectors/)
[Lenses](https://lenses.io/apache-kafka-docker/)

# Schema Registry
Server process external to kafka Brokers
Maintains a database of schemas
HA option available
Consumer/Producer API component 
    - To check message whether is compatible with previous version or version they are expecting
    - Defines schema compatibility rules per topic
    - Producer API prevents incompatible messages from being produced
    - Consumer API prevents incompatible messages from being consumed
    - Schemas gets cached in producer and consumer to avoid round trips
    - Schemas have immutable IDs

Supported formats
- Json Schema
- Afro
- Proto Buffs

# Streams
- Filtering, Grouping, Aggregating, joining, etc...
- Scalable, fault tolerant state management
- Scalable computation based on Consumer groups
- It manages state off heap, persist it to local disk, persist it to internal topics of Kafka cluster
- A kafka stream application is a consumer group
- A library

# Examples
https://github.com/eugenp/tutorials/tree/master/apache-kafka


# Deploy
## Kubernetes operators
1. Strimzi (Magic Marker 2 for cross region replication)