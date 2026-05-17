```
GET identityprovider.com/v1/authorize
?clientId=myapp
&redirect_url=https://myapp.com/callback
&response_type=code
&scope=openid profile email
&nonce=xyz789abc (replay protection)
&code_challenge=fkjdshf43...
&code_challenge_method=S256
``` ->
```
{
    accessToken (api calls from resource server) (Authorize)
    IDToken (identity + JWT with claims) (Indentifies)
}
```

# IDToken
{
    Header: {
        alg: RS256
        typ: JWT
    }
    Payload: {
        "sub": "userUniqueID"
        "iss": "https://accounts.google.com" (Issuer)
        "aud": "my-app-client_id" (four your app)
        "email": ""
        "name": ""
        "picture": ""
        "nonce": "xyz789abc" (Echoed - Replay Protection)
        "exp": ""
    }
    Signature: RSASHA256(...)
}

# Validate ID Token
1. Verify the signature (RS256)
2. Check iss matches expected provider
3. check "aud" matches your client ID
4. check "nonce" matches what you sent
5. check exp hasn't passed


# SSO
## IdP Session

## Central IdP Providers
1. Azure Active Directory
2. Okta
