## POST (Create)
POST /v1/resources
Header {
    Authorization: jwt(claim: userId)
}
Body {
    attribute1
    mapAttribute {
        key
    }
    listAttribute [
        {
            key1
        }
    ]
} -> 201, 401, 403, 5xx {
    resourceId
}

## GET a resources
GET /v1/resources/{resourceId}
 Header {
     Authorization: jwt(claim: userId)
 } -> 200, 401, 403, 404, 429, 5xx {
    ...
}

## GET Paginated
GET /v1/resources?{cursor}&{limit}&{direction=before|after}
Header {
    Authorization: jwt(claim: userId)
    X-Forwarded-For
} -> 200, 401, 403, 429, 5xx
Header {
    X-RateLimit-Limit
    X-RateLimit-Remaining
    X-RateLimit-Reset
    Retry-After
}
Body {
    resources [
        {
            id
            ...
        }
    ]
    prevCursor
    nextCursor
}

## PATCH (Partial Update)
PATCH /v1/resources/{resourceId}
Header {
    Authorization: jwt(claim: userId)
}
Body {
    status
} -> 200, 401, 403, 409, 5xx


## Delete
DELETE /v1/resources/{resourceId}
Header {
    Authorization: jwt(claim: userId)
} -> 204, 401, 403, 404, 409, 5xx

## SSE - GET
GET /v1/resources/{resourceId}/subscribe?{lastId}
Headers {
    Authorization: jwt(claim: userId)
    Accept: text/event-stream
    Cache-Control: no-cache
    Connection: keep-alive
} -> 200, 401, 403, 404, 5xx
Header {
    Content-type: text/event-stream
}
Body [
    {
        id
        ...
    }
]

## WebSocket
WS /v1/resources/{resourceId}/session
Header {
    Authorization: jwt(claim: userId)
    Connection: Upgrade
    Upgrade: websocket
} -> 101 (Switching Protocols), 401, 403, 404, 400/426, 5xx
Header {
    Connection: Upgrade
    Upgrade: websocket
}
SEND {
    id
    ...
}
RECV {
    id
    ...
}

## RateLimit
```
HTTP/1.1 429 Too Many Requests
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 0
X-RateLimit-Reset: 1640995200
Retry-After: 60
Content-Type: application/json

{
  "error": "Rate limit exceeded",
  "message": "You have exceeded the rate limit of 100 requests per minute. Try again in 60 seconds."
}
```

## Payment
```
POST /v1/payments/:paymentIntentId/transactions
Headers {
    Authorization: jwt(claim: merchantId)
    X-Api-Key: merchant-api-key-value
    X-Request-TimeStamp: timestampz
    X-Request-Nonce: unique-non-value (cached in db for unique)
    X-Signature: HMAC#SHA256(private_key, method, endpoint, parameters, body, timestamp, nonce)
    ClientRefId: uuidv7
}
Body {
    transactionType
    encrypted(public_key, cardDetails{})
}-> 404/409/200 {
    transactionId
    status
}
```

## SMTP
```
C: MAIL FROM:<a@outlook.com>
C: RCPT TO:<b@gmail.com>

C: DATA
S: 354  Go ahead

C: From: sender@outlook.com
C: To: receiver@gmail.com
C: Subject: Test Email
C:
C: Hello, this is a test email.
C: .
S: 250 OK
```