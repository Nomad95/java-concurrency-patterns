package scatter_gather.count_down_latch;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Problem is to get three products from 3 different websites
 */
public class ScatterGatherCountDownLatch {

    public static void main(String[] args) throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(4);

        final Set<Integer> prices = Collections.synchronizedSet(new HashSet<>());

        final CountDownLatch latch = new CountDownLatch(3); //await blocks thread until CDL reaches zero

        executorService.submit(new Task("asd", "123", prices, latch));
        executorService.submit(new Task("asd", "123", prices, latch));
        executorService.submit(new Task("asd", "123", prices, latch));

        latch.await(10, TimeUnit.SECONDS); //wait for countdown to zero or timeout/ whatever comes first

        System.out.println(prices);
    }

}


class Task implements Runnable {

    private String url;
    private String productId;
    private Set<Integer> prices;
    private CountDownLatch latch;

    public Task(final String url, final String productId, final Set<Integer> prices, final CountDownLatch latch) {
        this.url = url;
        this.productId = productId;
        this.prices = prices;
        this.latch = latch;
    }

    @Override
    public void run() {
        int price = 0;
        //make http call

        prices.add(price);
        latch.countDown(); //add price and count down
    }
}