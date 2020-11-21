package misc;

public class NeverEndingTask implements Runnable {

    @Override
    public void run() {
        while (true) {

            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Interrupted");
                return;
            }

        }
    }
}
