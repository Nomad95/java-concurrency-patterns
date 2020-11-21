package completable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SimpleFuture {

    public Future<String> calculateAsync() throws InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(500);
            completableFuture.complete("Hello");
            return null;
        });

        return completableFuture;
    }

    public Future<String> calculateAsyncWithCancellation() throws InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(500);
            completableFuture.cancel(false);
            return null;
        });

        return completableFuture;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final SimpleFuture program = new SimpleFuture();
        final Future<String> stringFuture = program.calculateAsync();

        System.out.println("Doing some work in main thread!");
        Thread.sleep(100);

        final String s = stringFuture.get();

        System.out.println("Aand we get value computed in another thread: " + s);

        final CompletableFuture<String> s2Future = CompletableFuture.completedFuture("Henlo");
        System.out.println("Alternatively get already known value that never blocks thread! " + s2Future.get());

        final Future<String> cancelledFuture = program.calculateAsyncWithCancellation();
        System.out.println("Throws when cancelled ang get invoked: ");
        cancelledFuture.get();

    }


}
