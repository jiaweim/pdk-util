package pdk.util;

import java.io.*;

import static java.util.Objects.requireNonNull;

/**
 * Serialization utility
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 19 Jun 2025, 5:20 PM
 */
public class SerializationUtils {

    private SerializationUtils() {}

    /**
     * Serializes an {@link Object} to the specified stream.
     *
     * <p>The stream will be closed once the object is written.
     * This avoids the need for a finally clause, and maybe also exception
     * handling, in the application code.</p>
     *
     * <p>The stream passed in is not buffered internally within this method.
     * This is the responsibility of your application if desired.</p>
     *
     * @param obj          the object to serialize to bytes, may be null
     * @param outputStream the stream to write to, must not be null
     * @throws NullPointerException   if {@code outputStream} is {@code null}
     * @throws SerializationException (runtime) if the serialization fails
     */
    @SuppressWarnings("resource") // outputStream is managed by the caller
    public static void serialize(final Serializable obj, final OutputStream outputStream) {
        requireNonNull(outputStream, "outputStream");

        try (ObjectOutputStream out = new ObjectOutputStream(outputStream)) {
            out.writeObject(obj);
        } catch (final IOException ex) {
            throw new SerializationException(ex);
        }
    }

    /**
     * Serializes an {@link Object} to a byte array for
     * storage/serialization.
     *
     * @param obj the object to serialize to bytes
     * @return a byte[] with the converted Serializable
     * @throws SerializationException (runtime) if the serialization fails
     */
    public static byte[] serialize(final Serializable obj) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        serialize(obj, baos);
        return baos.toByteArray();
    }


    /**
     * Deserializes a single {@link Object} from an array of bytes.
     *
     * <p>
     * If the call site incorrectly types the return value, a {@link ClassCastException} is thrown from the call site.
     * Without Generics in this declaration, the call site must type cast and can cause the same ClassCastException.
     * Note that in both cases, the ClassCastException is in the call site, not in this method.
     * </p>
     *
     * @param <T>        the object type to be deserialized
     * @param objectData the serialized object, must not be null
     * @return the deserialized object
     * @throws NullPointerException   if {@code objectData} is {@code null}
     * @throws SerializationException (runtime) if the serialization fails
     */
    public static <T> T deserialize(final byte[] objectData) {
        requireNonNull(objectData, "objectData");
        return deserialize(new ByteArrayInputStream(objectData));
    }

    /**
     * Deserializes an {@link Object} from the specified stream.
     *
     * <p>
     * The stream will be closed once the object is written. This avoids the need for a finally clause, and maybe also
     * exception handling, in the application code.
     * </p>
     *
     * <p>
     * The stream passed in is not buffered internally within this method. This is the responsibility of your
     * application if desired.
     * </p>
     *
     * <p>
     * If the call site incorrectly types the return value, a {@link ClassCastException} is thrown from the call site.
     * Without Generics in this declaration, the call site must type cast and can cause the same ClassCastException.
     * Note that in both cases, the ClassCastException is in the call site, not in this method.
     * </p>
     *
     * @param <T>         the object type to be deserialized
     * @param inputStream the serialized object input stream, must not be null
     * @return the deserialized object
     * @throws NullPointerException   if {@code inputStream} is {@code null}
     * @throws SerializationException (runtime) if the serialization fails
     */
    @SuppressWarnings("resource") // inputStream is managed by the caller
    public static <T> T deserialize(final InputStream inputStream) {
        requireNonNull(inputStream, "inputStream");
        try (ObjectInputStream in = new ObjectInputStream(inputStream)) {
            @SuppressWarnings("unchecked") final T obj = (T) in.readObject();
            return obj;
        } catch (final ClassNotFoundException | IOException | NegativeArraySizeException ex) {
            throw new SerializationException(ex);
        }
    }

    /**
     * Serializes an object, and deserializes it, return the deserialized version.
     * <p>
     * Used to test whether the object is serialized correctly
     *
     * @param serializable {@link Serializable} object
     * @param <T>          type to return
     * @return obj
     */
    public static <T> T round(Serializable serializable) {
        requireNonNull(serializable);
        byte[] bytes = serialize(serializable);
        return deserialize(bytes);
    }
}
