package producer_consumer.delayed_queue;

import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DelayedQueueConsumerProducer {

    public static class DelayQueueProducer implements Runnable {

        private BlockingQueue<DelayedObject> queue;
        private Integer numberOfElementsToProduce;
        private Integer delayOfEachProducedMessageMilliseconds;

        public DelayQueueProducer(final BlockingQueue<DelayedObject> queue, final Integer numberOfElementsToProduce,
            final Integer delayOfEachProducedMessageMilliseconds) {
            this.queue = queue;
            this.numberOfElementsToProduce = numberOfElementsToProduce;
            this.delayOfEachProducedMessageMilliseconds = delayOfEachProducedMessageMilliseconds;
        }

        @Override
        public void run() {
            for (int i = 0; i < numberOfElementsToProduce; i++) {
                DelayedObject object
                    = new DelayedObject(
                    UUID.randomUUID().toString(), delayOfEachProducedMessageMilliseconds);
                System.out.println("Put object: " + object);
                try {
                    queue.put(object);
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }

    public static class DelayQueueConsumer implements Runnable {
        private BlockingQueue<DelayedObject> queue;
        private Integer numberOfElementsToTake;
        public AtomicInteger numberOfConsumedElements = new AtomicInteger();

        public DelayQueueConsumer(final BlockingQueue<DelayedObject> queue, final Integer numberOfElementsToTake) {
            this.queue = queue;
            this.numberOfElementsToTake = numberOfElementsToTake;
        }

        @Override
        public void run() {
            for (int i = 0; i < numberOfElementsToTake; i++) {
                try {
                    DelayedObject object = queue.take();
                    numberOfConsumedElements.incrementAndGet();
                    System.out.println("Consumer take: " + object);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(8);

        final DelayQueue<DelayedObject> queue = new DelayQueue<>();
        int numberOfElementsToProduce = 30;
        int delayOfEachProducedMessageMilliseconds = 2_000;
        final DelayQueueConsumer delayQueueConsumer = new DelayQueueConsumer(queue, numberOfElementsToProduce);
        final DelayQueueProducer delayQueueProducer = new DelayQueueProducer(queue, numberOfElementsToProduce, delayOfEachProducedMessageMilliseconds);

        executor.submit(delayQueueConsumer);
        executor.submit(delayQueueProducer);

        executor.awaitTermination(5, TimeUnit.SECONDS);
        executor.shutdown();

        assert delayQueueConsumer.numberOfConsumedElements.get() == numberOfElementsToProduce;
    }
}
