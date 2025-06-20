package pdk.util.exception;

/**
 * PDK RuntimeException
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 21 Jun 2024, 8:08 PM
 */
public class PDKRuntimeException extends RuntimeException {
    /**
     * Constructor for PDKRuntimeException.
     *
     * @param msg a {@link String} object.
     */
    public PDKRuntimeException(String msg) {
        super(msg);
    }

    /**
     * Constructor for PDKRuntimeException.
     *
     * @param exception a {@link Throwable} object.
     */
    public PDKRuntimeException(Throwable exception) {
        super(exception);
    }
}
