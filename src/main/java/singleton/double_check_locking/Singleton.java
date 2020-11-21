package singleton.double_check_locking;

/**
 *  Parallel access but buggy! No happens-before link!
 *  Buggy on -> random memory allocation, pointer copy, obj construction tasks
 */
public class Singleton {

    private static Singleton instance;

    private static final Object key = new Object();

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance != null) {
            return instance;
        }

        synchronized(key) {
            if (instance == null) {
                instance = new Singleton();
            }
            return instance;
        }
    }
}
