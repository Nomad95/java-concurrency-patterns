package misc;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLock {

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    private ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    private ReentrantReadWriteLock.ReadLock readLock = lock.readLock();

    private void readResource() {
        readLock.lock();
        //allows many threads to read at the same time to boost the performance
        readLock.unlock();
    }

    private void writeResource() {
        writeLock.lock();
        //works same like normal reentrantLock
        writeLock.unlock();
    }
}
