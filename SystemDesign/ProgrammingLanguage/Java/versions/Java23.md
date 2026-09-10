# Primitive Types in Patterns, instanceof, and switch (Preview)
```
    static String processInput(Object obj) {
        return switch (obj) {
            case Integer i -> "Integer value: " + i;
            case Double d -> "Double value: " + d;
            default -> "Unknown type";
        };
    }
    
    public static void main(String[] args) {
        System.out.println(processInput(10));    // Integer value: 10
        System.out.println(processInput(5.5));   // Double value: 5.5
    }
```

# Class-File API (Second Preview)
```
    import java.nio.file.Paths;
    import java.io.IOException;
    import jdk.classfile.ClassFile;
    import jdk.classfile.ClassModel;
    
    public class ClassFileExample {
        public static void main(String[] args) throws IOException {
            ClassFile classFile = ClassFile.of();
            ClassModel model = classFile.parse(Paths.get("ExampleClass.class"));
            System.out.println("Class Name: " + model.thisClass());
            model.methods().forEach(method -> System.out.println(" - " + method.methodName()));
        }
    }
```

#  Stream Gatherers (Second Preview)
```
    import java.util.stream.Stream;
    import java.util.stream.Gatherers;
    
    public class StreamGatherersExample {
        public static void main(String[] args) {
            Stream.of("A", "B", "C", "D", "E")
                  .gather(Gatherers.fold(() -> "", (a, b) -> a + b))
                  .forEach(System.out::println);
            // Output: ABCDE
    
            Stream.of("A", "B", "C", "D")
                  .gather(Gatherers.windowFixed(2))
                  .forEach(System.out::println);
            // Output: [A, B]
            //         [C, D]
        }
    }
```

# Flexible Constructor Bodies (Second Preview)
```
    import java.math.BigInteger;
    
    public class FlexibleConstructorsExample extends BigInteger {
        FlexibleConstructorsExample(long value) throws Exception {
            if (value < 0) throw new Exception("Invalid value");
            System.out.println("Initialized with value: " + value);
            super(String.valueOf(value));
        }
    }
```

# Module Import Declarations (Preview)
```
    // module-info.java
    module com.example.mymodule {
        requires java.logging; // Import java.logging module
        exports com.example.mymodule;
    }
```

# Markdown Documentation Comments
```
    /**
     * # My Example Class
     *
     * This class provides an example of Markdown documentation comments.
     *
     * ## Methods
     *
     * - `myMethod()`: Prints a message to the console.
     */
```

# Generational Z Garbage Collector (ZGC) by Default