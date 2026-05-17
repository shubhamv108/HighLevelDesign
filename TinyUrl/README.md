### Functional Requirements
    1) our service should generate a shorter and unique alias (shortened URL is nearly one-third the size of the actual URL)
    2) our service should redirect them to the original link
    3) custom short link for their URL
    4) Links will expire after a standard default time span. Users should be able to specify the expiration time.


#### Non-Functional Requirements
    1) Highly Available
    2) Minimal latency while redirection
    3) Shortened links should not be guessable (not predictable)

### Extended Requirements
    1) Analytics; e.g., redirections
    2) Accessible through REST Apis

### Estimations
#### Traffic
    READ:WRITE: 100:1
    New URL's: 500million / month
    Redirections: 500m/month * 100 = 50B/month
    QPS: 
        Write: 500m / (2.5M sec/month) = ~= 200 write/sec
        Read: 100 * 200 write/sec = 20K read/sec

#### Storage
    URLs: 12months * 500M = 6B urls/year i.e 30B urls/5year
    Object Size: 500bytes
    30B * 500bytes = 15TB

#### Bandwidth
    Write: 500bytes * 200write/sec = 100KB/sec
    Read: 500bytes * 20K/sec = 10MB/sec
    
#### Memory
    ColdRead:HotRead : 80:20 %
    20K read/sec * 100K sec/day = 2B read/day
    HotReads = 20% 0f 2B read/day = 400M/day
    size: 400M/day * 500Bytes = 200GB

### DESIGN
#### APIs
    createURL(api_dev_key, url, Option(custom_alias), Option(expire_date), Option(user_name)): (String shortenedUrl)
        - RateLimit for creation in a single day    

    deleteUrl(api_dev_key, url): Boolean
    redirect(shortened_url): (String: original_url, HHTP Status 302(Redirect)|404(NotFound))

#### Database Schema Design (DynamoDB)
    URLs(shortened_url:Varchar(16)(PK), 
        original_url:Varchar(512),
        created_at:DateTime,
        expiration_at:DateTime
        user_id:Int)
    
    Users(id:Int,
          name:Varchar(20),
          email:Varchar(128),
          created_at:DateTime,
          last_login:DateTime)

#### Algorithms
    Base62(SnowflakeID)
        ~11 characters in length
        62^11
    Base62(Redis Counter)

    Base62(UUIDv7)
        ~22 characters in length
        62^22
    Base62(MD5Hash(original_url)=128Bits))
        TotalUniqueShortenedUrls= 62^21
        6LetterShortenedUrl = 62^6 = 6 Billion possible strings
        8LetterShortenedUrl = 62^8 = 281 Trillion possible strings
        On duplication Swap any characters

#### Components
##### Creation Service
    - createUrl
    - deleteUrl

##### Redirect Service
    - redirect

##### Key GenerationService
    KeyDB:
        UsedKeys(keys), AvailableKeys(keys) 
        Storage: 10bytes * 70B = 700GB
    Cache keys in Application server for fast access
    Not useful with SnowflakeID generation
##### Cleanup Service
    - Should run when traffic is low
    - Default expiration time of 2 years

#### Cache
    - 20% of daily hot traffic
    - LRU for cache eviction
    - Replicate the caching servers to distribute read load

#### SystemDesign.LoadBalance
    - For distribution of load on Create & Redirect Service
    - RoundRobin / LeastConnection

#### DB Cleanup
    - Asynchronously delete expired links when user tries to access
    
#### Telemetry
    - Metrics, Alarms, visitor, most redirects, coming from


## Follow Ups
### How would you handle Redis failures or restarts for the counter service, given that losing the counter state could lead to duplicate short URLs?
Redis will be able to handle 20K counter increement requests per second. Redis uses AOF for imeediate rurability.
in case replica is updated to master during failover in redis sentinel then counter in replica might lag from counter in master due to asynchrous replication in redis. this might create some duplicates. To avoid duplicates we checkpoint  the counter + 10000 in database every 1 min from primary and have new primary start from this chekpoint which it fetches during startup using a script.
the checkpoint buffer and frequency can be tunable based on real life practice and load on the system

### How can we ensure that redirects are fast?
"We can cache the recently used short urls in the redis cache. so we do not have to hit the database for all shortuls which are cached. we set the expiry for them as ttl in redis. similarly we can cache them in CDN with expiry. Which will reduce load on servers. CDN reduces latency as these are geographically distributed nodes closer to the user. for redirect user hits the CDN and then cdn forwards to LB incase it is not cached in cdn. Although if CDn is used then request will not reach backend and analtics will not be possible"

### How would you set the TTL values for your CDN cache versus your Redis cache, and what factors would influence those decisions?
Set longer TTLs on the CDN (hours to days) since edge invalidation is hard, and shorter TTLs on Redis (minutes to hours) since it is centralized and easy to refresh, then tie both to link expiration and invalidation strategy. Creation time can live in Redis for freshness checks.
Given too high read to write ratio longer expiration makes more sense. CDn can have longer expiration for popular urls. Although redis ttl should be shorter thean actual expiration of the shorturl. bedefault redis can have 1 hour of ttl and cdn can have 24 hour ttl. but this could be minimum of url expiration and ttl for each caching layer."

### How can your system scale to support 10k redirects (reads) per second?
"Redirect requests comes through CDN.
CDN should be able to handle 80% of the redirect load. remaining will come to redis and the database. redis can handle 20K requests per second. we can have read replicas of our database and reads requests can go to read replicas instead overloading the master.
our services can scale out automatiaccaly on bases of cpu, memory ussage to hand;e the requests. load balncer cuses round roben algo to keep equal load on each URl shortening Redirection service applicaiton instance."
