Test Plan
 └── Thread Group
      Threads: 200
      Ramp-up: 60
      Loop: 10

      ├── HTTP Request Defaults
      │     Protocol: https
      │     Server: api.myservice.com
      │     Port: 443

      ├── CSV Data Set Config
      │     File: users.csv
      │     Vars: userId,token

      ├── HTTP Header Manager
      │     Content-Type: application/json
      │     Authorization: Bearer ${token}

      ├── Gaussian Random Timer
      │     Delay: 1000 ms
      │     Deviation: 300 ms

      ├── HTTP Request
      │     POST /v1/orders
      │     Body:
      │     {
      │       "userId": "${userId}",
      │       "amount": 100
      │     }

      └── Response Assertion
            Response Code = 200