# Zero-Downtime Deployment
1. Blue-Green Deployment
2. Canary Deployment
3. Rolling Deployment
4. Rolling with Surge
5. Feature Toggles / Feature Flags
6. Shadow Deployment
7. Immutable Deployment
8. Ring-Based Deployment
9. Progressive Delivery
10. Staged Deployment
11. Blue-Green with Traffic Mirroring
12. Toggled Deployments
13. Matrix Deployment
14. On-Demand Deployment
15. Serverless Deployment with Traffic Shifting

# Downtime Deployment
1. Recreate Deployment
2. Maintenance Window Deployment
3. Manual Deployment (Stop-and-Start)
4. Big Bang Deployment
5. In-Place Deployment Without Orchestration
6. Cold Deployment
7. Initial Production Launch (First-Time Deploy)



# Rolling Update
- Sale down old pods when new pods become available.
- for applications which require constant uptime - Web applications

# Rolling with Surge (a.k.a. Rolling + Extra Capacity)
- Like a rolling deployment, but temporarily adds extra pods or instances to avoid service degradation during rollout.
- Kubernetes allows this with maxSurge in the deployment strategy.
- Slightly higher resource usage during deploy

# Blue-Green Deployment
- Blue - old environments
- Green - new environment
- traffic is switched over to new environment
- minimizes downtime with easy rollbacks

# Red-Black Deployment

# Canary Deployment
- Released to small subset of users incrementally
- useful for testing new features or versions of the application

# Shadow Deployment
- New version with old version
- new version receives request as well but does not return response
- Old version handles the actual traffic
- after monitoring deploy new version in production

# Feature Toggles/Flags
Toggle features on/off at runtime, per user or environment.
- ex. LaunchDarkly, Optimizely, Unleash

# A/B testing (Spli testing strategy)
- run two (or more) versions of a feature
- Users are randomly assigned to different versions (A, B, etc.).
- Used to measure impact before a full rollout
- sits on top of a zero-downtime deployment
- often implemented using:
    1. Feature flag tools (like LaunchDarkly, Optimizely, Unleash)
    2. Load balancers or **service meshes** for routing
- often long running
- Trying different algorithms or pricing models
- Measuring impact before a full rollout

# Blue-Green + Canary Hybrid
- First deploy to Green, but only route a small % of traffic (canary-style).
- Safer rollouts with rollback
- More complex setup

# Ring Deployment
- Deploy to specific user groups (rings) progressively (e.g., internal users → beta users → general public).
- Needs user segmentation & routing logic

# Progressive Delivery
- A higher-level concept that includes feature flags, canaries, A/B testing, and automated analysis.
- managed by tools like Argo Rollouts, Flagger, Spinnaker.
- Intelligent rollouts based on real-time metrics
- Requires tooling and observability

# Dark Launch
- Deploy a new feature into production, but hide it from users (usually via a feature flag).
- Used to test stability under real workloads before enabling it.
- Test performance, scaling in prod
- Feature must be disabled safely

# Serverless Deployment
- Versioning and traffic shifting (e.g., alias weights) let you do canary-style rollouts.

# Recreate
The old version is shut down completely, and then the new version is deployed fresh.


# Partitioned Deployment
- Useful in multi-tenant systems or sharded architectures.

# Black Hole Deployment (for chaos testing)
- Route a small % of traffic to an intentionally broken version to see if the system handles it.
- Controlled chaos testing