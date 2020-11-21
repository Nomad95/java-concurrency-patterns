package producer_consumer.blocking_queue_poison_pill.blocking_queue;

import java.util.concurrent.BlockingQueue;

public class ConsumerPoison implements Runnable {

    private final BlockingQueue<Integer> blockingQueue;
    private final Integer poison;

    public ConsumerPoison(final BlockingQueue<Integer> blockingQueue, final int poison) {
        this.blockingQueue = blockingQueue;
        this.poison = poison;
    }

    @Override
    public void run() {
        try {
            while (true) {
                final Integer take = blockingQueue.take();
                System.out.println("Resource Consumed by " + Thread.currentThread().getId());
                if (take.equals(poison)) {
                    break;
                }
            }
        } catch (InterruptedException e) {
            System.out.println("STOPPING CONSUMER");
            Thread.currentThread().interrupt();
        }
    }
}
