# Communication b/w microservices
1. ServiceDiscovery
2. Client Side Load Balance
3. Authentication/Authorization
4. Circuit Breaker
5. Retry - 5xx are retryable
6. Deployment Strategy - Canary, Red-Black, Blue-Green
7. Telemetry Capability (Latency. Error Rate, Traffic) - for analysis and monitoring of the component


# Sidecar Proxy
Intercepts any request coming in or going out of microservice
is in Data plane

# Traffic Controller
- Is in Control plane
- Passes config to Sidecar Proxy
# Security Manager
- Passes TLS certificate to microservice
- Authorization


# Istio
- Sidecar Proxy - Envoy
- Configuration Manager - Galley
- Traffic Controller - Pilot
- Security Manager - Citadel
- Console - kiali
- Log Aggregator - Loki
- APM - Skywalking

## Istio System Namespace - Sidecar
1) discovery
