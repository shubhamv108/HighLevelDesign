# Scoped Values (Incubator)
# Record Patterns (Second Preview)
# Pattern Matching for switch (Fourth Preview)
```
    public class PatternSwitchExample {
        public static void main(String[] args) {
            Object obj = 123;
    
            String result = switch (obj) {
                case Integer i -> "Integer: " + i;
                case String s -> "String: " + s;
                case null -> "Null value";
                default -> "Other: " + obj;
            };
    
            System.out.println(result);
        }
    }
```

# Foreign Function & Memory API (Third Preview)
# Virtual Threads (Second Preview)
# Structured Concurrency (Incubator)
```
    import java.util.concurrent.StructuredTaskScope;
    
    public class StructuredConcurrencyExample {
        public static void main(String[] args) throws InterruptedException {
            try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
                var future1 = scope.fork(() -> fetchDataFromServiceA());
                var future2 = scope.fork(() -> fetchDataFromServiceB());
    
                scope.join(); // Wait for all tasks
                scope.throwIfFailed(); // Rethrow exceptions if any
    
                String result = future1.resultNow() + " and " + future2.resultNow();
                System.out.println("Result: " + result);
            }
        }
    
        static String fetchDataFromServiceA() {
            return "Data from Service A";
        }
    
        static String fetchDataFromServiceB() {
            return "Data from Service B";
        }
    }
```
# Improved Garbage Collectors
Java 20 brought minor improvements to garbage collectors, including enhancements to the ZGC and G1 collectors for better performance and scalability.

