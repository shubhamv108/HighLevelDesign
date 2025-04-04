## Functional Requirements

1) One to One messaging
2) Realtime
3) Last Seen
4) Persist Chat History
5) Read Receipts
6) Group messaging
7) End to End encryption (ToDo)


## Non-Functional Requirements
1) Scalability 
2) Low Latency
3) High Availability    

## Estimations (Assumptions)
    **Users**: 20 billion
    
    **DAU**: 500 million

    **Groups**: 100 million
    
    **Messages/day**: 
        500 million users * 40 messages/day = 20 billion messages/day

    **Read Receipts**
        20 billion messages/day * 2 = 40 billion read receipts / day

    **Typing Events** 
        20 billion messages/day * 50 characters/message = 1 Trillion typing evnts / day

    **Untyped Events*
        20 billion messages/day * 1 = 20 billion untyping events / day

    **Images**
        5 imgaes/day
        500 million users * 5 images/day = 2.5 billion images/day

    **Storage**:
        Messages:
            Message Size: 100 bytes;
            20 billion messages/day * 100bytes = 2TB/day
            2TB/day * 365 day * 5 years = 3.6 PB
        
        Images:
            Image Size: 1MB
            2.5 billion images/day * 1 MB = 2.5 PB

        User, group info
    
    **Bandwidth**:
        Messages:
            2 TB per day / 86400 seconds ~= 25 Mb/sec
            40 billion read receipts / day * 20 bytes = 800Gb / 864000 = 10Mb / sec
        
        Typing & Not typing Message:
            1 Trillion tying events / day * 20bytes = 20TB/day / 864000 seconds = 250 Mb/sec
            20 billion untyping events / day + 20bytes = 25Mb/sec
        
        Imgaes:
            2.5 PB per day / 86400 seconds = 28 GB/sec

        Total:
            25 Mb/sec(messages) + 10 Mb/sec + 250 Mb/sec(Typing) + 25 Mb/sec(untyping) + 28 GB/sec(Images) ~= 28.300 GB/sec
        

## Design

### Protocols
#### WebSocket: 
    Application Layer protocol for duplex connection. Uses TCP at Transport layer
#### [Signal Protocol](https://github.com/signalapp/libsignal-protocol-java):
    For message encryption

### Client APIs
<details>
    <summary>POST /sendOTP?mobileNumber=</summary>
</details>

<details>
    <summary>POST /validateOTPAndRegisterDevice?mobileNumber=&otp=</summary>
</details>
    
<details>
    <summary>POST /updateUser?mobileNumber=&userId=</summary>
        
        **Request:**
        { 
            “name”:””,
            ”profile_status”:””, 
            “notificationToken”:[
                {
                    ”token”:””, 
                    “type”:”FCM/APN”, 
                    "deviceInfo":{}
                }
            ]
        }
</details>

<details>
    <summary>PUT /createGroup?mobileNumber=</summary>
    
    **Request:**
    { 
        “name”:””,”profile_status”:”” 
    }
</details>

<details>
    <summary>PUT /addUserToGroup/{userId}?groupId=</summary>
</details>

<details>
    <summary>GET /fetchAllUserGroups?userId=</summary>
</details>

<details>
    <summary>GET /fetchAllUsersForGroup?groupId=</summary>
</details>

<details>
    <summary>GET /fetchAllNotReceivedMessages?userId=</summary>
</details>

<details>
    <summary>GET /fetchAllNotReceivedMessagesFromGroups?userId=</summary>
</details>

<details>
    <summary>GET /getLastSeen?userId=[]</summary>
</details>

<details>
    <summary>POST /subscribe</summary>
</details>
<details>
    <summary>POST sendMessage?senderId=&receiverId=&type={TYPING|UNTYPED|TEXT}</summary>
        
        **Request**: 
        {
            “content”:””
        }
</details>

<details>
    <summary>POST sendGroupMessage?senderId=&receiverId=&type={TYPING|UNTYPED|TEXT}</summary>
    
    **Request**:
    {
        “content”:””
    }
</details>


### Internal APIs
<details>
    <summary>GET /generateSessionToken?userId=</summary>
</details>

<details>
    <summary>POST /validateSessionToken?userId=</summary>
</details>

<details>
    <summary>PATCH /updatelasSeen/{userId}/</summary>
        
        **Request**:
        {
            "timestamp": " IST"
        }
</details>

<details>
    <summary>POST /validateOTP?mobileNumber&otp=</summary>
</details>

<details>
    <summary>GET /fetchAllNotReceivedMessages?receiverUserId=&fromTimestamp=</summary>
</details>

[comment]: <> (### Client Side APIs)

[comment]: <> (<details>)

[comment]: <> (    <summary>/ping</summary>)

[comment]: <> (</details>)

### Tables
    Users(Id, mobile_number, profile_status)
    
    Groups(id, name, group_status)
    
    UserGroupMapping(id, user_id, group_id, is_admin)
    
    Messages(id, sender, receiver, sentAt, aes(content), status[RECEIVED_AT_SERVER | RECEIVED_BY_RECEIVER | SEEN_BY_RECEIVER | SENDER_INFORMED_SEEN_BY_RECEIVER])
    
    GroupMessages(id, sender, group_id, sentAt, isReceived, isSeenByReceiver)

    LastSeen(user_id, seen_at)

    UserSessionTokens(user_id, aes_encrypted(session_token), device_type, device_name, time_to_live)

    UserConnectionServerMapping(user_id, ListOf(frontend_server_ip, time_to_live))    #port&api is fixed
    
    OTP(mobile_number, otp, ttl)

    Notifications(mobile_number, message, type, generatedAt, status, sentAt)

    UserNotificationTokens(user_id, token_type, token)

    Images(id, uploader_user_id, url, file_hash, uploadedAt)


### Components

#### SystemDesign.LoadBalancer

#### API Gateway

#### User Service

#### Authentication Service

#### Group Service

#### One to One Message Handler

#### Group Message Handler

#### Last Seen Service

#### WebSocket Manager
    Holds the info about which user is connected which WebSocket Handler
    WebSocketHandler writes to WebSocket Manager when a user connects or disconnets
    Used by One to One message handler & Group Message Handler before routing message to connected user.
    Holds mapping of user->List(WebSocket_Handler) in Redis
    
#### WebSocket Handler:
    User connects using WebSocket for realtime chat messaging
    User connects to the most nearest service
    Each web socket connection is held in inmemory map user->List(connection)
    It maintains heartbeat with client by sending ping pong messages evrry 10 seconds
    When user does not reply with pong for 30 seconds then it updates the user connection state in WbSocket Manager

#### SystemDesign.DNS Geographical Load Balancer

#### SystemDesign.DNS

#### Image Upload Service
    Uploaded file is saved in bucket for user in blob store
    Save hash of file, url of blob, uploaderUserId
    Sender sendds message 

#### Image Downloader Reverse Proxy


## [HLD Diagram](https://www.github.com/shubham-v/HighLevelDesign/whatsapp/whatsapp.hld.drawio)

## Flow Diagrams

## Technology Stack
    SystemDesign.DNS Load Balancer: AWS Route53 (Geoproximity routing policy|Latency routing policy|)
    Load Balancer: Custom|NGINX|AWS ELB
    TLS Termination proxy: NGINX
    Server Host Machine: AWS EC2 (AWS Auto Scaling Group/service)
    OLTP ACID Database: MySQL
    Cache: Redis
    OLTP Non-ACID Database: Cassandra|HBase,
    Image Blob Store: AWS S3


