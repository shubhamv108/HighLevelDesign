i. CHAR
    effectively allows fast and random access, VARCHAR find end of string before mmoving to next one</b>
ii. TEXT
    use for large blocks posts
    allows boolean searches
    results in storing a pointer on disk that is used to locate a text blocks</b>
iii. INT
    - for large numbers up to 2^32 or 4 billion
iv. Decimal
    - use for currency</b>
v. Avoid Blobs</b>
vi. set NOT NULL wherever applicable</b>
vii. use indices o col used in (SELECT, GROUP BY, ORDER BY, JOIN)</b>
viii. index takes more memory</b>
ix. writes can be slower as index needs to be updated</b>
x. when loading large amounts of data, it is better to disable indices. Load Data -> Rebuild Indices</b>
xi. Denormalize if required for performance</b>
xii. Carefully use query cache.</b>
xiii. use UIDv7</b>
xiv. in mysql suggest/force use index in query
```
USE INDEX (idx_customer_id)     -- suggest
FORCE INDEX (idx_customer_id)   -- strongly prefer
IGNORE INDEX (idx_customer_id)  -- don't use this index
```
- SELECT
```sql
SELECT *
FROM orders USE INDEX (idx_customer_id)
WHERE customer_id = 100;
```
- UPDATE
```sql
UPDATE orders FORCE INDEX (idx_customer_id)
SET status = 'COMPLETED'
WHERE customer_id = 100;
```
- DELETE
```sql
DELETE FROM orders FORCE INDEX (idx_customer_id)
WHERE customer_id = 100;
```


