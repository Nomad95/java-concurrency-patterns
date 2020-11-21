package reader_writer_problem.semaphore_solution_2;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

public class Resource {
    private final Lock readLock;
    private final Lock writeLock;
    private AtomicInteger readersCounter;
    private final Semaphore maxReaders = new Semaphore(3);

    public Resource(final Lock readLock, final Lock writeLock, AtomicInteger readersCounter) {
        this.readLock = readLock;
        this.writeLock = writeLock;
        this.readersCounter = readersCounter;
    }

    public int read() {
        try {
            maxReaders.acquire();
            final int available = readersCounter.incrementAndGet();
            if (available == 1) {
                writeLock.lock();
            }
            readLock.lock();
            System.out.println("Reading...");
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
            maxReaders.release();
            final int available = readersCounter.decrementAndGet();
            if (available == 0) {
                writeLock.unlock();
            }
        }

        return ThreadLocalRandom.current().nextInt();
    }

    public void write() {
        try {
            writeLock.lock();
            System.out.println("Writing...");
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }
}
