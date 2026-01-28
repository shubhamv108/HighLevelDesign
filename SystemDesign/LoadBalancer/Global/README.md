# GCP Global HTTP(S) LB
    1. Deployed in multiple regions
    2. Advertises a Gloabal Anycast IP
    3. Edge location advertises the same Global Anycast IP.
    4. Edge locations route to nearest up CRLB
    7. Layer 7

# Global External Proxy Network Load Balancer
    1. TCP traffic (with optional SSL termination)
    2. SSL proxy capability
    3. Does NOT support UDP (For UDP use Regional External Network Load Balancer (Regional only, not global))
    4. Layer 4
    5. Global anycast IP
    6. Routes TCP connections globally

# AWS Global Accelerator (Closest to "Global LB")
This is AWS's true global load balancing service with anycast IPs:

    1. Two static anycast IP addresses that route globally
    2. Routes traffic over AWS's private network to optimal regional endpoints
    3. Works with ALB, NLB, EC2 instances, or Elastic IPs as backends
    4. Layer 4 (TCP/UDP) routing
    5. Health checks and automatic failover across regions
    6. Better performance than DNS-based routing (no DNS caching delays)
    7. DDoS protection via AWS Shield

# Azure Front Door
    1. Global anycast entry point
    2. Routes traffic to the nearest healthy backend across regions
    3. Built-in WAF (Web Application Firewall)
    4. SSL offloading and caching
    5. Path-based and host-based routing
    6. Best for web applications and APIs
    7. Layer 7

# Azure Cross-Region Load Balancer (Layer 4 - TCP/UDP)