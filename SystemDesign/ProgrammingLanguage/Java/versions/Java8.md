# LambdaExpressions
Enables functional programming by allowing functions to be passed as parameters.

# Stream API
Facilitates functional-style operations on collections (e.g., filter, map, reduce).

# Default Methods in Interfaces
Allows interfaces to have method implementations, **enabling backward compatibility for interfaces.**

# Functional Interfaces
interfaces with a single abstract method, which can be implemented using lambda expressions.
**Predicate<T>**: Represents a condition (boolean-valued function).
**Consumer<T>**: Represents an operation on a single input.
**Supplier<T>**: Supplies a result without input.
**Function<T, R>**: Maps input to output.

# Optional Class
Helps avoid NullPointerException by representing optional values.

# Date and Time API (java.time)
```
    LocalDate date = LocalDate.now();
    LocalTime time = LocalTime.now();
    LocalDateTime dateTime = LocalDateTime.now();
```

# Method References
Shortens the syntax for a lambda expression by referencing existing methods.

# Nashorn JavaScript Engine
Provides a JavaScript runtime in Java, allowing JavaScript to be executed from within Java applications.

# Base64 Encoding and Decoding
```Base64.getEncoder().encodeToString("Hello".getBytes());```

# Parallel Arrays (via Arrays.parallelSort)
Arrays.parallelSort(numbers);

# CompletableFuture
Enhances asynchronous programming by providing a more functional way to handle future tasks.
```
    CompletableFuture.supplyAsync(() -> "Hello")
                     .thenApply(result -> result + " World")
                     .thenAccept(System.out::println);
```