package singleton.double_check_locking;

/**
 *  No bugs but still performance issues as in the first take ;/
 */
public class SingletonBadSolution {

    private static volatile SingletonBadSolution instance;

    private static final Object key = new Object();

    private SingletonBadSolution() {
    }

    public static SingletonBadSolution getInstance() {
        if (instance != null) {
            return instance;
        }

        synchronized(key) {
            if (instance == null) {
                instance = new SingletonBadSolution();
            }
            return instance;
        }
    }
}
