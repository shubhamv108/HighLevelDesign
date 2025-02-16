# Fragments
Fragments are reusable units of query logic that allow you to define a set of fields that can be reused across multiple queries.
-  Reusable Fields: You can define a fragment once and reuse it in multiple operations.
-   Consistency: Ensures consistent field selection across different queries.
-   Modularity: Breaks large queries into smaller, manageable parts.


## Inline Fragments
for conditional queries when dealing with interfaces or union types.
```graphql
    fragment UserInfo on User {
        id
        name
        email
    }
```
```graphql
    query {
      user {
        id
        name
        ... on Admin {
          permissions
        }
        ... on RegularUser {
          subscriptionStatus
        }
      }
    }
```

# Directives
Type system directives may be defined and added to the types and fields in a schema to apply generalized authorization rules

```graphql
    directive @auth(rule: Rule) on FIELD_DEFINITION

    enum Rule {
        IS_AUTHOR
    }
    
    type Post {
        authorId: ID!
        body: String @auth(rule: IS_AUTHOR)
    }
```
# interface
```graphql
# An object with a Globally Unique ID
interface Node {
  # The ID of the object.
  id: ID!
}

type User implements Node {
    id: ID!
    # Full name
    name: String!
}
```

# Input
```graphql
input ReviewInput {
  stars: Int!
  commentary: String
}

type Mutation {
    createReview(episode: Episode, review: ReviewInput!): Review
}
```

# Type
## Scalar 
Int, Float, String, Boolean, and ID

## Enum
```graphql
enum Episode {
  NEWHOPE
  EMPIRE
  JEDI
}
```

# Union
```graphql
union SearchResult = Human | Droid | Starship

{
  search(text: "an") {
    __typename
    ... on Human {
      name
      height
    }
    ... on Droid {
      name
      primaryFunction
    }
    ... on Starship {
      name
      length
    }
  }
}
```

# Query
## Aliases
```graphql
query {
  empireHero: hero(episode: EMPIRE) {
    name
  }
  jediHero: hero(episode: JEDI) {
    name
  }
}
```