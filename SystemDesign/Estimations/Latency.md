# Latency
    L1 Cache Reference                          0.5 ns
    Branch MisPredict                           5 ns
    L2 Cache Reference                          7 ns
    Mutex Lock/Unlock                           25 ns
    Main Memory                                 100 ns   (20 8 L2, 200 * L1)
    Compress 1KB (Snappy)                       10 us
    1 KB over 1 GBps N/W                        10 us
    Read 4KB randomly for SSD                   150 us   (~1GB per second from SSD)
    Read 1MB sequentially from memory           250us    
    Round trip within same datacenter           500us
    1 MB sequentially from SSD                  1 ms     (~1GB per second for SSD, 4 * Memory)
    HDD seek                                    10 ms    (20x of datacenter roundtrip)
    Read 1MB sequentially from 1GBps Ethernet   10 ms    (40x memory, 10 x SSD)
    Read 1MB from HDD                           30ms     (120x memory, 30 x SSD)
    Send Packet CA -> netherlands -> CA 150 ms

# Read sequentially
    HDD:            30MB per second
    1GBps Ethernet: 100MB per second
    SSD:            1GB per second
    Main Memory: 

# RoundTrips
    Worldwide:  6-7 per second
    Datacenter: 2000 per second



