## Read Uncommitted
Theoretically allows dirty reads (reading uncommitted changes from other transactions). In Postgres, this is silently upgraded to Read Committed — dirty reads simply don't happen due to MVCC.
## Read Committed (default)
Each statement sees only data committed before that statement began. Within a single transaction, two identical queries can return different results if another transaction commits between them (non-repeatable reads).
## Repeatable Read
The transaction sees a snapshot of the database as of its first statement. Prevents non-repeatable reads and phantom reads (unlike standard SQL, Postgres's MVCC also prevents phantoms here). Can fail with a serialization error if a conflict is detected — you must retry the transaction.
## Serializable
Full SSI (Serializable Snapshot Isolation). Guarantees transactions behave as if they ran one-at-a-time serially. Detects dangerous read/write dependencies and aborts one of the conflicting transactions. Also requires retry logic.