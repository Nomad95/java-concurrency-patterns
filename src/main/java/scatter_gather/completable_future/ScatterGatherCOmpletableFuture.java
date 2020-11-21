package scatter_gather.completable_future;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Problem is to get three products from 3 different websites
 */
public class ScatterGatherCOmpletableFuture {

    public static void main(String[] args) throws InterruptedException, TimeoutException, ExecutionException {
        final Set<Integer> prices = Collections.synchronizedSet(new HashSet<>());

        final CompletableFuture<Void> task1 = CompletableFuture.runAsync(new Task("asd", "123", prices));
        final CompletableFuture<Void> task2 = CompletableFuture.runAsync(new Task("asd", "123", prices));
        final CompletableFuture<Void> task3 = CompletableFuture.runAsync(new Task("asd", "123", prices));

        final CompletableFuture<Void> allTasks = CompletableFuture.allOf(task1, task2, task3);

        allTasks.get(10, TimeUnit.SECONDS); //wait for all tasks to complete with timeout of 10 sec

        System.out.println(prices);
    }

}

class Task implements Runnable {

    private String url;
    private String productId;
    private Set<Integer> prices;

    public Task(final String url, final String productId, final Set<Integer> prices) {
        this.url = url;
        this.productId = productId;
        this.prices = prices;
    }

    @Override
    public void run() {
        int price = 0;
        //make http call

        prices.add(price);
    }
}