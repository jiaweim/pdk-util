package pdk.util;

/**
 * Generic exception type for PDK (Proteomics Development Kit).
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 26 Oct 2026, 9:56 AM
 */
public class PDKException extends Exception {

    /**
     * Constructs a new {@code PDKException} with {@code null} as its
     * detail message and no cause.
     */
    public PDKException() {
        super();
    }

    /**
     * Constructs a new {@code PDKException} with the specified detail message.
     *
     * @param message the detail message, which is saved for later retrieval
     *                by the {@link #getMessage()} method; may be {@code null}
     */
    public PDKException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code PDKException} with the specified cause.
     *
     * <p>The detail message is initialized to
     * {@code cause == null ? null : cause.toString()}.</p>
     *
     * @param cause the cause of this exception, which is saved for later
     *              retrieval by the {@link #getCause()} method; may be
     *              {@code null}, indicating that the cause is nonexistent
     *              or unknown
     */
    public PDKException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new {@code PDKException} with the specified detail message
     * and cause.
     *
     * @param message the detail message, which is saved for later retrieval
     *                by the {@link #getMessage()} method; may be {@code null}
     * @param cause   the cause of this exception, which is saved for later
     *                retrieval by the {@link #getCause()} method; may be
     *                {@code null}, indicating that the cause is nonexistent
     *                or unknown
     */
    public PDKException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@code PDKException} with the specified detail message,
     * cause, suppression behaviour and stack trace writability.
     *
     * @param message            the detail message, which is saved for later
     *                           retrieval by the {@link #getMessage()} method;
     *                           may be {@code null}
     * @param cause              the cause of this exception, which is saved
     *                           for later retrieval by the
     *                           {@link #getCause()} method; may be
     *                           {@code null}, indicating that the cause is
     *                           nonexistent or unknown
     * @param enableSuppression  whether suppression is enabled or disabled
     * @param writableStackTrace whether the stack trace should be writable
     */
    public PDKException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
