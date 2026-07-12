## ScalableBaseInfra
Clients access the system through
Clients access the system through Route 53 DNS A/AAAA alias records that resolve to
AWS Global Accelerator's static anycast IPs; Global Accelerator routes traffic to the nearest healthy regional endpoint and performs automatic cross-region failover, after which requests flow through
CloudFront → AWS WAF + Shield Advanced → API Gateway (authentication, authorization, throttling, and rate limiting via Lambda authorizers/Cognito) → VPC Link/Private Endpoint →
internal ALB/NLB in a private VPC → Auto Scaling application services (EKS/ECS/EC2) protected by circuit breakers, retries, and bulkhead isolation →
Redis/ElastiCache →
RDS Proxy → Aurora PostgreSQL Multi-AZ with read replicas and cross-region replication,
while asynchronous workloads are handled through SQS/Kafka,
observability is provided by CloudWatch, OpenTelemetry, X-Ray, Prometheus, and Grafana,
and disaster recovery is achieved through Aurora cross-region replica promotion and Global Accelerator traffic failover.

        Clients access the system through Route 53 DNS A/AAAA alias records that resolve to AWS Global Accelerator's static anycast IPs; Global Accelerator routes traffic to the nearest healthy regional endpoint and performs automatic cross-region failover, after which requests flow through CloudFront → AWS WAF + Shield Advanced → API Gateway (authentication, authorization, throttling, and rate limiting via Lambda authorizers/Cognito) → VPC Link/Private Endpoint → internal ALB/NLB in a private VPC → Auto Scaling application services (EKS/ECS/EC2) protected by circuit breakers, retries, and bulkhead isolation → Redis/ElastiCache → RDS Proxy → Aurora PostgreSQL Multi-AZ with read replicas and cross-region replication, while asynchronous workloads are handled through SQS/Kafka, observability is provided by CloudWatch, OpenTelemetry, X-Ray, Prometheus, and Grafana, and disaster recovery is achieved through Aurora cross-region replica promotion and Global Accelerator traffic failover.