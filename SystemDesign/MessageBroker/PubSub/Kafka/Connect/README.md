1.  ecosystem of pluggabble connectors
2.  client application
3.  Scalable
4.  Fault Tolerant
5.  Declarative
6.  Pluggable
7.  interface to external system to kafka
8.  Also exist atr runtime entities
9.  Source Connectors act as Producers
10. Sink Connectors act as Consumers

# Port 
8083

# API
GET /connectors – returns a list with all connectors in use
GET /connectors/{name} – returns details about a specific connector
POST /connectors – creates a new connector; the request body should be a JSON object containing a string name field and an object config field with the connector configuration parameters
GET /connectors/{name}/status – returns the current status of the connector – including if it is running, failed or paused – which worker it is assigned to, error information if it has failed, and the state of all its tasks
DELETE /connectors/{name} – deletes a connector, gracefully stopping all tasks and deleting its configuration
GET /connector-plugins – returns a list of connector plugins installed in the Kafka Connect cluster

# Topics
connect-configs
connect-offsets
connect-status
