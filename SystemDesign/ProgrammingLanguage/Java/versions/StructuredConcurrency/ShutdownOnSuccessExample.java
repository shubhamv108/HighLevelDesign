package SystemDesign.ProgrammingLanguage.Java.versions.StructuredConcurrency;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

public class ShutdownOnSuccessExample {
    public static void main(String[] args) {
        try {
            String result = fetchFirstSuccessfulResult();
            System.out.println("First successful result: " + result);
        } catch (ExecutionException | InterruptedException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    public static String fetchFirstSuccessfulResult() throws ExecutionException, InterruptedException {
        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
            var taskA = scope.fork(() -> taskThatMightSucceed("Task A", 3000));
            var taskB = scope.fork(() -> taskThatMightSucceed("Task B", 2000));
            var taskC = scope.fork(() -> taskThatMightSucceed("Task C", 1000));

            scope.join(); // Wait for one task to succeed
            return scope.result(); // Get the first successful result
        }
    }

    public static String taskThatMightSucceed(String taskName, long duration) throws InterruptedException {
        Thread.sleep(duration); // Simulate work
        System.out.println(taskName + " completed");
        return taskName + " result";
    }
}

