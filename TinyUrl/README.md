### Functional Requirements
    1) our service should generate a shorter and unique alias (shortened URL is nearly one-third the size of the actual URL)
    2) our service should redirect them to the original link
    3) custom short link for their URL
    4) Links will expire after a standard default timespan. Users should be able to specify the expiration time.


#### Non-Functional Requirements
    1) Highly Available
    2) Minimal latency while redirection
    3) Shortened links should not be guessable (not predictable)

### Extended Requirements
    1) Analytics; e.g., redirections
    2) Accesible through REST Apis

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
    Base64Encoding(MD5Hash(original_url)=128Bits))
    TotalUniqueShortenedUrls= 64^21
    6LetterShortenedUrl = 64^6 = 6 Billion possible strings
    8LetterShortenedUrl = 64^8 = 281 Trillion possible strings
    On duplication Swap any charcaters

#### Components
##### Creation Service
    - createUrl
    - deleteUrl

##### Redirect Service
    - redirect

##### Key GenerationService
    KeyDB:
        UsedKeys(keys), AvailableKeys(keys) 
        Strorage: 10bytes * 70B = 700GB
    Cahce keys in Application server for fast access
##### Cleanup Service
    - Should run when traffic is low
    - Default expiration time of 2 years


#### Partitioning
    - ConsisitentHashing on shortened_url

#### Cache
    - 20% of daily hot traffic
    - LRU for cache eviction
    - Replicate the caching servers tdistribute read load

#### SystemDesign.LoadBalancer
    - Forr distrubution load on Create & Redirect Service

#### DB Cleanup
    - Asychronously delete expired links when user tries to access
    
#### Telemetry
    - Metrics, Alarms, visitor, most redirects, comming from
