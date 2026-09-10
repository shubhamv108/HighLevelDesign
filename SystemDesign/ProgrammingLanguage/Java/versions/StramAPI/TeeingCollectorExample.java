package SystemDesign.ProgrammingLanguage.Java.versions.StramAPI;

import java.util.List;
import java.util.stream.Collectors;

public class TeeingCollectorExample {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);

        var result = numbers.stream()
                .collect(Collectors.teeing(
                        Collectors.summingInt(i -> i),
                        Collectors.averagingInt(i -> i),
                        (sum, avg) -> "Sum: " + sum + ", Average: " + avg
                                          ));

        System.out.println(result);
    }
}
