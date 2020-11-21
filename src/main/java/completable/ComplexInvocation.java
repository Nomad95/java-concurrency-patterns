package completable;

import java.util.concurrent.*;

public class ComplexInvocation {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final ExecutorService executorService1 = Executors.newFixedThreadPool(2);
        final ExecutorService executorService2 = Executors.newFixedThreadPool(6);

        //pass task to executor
        for (int i = 0; i < 10; i++) {
            final CompletableFuture<Void> cf = CompletableFuture.supplyAsync(() -> "Koty ", executorService1)
                .thenApplyAsync(s -> s.concat("Liżą "), executorService2)
                .thenApplyAsync(s -> s.concat("Masło"), executorService2)
                .thenAcceptAsync(System.out::println, executorService1);
        }

    }


}
