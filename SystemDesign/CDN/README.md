# AWS CloudFront
    1. Content caching
    2. SSL/TLS termination
    3. Compression (gzip, brotli)
    4. Request/response manipulation
    5. Origin selection and failover
    6. Geographic restrictions
    7. Layer 7
    8. Custom error pages
    9. Health Checks
   10. Routing
        a. Path-Based Origin Routing
        b. Origin Groups (Failover)**
        c. Lambda@Edge (Custom Routing Logic)
   11. Signed URLs and Signed Cookies

# GCP SystemDesign.Cloud CDN
    1. Content caching
    2. SSL/TLS termination
    3. Compression (gzip, brotli)
    7. Layer 7
    8. Cache Modes
        a. CACHE_ALL_STATIC
        b. USE_ORIGIN_HEADERS
        c. FORCE_CACHE_ALL
    9. Negative Caching Policy:
        - HTTP 404: Cache for 120 seconds
        - HTTP 410: Cache for 600 seconds
        - HTTP 403: Cache for 60 seconds
        - HTTP 500: Do not cache
    10. Routing
        No support
    11. Signed URLs and Signed Cookies
    12. Cache Invalidation
        a. Invalidate by path
        b. Invalidate by host
    13. Serve While Stale:
            Duration: 86400 seconds (24 hours)

# Azure Front Door