MySQL cluster can be
1. Filter on consistency requirements (replica lag, Seconds_Behind_Source)
2. Group by Proximity (group by AZ, Region, route with Anycast IP)
3. Select on fast response or go to next in group or next group (on basis of recent read latency, least connection/round robin, this is responsiblilty of the LB)

Amazon Aurora provides single FQDN for auto lb amongst read replicas