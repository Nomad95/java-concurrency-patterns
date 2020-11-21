package misc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Exchanger;

public class ExchangerExample {

    public static void main(String[] args) {
        final Exchanger<String> exchanger = new Exchanger<>();

        Runnable taskA = () -> {
            try {
                String message = exchanger.exchange("from A");
                assert "from B".equals(message);
                System.out.println("i am A and i got: "  + message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        };

        Runnable taskB = () -> {
            try {
                String message = exchanger.exchange("from B");
                assert "from A".equals(message);
                System.out.println("i am B and i got: "  + message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        };

        final CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(
            CompletableFuture.runAsync(taskA),
            CompletableFuture.runAsync(taskB)
        );

        voidCompletableFuture.join();
    }
}
