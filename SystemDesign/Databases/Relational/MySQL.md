# Storage
gp3 - 3K to 16K @ 1000 mB/s - GB to 16TB - 5K WPS, 20K RPS
io2 -                                    - 10K+ WPS, 40K RPS

# RDS Instance
db.r6g.16xlarge - Write - 64 vCPUs, 512 GiB RAM, 10GPps - $7 per hour * 24 = $168 per day * 30 = $5040 per month * 12 = $62K * 5 = $310K * 3 (2 standby AZ) = $930K
db.r6g.8xlarge - Read - 64 vCPUs, 512 GiB RAM, 10GBps - $3.5 per hour * 24 = $84 per day * 30 = 2520 per month * 12 = $31K * 10 = $310K * 3 (2 standby AZ) = ($930K * 2 (DR)) + $1K(Backup) + (... Replication Bandwidth) = $1861K


## Configurations
### PageCache
innodb_old_blocks_pct

