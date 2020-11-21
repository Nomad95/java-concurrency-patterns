package producer_consumer.blocking_queue;

import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {

    private final BlockingQueue<Integer> blockingQueue;

    public Producer(final BlockingQueue<Integer> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; i++) {
                blockingQueue.put(1);
                System.out.println("Produced resource by " + Thread.currentThread().getId());
            }
        } catch (InterruptedException e) {
            System.out.println("STOPPING PRODUCER");
        }
    }
}
