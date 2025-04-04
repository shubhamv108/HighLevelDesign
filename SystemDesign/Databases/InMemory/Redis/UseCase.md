# Cache
## Expire 
```expire {key}```
```key value ttl```
## LRU

# RateLimit
```INCR svc-rate-limit 5```
```EXPPIRE svc_rate-limit 60```
## Lua Script
TokenBucket

# PubSub
- At-most once delivery
- Chat

# Stream
- ConsumerGroup

# SortedSet
## Leaderboard
```ZADD leadeboard1 {score} "{name}"```
```ZADD leadeboard1 {score} "{name}"```
```ZREMRANGEBYRANK leadeboard1 0 5``` # top 5

## GeoHash
```GEOADD purpose lat long name```
```GEOSEARCH purpose FROMLONLAT {long} {lat} BYRADIUS {km}km WITHDIST```
 - For frequently changing location otherwise use haversine formula on static list on server

# Distributed Locks
