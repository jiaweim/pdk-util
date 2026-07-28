package pdk.util.io;

import pdk.util.PDKRuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class for binary serialization and deserialization of objects
 * implementing {@link BinaryWritable} (either {@link MutableBinary} or
 * {@link ImmutableBinary}).
 * <p>
 * Serialization can be performed directly to any {@link Output} (e.g.
 * in-memory buffer, file stream, network stream), or conveniently into a
 * growable byte array. Deserialization automatically chooses the correct
 * strategy based on the runtime type.
 *
 * @author Jiawei Mao
 * @version 1.2.0
 */
public final class BinaryIO {

    private BinaryIO() {}

    /**
     * Serializes the given object into the specified {@link Output}.
     * The caller is responsible for configuring the {@code Output}
     * (buffer size, maximum capacity, underlying stream, etc.) and
     * for closing it after use.
     *
     * @param obj the object to serialize; must not be {@code null}
     * @param out the output to write to; not closed by this method
     * @throws PDKRuntimeException if an I/O error occurs
     */
    public static void serialize(BinaryWritable obj, Output out) {
        obj.write(out);
    }

    /**
     * Serializes the given object into a new byte array using a
     * default growable buffer (initial size 256, no upper limit).
     *
     * @param obj the object to serialize
     * @return a byte array containing the binary representation
     * @throws PDKRuntimeException if an I/O error occurs
     */
    public static byte[] serialize(BinaryWritable obj) {
        return serialize(obj, 256);
    }

    /**
     * Serializes the given object into a new byte array with a
     * specified initial buffer size. The buffer will grow as needed
     * (no upper limit).
     *
     * @param obj               the object to serialize
     * @param initialBufferSize initial internal buffer size
     * @return a byte array containing the binary representation
     * @throws PDKRuntimeException if an I/O error occurs
     */
    public static byte[] serialize(BinaryWritable obj, int initialBufferSize) {
        Output out = new Output(initialBufferSize, -1); // -1 means no max capacity
        try {
            obj.write(out);
            return out.toBytes();
        } finally {
            out.close();
        }
    }

    /**
     * Serializes the given object directly to the file specified by the path.
     * The file is created if it does not exist, or overwritten if it does.
     * A default buffer of 4096 bytes is used internally.
     *
     * @param obj  the object to serialize
     * @param path the path to the target file
     * @throws PDKRuntimeException if an I/O error occurs during writing
     */
    public static void writeToFile(BinaryWritable obj, Path path) {
        writeToFile(obj, path, 4096);
    }

    /**
     * Serializes the given object directly to the file with a custom
     * internal buffer size.
     *
     * @param obj        the object to serialize
     * @param path       the path to the target file
     * @param bufferSize internal buffer size for the {@link Output}
     * @throws PDKRuntimeException if an I/O error occurs
     */
    public static void writeToFile(BinaryWritable obj, Path path, int bufferSize) {
        try (OutputStream fos = Files.newOutputStream(path);
             Output out = new Output(fos, bufferSize)) {
            obj.write(out);
        } catch (IOException e) {
            throw new PDKRuntimeException("Failed to write to file: " + path, e);
        }
    }

    /**
     * Reads an object of the specified type from the given file path.
     * The file is read from the beginning; the type must implement
     * {@link MutableBinary} or {@link ImmutableBinary}.
     *
     * @param path the path to the file containing the serialized object
     * @param type the concrete class of the object
     * @param <T>  the type implementing {@link BinaryWritable}
     * @return a new instance deserialized from the file
     * @throws PDKRuntimeException if an I/O error occurs or the data is invalid
     */
    public static <T extends BinaryWritable> T readFromFile(Path path, Class<T> type) {
        try (InputStream fis = Files.newInputStream(path);
             Input in = new Input(fis, 4096)) {
            return read(in, type);
        } catch (IOException e) {
            throw new PDKRuntimeException("Failed to read from file: " + path, e);
        }
    }

    /**
     * Reads an object of the specified type from the given {@link Input}.
     * <p>
     * The type must implement either {@link MutableBinary} or
     * {@link ImmutableBinary}. The deserialization strategy is chosen
     * accordingly:
     * <ul>
     *   <li>For {@code MutableBinary}, a new instance is created via the
     *       no-argument constructor and then filled with
     *       {@link MutableBinary#read(Input)}.</li>
     *   <li>For {@code ImmutableBinary}, the static method
     *       {@code fromBinary(Input)} is called to construct the instance.</li>
     * </ul>
     *
     * @param input the input stream to read from
     * @param type  the concrete class of the object (must extend
     *              {@link MutableBinary} or {@link ImmutableBinary})
     * @param <T>   the type of the object
     * @return a new instance of {@code T} populated from the input
     * @throws PDKRuntimeException if an I/O error occurs, the data is invalid,
     *                             or the type does not support the required deserialization
     *                             mechanism
     */
    @SuppressWarnings("unchecked")
    public static <T extends BinaryWritable> T read(Input input, Class<T> type) {
        if (MutableBinary.class.isAssignableFrom(type)) {
            // Mutable: no-arg constructor + read(Input)
            try {
                T obj = type.getDeclaredConstructor().newInstance();
                ((MutableBinary) obj).read(input);
                return obj;
            } catch (ReflectiveOperationException e) {
                throw new PDKRuntimeException("Cannot instantiate mutable type " + type, e);
            }
        } else if (ImmutableBinary.class.isAssignableFrom(type)) {
            // Immutable: static fromBinary(Input) factory
            try {
                Method factory = type.getMethod("fromBinary", Input.class);
                if (!Modifier.isStatic(factory.getModifiers())) {
                    throw new PDKRuntimeException(
                            "fromBinary must be static in " + type);
                }
                return (T) factory.invoke(null, input);
            } catch (NoSuchMethodException e) {
                throw new PDKRuntimeException(
                        "ImmutableBinary type " + type
                                + " must define: public static T fromBinary(Input)", e);
            } catch (Exception e) {
                throw new PDKRuntimeException(
                        "Failed to invoke fromBinary on " + type, e);
            }
        } else {
            throw new PDKRuntimeException(
                    "Type " + type + " must implement MutableBinary or ImmutableBinary");
        }
    }
}