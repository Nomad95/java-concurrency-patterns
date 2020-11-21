package reader_writer_problem.semaphore_solution;

import java.util.concurrent.Semaphore;

public class Writer implements Runnable {
    private final Semaphore writerSemaphore;
    private final Semaphore serviceSemaphore;

    public Writer(final Semaphore writerSemaphore, final Semaphore serviceSemaphore) {
        this.writerSemaphore = writerSemaphore;
        this.serviceSemaphore = serviceSemaphore;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                serviceSemaphore.acquire();
                writerSemaphore.acquire();
                serviceSemaphore.release();

                System.out.println(Thread.currentThread().getName() + " has written something!");
                writerSemaphore.release();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
