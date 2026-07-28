package pdk.util.io;

import pdk.util.PDKRuntimeException;

/**
 * Interface for mutable binary-serializable objects.
 * <p>
 * Implementations must provide a public no-argument constructor and an
 * in-place {@link #read(Input)} method that populates all fields from the
 * given input. Because the object is mutable, its fields can be reassigned
 * during deserialization.
 * <p>
 * Typical usage with {@link BinaryIO}:
 * <pre>{@code
 *   MyMutable obj = BinaryIO.read(input, MyMutable.class);
 *   }</pre>
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 28 Jul 2026, 10:28 AM
 */
public interface MutableBinary extends BinaryWritable {

    /**
     * Reads the state of this object from the given {@link Input}, restoring
     * all fields. The reading order must correspond exactly to the order used
     * by {@link #write(Output)}.
     *
     * @param input the input stream to read binary data from
     * @throws PDKRuntimeException if an I/O error occurs during reading
     *                             or the data is malformed
     */
    void read(Input input) throws PDKRuntimeException;
}
