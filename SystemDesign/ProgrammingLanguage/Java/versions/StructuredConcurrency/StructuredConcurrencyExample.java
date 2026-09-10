package SystemDesign.ProgrammingLanguage.Java.versions.StructuredConcurrency;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

public class StructuredConcurrencyExample {
    public static void main(String[] args) {
        // Use structured concurrency to run related tasks
        try {
            String result = fetchDataFromServices();
            System.out.println("Result: " + result);
        } catch (ExecutionException | InterruptedException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    public static String fetchDataFromServices() throws ExecutionException, InterruptedException {
        // Combine results from multiple services using structured concurrency
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var serviceA = scope.fork(() -> fetchDataFromServiceA());
            var serviceB = scope.fork(() -> fetchDataFromServiceB());
            var serviceC = scope.fork(() -> fetchDataFromServiceC());

            // Wait for all tasks to complete or fail
            scope.join();
            scope.throwIfFailed();

            // Combine results from services
            String resultA = serviceA.get();
            String resultB = serviceB.get();
            String resultC = serviceC.get();

            return String.format("Results: %s, %s, %s", resultA, resultB, resultC);
        }
    }

    public static String fetchDataFromServiceA() throws InterruptedException {
        // Simulate fetching data
        Thread.sleep(1000);
        System.out.println("Service A completed");
        return "Data from Service A";
    }

    public static String fetchDataFromServiceB() throws InterruptedException {
        // Simulate fetching data
        Thread.sleep(1500);
        System.out.println("Service B completed");
        return "Data from Service B";
    }

    public static String fetchDataFromServiceC() throws InterruptedException {
        // Simulate fetching data with a potential error
        Thread.sleep(2000);
        System.out.println("Service C completed");
        return "Data from Service C";
    }
}