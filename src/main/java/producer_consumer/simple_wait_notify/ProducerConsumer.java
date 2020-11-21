package producer_consumer.simple_wait_notify;

public class ProducerConsumer {

    private static int[] buffer;
    private static int count;

    static final Object lock = new Object();

    static class Producer {
        void produce() {
            synchronized(lock) {
                if (isFull()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                buffer[count++] = 1;
                lock.notifyAll();
            }
        }
    }

    static class Consumer {

        void consume() {
            synchronized(lock) {
                if (isEmpty()) {
                    try {
                        //yield lock object and become in an WAIT state
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                buffer[--count] = 0;
                //notify other threads to exit the wait state and continue working
                lock.notifyAll();
            }
        }

    }

    static boolean isEmpty() {
        return count == 0;
    }

    static boolean isFull() {
        return count == buffer.length;
    }

    public static void main(String[] args) throws InterruptedException {
        buffer = new int[10];
        count = 0;

        final Producer producer = new Producer();
        final Consumer consumer = new Consumer();

        final Runnable produceTask = () -> {
            for (int i = 0; i < 50; i++) {
                producer.produce();
                System.out.println("Produce!");
            }
            System.out.println("stopped producing");
        };

        final Runnable consumeTask = () -> {
            for (int i = 0; i < 50; i++) {
                consumer.consume();
                System.out.println("Consume!");
            }
            System.out.println("stopped consuming");
        };

        final Thread produce = new Thread(produceTask);
        final Thread consume = new Thread(consumeTask);

        produce.start();
        consume.start();

        produce.join();
        consume.join();
        System.out.println("Count is: " + count);
    }
}
