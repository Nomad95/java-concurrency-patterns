package reader_writer_problem.semaphore_solution_2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ReadersWriter2 {

    public static void main(String[] args) throws InterruptedException {
        final ReentrantLock writeLock = new ReentrantLock(true);
        final ReentrantLock readLock = new ReentrantLock(true);

        final Resource resource = new Resource(readLock, writeLock, new AtomicInteger(0));

        final ExecutorService readers = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 50; i++) {
            readers.submit(resource::read);
            readers.submit(resource::read);
            readers.submit(resource::write);
            readers.submit(resource::read);
        }

        Thread.sleep(5000);

        readers.shutdown();
    }
}
