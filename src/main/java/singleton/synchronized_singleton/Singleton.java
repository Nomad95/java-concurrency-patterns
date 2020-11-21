package singleton.synchronized_singleton;

/**
 * Correct but not perfect. in a multicore environment multiple threads are blocked at getInstance method so it can cause some performance drawback.
 */
public class Singleton {

    private static Singleton instance;

    private Singleton() {
    }

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }

        return instance;
    }
}
