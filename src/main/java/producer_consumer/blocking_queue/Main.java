package producer_consumer.blocking_queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        final ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(20);

        final ExecutorService executorService = Executors.newFixedThreadPool(8);

        for (int i = 0; i < 2; i++) {
            executorService.submit(new Producer(queue));
        }

        for (int i = 0; i < 5; i++) {
            executorService.submit(new Consumer(queue));
        }

        Thread.sleep(5 * 1000);
        executorService.shutdown();
        System.out.println("Stopped Processing! " + queue.size());
    }
}
 