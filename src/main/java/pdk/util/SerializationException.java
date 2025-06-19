package pdk.util;

/**
 * Exception thrown when the Serialization process fails.
 *
 * <p>The original error is wrapped within this one.</p>
 *
 * <p>#NotThreadSafe# because Throwable is not thread-safe</p>
 *
 * @since 1.0
 */
public class SerializationException extends RuntimeException {

    /**
     * Required for serialization support.
     *
     * @see java.io.Serializable
     */
    private static final long serialVersionUID = 4029025366392702726L;

    /**
     * Constructs a new {@link SerializationException} without specified
     * detail message.
     */
    public SerializationException() {}

    /**
     * Constructs a new {@link SerializationException} with specified
     * detail message.
     *
     * @param msg The error message.
     */
    public SerializationException(final String msg) {
        super(msg);
    }

    /**
     * Constructs a new {@link SerializationException} with specified
     * detail message and nested {@link Throwable}.
     *
     * @param msg   The error message.
     * @param cause The {@link Exception} or {@link Error}
     *              that caused this exception to be thrown.
     */
    public SerializationException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs a new {@link SerializationException} with specified
     * nested {@link Throwable}.
     *
     * @param cause The {@link Exception} or {@link Error}
     *              that caused this exception to be thrown.
     */
    public SerializationException(final Throwable cause) {
        super(cause);
    }

}
