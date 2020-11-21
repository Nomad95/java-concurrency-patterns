package misc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

public class Semaphores {


    public static void main(String[] args) throws InterruptedException {
        LoginQueueUsingSemaphore semaphore = new LoginQueueUsingSemaphore(3);
        final ExecutorService executorService = Executors.newFixedThreadPool(50);
        IntStream.range(0, 100).forEach(i -> executorService.execute(semaphore::tryLogin));

        executorService.shutdown();
        System.out.println(semaphore.availableSlots());
    }
}

class LoginQueueUsingSemaphore {

    private Semaphore semaphore;

    public LoginQueueUsingSemaphore(int slotLimit) {
        semaphore = new Semaphore(slotLimit);
    }

    void tryLogin() {
        try {
            semaphore.acquire(); //only three threads will be able to log in
            //4th thread will be waiting
            //try acquire does not block thread
            System.out.println(Thread.currentThread().getId() + " OMG I HAVE BEEN LOGGED!!!");
        } catch (InterruptedException e) {
            semaphore.release();
            e.printStackTrace();
        }
    }

    void logout() {
        semaphore.release();
        System.out.println(Thread.currentThread().getId() + " NOO I LOGGED OUT!!!");
    }

    int availableSlots() {
        return semaphore.availablePermits();
    }

}
