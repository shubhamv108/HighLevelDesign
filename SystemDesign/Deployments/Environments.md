Local -> Dev -> Integration -> QA -> Sandbox -> Live -> Staging

Steps
1. github login with credentials
2. git clone
3. build
4. Approval Gateway
5. runner service account token generation
6. apply iac
7. Approve config update
8. update config
9. Approve deployment
10. deploy
    - one by one / part by part approve if possible
10.1 Check Observability - Alerts, Metrics - POd restarts, api latency spike, cpu/memory usage spikes, APIs 5xx,
11. rollback when required
12. rollback config (like using argo cd...)
