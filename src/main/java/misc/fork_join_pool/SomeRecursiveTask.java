package misc.fork_join_pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class SomeRecursiveTask extends RecursiveTask<Long> {

    private long workload = 0;

    public SomeRecursiveTask(final long workload) {
        this.workload = workload;
    }

    @Override
    protected Long compute() {
        //break task in half when workload is above threshold

        if (workload > 16) {
            System.out.println("Splitting workLoad : " + workload);

            final List<SomeRecursiveTask> subtasks = createSubtasks();
            //invoke (fork()) all tasks and then join
            return ForkJoinTask.invokeAll(subtasks).stream()
                .mapToLong(ForkJoinTask::join)
                .sum();
        } else {
            System.out.println("Doing workload: " + workload);
            return workload * 3;
        }
    }

    private List<SomeRecursiveTask> createSubtasks() {
        final ArrayList<SomeRecursiveTask> someRecursiveTasks = new ArrayList<>();

        someRecursiveTasks.add(new SomeRecursiveTask(workload/2));
        someRecursiveTasks.add(new SomeRecursiveTask(workload/2));

        return someRecursiveTasks;
    }

    public static void main(String[] args) {
        final SomeRecursiveTask someRecursiveTask = new SomeRecursiveTask(512);
        final Long mergedResult = ForkJoinPool.commonPool().invoke(someRecursiveTask);

        System.out.println("Result: " + mergedResult);
    }
}
