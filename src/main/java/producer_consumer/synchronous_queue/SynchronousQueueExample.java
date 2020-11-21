package producer_consumer.synchronous_queue;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SynchronousQueueExample {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        final SynchronousQueue<Integer> queue = new SynchronousQueue<>();

        Runnable producer = () -> {
            Integer producedElement = ThreadLocalRandom
                .current()
                .nextInt();
            try {
                queue.put(producedElement);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Productd: " + producedElement);
        };

        Runnable consumer = () -> {
            try {
                Integer consumedElement = queue.take();
                System.out.println("Consumed: " + consumedElement);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        };

        executor.execute(consumer);
        executor.execute(producer);

        executor.awaitTermination(500, TimeUnit.MILLISECONDS);
        executor.shutdown();
        assert queue.size() == 0;
    }

}
