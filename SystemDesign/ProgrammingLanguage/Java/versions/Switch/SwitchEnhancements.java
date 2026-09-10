package SystemDesign.ProgrammingLanguage.Java.versions.Switch;

// Define a sealed interface with subclasses
sealed interface Shape permits Circle, Rectangle, Triangle {}

record Circle(double radius) implements Shape {}
record Rectangle(double length, double width) implements Shape {}
record Triangle(double base, double height) implements Shape {}

public class SwitchEnhancements {

    public static void main(String[] args) {
        // Example 1: Switch as an expression
        String day = "MONDAY";
        int dayValue = switch (day) {
            case "MONDAY", "FRIDAY", "SUNDAY" -> 1; // Arrow syntax
            case "TUESDAY" -> 2;
            case "THURSDAY", "SATURDAY" -> 3;
            case "WEDNESDAY" -> {
                System.out.println("Midweek day!");
                yield 4; // Using yield to return a value
            }
            default -> throw new IllegalArgumentException("Invalid day: " + day);
        };
        System.out.println("Day value: " + dayValue);

        // Example 2: Pattern matching in switch
        Object obj = "Hello, World!";
        String result = switch (obj) {
            case String s -> "It's a string of length: " + s.length();
            case Integer i -> "It's an integer: " + i;
            case null -> "It's null";
            default -> "Unknown type";
        };
        System.out.println(result);

        // Example 3: Handling null explicitly
        String input = null;
        String output = switch (input) {
            case null -> "Input is null";
            case "HELLO" -> "Greeting detected";
            default -> "Some other string";
        };
        System.out.println(output);

        // Example 4: Switch with sealed types
        Shape shape = new Circle(5.0);
        String shapeDescription = switch (shape) {
            case Circle c -> "Circle with radius: " + c.radius();
            case Rectangle r -> "Rectangle with dimensions: " + r.length() + "x" + r.width();
            case Triangle t -> "Triangle with base: " + t.base() + " and height: " + t.height();
        };
        System.out.println(shapeDescription);

        // Example 5: Constant case labels
        final int X = 1, Y = 2, Z = 3; // Constants
        int value = 2;
        String constantLabelOutput = switch (value) {
            case X -> "X was selected";
            case Y -> "Y was selected";
            case Z -> "Z was selected";
            default -> "Unknown value";
        };
        System.out.println(constantLabelOutput);
    }
}
