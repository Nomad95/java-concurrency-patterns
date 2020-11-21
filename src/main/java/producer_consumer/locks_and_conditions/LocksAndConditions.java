package producer_consumer.locks_and_conditions;

public class LocksAndConditions {

    private static MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(10);

    static class Producer {

        void produce() {
            queue.put(1);
            System.out.println(Thread.currentThread().getId() + " PRODUCED!!");
        }
    }

    static class Consumer {

        void consume() {
            final Integer take = queue.take();
            System.out.println(Thread.currentThread().getId() + " CONSUMED!!");
        }

    }

    public static void main(String[] args) throws InterruptedException {
        final Producer producer = new Producer();
        final Consumer consumer = new Consumer();

        final Runnable produceTask = () -> {
            for (int i = 0; i < 50; i++) {
                producer.produce();
            }
            System.out.println("stopped producing");
        };

        final Runnable consumeTask = () -> {
            for (int i = 0; i < 50; i++) {
                consumer.consume();
            }
            System.out.println("stopped consuming");
        };

        final Thread produce1 = new Thread(produceTask);
        final Thread produce2 = new Thread(produceTask);
        final Thread consume1 = new Thread(consumeTask);
        final Thread consume2 = new Thread(consumeTask);

        produce1.start();
        produce2.start();
        consume1.start();
        consume2.start();

        produce1.join();
        produce2.join();
        consume1.join();
        consume2.join();

        System.out.println("Queue size: " + queue.size());
    }
}
