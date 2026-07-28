package pdk.util.io;

import pdk.util.PDKRuntimeException;

/**
 * Base interface for binary serialization. Implementations must be able to write
 * their complete state to an {@link Output}.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 28 Jul 2026, 10:26 AM
 */
public interface BinaryWritable {
    /**
     * Writes the state of this object to the given {@link Output}.
     * The order of written fields must be consistent with the corresponding
     * read logic defined in {@link MutableBinary#read(Input)} or in a static
     * {@code fromBinary(Input)} factory of an {@link ImmutableBinary} type.
     *
     * @param output the output stream to write binary data to
     * @throws PDKRuntimeException if an I/O error occurs during writing
     */
    void write(Output output) throws PDKRuntimeException;
}

