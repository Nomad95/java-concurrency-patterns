package stencil_problem;

import java.util.Arrays;
import java.util.concurrent.*;

/**
 * https://dzone.com/articles/java-phasers-made-simple
 */
public class CyclicBarrierStencilSolution {

    private double[] initArray = new double[] { 1, 2, 4, 6, 6, 4, 2, 1 };
    private final int num_iterations = 20;

    public void performAction() {
        var oldArr = Arrays.copyOf(initArray, initArray.length);
        var newArray = Arrays.copyOf(initArray, initArray.length);

        final ExecutorService executorService = Executors.newFixedThreadPool(initArray.length - 1);//need to have same number (or more) of threads as number of cyclic barrier's parties
        final CyclicBarrier barrier = new CyclicBarrier(initArray.length - 1);

        for (int i = 0; i < num_iterations; i++) {

            for (int j = 1; j < initArray.length - 1; j++) {
                executorService.submit(new Worker(oldArr, newArray, j, barrier));
            }

            try {
                barrier.await(); // synchronize here
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

            System.out.println(Arrays.toString(newArray));
            oldArr = Arrays.copyOf(newArray, oldArr.length);
            newArray = Arrays.copyOf(oldArr, oldArr.length);
        }

        executorService.shutdown();
    }

    class Worker implements Runnable {
        private double[] sourceArray;
        private double[] destinationArray;
        private int index;
        private CyclicBarrier barrier;

        public Worker(final double[] sourceArray, final double[] destinationArray, final int index, final CyclicBarrier barrier) {
            this.sourceArray = sourceArray;
            this.destinationArray = destinationArray;
            this.index = index;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            destinationArray[index] = (sourceArray[index - 1] + sourceArray[index + 1]) / 2;
            //System.out.println("Index " + index + " " + sourceArray[index - 1] + " + " + sourceArray[index + 1] + " = " + destinationArray[index]);
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        final CyclicBarrierStencilSolution cyclicBarrierStencilSolution = new CyclicBarrierStencilSolution();
        cyclicBarrierStencilSolution.performAction();
    }
}
