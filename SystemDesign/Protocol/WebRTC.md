- Provides real time communication

# APIs
    - GetUserMedia - get access to camera and microphone of the user device
    - PeerConnection - establish connection between two peers to send and receive data
    - DataChannel - send and receive data between peers


# Protocols
    - ICE (Interactive Connectivity Establishment) Framework - used to establish connection between peers
    - STUN (Session Traversal Utilities for NAT) - used to get public IP address of the peer
    - TURN (Traversal Using Relays around NAT) - used to relay data between peers if direct connection fails
Supports sTCP & UDP
- Also involves NAT & Firewall traversal

                    Signling server
        
        User 1  NAT          NAT  User 2

        STUN Server          STUN Server
                  TURN SERVER
- User send ICE candidate to STUN server
- PeerConnect with ICE Urls
- DataChanel(Connection)
- Turn Server is relay server incase user not in Stun server

# Signalling Server
- Metadata about user1 & user2 so that we can establish connection between them


# Components
- User
- Group
- Messages
- Notification
- Asset
- WebSocket Handlers
- WebSocket Management
- Group Management Handlers

# SDP Session description Message
- codec
- encryption