# Search
    1. Criteria (where)
    2. Sort (order by)
    3. Facets

# APIs
## PUT /{indexName}
```json
{
  "settings": {
    "number_of_shards": 1,
    "number_of_replicas": 1
  }
}

```

## PUT /{indexName}/_mapping
```json
{
  "properties": {
    "key": { "type": "text" },
    "nestedKey": {
      "type": "nested",
      "properties": {
        "key": { "type":  "text" }
      }
    }
  }
}
```

## POST /{indexName}/_doc
```json
{
    "key": "value",
    "nestedKey": {
        "key": "value"
    }
}
```

## PUT /{indexName}/_dox/{docId}?version=1
```json
{
  
}
```

## POST /{indexName}/_update/{docId} (fieldByFieldUpdate)
```json
{
  
}
```

## GET /{indexName}/_search
```json
{
  "query": {
    "match": {
      "title": "Great"
    }
  }
}
```
```json
{
  "query": {
    "match": {
      "title": "Great"
    }
  }
}
```
```json
{
  "query": {
    "bool": {
      "must": [
        { "match":  { "title": "Great" } },
        { "range": { "price": { "lte": 15 } } }
      ]
    }
  }
}
```
```json
{
  "sort": [
    {
      "_script": {
        "type": {
          
        }
      }
    }
  ],
  "query": {
    "nested": {
      "path": "reviews",
      "query": {
        "bool": {
          "must": [
            { "match": { "reviews.comment": "excellent" } },
            { "range": { "reviews.rating": { "gte": 4 } } }
          ]
        }
      }
    }
  }
}
```

## POST /{indexName}/_pit?keep_aive=1m
```json

```

## DELETE //_pit

# TF-IDF (~)
when not defining sort order

# DocValues (lucene index)
- For columnar storage
- 

#
1. Tokenization 
2. Stemming
3. Lemmatization

# Limits
2B Doc in a Shard