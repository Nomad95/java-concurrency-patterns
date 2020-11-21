package producer_consumer.semaphores;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class SemaphoresProducerConsumer {

    public static void main(String[] args) throws InterruptedException {
        final SemaphoreQueue<Integer> queueue = new SemaphoreQueue<>(17);

        final Thread prod1 = new Thread(new Producer(queueue));
        final Thread prod2 = new Thread(new Producer(queueue));

        final Thread cons1 = new Thread(new Consumer(queueue));
        final Thread cons2 = new Thread(new Consumer(queueue));

        cons2.start();
        cons1.start();

        prod1.start();
        prod2.start();

        cons1.join();
        cons2.join();
        prod1.join();
        prod2.join();

        System.out.println("Done queue size= " + queueue.size());
    }
}

class SemaphoreQueue<T> {
    private Queue<T> queue;
    private ReentrantLock lock = new ReentrantLock(true); //Fair lock is costly!
    private Semaphore semaphoreConsumer = new Semaphore(0);
    private Semaphore semaphoreProducer;

    public SemaphoreQueue(final int max) {
        queue = new LinkedList<>();
        semaphoreProducer = new Semaphore(max);
    }

    public void put(T element) throws InterruptedException {
        semaphoreProducer.acquire();

        lock.lock();
        queue.add(element);
        lock.unlock();

        semaphoreConsumer.release();
    }

    public T take() throws InterruptedException {
        semaphoreConsumer.acquire();

        lock.lock();
        final T remove = queue.remove();
        lock.unlock();

        semaphoreProducer.release();
        return remove;
    }

    public int size() {
        return queue.size();
    }
}

class Producer implements Runnable {

    private final SemaphoreQueue<Integer> queue;

    Producer(final SemaphoreQueue<Integer> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            for (int i = 0; i < 30; i++) {
                queue.put(new Integer(i));
                System.out.println("produced: " + i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable {

    private final SemaphoreQueue<Integer> queue;

    Consumer(final SemaphoreQueue<Integer> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            for (int i = 0; i < 30; i++) {
                final Integer take = queue.take();
                System.out.println("consumed: " + take);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
