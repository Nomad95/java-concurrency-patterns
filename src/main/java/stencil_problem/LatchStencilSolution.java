package stencil_problem;

import java.util.Arrays;
import java.util.concurrent.*;

/**
 *  https://dzone.com/articles/java-phasers-made-simple
 */
public class LatchStencilSolution {

    private double[] initArray = new double[] {1, 2, 4, 6, 6, 4, 2, 1};
    private final int num_iterations = 20;

    public void performAction() {
        var oldArr = Arrays.copyOf(initArray, initArray.length);
        var newArray = Arrays.copyOf(initArray, initArray.length);

        final ExecutorService executorService = Executors.newFixedThreadPool(3);

        for (int i = 0; i < num_iterations; i++) {
            final CountDownLatch latch = new CountDownLatch(initArray.length - 2);

            for (int j = 1; j < initArray.length - 1; j++) {
                executorService.submit(new Worker(oldArr, newArray, j, latch));
            }

            try {
                latch.await();
                System.out.println(Arrays.toString(newArray));
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }

            oldArr = Arrays.copyOf(newArray, oldArr.length);
            newArray = Arrays.copyOf(oldArr, oldArr.length);
        }

        executorService.shutdown();
    }

    class Worker implements Runnable {

        private double[] sourceArray;
        private double[] destinationArray;
        private int index;
        private CountDownLatch latch;

        public Worker(final double[] sourceArray, final double[] destinationArray, final int index, final CountDownLatch latch) {
            this.sourceArray = sourceArray;
            this.destinationArray = destinationArray;
            this.index = index;
            this.latch = latch;
        }

        @Override
        public void run() {
            destinationArray[index] = (sourceArray[index - 1] + sourceArray[index + 1]) / 2;
            latch.countDown();
        }
    }

    public static void main(String[] args) {
        final LatchStencilSolution latchStencilSolution = new LatchStencilSolution();
        latchStencilSolution.performAction();
    }
}
