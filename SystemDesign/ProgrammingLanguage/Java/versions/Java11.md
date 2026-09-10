# New String Methods
**strip()**: Removes leading and trailing whitespace (similar to trim() but handles Unicode whitespaces better).
**stripLeading()**: and stripTrailing(): Remove leading or trailing whitespace.
**isBlank():** Checks if a string is empty or contains only whitespace.
**lines()**: Converts a String into a Stream of lines.
**repeat(int count):** Repeats the string a specified number of times.

# var in Lambda Parameters
```
    (var a, var b) -> a + b;
```

# HTTP Client API
- Synchronous and asynchronous HTTP requests.
- Support for HTTP/1.1 and HTTP/2.
- Built-in support for WebSocket communication.
```
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://example.com"))
            .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    System.out.println(response.body());
```

# Nest-Based Access Control
This feature simplifies access control for nested classes, enabling private members of a class to be accessed by nested classes without generating synthetic bridge methods.

# JEP 328: Flight Recorder
Java Flight Recorder is a profiling and diagnostics tool for Java applications, allowing developers to capture and analyze runtime data efficiently.

# JEP 331: Low-Overhead Heap Profiling
This provides better tools to profile heap usage with low overhead for monitoring production systems.

# Deprecation of Java EE and CORBA Modules

# Running Java Files Without Compilation
java MyFile.java

# New Files Methods
The java.nio.file.Files class received new methods:
**readString(Path)**
**writeString(Path, String)**
```
    Path path = Files.writeString(Files.createTempFile("demo", ".txt"), "Hello, Java 11!");
    String content = Files.readString(path);
    System.out.println(content);
```

# Z Garbage Collector (ZGC)
Java 11 introduced the Z Garbage Collector, which is designed for low-latency and scalability with minimal pauses.

# Unicode 10 Support
Java 11 includes support for Unicode 10, enhancing support for emojis and other symbols.

# Enhanced Optional
**isEmpty():** Checks if the Optional is empty (the inverse of isPresent()).

# Removal of JavaFX from JDK

# TLS 1.3
Java 11 introduced support for TLS 1.3, improving security and performance for secure communications.

# Epsilon Garbage Collector
The Epsilon GC is a "no-op" garbage collector meant for testing and benchmarking.

# Compact Number Formatting
The java.text.NumberFormat class now supports compact number formatting, ideal for displaying numbers in a human-friendly way.
```
    NumberFormat fmt = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
    System.out.println(fmt.format(1000)); // 1K 
```