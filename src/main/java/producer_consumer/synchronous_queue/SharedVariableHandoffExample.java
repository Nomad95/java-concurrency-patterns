package producer_consumer.synchronous_queue;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SharedVariableHandoffExample {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        AtomicInteger sharedState = new AtomicInteger();
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Runnable producer = () -> {
            Integer producedElement = ThreadLocalRandom
                .current()
                .nextInt();
            sharedState.set(producedElement);
            System.out.println("Productd: " + producedElement);
            countDownLatch.countDown();
        };

        Runnable consumer = () -> {
            try {
                countDownLatch.await();
                Integer consumedElement = sharedState.get();
                System.out.println("Consumed: " + consumedElement);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        };

        executor.execute(consumer);
        executor.execute(producer);

        executor.awaitTermination(500, TimeUnit.MILLISECONDS);
        executor.shutdown();
        assert countDownLatch.getCount() == 0;
    }

}
