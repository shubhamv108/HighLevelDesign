# Invalidation
## Cache Aside
### Read Path:
The application first checks the cache for the requested data.
If the data is found in the cache (cache hit), it is returned to the application.
If the data is not found in the cache (cache miss), the application fetches it from the database, stores it in the cache, and then returns it to the application.
### Write Path:
The application writes data directly to the database.
The cache is **not updated or invalidated** during the write operation. The cache is only populated during read operations.

## Write-around cache
written directly to permanent storage, bypassing the cache
### Advantage
reduce the cache being flooded with write operations
works well for write heavy systems
### Disadvantage
recently written data will create a “cache miss”
stale data if recently updated data is not updated
The cache is not updated during the write operation. Instead, **the cache is invalidated (the corresponding cache entry is deleted)** to ensure that stale data is not served
## Examples
1. Transaction logs
2. rarely accessed records
3. Use Write-Around Cache if your app is write-heavy and caching every write would waste memory.

## Write-through Cache
data is written into the
cache and the corresponding database at the same time
### Advantage
cached data allows for fast retrieval, and since the same data gets written in the permanent storage, 
we will have complete data consistency between cache and storage
cache hit chances increases a lot
### Disadvantage
higher latency for write operations
2 phase commit needs to be implement
if DB goes down write operation will fail, so not fault tolarent
### Examples
L2 Cache to main memory


## Write-back cache
data is written to cache alone, and completion is immediately confirmed to the client
Async writes to db
### Advantage
results in low latency and high throughput for write-intensive applications
### Disadvantage
speed comes with the risk of data loss in case of a crash or other adverse event because the only copy of the written data is in the cache.
### Example
Google TypeAhead for word search count

## Read-Through Cache
### Read path
Reads are first checked in the cache
Cache Miss → Data is fetched from the DB by cache and stored in the cache
### Write path
Writes go directly to the database (cache is not updated on write
### Advantages
Logic for fetching the data from db is with cache and not with application

### Disadvantages

### Examples
1. Product Catalogs
2. User profiles

# Eviction
FIFO
LIFO
LRU
MRU
LFU
RR
