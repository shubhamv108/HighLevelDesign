# Why ColumOriented (ColumnFamilyOriented)

## Linearizable
It does wait for ReadRepair to complete on quorum reads, but it loses linearizability if there are multiple concurrent writes to the same key, 
due to its use of **last-write-wins conflict resolution**. 
