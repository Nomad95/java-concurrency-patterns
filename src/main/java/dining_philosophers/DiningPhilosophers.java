package dining_philosophers;

public class DiningPhilosophers {

    public static void main(String[] args) {
        final Philosopher[] philosophers = new Philosopher[5];
        final Fork[] forks = new Fork[philosophers.length];

        for (int i = 0; i < forks.length; i++) {
            forks[i] = new Fork();
        }

        for (int i = 0; i < philosophers.length; i++) {
            final Fork leftFork = forks[i];
            final Fork rightFork = forks[(i + 1) % forks.length];

            if (i == philosophers.length - 1) {

                //SOLUTION nr1
                // The last philosopher picks up the right fork first
                //we introduce the condition that makes the last philosopher reach for his right fork first, instead of the left. This breaks the circular wait condition and we can avert the deadlock.
                philosophers[i] = new Philosopher(rightFork, leftFork);
            } else {
                philosophers[i] = new Philosopher(leftFork, rightFork);
            }

            new Thread(philosophers[i], "Philosopher: " + i).start();
        }
    }
}
