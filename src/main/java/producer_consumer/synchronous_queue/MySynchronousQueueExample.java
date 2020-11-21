package producer_consumer.synchronous_queue;

import java.util.concurrent.*;

public class MySynchronousQueueExample {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        final MySynchronousQueue<Integer> queue = new MySynchronousQueue<>();

        Runnable producer = () -> {
            Integer producedElement = ThreadLocalRandom
                .current()
                .nextInt();
            queue.put(producedElement);
            System.out.println("Productd: " + producedElement);
        };

        Runnable consumer = () -> {
            Integer consumedElement = queue.take();
            System.out.println("Consumed: " + consumedElement);
        };

        executor.execute(consumer);
        executor.execute(producer);

        executor.awaitTermination(500, TimeUnit.MILLISECONDS);
        executor.shutdown();
    }

}
