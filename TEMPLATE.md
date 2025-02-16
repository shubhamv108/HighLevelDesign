# Functional Requirements

# Non-Functional Requirements

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

# APIs
## POST
/files  -> 201 Created
### Request
```json
{
  "": "",
  "": ""
}
```
### Response
```json
{
  "": "",
  "": ""
}
```
## GET
### Paginated
/?&size={size}&cursor={Base64EncodedCursor}
## PATCH
/
### Request
```json
{
  "": "",
  "": ""
}
```
/files/{file_id}/chunks/{chunkId}
### Request
```json
{
  "": "",
  "": ""
}
```
## DELETE
/   -> 204 no content
## PUT

# DB 
## Schema

## Sharding
PartitionKey=

# Design
# HighLevelDesign

# Cache
## Eviction

## SystemDesign.LoadBalancer

## RateLimiter
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
401 
```json
  { "error": "Unauthorized" }
```

## Authorization
### RBAC
### ABAC
403
```json
  { "error": "Forbidden" }
```

## Encryption
### SSL/TLS

# Scalability
## Vertical
Till certain limit
## Horizontal
Using auto scaling on LB metrics
By precalculating & load testing
### Sharding
PartitionKey=
## Global Availability

# Performance
## Cache
## CDN


# Disaster Recovery
## Replication

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

# Trade-offs
## Strong Consistency vs High Availability

# Bottlenecks