- The server should handle user **authentication** before a GraphQL request is validated; 
- **authorization** should happen within your business logic during GraphQL request execution
- are exposed at a single endpoint, typically ending with /graphql

# Request format
## Request
### Headers
the client should include the application/graphql-response+json in the Accept header
```http
    Accept: application/graphql-response+json;charset=utf-8, application/json;charset=utf-8
    Content-type: application/json
```

### Body
```json
    {
      "query": "...",
      "operationName": "...",
      "variables": { "myVariable": "someValue", ... },
      "extensions": { "myExtension": "someValue", ... }
    }
```

## Response
### Headers
```http
  Content-type: application/graphql-response+json
```
for legacy
```http
  Content-type: application/json
```

### Body
```json
    {
      "data": { ... },
      "errors": [ ... ],
      "extensions": { ... }
    }
```

### StatusCodes
**2xx** - even if the response includes errors
**400** - For validation errors that prevent the execution of a GraphQL operation, although some legacy servers may return a 2xx status code when the application/json

# GET request and parameters
support for HTTP methods other than POST will be at the discretion of the GraphQL server, so a client will be limited to the supported verbs
GET HTTP method may only be used for query operations
a common approach is for the server to store identified GraphQL documents using a technique such as persisted documents, automatic persisted queries, or trusted documents.
```http
http://myapi/graphql?query={me{name}}
```

# Pagination
```graphql
friends(first:2 offset:2)
friends(first:2 after:$friendId)
friends(first:2 after:$friendCursor)
```
## cursor-based pagination
base64 encoding
Q. how do we get the cursor from the object?
A. we might want to introduce a new layer of indirection; our friends field should give us a list of edges, and an edge has both a cursor and the underlying node:

Q. how do we know when we reach the end of the connection?
A.  The connection object will be an Object type that has a field for the edges, as well as other information (like total count and information about whether a next page exists)
```graphql
    query {
      hero {
        name
        friends(first: 2) {
          totalCount
          edges {
            node {
              name
            }
            cursor
          }
            pageInfo {
                endCursor
                hasNextPage
            }
        }
      }
    }
```

```graphql

```

# Versioning
new capabilities can be added via new types or new fields on existing types without creating a breaking change

# Nullability
every field is nullable by default

# [Global Object Identification](https://graphql.org/learn/global-object-identification/)
Consistent object access enables simple caching and object lookups

# Caching
Provide Object Identifiers so clients can build rich caches
if the backend doesn’t have a globally unique ID for every object already, the GraphQL layer might have to construct one. Oftentimes, 
    that’s as simple as appending the name of the type to the ID and using that as the identifier. 
    The server might then make that ID opaque by base64-encoding it.

# JSON (with GZIP)
```http
    Accept-Encoding: gzip
```