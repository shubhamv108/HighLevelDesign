```
client --Data--> NetworkCard --Interrupt--> KernelBuffer(kernel read from n/w card) --copy(when proc scheduled on cpu)--> UserSpace(socket gets the data)
```


## FileDescriptor
32 bit integer value that uniquely identifies a file - Disk or Socket.

In Unix everything is a file, hence they have a file descriptor.
    - Disk I/O
    - Socket I/O



## EPOLL
EPOLL monitors alot of File Descriptors for new I/O
EPOLL: Linux
KQueue: Max
IOCP: Windows

#### Create New Epoll
```
epoll_create1
```

#### Register/Deregister FileDescriptor (Monitor FileDescriptor)
```
epoll_ctl
```

#### Ask epoll to monitor every single client connection along with the main server socket.
```
client Socket 1
client Socket 2
client Socket 3
...
...
Server Socket 3
```

#### Waits for update on registered file descriptors
How to know some I/O is ready ?
```
epoll_wait
```
This is a blocking and moves forward only when some FDs are ready for an I/O.


