package reader_writer_problem.semaphore_solution;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Reader implements Runnable {
    private final Semaphore writerSemaphore;
    private final Semaphore readerSemaphore;
    private final Semaphore serviceQueueSemaphore;
    private final AtomicInteger readCount;

    public Reader(final Semaphore writerSemaphore, final Semaphore readerSemaphore, final Semaphore serviceQueueSemaphore,
        final AtomicInteger readCount) {
        this.writerSemaphore = writerSemaphore;
        this.readerSemaphore = readerSemaphore;
        this.serviceQueueSemaphore = serviceQueueSemaphore;
        this.readCount = readCount;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                serviceQueueSemaphore.acquire();
                readerSemaphore.acquire();
                final int count = readCount.incrementAndGet();
                if (count == 1) {
                    writerSemaphore.acquire();
                }
                serviceQueueSemaphore.release();
                readerSemaphore.release();


                System.out.println(Thread.currentThread().getName() + " has read something!");

                readerSemaphore.acquire();
                final int countAfter = readCount.decrementAndGet();
                if (countAfter == 0) {
                    writerSemaphore.release();
                }
                readerSemaphore.release();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
