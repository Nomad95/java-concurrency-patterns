package producer_consumer.delayed_queue;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayedObject implements Delayed {
    private String data;
    private long startTime; //his is a time when the element should be consumed from the queue.

    public DelayedObject(final String data, final long startTime) {
        this.data = data;
        this.startTime = System.currentTimeMillis() + startTime;
    }

    @Override
    public long getDelay(final TimeUnit unit) { //it should return the remaining delay associated with this object in the given time unit.
        var diff = startTime - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(final Delayed o) {
        return (int) (this.startTime - ((DelayedObject) o).startTime);
    }
}
