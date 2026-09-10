## **_features, like Records and Pattern Matching for instanceof_**


# Pattern Matching for instanceof (Preview)
- Simplifies the use of the instanceof operator.
- Removes the need for explicit casting after type-checking.
```
    if (obj instanceof String s) {
        System.out.println(s.toLowerCase());
    }

```

# JEP 343: Packaging Tool (Incubator)
- Provides a jpackage tool for creating native installers (e.g., .exe, .dmg, .deb).
- Helps developers package Java applications into platform-specific packages.
```
jpackage --input input-dir --name MyApp --main-jar app.jar --type dmg
```

# Switch Expressions (Standard)
- Switch expressions, introduced as a preview in Java 12 and 13, became a standard feature in Java 14.
- Allows switch to be used as both a statement and an expression.
- Reduces boilerplate and ensures safer control flow.
```
    int day = 3;
    String dayType = switch (day) {
        case 1, 7 -> "Weekend";
        case 2, 3, 4, 5, 6 -> "Weekday";
        default -> throw new IllegalArgumentException("Invalid day");
    };
    System.out.println(dayType);
```

# JEP 368: Text Blocks (Second Preview)

# JEP 349: JFR Event Streaming
- Enables continuous monitoring and diagnostics of JVM-based applications using Java Flight Recorder (JFR).
- Allows for real-time streaming of JFR events.

# JEP 370: Foreign-Memory Access API (Incubator)
- Provides a low-level API for accessing and manipulating foreign memory outside of the Java heap.
- Useful for handling large datasets and interfacing with native libraries.

# JEP 364: ZGC on macOS
- Extends the Z Garbage Collector (ZGC) to macOS.
- ZGC is designed for sub-millisecond pause times

# Helpful NullPointerExceptions
Improves the error messages for NullPointerException (NPE) by showing precisely which variable was null

# JEP 359: Records (Preview)
- Introduces Records as a new type for modeling immutable data.
- Automatically generates boilerplate code like getters, equals(), hashCode(), and toString().
```
    record Point(int x, int y) {}

    Point p = new Point(1, 2);
    System.out.println(p.x()); // Output: 1
    System.out.println(p.y()); // Output: 2
```

# Other Enhancements
Improved Garbage Collection: Enhancements to G1 and other garbage collectors.
Deprecations: Some APIs and features were marked as deprecated for removal.
JVM Performance Improvements: Various optimizations were introduced in the JVM.
