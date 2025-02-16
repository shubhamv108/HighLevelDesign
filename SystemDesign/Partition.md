# Methods
## Horizontal

## Vertical

## DirectoryBased
To find out where does a particular data entity resides, we query our directory server that holds the mapping between each tuple key to its DB server

# Criteria
## Key or Hash-based partitioning
## RangeBasedPartitioning
## List partitioning
each partition is assigned a list of values, so whenever we want to insert a new record, we will see which partition contains our key and then store it there

## Round-robin partitioning
## Composite partitioning
e.g. first applying a list partitioning and then a hash based partitioning.
Consistent hashing could be considered a composite of hash and list partitioning where the hash reduces the key space to a size that can be listed.