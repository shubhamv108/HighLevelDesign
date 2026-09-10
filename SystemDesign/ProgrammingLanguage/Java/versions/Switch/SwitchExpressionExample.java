package SystemDesign.ProgrammingLanguage.Java.versions.Switch;

public class SwitchExpressionExample {
    public static void main(String[] args) {
        int day = 1;

        String dayType = switch (day) {
            case 1, 7 -> "Weekend";
            case 2, 3, 4, 5, 6 -> "Weekday";
            default -> throw new IllegalArgumentException("Invalid day: " + day);
        };

        System.out.println("Day type: " + dayType);
    }
}
