A metrics monitoring platform collects performance data (CPU, memory, throughput, latency) from servers and services, stores it as time-series data, visualizes it on dashboards, and triggers alerts when thresholds are breached. Think Datadog, Prometheus/Grafana, or AWS CloudWatch. This is infrastructure that engineers rely on to understand system health and respond to incidents.

Functional Requirements
1. Services emit metrics to platform
2. Query/visualize metrics on dashboards
3. Define alert rules with thresholds
4. Receive notifications when alerts fire

## Estimations
    - 500K servers * 10 data points per sec = 5M data points per sec
    - Volume: each data point is roughly 200 bytes = 1GB per second


## Non-Functional Requirements
1. Scale: 5M metrics/sec, 500k servers
2. Dashboard queries return in seconds
3. Alert latency < 1 minute
4. High availability (eventual consistency OK)
5. Handle late/out-of-order data gracefully

## Core Entities
1. Label (host=server-1)
2. Metric (cpu_usage{host=server-1, region=us-east1})
3. Series
4. Alert Rule
5. Dashboard

## APIs
```
POST /metrics/ingest
{
  "metrics": [
    { "name": "cpu_usage", "labels": {"host": "server-1"}, "value": 0.75, "timestamp": 1640000000 },
    ...
  ]
}
```

```
GET /metrics/query?query=avg(cpu_usage{region="us-east"})&start=A&end=B&step=60 -> { "timestamps": [...], "values": [...] }
```

```
POST /alerts/rules
{
  "name": "High CPU Alert",
  "query": "avg(cpu_usage{region='us-east'}) > 0.9",
  "for": "5m",
  "notifications": ["slack:#oncall", "pagerduty:team-infra"]
}
```

