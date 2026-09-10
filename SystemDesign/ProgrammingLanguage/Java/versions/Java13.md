# Text Blocks (Preview Feature)
Text blocks provide a way to handle multi-line string literals more easily, without excessive escape sequences.

```
    String json = """
    {
        "name": "John",
        "age": 30
    }
    """;
```

# Switch Expressions (Second Preview)
```
    int result = switch (day) {
        case MONDAY, FRIDAY, SUNDAY -> 6;
        case TUESDAY                -> 7;
        case THURSDAY, SATURDAY     -> 8;
        case WEDNESDAY              -> 9;
        default -> throw new IllegalArgumentException("Unexpected day: " + day);
    };
```

# Dynamic CDS Archives
- Class Data Sharing (CDS) allows classes to be pre-loaded and shared among JVMs to reduce startup time and memory usage.
- With Dynamic CDS Archives, the JDK automatically generates a CDS archive during the application’s first execution, making the process more user-friendly.

# Reimplementation of the Legacy Socket API
The underlying implementation of the socket API was replaced with a simpler, more modern, and maintainable implementation.
This change doesn't affect existing applications but improves maintainability and scalability.

# ZGC Enhancements
Z Garbage Collector (ZGC) introduced in Java 11 received updates to support memory uncommitment, which allows ZGC to return unused heap memory to the operating system.

# FileSystems New System Property
A new system property, jdk.nio.file.disableFastPath, was added to allow disabling the "fast path" for file operations in certain environments.

# Deprecated and Removed Features:
- Removal of the Solaris/Sparc and Solaris/x64 ports.
- Deprecation of the RMI Activation for future removal.