```
Pessimistic locking
    ├── FOR UPDATE
    ├── FOR SHARE
    ├── NOWAIT
    └── SKIP LOCKED

Coarse locking
    └── TABLE LOCK

Logical/application locking
    └── Advisory lock

Non-blocking concurrency control
    └── Optimistic locking / versioning

Database-level concurrency
    └── Isolation levels + MVCC
```


SQL |	Lock |	Other normal SELECT |	Other shared lock	| Other FOR UPDATE / UPDATE
SELECT ... FOR UPDATE	| Exclusive (X)	✅	| ❌ waits	| ❌ waits
SELECT ... FOR SHARE	| Shared (S)	| ✅	| ✅	| ❌ waits
Normal SELECT	| Usually no row lock | ✅ | ✅ | ✅


## FOR UPDATE
```sql
START TRANSACTION;

SELECT *
FROM accounts
WHERE id = 1
FOR UPDATE;
```
Another transaction can usually still do:
```sql
SELECT * FROM accounts WHERE id = 1;
```

## FOR SHARE / LOCK IN SHARE MODE
```sql
SELECT *
FROM accounts
WHERE id = 1
LOCK IN SHARE MODE;
```

## FOR UPDATE NOWAIT
Instead of waiting for another transaction:
```sql
SELECT *
FROM account
WHERE id = 10
FOR UPDATE NOWAIT;
```

## SKIP LOCKED
```sql
SELECT *
FROM jobs
WHERE status = 'PENDING'
LIMIT 10
FOR UPDATE SKIP LOCKED;
```

## LOCK TABLE ... IN EXCLUSIVE MODE
- Databases provide different table-lock modes such as shared, exclusive, row-share, row-exclusive, etc.
These are much coarser than row locks and therefore can significantly reduce concurrency.
```sql
LOCK TABLE orders IN EXCLUSIVE MODE;
```
### ACCESS SHARE — normal SELECT
```sql
BEGIN;
SELECT * FROM accounts;
```
prevents
```sql
DROP TABLE accounts;
```

```
ACCESS SHARE
      ↓
ROW SHARE
      ↓
ROW EXCLUSIVE
      ↓
SHARE UPDATE EXCLUSIVE
      ↓
SHARE
      ↓
SHARE ROW EXCLUSIVE
      ↓
EXCLUSIVE
      ↓
ACCESS EXCLUSIVE
```
```
SELECT	| ACCESS SHARE
SELECT ... FOR UPDATE	| ROW SHARE + row locks
INSERT/UPDATE/DELETE	| ROW EXCLUSIVE + applicable row locks
DROP/TRUNCATE	| ACCESS EXCLUSIVE
```

### ROW SHARE — SELECT FOR UPDATE/SHARE
```sql
BEGIN;

SELECT *
FROM accounts
WHERE id = 10
FOR UPDATE;
```
- automatically acquires a ROW SHARE lock on the table, in addition to the relevant row-level locks.
- explicitly request it
```sql
LOCK TABLE accounts IN ROW SHARE MODE;
```
- It still allows other transactions to read and modify other rows.

### ROW EXCLUSIVE — INSERT/UPDATE/DELETE
```sql
BEGIN;
UPDATE accounts
SET balance = 500
WHERE id = 10;
```

### SHARE — protect table against concurrent writes
```sql
BEGIN;

LOCK TABLE accounts IN SHARE MODE;

SELECT SUM(balance)
FROM accounts;

COMMIT;
```

### SHARE ROW EXCLUSIVE
```sql
BEGIN;
LOCK TABLE accounts
IN SHARE ROW EXCLUSIVE MODE;
```
- This is useful when you want to prevent concurrent modifications and also prevent another transaction from obtaining the same kind of strong table lock.
- uncommon

### EXCLUSIVE
```sql
BEGIN;
LOCK TABLE accounts
IN EXCLUSIVE MODE;
```

### ACCESS EXCLUSIVE — strongest
```sql
BEGIN;

LOCK TABLE accounts
IN ACCESS EXCLUSIVE MODE;
```
- DROP, TRUNCATE requires

## Advisory Lock
Some databases let the application create a lock on an arbitrary logical resource.
```sql
SELECT pg_advisory_lock(12345);
```

## Optimistic Locking

