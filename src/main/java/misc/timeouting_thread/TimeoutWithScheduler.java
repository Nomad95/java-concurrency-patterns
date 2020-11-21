package misc.timeouting_thread;

import misc.NeverEndingTask;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimeoutWithScheduler {

    public static void main(String[] args) {
        final NeverEndingTask neverEndingTask = new NeverEndingTask();
        final Thread thread = new Thread(neverEndingTask);

        thread.start();

        //timeout for 10 sec

        final ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);
        sched.schedule(thread::interrupt, 10, TimeUnit.SECONDS);
    }
}
