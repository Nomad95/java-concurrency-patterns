package stencil_problem;

import java.util.Arrays;
import java.util.concurrent.*;

/**
 * https://dzone.com/articles/java-phasers-made-simple
 */
public class PhaserStencilSolution {

    private double[] initArray = new double[] { 1, 2, 4, 6, 6, 4, 2, 1 };
    private final int num_iterations = 20;

    public void performAction() {
        var oldArr = Arrays.copyOf(initArray, initArray.length);
        var newArray = Arrays.copyOf(initArray, initArray.length);

        final ExecutorService executorService = Executors.newCachedThreadPool();    //?
        final Phaser phaser = new Phaser(1);

        for (int i = 0; i < num_iterations; i++) {

            for (int j = 1; j < initArray.length - 1; j++) {
                executorService.submit(new Worker(oldArr, newArray, j, phaser));
            }

            phaser.arriveAndAwaitAdvance();

            System.out.println(Arrays.toString(newArray));
//            System.out.println(phaser.getPhase());
            oldArr = newArray;
            newArray = Arrays.copyOf(oldArr, oldArr.length);
        }

        phaser.arriveAndDeregister();
        executorService.shutdown();
    }

    class Worker implements Runnable {
        private double[] sourceArray;
        private double[] destinationArray;
        private int index;
        private Phaser phaser;

        public Worker(final double[] sourceArray, final double[] destinationArray, final int index, final Phaser phaser) {
            this.sourceArray = sourceArray;
            this.destinationArray = destinationArray;
            this.index = index;
            this.phaser = phaser;
            phaser.register();
        }

        @Override
        public void run() {
            destinationArray[index] = (sourceArray[index - 1] + sourceArray[index + 1]) / 2;
            phaser.arriveAndAwaitAdvance();
            phaser.arriveAndDeregister();
        }
    }

    public static void main(String[] args) {
        final PhaserStencilSolution phaserStencilSolution = new PhaserStencilSolution();
        phaserStencilSolution.performAction();
    }
}
