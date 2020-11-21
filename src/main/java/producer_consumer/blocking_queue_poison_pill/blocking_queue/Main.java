package producer_consumer.blocking_queue_poison_pill.blocking_queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        final ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(20);

        final ExecutorService executorService = Executors.newFixedThreadPool(8);

        final Integer poison = -1;

        for (int i = 0; i < 2; i++) {
            executorService.submit(new ProducerPoison(queue, poison));
        }

        for (int i = 0; i < 2; i++) {
            executorService.submit(new ConsumerPoison(queue, poison));
        }

        Thread.sleep(5 * 1000);
        executorService.shutdown();
        System.out.println("Stopped Processing! " + queue.size());
    }
}
