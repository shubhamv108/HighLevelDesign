#### SMTP
    Simple Mail Transfer Protocol
    Sending emails from client to mail server.
    When we compose and send email, SMTP is use to deliver the email to recepient mail server.

    Rule/Alert creation or any other client side activity; client will send notification to SMTP --> Server/Storage

#### POP
    Post office Protocol
    Retrieves email from mail server and then deletes the email from server.

    User1 -SMTP/Send-> MailServer -POP/Pull-> User2

#### IMAP
    Retrieve the email but doesn't delete it from server rather let user manage it directly on server.
    Access email in multiple devices.
    Realtime sync.
    Makes management of email easy.
    Folder structure sync.
    Changes on one device are reflected to another device.
    **** IMAP doesn't store anything.

    User1 --SMTP/Send--> MailServer --IMAP/Sync--> User2

#### DNS
    DNS lookup to find the MX records.
    Returns mail servers with Priority. This allows Redundancy of Mail servers.

### APIs
```
POST /v1/messages
Header {
    Authorization: jwt(claim: userId)
}
Body {
    subject
    body
    senderEmailId?
    receivers[]
    cc[]
    bcc[]
} -> 202/250, 400, 401, 403, 429, 5xx {
    id
}
```
```
GET /v1/messages/{messageId}
Header {
    Authorization: jwt(claim: userId)
} -> 200, 401, 403, 404, 5xx {
    subject
    body
    senderEmailId?
    receivers[]
    cc[]
    bcc[]
}
```

```
POST /v1/attachment/upload
Header {
    Authorization: jwt(claim: userId)
} -> 201, 401, 403, 5xx {
    attachmentId,
    storageUrl
}
```

```
POST /v1/messages/{messageId}/send
} -> 200, 401, 403, 409, 5xx
```

```
PATCH /v1/messages/{messageId}
Header {
    Authorization: jwt(claim: userId)
}
Body {
    status: {read|unread|}
} -> 200, 401, 403, 404, 409, 5xx
```
```
DELETE /v1/messages/{messageId}
Header {
    Authorization: jwt(claim: userId)
} -> 204, 401, 403, 404, 5xx
```
```
GET /v1/labels
Header {
    Authorization: jwt(claim: userId)
} -> 200, 401, 403, 5xx {
    labels[]
}
```
```
GET /v1/messages?labels=[*,{labelId}]?{cursor}&{direction}&{limit}
-> 200, 401, 403, 5xx {
    emails[]
    prevCursor
    nextCursor
}

GET /v1/search?{keyword}&labels=[*,{labelId}]?{cursor}&{direction}&{limit}
-> 200, 401, 403, 5xx {
    emails[]
    prevCursor
    nextCursor
}
```

#### Internal APIs
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

## DateStorage+ Cache
(RDBMS or Cassandra) + ObjectStore + Elasticsearch + Redis(Rate Limit + Search + MX records) + CDN + DNS Mx Resolver + Temporal(Kafka - queueing, Validation orchestration, sending, smtp, internal)

### Cassandra
Attachment
id PK uuidv7
userId FK uuidv7
storageUrl

Thread
id PK uuidv7

Inbox
id PK uuidv7
sender
sub
receiver[]
cc[]
bcc[]
bodyObjectStoreLink
attachmentIds[]
sentAt
clientRefId
threadId
threadOrderNumber
status: {DRAFT|SENT|READ|NOT_READ}

### RDBMS
Thread
id PK uuidv7

Email
id PK uuidv7
sender
sub
bodyObjectStoreLink
sentAt
threadId FK
threadOrderNumber
clientRefId UK
status: {DRAFT|SENT|READ|NOT_READ}

EmailReceivers
id PK uuidv7
emailId FK
receiverEmail
UK (emailId, receiverEmail)

EmailCC
id PK uuidv7
emailId FK
ccEmail
UK (emailId, ccEmail)

EmailBCC
id PK uuidv7
emailId FK
bccEmail
UK (emailId, bccEmail)

Attachment
id PK uuidv7
userId FK uuidv7
storageUrl

EmailAttachment
id PK uuidv7
name
emailId FK
attachmentId
order
UK(emailId, order)

Label
id PK uuidv7
name varchar(32) UK

EmailLabel
id PK uuidv7
emailId FK
labelId
UK(emailId, label)


EmailValidation (AsyncWorkflow)
id PK uuidv7
emailId FK uuidv7
policy_check
spam_check
attachment_check

