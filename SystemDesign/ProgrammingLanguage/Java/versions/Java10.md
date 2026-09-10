# Local Variable Type Inference
Introduces the var keyword to allow local variable type inference, making code more concise and readable.
- The compiler infers the type of the variable based on the assigned value.
- Works only for local variables with initializers, for-loops, and try-with-resources.
```
    var list = List.of("Java", "10", "Features");
    for (var item : list) {
        System.out.println(item);
    }
```
# Unmodifiable Collections Enhancements
```List.copyOf(originalList);```

# Optional.orElseThrow()
```
    Optional<String> optionalValue = Optional.of("Java 10");

    // Using orElseThrow()
    String value = optionalValue.orElseThrow();
```

# Garbage Collector Interface
Introduces a clean and unified interface for different Garbage Collectors, making it easier to experiment with or switch between GC implementations.

# G1 Garbage Collector Improvements
Enhances the G1 GC to improve performance and reduce Full GC pauses by enabling parallel processing for Full GC.

# Application Class-Data Sharing (AppCDS)\
Extends Class-Data Sharing (CDS) to allow application classes to be included in the shared archive.
- Improves startup time and reduces memory usage.
- Enables sharing of common class metadata across Java Virtual Machine (JVM) instances.
```
    # Step 1: Create a classlist file
    java -Xshare:off -XX:DumpLoadedClassList=classlist.txt -cp myapp.jar com.example.MyApp
    
    # Step 2: Create a shared archive
    java -Xshare:dump -XX:SharedClassListFile=classlist.txt -XX:SharedArchiveFile=app-cds.jsa -cp myapp.jar
    
    # Step 3: Use the shared archive to run the application
    java -Xshare:on -XX:SharedArchiveFile=app-cds.jsa -cp myapp.jar com.example.MyApp
```        

# Consolidation of JDK Repositories
Combines multiple repositories of the JDK into a single repository to simplify development and reduce maintenance overhead.

# Heap Allocation on Alternative Memory Devices
Java 10 introduced the ability to allocate the Java heap on alternative memory devices, such as NV-DIMMs.

# Root Certificates (OpenJDK)
includes a default set of root Certification Authority (CA) certificates in the OpenJDK build.

# Thread-Local Handshakes
Thread-Local Handshakes is a JVM-level enhancement introduced in Java 10 to allow operations to be performed on individual 
threads without requiring global safepoints (pausing all application threads). 
This improvement mainly benefits JVM developers and enhances the performance of operations 
like deoptimization, biased locking revocation, and thread suspension.
