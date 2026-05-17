# 1. Generate the verifier and challenge

# 2. Send challenge with auth request
## get Auth Code
GET /v1/oauth/authorize
?clientId=myapp123
&redirect_uri=https://myapp.com/callback
&response_type=code
&scope=email profile
&state=xyz123
&code_challenge=hdksahdkjsdsd PKCE_(pixie) SHA256(code_verifier_which_is_generated_random_secret)
&code_challenge_method=S256

# 3. User Authenticates
# 4. User approve scopes

# AuthrozationServer
- Stores the Code Challenge

# 5. user Receives Auth code
HTTP/1.1 302 Found
Location: https://myapp.com/callback
?code={authCode} (expires soon, one time use)
&state=xyz123

# 6. Exchange code and verifier token
## Resource Server
POST /oauth/token
{
    grant_type: authorization_code
    code={authCode}
    clientId={}
    clientSecret=secret_key
    redirectUri=https://myapp.com/callback
    code_verifier={code_verifier}
} -> {
    access_token
    token_type: Bearer
}

# 7. AuthrozationServer verifies hash(code_verifier)= code_challenge match
# AuthrozationServer
- Receives the code_verifier hashes it and compares with code_challenge. access token is issued.

# 8. Access the API
GET google.com/v1/auth/calender/events
Header {
    accessToken
} -> 200 {}

# OAuth Grant Types
1. Client Credentials
- Machine to Machine communication, microservices, cron job
```
POST /v1/oauth/token
gran_type=client-credentials
&client_id=
&client_secret=
&scope=api.read
```
2. Device Authorization
```
```
3. Implicit Flow - Deprecated


# Tokens
## Access Token
## RefreshToken
- Allows to fetch new AccessToken
- Long expiry
- Silent Refresh



1. Always use PKCE
2. match redirect URIs exactly
3. keep access tokens under 1 hour
4. https Everywhere - np exceptions
5. Request only scopes you need


