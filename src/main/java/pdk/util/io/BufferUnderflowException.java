package pdk.util.io;

/**
 * Buffer UnderflowException
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 15 Apr 2025, 11:34 AM
 */
public class BufferUnderflowException extends RuntimeException {

    public BufferUnderflowException() {}

    public BufferUnderflowException(String message) {
        super(message);
    }

    public BufferUnderflowException(String message, Throwable cause) {
        super(message, cause);
    }

    public BufferUnderflowException(Throwable cause) {
        super(cause);
    }
}
