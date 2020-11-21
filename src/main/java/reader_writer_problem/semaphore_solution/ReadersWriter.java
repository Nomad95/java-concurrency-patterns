package reader_writer_problem.semaphore_solution;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class ReadersWriter {

    public static void main(String[] args) throws InterruptedException {
        final Semaphore serviceSemaphore = new Semaphore(1);
        final Semaphore writerSemaphore = new Semaphore(1, true);//fair semaphore to prevent writer thread starvation
        final Semaphore readerSemaphore = new Semaphore(1, true);//threads need to read simultaneously
        final AtomicInteger readersCount = new AtomicInteger(0);

        final ExecutorService readers = Executors.newFixedThreadPool(3);

        List<Future<?>> readersTasks = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            final Future<?> reader = readers.submit(new Reader(writerSemaphore, readerSemaphore, serviceSemaphore, readersCount));
            readersTasks.add(reader);
        }

        final ExecutorService writers = Executors.newFixedThreadPool(1);
        final Future<?> writer1 = writers.submit(new Writer(writerSemaphore, serviceSemaphore));

        Thread.sleep(5000);
        writer1.cancel(true);
        readersTasks.forEach(t -> t.cancel(true));

        readers.shutdown();
        writers.shutdown();
    }
}
