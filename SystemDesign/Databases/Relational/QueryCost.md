```
Cost ≈ I/O cost + CPU cost
```

```
Table scan on orders
(cost=0.65 rows=4)
```

```
Table Scan Cost
    ≈ pages_to_read × page_read_cost
      + rows_to_evaluate × row_evaluation_cost
```

```
Index Cost
    ≈ index_pages_read
      + index_comparisons
      + rows_found
      + table-row lookups
```

```
Join Cost
    ≈ outer_table_cost
      + number_of_outer_rows × inner_lookup_cost
```

```sql
CREATE TABLE users (
    id INT,
    name VARCHAR(100) NOT NULL,
    age INT
);
INSERT INTO users (id, name, age)
VALUES
    (1, 'Shubham', 31),
    (2, 'Rahul', 28),
    (3, 'Amit', 35),
    (4, 'Priya', 26);
CREATE TABLE orders (
    id BIGINT,
    user_id INT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50),
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO orders (user_id, product_name, quantity, amount, status)
VALUES
    (1, 'Laptop', 1, 75000.00, 'DELIVERED'),
    (1, 'Mouse', 2, 1500.00, 'SHIPPED'),
    (2, 'Keyboard', 1, 3000.00, 'PENDING'),
    (3, 'Monitor', 2, 40000.00, 'DELIVERED');
EXPLAIN  SELECT
    u.id,
    u.name,
    o.product_name,
    o.quantity,
    o.amount,
    o.status
FROM users u
JOIN orders o
    ON u.id = o.user_id;
```
```
Inner hash join (o.user_id = u.id) (cost=2.5 rows=4) -> Table scan on o (cost=0.0879 rows=4) -> Hash -> Table scan on u (cost=0.65 rows=4)
```