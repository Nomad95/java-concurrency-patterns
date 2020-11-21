package producer_consumer.locks_and_conditions;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyBlockingQueue<T> {

    private Queue<T> queue;
    private int max = 16;
    private ReentrantLock lock = new ReentrantLock(true); //Fair lock is costly!
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();

    public MyBlockingQueue(final int max) {
        queue = new LinkedList<>();
        this.max = max;
    }

    public void put(T element) {
        lock.lock(); //same as synchronized (obj){}
        try {
            while (queue.size() == max) { //need to be while because
                notFull.await();//wait fot this condition to be triggered
            }
            queue.add(element);
            notEmpty.signalAll(); //notify to consumers that item has ben put and queue is not empty anymore
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
            while (queue.size() == 0) {
                notEmpty.await();
            }
            final T remove = queue.remove();
            notFull.signalAll(); //signal producers that item has been taken and the queue is not full anymore
            return remove;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        return null;
    }

    public int size() {
        return queue.size();
    }
}
