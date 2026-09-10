package SystemDesign.ProgrammingLanguage.Java.versions;

public class ThreadLocalHandshakeDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Thread 1 is running...");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("Thread 1 stopped.");
        });

        Thread thread2 = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Thread 2 is running...");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("Thread 2 stopped.");
        });

        thread1.start();
        thread2.start();

        // Pause for 2 seconds
        Thread.sleep(2000);

        // Interrupt only Thread 1
        System.out.println("Stopping Thread 1...");
        thread1.interrupt();

        // Pause for 2 more seconds
        Thread.sleep(2000);

        // Interrupt Thread 2
        System.out.println("Stopping Thread 2...");
        thread2.interrupt();

        thread1.join();
        thread2.join();

        System.out.println("All threads stopped.");
    }
}
