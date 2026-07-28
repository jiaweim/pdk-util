package pdk.util.io;

/**
 * Interface for immutable binary-serializable objects.
 * <p>
 * Implementations do <strong>not</strong> support in-place deserialization.
 * Instead, they must provide a <strong>static factory method</strong> with the
 * exact signature:
 * <pre>{@code public static MyType fromBinary(Input input)}</pre>
 * This method should read all fields from the input and return a new, fully
 * initialized instance. The utility class {@link BinaryIO} automatically
 * detects and uses this factory when deserializing.
 * <p>
 * Because the object is immutable, it is safe to share and does not require
 * a public no-argument constructor.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 28 Jul 2026, 10:29 AM
 */
public interface ImmutableBinary extends BinaryWritable {
}
