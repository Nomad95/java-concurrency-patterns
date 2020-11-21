package misc.timeouting_thread;

import misc.NeverEndingTask;

public class TimeoutWithSleep {

    public static void main(String[] args) {
        final NeverEndingTask neverEndingTask = new NeverEndingTask();
        final Thread thread = new Thread(neverEndingTask);

        thread.start();

        //timeout for 10 sec

        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // timeout thread
        thread.interrupt();
    }
}
