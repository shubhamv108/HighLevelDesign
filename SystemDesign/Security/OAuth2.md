# Flow
1. Invoke authorize api on backend
    ```
        http://localhost:8000/authorize
    ```
   - save the encoded_state against user_id in redis
   Return Authorization url for auth sever with backend's redirect uri & encoded_state
   ``` 
    https://app.auth.com/oauth/authorize?client_id={CLIENT_ID}&response_type=code&owner=user&redirect_uri=http%3A%2F%2Flocalhost%3A8000%2Fintegrations%2Fhubspot%2Foauth2callback&state={encoded_state}&scope={scope}
   ```
   
2. Authentication Server will invoke our backend oauth2callback api with code and encoded-state passed as redirect_uri above
    ```http://localhost:8000/integrations/hubspot/oauth2callback?code={code}&state={encoded_state}```

    - Match the received state with cached state
    - invoke oauth token api of Auth server with grant_type and code and clientId and clientSecret
        ```
            https://api.auth.com/oauth/v1/token?grantType=authorization_code&code={code}&client_id={}&clientSecret={}
        ``` -> returns credentials
    ```json
         {
             "access_token": "ACCESS_TOKEN",
             "token_type": "Bearer",
             "expires_in": 3600,
             "refresh_token": "REFRESH_TOKEN",
             "scope": "email profile"
         }
      ```
         
      
    - delete the encoded state from redis
    - save the response as credentials against useId in redis

3. fetch userInfo from Auth server
    ```
        GET https://api.auth.com/userinfo
        Authorization: Bearer ACCESS_TOKEN
    ```
4. Generate JWT token for user and cache in Redis with expiry, update last login time of user



# Other OAuth flows
Flow | 	Client Type | 	User Login |	Secure | Refresh Token | Recommended
---- |--------------|-------------| ------- | ----------- | -----------
Authorization Code | 	Web (backend) | 	✅          |	✅ |	✅ |	✅✅✅
Auth Code + PKCE | 	SPA / Mobile | 	✅          |	✅✅ |	✅ |	✅✅✅
Client Credentials | 	Backend (M2M) | 	❌          |	✅ |	❌ |	✅✅
Implicit Grant | 	SPA (legacy) | 	✅          |	❌ |	❌ |	❌
ROPC | 	Legacy/Mobile | 	✅          |	❌ |	✅ |	⚠️
Device Code | 	Smart devices | 	✅          |	✅ |	✅ |	✅✅