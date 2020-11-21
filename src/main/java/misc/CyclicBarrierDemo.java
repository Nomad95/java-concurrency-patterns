package misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class CyclicBarrierDemo {

    private int NUM_WORKERS = 5;
    private int NUM_PARTIAL_RESULTS = 3;
    private CyclicBarrier cyclicBarrier;
    private List<Integer> numbers = Collections.synchronizedList(new ArrayList<>());

    class NumberRandomizer implements Runnable {

        @Override
        public void run() {
            //add some numbers to common list
            for (int i = 0; i < NUM_PARTIAL_RESULTS; i++) {
                final int someNumber = ThreadLocalRandom.current().nextInt(10);
                System.out.println(Thread.currentThread().getName() + " produced " + someNumber);
                numbers.add(someNumber);
            }

            try {
                //threads wait at this line for other threads
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    //do this after all threads have reached the barrier
    class NumberAggregator implements Runnable {

        @Override
        public void run() {
            System.out.println("OK! summing up!");
            final int sum = numbers.stream().mapToInt(e -> e).sum();

            System.out.println("Sum: " + sum);
        }
    }

    void runSimulation() {
        cyclicBarrier = new CyclicBarrier(NUM_WORKERS, new NumberAggregator());
        final ExecutorService pool = Executors.newFixedThreadPool(10);

        for (int i = 0; i < NUM_WORKERS; i++) {
            pool.submit(new NumberRandomizer());
        }

        System.out.println("Siemandejro");

        pool.shutdown();
    }

    public static void main(String[] args) {
        final CyclicBarrierDemo cyclicBarrierDemo = new CyclicBarrierDemo();
        cyclicBarrierDemo.runSimulation();
    }
}
