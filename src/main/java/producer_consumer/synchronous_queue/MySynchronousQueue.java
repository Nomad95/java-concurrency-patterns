package producer_consumer.synchronous_queue;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MySynchronousQueue<T> {

    private ReentrantLock lock = new ReentrantLock(true);
    private Condition consumerReady = lock.newCondition();
    private Condition producerReady = lock.newCondition();
    AtomicReference<T> obj = new AtomicReference<>();

    public MySynchronousQueue() {
    }

    public void put(T element) {
        lock.lock(); //same as synchronized (obj){}
        try {
            obj.set(element);
            producerReady.signalAll();
            consumerReady.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //make sure this lock is unlocked
            lock.unlock();
        }
    }

    public T take() {
        lock.lock();
        try {
            producerReady.await();
            consumerReady.signalAll();
            return obj.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        return null;
    }

}
