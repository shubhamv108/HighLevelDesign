# Enable CDC event stream

# Create & Write Snapshot
```
-- Connection 1: Create slot and export snapshot (in ONE transaction)
BEGIN;
-- This returns BOTH the slot name AND the LSN
SELECT slot_name, lsn
SELECT * FROM pg_create_logical_replication_slot('migration', 'pgoutput');
SELECT pg_export_snapshot();  -- Returns something like '00000003-0000001B-1'
-- Keep this transaction open!
sql

-- Connection 2, 3, 4... (parallel workers): Use the snapshot
BEGIN TRANSACTION ISOLATION LEVEL REPEATABLE READ;
SET TRANSACTION SNAPSHOT '00000003-0000001B-1';  -- Use the exported snapshot ID
SELECT * FROM users WHERE id >= 1 AND id < 100000;  -- Worker 1's chunk
COMMIT;
sql

-- Connection 1: After all workers finish their snapshots
COMMIT;  -- Now release the original transaction
```

# Filter cdc events till snapshot and apply rest
for event in cdc_events:
    if event['lsn'] > stored_lsn:
        apply_to_target(event)


4. Enable reading apis in new
5. stop writing to old for < 5 seconds
6. enable writing to new