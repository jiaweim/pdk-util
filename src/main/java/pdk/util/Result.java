package pdk.util;

/**
 * Running result for a task.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Jun 2024, 11:18 AM
 */
public interface Result<V> {

    record Warning<V>(V result) implements Result<V> {

        @Override
        public String getMessage() {
            return "";
        }

        @Override
        public boolean isOK() {
            return false;
        }

        @Override
        public boolean isError() {
            return false;
        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Override
        public boolean isWarning() {
            return true;
        }
    }

    record OK<V>(V result) implements Result<V> {

        @Override
        public String getMessage() {
            return null;
        }

        @Override
        public boolean isOK() {
            return true;
        }

        @Override
        public boolean isError() {
            return false;
        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Override
        public boolean isWarning() {
            return false;
        }
    }

    record Error<V>(String msg) implements Result<V> {

        @Override
        public String getMessage() {
            return msg;
        }

        @Override
        public V result() {
            return null;
        }

        @Override
        public boolean isOK() {
            return false;
        }

        @Override
        public boolean isError() {
            return true;
        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Override
        public boolean isWarning() {
            return false;
        }
    }

    record Cancel<V>(String msg) implements Result<V> {

        @Override
        public String getMessage() {
            return msg;
        }

        @Override
        public V result() {
            return null;
        }

        @Override
        public boolean isOK() {
            return false;
        }

        @Override
        public boolean isError() {
            return false;
        }

        @Override
        public boolean isCanceled() {
            return true;
        }

        @Override
        public boolean isWarning() {
            return false;
        }
    }

    static Result<Void> ok() {
        return new OK<>(null);
    }

    /**
     * Running ok.
     *
     * @param result result to result.
     */
    static <T> Result<T> ok(T result) {
        return new OK<>(result);
    }

    static <T> Result<T> error(String msg) {
        return new Error<>(msg);
    }

    static <T> Result<T> cancel() {
        return new Cancel<>("The task is canceled");
    }

    static <T> Result<T> cancel(String msg) {
        return new Cancel<>(msg);
    }

    static <T> Result<T> warning(T value) {
        return new Warning<>(value);
    }

    /**
     * @return message of the task.
     */
    String getMessage();

    /**
     * @return the running result, only work for OK and warning
     */
    V result();

    /**
     * @return true if run successfully.
     */
    boolean isOK();

    /**
     * @return true if run with error.
     */
    boolean isError();

    /**
     * @return true if the task is canceled.
     */
    boolean isCanceled();

    /**
     * @return true if the task complete with warning
     */
    boolean isWarning();
}
