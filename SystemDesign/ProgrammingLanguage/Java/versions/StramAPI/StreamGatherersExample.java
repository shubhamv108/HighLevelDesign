package SystemDesign.ProgrammingLanguage.Java.versions.StramAPI;

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
