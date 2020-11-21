package misc.stopping_thread;

import misc.NeverEndingTask;

public class StoppingByInterruption {

    public static void main(String[] args) throws InterruptedException {
        final Thread thread = new Thread(new NeverEndingTask());
        System.out.println("Program Started");

        thread.start();

        Thread.sleep(2000);

        thread.interrupt(); //its invoked by future.cancel() and thread pool shutdownNow()

        Thread.sleep(2000);

        thread.join();
        System.out.println("Program Stopped");
    }

}
