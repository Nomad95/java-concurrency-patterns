package stencil_problem;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.stream.Stream;

/**
 * https://dzone.com/articles/java-phasers-made-simple
 */
public class FuzzyArrayGroupingPhaserStencilSolution {

    public static final int NUMBER_OF_GROUPS = 2;

    private double[] initArray = new double[] { 1, 2, 4, 6, 6, 4, 2, 1 };
    private final int num_iterations = 20;

    public void performAction() {
        var oldArr = Arrays.copyOf(initArray, initArray.length);
        var newArray = Arrays.copyOf(initArray, initArray.length);

        final ExecutorService executorService = Executors.newCachedThreadPool();
        final Phaser[] phasers = new Phaser[]{new Phaser(2), new Phaser(2)};

        for (int i = 0; i < num_iterations; i++) {
            executorService.submit(new Worker(oldArr, newArray, 0, 3, 0, phasers));
            executorService.submit(new Worker(oldArr, newArray, 4, 7, 1, phasers));

            phasers[0].arriveAndAwaitAdvance();
            phasers[1].arriveAndAwaitAdvance();

            System.out.println(Arrays.toString(newArray));
            oldArr = Arrays.copyOf(newArray, newArray.length);
            newArray = Arrays.copyOf(oldArr, oldArr.length);
        }

        for (final Phaser phaser : phasers) {
            phaser.arriveAndDeregister();
            phaser.arriveAndDeregister();

        }
        executorService.shutdown();
    }

    class Worker implements Runnable {
        private double[] sourceArray;
        private double[] destinationArray;
        private int indexFrom;
        private int indexTo;
        private int group;
        private Phaser[] phasers;

        public Worker(final double[] sourceArray, final double[] destinationArray, final int indexFrom, final int indexTo, final int group, final Phaser[] phasers) {
            this.sourceArray = sourceArray;
            this.destinationArray = destinationArray;
            this.indexFrom = indexFrom;
            this.indexTo = indexTo;
            this.group = group;
            this.phasers = phasers;
        }

        @Override
        public void run() {
            //first get first and last element of group
            if(indexFrom > 0 && indexFrom < initArray.length - 1) {
                destinationArray[indexFrom] = (sourceArray[indexFrom - 1] + sourceArray[indexFrom + 1]) / 2;
            }

            if(indexTo > 0 && indexTo < initArray.length - 1) {
                destinationArray[indexTo] = (sourceArray[indexTo - 1] + sourceArray[indexTo + 1]) / 2;
            }

            //END OF PhaseA
            //now arrive at synchronizationPoint (?)
            final int phase = phasers[group].arrive();

            //rest of middle indexes sequentially
            for (int i = indexFrom + 1; i <= indexTo - 1; i++) {
                destinationArray[i] = (sourceArray[i - 1] + sourceArray[i + 1]) / 2;
            }

            //END OF PhaseB
            //await for other (adjacent in the case of more groups) phase to arrive
            if (group == 0) {
                phasers[1].awaitAdvance(phase);
            } else {
                phasers[0].awaitAdvance(phase);
            }
        }
    }

    public static void main(String[] args) {
        final FuzzyArrayGroupingPhaserStencilSolution phaserStencilSolution = new FuzzyArrayGroupingPhaserStencilSolution();
        phaserStencilSolution.performAction();
    }
}
