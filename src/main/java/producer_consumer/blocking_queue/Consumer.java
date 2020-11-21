package producer_consumer.blocking_queue;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {

    private final BlockingQueue<Integer> blockingQueue;

    public Consumer(final BlockingQueue<Integer> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 40; i++) {
                final Integer take = blockingQueue.take();
                System.out.println("Resource Consumed by " + Thread.currentThread().getId());
            }
        } catch (InterruptedException e) {
            System.out.println("STOPPING CONSUMER");
        }
    }
}
