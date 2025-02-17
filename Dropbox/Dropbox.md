# Functional Requirements

# Non-Functional Requirements
Scalable
Highly Available
Secure
Reliable
Performant
Cost efficiency
Fault Tolerant
Loggable
Monitored

# Capacity Estimations & Assumptions
## DAU
## MAU
## Read:Write
## Bandwidth
## Storage
## Memory 
### Cache
## Pods
### Memory
### CPU

# Commands
## Split
```bash
  split --bytes=1000M 6GB 6GB.part.
```
## Combine
```bash
  cat 6GB.part.* > 6GB.combined
```

# APIs
## POST
/files  -> 201 Created
### Request
```json
{
  "type": "file",
  "name": "report.pdf",
  "parent_directory_id": "dir_123",
  "isDirectory": false,
  "chunks": [
    {
      "name": "base64_encoded_chunk_1",
      "checksum": "sha256_abc123" 
    },
    {
      "name": "base64_encoded_chunk_2",
      "checksum": "sha256_def456"
    }
  ],
  "file_hash": "sha256_fullfilehash"
}
```
### Response
```json
{
  "id": "file_123",
  "type": "file",
  "upload_id": "upl_abc",
  "chunks": [
    {
      "index": 0,
      "s3_upload_url": "https://my-bucket.s3.amazonaws.com/chunks/chunk1?X-Amz-Signature=...",
      "expires_at": "2023-09-01T12:05:00Z"
    },
    {
      "index": 1,
      "s3_upload_url": "https://my-bucket.s3.amazonaws.com/chunks/chunk2?X-Amz-Signature=...",
      "expires_at": "2023-09-01T12:05:00Z"
    }
  ],
  "version_id": "ver_456",
  "status": "pending"
}
```
## GET
### Paginated
    /files?&size={size}&cursor={Base64EncodedCursor}
    /files/{fileId}/chunks/{chunksId}?size={size}&cursor={Base64EncodedCursor}
    /updates?lastEventId={lastEventId}&timeout={timeoutInSeconds} (Long Polling)
### Response
200 OK
```json
    {
  "events": [
    {
      "event_id": "evt_123",
      "type": "file_created",
      "timestamp": "2023-09-01T12:00:00Z",
      "data": {
        "file_id": "file_abc",
        "path": "/Documents/report.pdf",
        "version": 3
      }
    },
    {
      "event_id": "evt_456",
      "type": "file_updated",
      "timestamp": "2023-09-01T12:05:00Z",
      "data": {
        "file_id": "file_def",
        "path": "/Photos/image.jpg",
        "version": 2
      }
    }
  ]
}
```
400
```json
{ "error": "Timeout exceeds limit" }
```
410
```json
{ "error": "Reset required" }
```

## PATCH
/files/{file_id}
### Request
```json
{
  "new_name": "report_final.pdf",
  "new_parent_directory_id": "dir_789",
  "version": 1,
  "status": "UPLOADED"
}
```
/files/{file_id}/chunks/{chunkId}
### Request
```json
{
  "new_name": "report_final.pdf",
  "new_parent_directory_id": "dir_789",
  "version": 1,
  "status": "UPLOADED"
}
```
## DELETE
DELETE /files/{file_id}   -> 204 no content
## PUT

# DB 
## Schema
files
-----
    id uuid (PK)
    size bigint not null
    status varchar(16) (CREATED/UPLOADED/DELETED)
    version int not null
    parent_directory_id uuid (FK files.id)
    is_directory boolean default false  // to preserve hierarchy
    name TEXT not null
    account_id (FK account.id) not null (INDEX)
    constraint (parent_directory_id != id)

files_versions
--------------
    id uuid (PK)
    file_id uuid (Fk files.id)
    version int not null
    parent_directory_id uuid (FK files.id)
    parent_version int
    size bigint not null
    checksum varchar(255) (UK)
    UK (file_id, version)

chunks
------
    id uuid (PK)
    file_id (FK files.id)
    checksum varchar(255) INDEX
    status varchar(16) (CREATED/UPLOADED/DELETED)
    file_version int not null
    chunk_order int not null
    dfs_bucket varchar(255) not null
    dfs_key uuid not null
    UK (dfs_bucket, dfs_key, checksum)

chunk_versions
--------------
    id uuid PK
    files_version_id uuid (FK files_versions.id)
    chunk_id uuid (FK chunks.id)
    chunk_order int not null
    UK (chunk_order int not null, chunk_id, chunk_order)

devices
-------
    id uuid (PK)
    account_id uuid (FK accounts.id)
    device_type varchar(16) (APN/GCM/Desktop)
    token text

## Sharding
    PartitionKey= account_id

# Design
# HighLevelDesign

# Cache
## Eviction

## SystemDesign.LoadBalancer
    PeakExponentialWeightedMovingAverage

## RateLimiter
    FixedWindow or TockenBucket for bursts
    429 - Too many requests

## N/W Protocol
### TCP
#### HTTP
##### CORS
#### WebSocket
### UDP

## Interface
### REST
### GraphQL
### GRPC

# Security
## Authentication
    Authorization: Bearer <JWT_TOKEN>
    Cookies: credentials=<JWT_TOKEN>
### Invalid Response
    401 
    ```json
      { 
        "error": "Unauthorized"
      }
    ```

## Authorization
### RBAC
### ABAC
### Invalid Response
    403
    ```json
      { "error": "Forbidden" }
    ```

## Encryption
### SSL/TLS
    TLS 1.3
    AES-256 for store chunks at rest

# Scalability
## Vertical
    Till certain limit
## Horizontal
    Using auto scaling on LB metrics
    By precalculating & load testing
### Sharding
    PartitionKey=account_id
## Global Availability
    DFS & CDN

# Performance
## Cache
    InMemory (Redis) for metadata
## CDN
    deployed worldwide
    for static files


# Disaster Recovery
## Replication
- Replicate chunks in >= 3 geographically data centers
## Backups
- DB snapshot
## Restore/Rollback Process
- Container Orchestration - Kubernetes

# Monitoring & Observability
## Logging
    - Centralized logs (e.g., ELK Stack) for API requests and errors.
    - Do not log plaintext secrets.
    - Do not log senssitive data.
    - Service <- FluentBit(Sidecar) -> AWS CloudWacth -> Opensearch <- OpensearchUI
    - Service <- FluentBit(Sidecar) -> Elasticsearch <- Kibanna
## Metrics
    - Service <- Prometheus -> InfluxDB <- Grafana
    - Service <- Prometheus -> Grafanna
    - Service <- FluentBit(Sidecar) -> AWS CloudWacth <- AWS CloudWatch Metrics
## Alerts
    - Service <- FluentBit(Sidecar) -> AWS CloudWacth <- AWS CloudWatch Metrics -> AWS CoudWatch Alerts -> SNS
    - Service <- Prometheus -> InfluxDB <- ObservabilitySVC -> NotificationSender


# Deployment
## Containers
## Orchestration
## Environment Selection

# Trade-offs & Challenges
## Consistency vs. Availability:
Prioritize availability (AP system) for sync; resolve conflicts eventually.
## Cost
Deduplication reduces storage costs but adds compute overhead.
## Latency
CDN and edge caching minimize latency for global users.
## Security
End-to-end encryption adds complexity but is critical for trust.

# Bottlenecks