package producer_consumer.blocking_queue_poison_pill.blocking_queue;

import java.util.concurrent.BlockingQueue;

public class ProducerPoison implements Runnable {

    private final BlockingQueue<Integer> blockingQueue;

    private final Integer poison;

    public ProducerPoison(final BlockingQueue<Integer> blockingQueue, final Integer poison) {
        this.blockingQueue = blockingQueue;
        this.poison = poison;
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
            Thread.currentThread().interrupt();
        } finally {
            while (true) {
                try {
                    blockingQueue.put(poison);
                    break;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
