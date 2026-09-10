# Stream Gatherers
```java
var gatherer = Gatherer.of(
    () -> new int[1],
    (state, value, downstream) -> {
        state[0] += value;
        if (state[0] >= 10) {
            downstream.push(state[0]);
            state[0] = 0;
        }
        return true;
    }
);

list.stream()
    .gather(gatherer)
    .forEach(System.out::println);

```

# Primitive Types in switch & Patterns (Preview)
```java
switch (x) {
    case int i when i < 10 -> System.out.println("Small");
    case int i when i < 100 -> System.out.println("Medium");
    default -> System.out.println("Large");
}
```

# Flexible Constructor Bodies (Preview)
```java
class User {
    User() {
        validate();
        this("guest");
    }

    User(String name) {
        System.out.println("User: " + name);
    }

    void validate() {
        System.out.println("Validating...");
    }
}
```

# Synchronize Virtual Threads Without Pinning (JEP 491)
```java
synchronized void process() {
    // safe even on virtual threads
    doWork();
}
```

# Class File API
```java
ClassFile cf = ClassFile.of();
cf.read(Path.of("MyClass.class"))
  .methods()
  .forEach(m -> System.out.println(m.methodName()));
```

# Simple Source Files & Instance main (Preview)
```java
void main() {
    System.out.println("Hello Java 24!");
}
```

# Post-Quantum Cryptography (ML-KEM / ML-DSA)
```java
KeyPairGenerator kpg =
    KeyPairGenerator.getInstance("ML-DSA");

KeyPair kp = kpg.generateKeyPair();
```

# Ahead-of-Time Class Loading & Linking (JEP 483)
```java
java -XX:AOTClassLinking=on MyApp
```
