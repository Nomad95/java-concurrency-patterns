package dining_philosophers;

public class Philosopher implements Runnable {

    private final Fork leftFork;
    private final Fork rightFork;

    public Philosopher(final Fork leftFork, final Fork rightFork) {
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // thinking
                doAction("Thinking");
                synchronized (leftFork) {
                    doAction("Picked up left fork");
                    synchronized (rightFork) {
                        // eating
                        doAction("Picked up right fork - eating");

                        doAction("Put down right fork");
                    }

                    // Back to thinking
                    doAction("Put down left fork. Back to thinking");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
    }

    private void doAction(String action) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " - " + System.nanoTime() + ": " + action);
        Thread.sleep((int) (Math.random() * 100));
    }
}
