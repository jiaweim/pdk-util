package pdk.util;


/**
 * Interface for any time-consuming tasks.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Jun 2024, 10:45 AM
 */
public interface ITask extends Runnable {

    /**
     * Execute a task, and the task being executed is usually time-consuming.
     *
     * @throws Exception For any error.
     */
    void go() throws Exception;

    @Override
    default void run() {
        try {
            go();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
