# Accept-count 
- Spring boot config name ```server.tomcat.accept-count=100  # Max queue size before rejecting requests```
- Defines the maximum number of connections that can wait in the request queue before being rejected.
- Default and Maximum Values:
  Default Value: 100
  Max Theoretical Value: Integer.MAX_VALUE (2,147,483,647)
  Practical Limit: Usually 10,000–65,535, depending on OS and system resources.

# Max threads
server.tomcat.max-threads=200   # Max worker threads to process requests