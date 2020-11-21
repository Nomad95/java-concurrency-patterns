package stencil_problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.*;

/**
 * https://dzone.com/articles/java-phasers-made-simple
 */
public class ForkJoinStencilSolution {

    private double[] initArray = new double[] { 1, 2, 4, 6, 6, 4, 2, 1 };
    private final int num_iterations = 20;

    public void performAction() {
        var oldArr = Arrays.copyOf(initArray, initArray.length);
        var newArray = Arrays.copyOf(initArray, initArray.length);

        for (int i = 0; i < num_iterations; i++) {

            final ArrayList<Worker> tasks = new ArrayList<>();
            for (int j = 1; j < initArray.length - 1; j++) {
                tasks.add(new Worker(oldArr, newArray, j));
            }

            ForkJoinTask.invokeAll(tasks).forEach(ForkJoinTask::join);

            System.out.println(Arrays.toString(newArray));
            oldArr = newArray;
            newArray = Arrays.copyOf(oldArr, oldArr.length);
        }
    }

    class Worker extends RecursiveAction {
        private double[] sourceArray;
        private double[] destinationArray;
        private int index;

        public Worker(final double[] sourceArray, final double[] destinationArray, final int index) {
            this.sourceArray = sourceArray;
            this.destinationArray = destinationArray;
            this.index = index;
        }

        @Override
        protected void compute() {
            destinationArray[index] = (sourceArray[index - 1] + sourceArray[index + 1]) / 2;
        }
    }

    public static void main(String[] args) {
        final ForkJoinStencilSolution phaserStencilSolution = new ForkJoinStencilSolution();
        phaserStencilSolution.performAction();
    }
}
