# FR
    Start group chats
    Send/receive messages
    Send/reaceive media
    Access messages after I've been offline

    ---- Below the line ----
    Audi / Video calling


## NFR
    Delivered with low-latency < 500ms
    Guarantee delivery messages
    Billions of users, high throughput
    Messages not stored unnecesserily
    Fault-tolerant


# Entities
    User
    Chat
        id (PK)
        name
        metadata
    ChatParticipants
        chatId (PK)
        participantsId (SK)
    ChatParticipantsGSI
        
    Messages
        id
        chatId
        senderId
        createdAt
        senderRefCounter
    Inbox
        receipientClientId
        messageId
    Client
        clientId
        userId

# APIs
    Command Sent
        createChat
        sendMessage
        createAttachement
        modifyParticipants
    Commnd Received
        newMessage
        chatUpdate


# Design
### Components
1. Client
2. Load Balancer (Least Connection)
3. Chat Server (Chat DB (DynamoDB))
4.