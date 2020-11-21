package misc.timeouting_thread;

import misc.NeverEndingTask;

import java.util.concurrent.*;

public class TimeoutWithFutures {

    public static void main(String[] args) {
        final NeverEndingTask neverEndingTask = new NeverEndingTask();
        final Thread thread = new Thread(neverEndingTask);

        final ExecutorService executorService = Executors.newFixedThreadPool(2);

        final Future<?> submit = executorService.submit(neverEndingTask);

        //wait 10 sec for response

        try {
            submit.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            submit.cancel(true);//using interrupts
            //task.stop() // using volatile flag
        }
    }
}
