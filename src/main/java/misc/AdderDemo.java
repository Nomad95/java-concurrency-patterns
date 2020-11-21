package misc;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdderDemo {

    public static void main(String[] args) {
        LongAdder longAdder = new LongAdder();//lng adder is more effective than AtomicLong because every thread has its own counter
        // that doesnt need to be synchronized. Synchronization occurs only on summing

        final ExecutorService executorService = Executors.newFixedThreadPool(16);

        Stream.generate(() -> executorService
            .submit(new Task(longAdder)))
            .limit(100)
            .forEach(e -> {
                try {
                    e.get();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                } catch (ExecutionException executionException) {
                    executionException.printStackTrace();
                }
            });

        System.out.println(longAdder.longValue());
        executorService.shutdown();

    }

    private static class Task implements Runnable {

        private final LongAdder longAdder;

        private Task(final LongAdder longAdder) {
            this.longAdder = longAdder;
        }

        @Override
        public void run() {
            longAdder.increment();
        }
    }
}
