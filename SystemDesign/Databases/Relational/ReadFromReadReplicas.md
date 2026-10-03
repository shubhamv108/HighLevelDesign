MySQL cluster can be
1. Filter on consistency requirements (replica lag, Seconds_Behind_Source)
2. Group by Proximity (group by AZ, Region, route with Anycast IP)
3. Select on fast response or go to next in group or next group (on basis of recent read latency, least connection/round robin, this is responsiblilty of the LB)

Amazon Aurora provides single FQDN for auto lb amongst read replicas


```sql
CREATE FUNCTION Proximity(
    replica_location INT,
    caller_location INT
)
RETURNS TINYINT
DETERMINISTIC
NO SQL
RETURN CASE
    WHEN replica_location = caller_location THEN 0
    ELSE 1
END;

CREATE TABLE replicas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    az INT NOT NULL,
    region INT NOT NULL,
    replication_lag_ms INT UNSIGNED NOT NULL DEFAULT 0,
    read_latency_ms INT UNSIGNED NOT NULL DEFAULT 0,
    active_connections INT UNSIGNED NOT NULL DEFAULT 0
);
INSERT INTO replicas (
    az,
    region,
    replication_lag_ms,
    read_latency_ms,
    active_connections
)
VALUES
    (101, 1,  10,  5,  20),
    (101, 1,  25,  5,  10),
    (102, 1,  40,  8,  15),
    (103, 1,  90, 12,  30),
    (101, 1, 150,  4,   5),
    (201, 2,  20, 35,  12),
    (202, 2,  60, 40,   8),
    (301, 3,  80, 70,  25),
    (302, 3, 100, 75,  18),
    (401, 4, 120, 95,  40);

SET @caller_region = 1;
SET @caller_az = 101;

SELECT replicas.id
FROM replicas
WHERE replicas.replication_lag_ms <= 100
ORDER BY
    Proximity(replicas.region, @caller_region),
    Proximity(replicas.az, @caller_az),
    replicas.read_latency_ms,
    replicas.active_connections,
    replicas.id;
```