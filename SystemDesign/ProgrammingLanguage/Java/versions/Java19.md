# Record Patterns (Preview)
This feature enhances the Java programming language's ability to destructure record types (introduced in Java 14) 
with a more concise syntax. It allows for the use of pattern matching with records, enabling easier access to components 
and simplifying code.
```
    public class Main {
        record Point(int x, int y) {}
        
        public static void main(String[] args) {
            Object obj = new Point(1, 2);
            
            if (obj instanceof Point(var x, var y)) {
                System.out.println("Point x = " + x + ", y = " + y);
            }
        }
    }
```

# JEP 422: Linux/AArch64 Port
This feature does not directly change how code is written, but it improves Java performance and stability on Linux AArch64. 
If you’re running on this architecture, Java 19 will natively support it.

# JEP 424: Foreign Function & Memory API (Incubator)
This incubator feature aims to provide a safer and more efficient way for Java programs to interact with native code 
(e.g., C, C++) by accessing memory outside the Java heap and calling functions in native libraries, without relying on 
the Java Native Interface (JNI).
```
    import jdk.incubator.foreign.*;
    public class Main {
        public static void main(String[] args) {
            try (MemorySegment segment = MemorySegment.allocateNative(4)) {
                segment.set(ValueLayout.JAVA_INT, 0, 123);
                int value = segment.get(ValueLayout.JAVA_INT, 0);
                System.out.println("Memory value: " + value);
            }
        }
    }
```

# JEP 427: Pattern Matching for Switch (Preview)
This example uses switch with pattern matching, making it easier to match and destructure values directly within the switch cases.
```
    public class Main {
        public static void main(String[] args) {
            Object obj = 42;
    
            String result = switch (obj) {
                case Integer i -> "Integer: " + i;
                case String s -> "String: " + s;
                default -> "Unknown type";
            };
    
            System.out.println(result);
        }
    }
```

# JEP 428: Structured Concurrency (Incubator)
The Structured Concurrency API simplifies working with threads by automatically managing the lifecycle of virtual threads. 
Here, Executors.newVirtualThreadPerTaskExecutor() creates a new virtual thread for a task.
```
    import java.util.concurrent.*;
    
    public class Main {
        public static void main(String[] args) throws InterruptedException, ExecutionException {
            try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                var future = executor.submit(() -> "Hello from Virtual Thread!");
                System.out.println(future.get());
            }
        }
    }
```

# JEP 429: Scoped Values (Incubator)
    Scoped Values (introduced in Java 19 as an incubator feature) provide a mechanism 
    to share immutable values across threads in a controlled, efficient, and thread-safe manner.
    efficient alternative to ThreadLocal.

    - Thread safety: Unlike ThreadLocal, ScopedValue is immutable, making it inherently thread-safe.
    - Efficiency: Scoped values avoid some of the pitfalls of ThreadLocal, such as memory leaks in thread pools.
    - Better scope control: Scoped values are tied to specific code regions, ensuring that their accessibility is limited 
        to only where they are explicitly declared.
    * ScopedValue: A container that holds an immutable value.
    * ScopedValue.Scope: Represents the scope of the value. The value can only be accessed when within this scope.
    
## UseCases
    - Share immutable context data (e.g., user information, request metadata) across threads in a structured and safe way.
    - Pass contextual information (like request IDs) for consistent logging without relying on ThreadLocal.
    - Manage access tokens, user roles, or other security-related data tied to specific tasks or requests.
                                                                                                                                                                                                                                                                            
Scoped values allow data to be scoped to a specific thread context. 
In this example, a ScopedValue is created and accessed within its scope.
```
    import jdk.incubator.concurrent.*;
    public class Main {
        public static void main(String[] args) {
            var scope = new ScopedValue<>("ScopedValue", "Hello, World!");
            
            try (var scopeContext = ScopedValue.asContext(scope)) {
                System.out.println(scope.value());
            }
        }
    }
```
```
    import jdk.incubator.concurrent.ScopedValue;
    
    public class ScopedValueExample {
    
        // Define a ScopedValue instance (this acts like a "key")
        private static final ScopedValue<String> USERNAME = ScopedValue.newInstance();
    
        public static void main(String[] args) {
            // Define a scope and bind a value to it
            ScopedValue.where(USERNAME, "JohnDoe")
                    .run(() -> {
                        // Access the ScopedValue within its scope
                        System.out.println("Current User: " + USERNAME.get());
                        
                        // Nested scope
                        ScopedValue.where(USERNAME, "JaneDoe")
                                .run(() -> System.out.println("Nested User: " + USERNAME.get()));
                        
                        // Back to the original scope
                        System.out.println("Back to Original User: " + USERNAME.get());
                    });
    
            // Attempting to access ScopedValue outside its scope results in an error
            try {
                System.out.println(USERNAME.get());
            } catch (IllegalStateException e) {
                System.out.println("Cannot access ScopedValue outside its scope!");
            }
        }
    }
```

# JEP 376: ZGC: Concurrent Thread-Stack Processing (zgc)
This enables the Z Garbage Collector (ZGC) for your application, 
and Java 19 now processes thread stacks concurrently, improving performance during garbage collection.
```
    java -XX:+UseZGC -Xms4g -Xmx4g -jar YourApplication.jar
```
