package pdk.util.exception;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 21 Jun 2024, 8:08 PM
 */
public class PDKRuntimeException extends RuntimeException {
    /**
     * <p>
     * Constructor for OmicsRuntimeException.
     * </p>
     *
     * @param msg a {@link String} object.
     */
    public PDKRuntimeException(String msg) {
        super(msg);
    }

    /**
     * <p>
     * Constructor for OmicsRuntimeException.
     * </p>
     *
     * @param exception a {@link Throwable} object.
     */
    public PDKRuntimeException(Throwable exception) {
        super(exception);
    }
}
