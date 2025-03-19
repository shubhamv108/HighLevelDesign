# IO Threads (Multithreading)
```shell
io-threads 4
```
- The number (4 in this case) should be based on your CPU cores.
- The default value is 1, which means single-threaded I/O.
- Enable I/O Threading for Reads By default, Redis only enables multithreading when handling very large payloads. You can force it to always use I/O threads by adding:
```io-threads-do-reads yes```