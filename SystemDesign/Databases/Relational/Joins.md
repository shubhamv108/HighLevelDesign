### Nested Loop Join
Very common when the inner table has a useful index.\

### Hash Join
Useful especially for equality joins when scanning/building a hash table is cheaper than repeated lookups.

### Nested Loop with Table Scan
A nested loop doesn't necessarily mean an index lookup.

### Batched Key Access (BKA)
MySQL also has an optimization called Batched Key Access for indexed joins.
```
Orders:

user_id=7
user_id=25
user_id=3
user_id=100
       ↓
    batch keys
       ↓
Index access to users
```
