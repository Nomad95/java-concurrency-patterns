package misc.phaser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class PhaserDemo {

    static class LongRunningAction implements Runnable {
        private String name;
        private Phaser phaser;

        public LongRunningAction(final String name, final Phaser phaser) {
            this.name = name;
            this.phaser = phaser;
            phaser.register(); //register this thread to phaser (parties++)
        }

        @Override
        public void run() {
            phaser.arriveAndAwaitAdvance();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            phaser.arriveAndDeregister();
        }
    }

    public static void main(String[] args) {
        final ExecutorService executorService = Executors.newCachedThreadPool();
        final Phaser phaser = new Phaser(1); //1 in constructor means we are registering main thread as a coordinator

        executorService.submit(new LongRunningAction("thread-1", phaser));
        executorService.submit(new LongRunningAction("thread-2", phaser));
        executorService.submit(new LongRunningAction("thread-3", phaser));

        phaser.arriveAndAwaitAdvance(); //one more call from main thread is needed to wait at the barrier (4 in total)

        System.out.println(phaser.getPhase());

        executorService.submit(new LongRunningAction("thread-4", phaser));
        executorService.submit(new LongRunningAction("thread-5", phaser));

        phaser.arriveAndAwaitAdvance();

        System.out.println(phaser.getPhase());

        phaser.arriveAndDeregister(); //registered parties becomes zero so phaser is terminated
    }
}
