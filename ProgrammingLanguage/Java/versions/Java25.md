# Primitive Types in Patterns (Preview — JEP 507)
```java
Object obj = 42;
if (obj instanceof int i) {
    System.out.println("Primitive int: " + i);
}
```

# Module Import Declarations (JEP 511)
```
module import java.sql.*;  // All exported packages from java.sql
```

# Compact Source Files & Instance Main Methods (JEP 512)
```java
void main() {
    IO.println("Hello, Java 25!");
}

```

# Flexible Constructor Bodies (JEP 513)
```java
class Point {
    Point(int x, int y) {
        check(x, y);
        this(x);  // allowed after initial logic
    }
}
```

# Structured Concurrency (Preview — JEP 505)
```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    // concurrent tasks managed as a unit
}
```

# Scoped Values (JEP 506)
```java
ScopedValue<String> user = ScopedValue.newInstance();
```

# Key Derivation Function API (JEP 510)
```java
SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
```

# Compact Object Headers (JEP 519)
Reduces JVM object header size on 64-bit systems, lowering memory footprint and improving cache locality.
```java
java -XX:+UseCompactObjectHeaders ...
```

# Class File API

# Stream Gathers
```java
class StreamGatherersExample {
    
}
```