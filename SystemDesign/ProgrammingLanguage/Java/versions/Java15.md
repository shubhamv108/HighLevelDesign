# Text Blocks (Standard Feature)

# Sealed Classes (Preview Feature)
JEP 360: Sealed Classes allow developers to control which classes can extend or implement a class or interface, 
providing better control over inheritance.

## use cases
1. Representing Domain Models with Controlled Hierarchies
    ```
        public sealed class Transaction permits Deposit, Withdrawal {
            // common transaction logic
        }
        
        public final class Deposit extends Transaction {
        // deposit-specific logic
        }
        
        public final class Withdrawal extends Transaction {
        // withdrawal-specific logic
        }
    ```
2. Enforcing Strict API Contracts
3. Maintaining Extensibility in Secure Systems
   ```
    public sealed class Role permits Admin, User, Guest {
    // common role logic
    }
   ```
4. Reducing Complexity in State Machines
    ```
        public sealed class TaskState permits Pending, InProgress, Completed, Cancelled {
             // common state logic
        }
        
        public final class Pending extends TaskState {
        // pending-specific logic
        }
        
        public final class InProgress extends TaskState {
        // in-progress-specific logic
        }
   ```
5. Better Pattern Matching
    - sealed classes make it easier to work with switch statements or instanceof checks
   ```
    public static String processShape(Shape shape) {
    return switch (shape) {
        case Circle c -> "Processing a circle";
        case Square s -> "Processing a square";
        case Triangle t -> "Processing a triangle";
        default -> throw new IllegalArgumentException("Unknown shape");
    };
    }
   ```
### In summary, sealed classes allow you to:

- Control class hierarchies more strictly.
- Prevent unwanted or accidental inheritance.
- Enable more efficient pattern matching.
- Ensure consistency and predictability in your codebase.  
  They're particularly useful when you're designing a closed set of related types and want to enforce rules about how they can be extended or used.

```
public abstract sealed class Shape
    permits Circle, Rectangle, Square {}

public final class Circle extends Shape {}
public final class Rectangle extends Shape {}
public final class Square extends Shape {}
```

# Hidden Classes
- JEP 371: Hidden Classes are classes that are not discoverable by the standard classloader and are meant to be used by frameworks to generate dynamic proxies or other use cases at runtime.
- Key Use Cases: Enhances frameworks like ByteBuddy and ASM for dynamically defining classes.

# Data Modeling with a Fixed Set of Values
