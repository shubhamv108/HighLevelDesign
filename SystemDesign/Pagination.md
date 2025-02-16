# Drawbacks of Offset based
- Using LIMIT OFFSET doesn’t scale well for large datasets. As the offset increases the farther you go within the dataset, the database still has to read up to offset + count rows from disk, before discarding the offset and only returning count rows.
- If items are being written to the dataset at a high frequency, the page window becomes unreliable, potentially skipping or returning
  duplicate results.

# Cursor based
Cursor-based pagination works by returning a pointer to a specific item in the dataset. On subsequent requests, the server returns results after the given pointer.

```json
{
  "size": "",
  "prevPageCursor": "",
  "nextPageCursor": ""
}
```