```
OTP = Last4To8(Base62(HMAC(user_secret, user_id + purpose + challenge_id + time_window)))
```

```
T=⌊
time step
current time
	​

⌋
```
Where:

current time = Unix timestamp (seconds)
time step = window size (usually 30 seconds)

```
key: otp_meta::{challenge_id}
value: {
  "user_id": "9876543210",
  "purpose": "LOGIN",
  "expires_at": 1710000300,
  "verify_attempts_left": 5,
  "resend_attempts_left": 2,
  "used": false
}

TTL: 5 min
```

Lua script to update verify, resend
delete after success verification

