# Worker Processes
Current worker process utilization
ps -aux | grep nginx
##  Static Content (Light Load)
    worker_processes 1;
Minimal CPU use, as requests are mainly I/O bound (serving files).
## Web
    worker_processes auto;
Number of workers = Number of CPU cores.
## Websockets
    worker_processes = min(CPU cores, (Max connections / worker_connections))


# tcp_nopush on;   
Helps with performance by reducing the number of packets

# tcp_nodelay on;  
Ensures that small packets are sent immediately

# proxy_buffering off;
Disable buffering to reduce latency for WebSocket connections

# Techniques
## Round Robin	
Distributes traffic evenly to each backend server in a cyclic manner.
## Least Connections	
Routes traffic to the server with the fewest active connections.
## IP Hash	
Routes traffic to a backend server based on the client's IP address.
# Weighted Round Robin	
Assigns different weights to servers, sending more traffic to higher-weight servers.
# Random	
Distributes traffic randomly across servers.
# Least Time	
Considers both response time and active connections to choose the server with the least combined time.
# Sticky Sessions	
Ensures requests from the same client are routed to the same backend server (based on cookies or IP).