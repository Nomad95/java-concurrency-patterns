package misc.locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TimedLock {
    private Lock lock = new ReentrantLock();

    public void operation() {
        if (lock.tryLock()) { //instead of being block thread does something else //we can
            try {
                //guarded block of code
            } finally {
                lock.unlock();
            }
        } else {
            //else
        }
    }

    public void operationTimeout() {
        try {
            if (lock.tryLock(1, TimeUnit.SECONDS)) { //instead of being block thread does something else //we can
                try {
                    //guarded block of code
                } finally {
                    lock.unlock();
                }
            } else {
                //else
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
