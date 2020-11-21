package stencil_problem;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  https://dzone.com/articles/java-phasers-made-simple
 */
public class NonConcurrentStencilSolution {

    private double[] initArray = new double[] {1, 2, 4, 6, 6, 4, 2, 1};
    private final int num_iterations = 20;

    public void performAction() {
        var oldArr = Arrays.copyOf(initArray, initArray.length);
        var newArray = Arrays.copyOf(initArray, initArray.length);

        for (int i = 0; i < num_iterations; i++) {
            for (int j = 1; j < initArray.length - 1; j++) {
                new Worker(oldArr, newArray, j).run();
            }

            System.out.println(Arrays.toString(newArray));
            oldArr = Arrays.copyOf(newArray, oldArr.length);
            newArray = Arrays.copyOf(oldArr, oldArr.length);
        }

    }

    class Worker implements Runnable {

        private double[] sourceArray;
        private double[] destinationArray;
        private int index;

        public Worker(final double[] sourceArray, final double[] destinationArray, final int index) {
            this.sourceArray = sourceArray;
            this.destinationArray = destinationArray;
            this.index = index;
        }

        @Override
        public void run() {
            destinationArray[index] = (sourceArray[index - 1] + sourceArray[index + 1]) / 2;
        }
    }

    public static void main(String[] args) {
        final NonConcurrentStencilSolution latchStencilSolution = new NonConcurrentStencilSolution();
        latchStencilSolution.performAction();
    }
}
