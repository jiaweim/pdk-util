package pdk.util.data;

import pdk.util.PDKRuntimeException;
import pdk.util.io.ImmutableBinary;
import pdk.util.io.Input;
import pdk.util.io.Output;

import java.util.Objects;

/**
 * A 3D point.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 20 Jan 2026, 5:02 PM
 */
public class Point3D extends Point2D {

    private final double z;

    /**
     * Create an {@link Point3D}
     *
     * @param x x value
     * @param y y value
     * @param z error
     */
    public Point3D(double x, double y, double z) {
        super(x, y);
        this.z = z;
    }

    /**
     * Return the error
     *
     * @return error
     */
    @Override
    public double getZ() {
        return z;
    }

    @Override
    public Point3D copy() {
        return new Point3D(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point3D point3D)) return false;
        if (!super.equals(o)) return false;
        return Double.compare(z, point3D.z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), z);
    }

    @Override
    public void write(Output output) throws PDKRuntimeException {
        output.writeDouble(x);
        output.writeDouble(y);
        output.writeDouble(z);
    }

    /**
     * Deserializes a {@link Point3D} from the given {@link Input}.
     * <p>
     * This static factory method reads two {@code double} values
     * and creates a new immutable Point3D. It is the counterpart to
     * {@link #write(Output)} and is automatically invoked by
     * {@link BinaryIO} when reading an {@link ImmutableBinary} type.
     *
     * @param input the input stream to read binary data from
     * @return a new {@code Point} instance with the deserialized x-coordinate
     * @throws PDKRuntimeException if an I/O error occurs or the data is insufficient
     */
    public static Point3D fromBinary(Input input) {
        return new Point3D(input.readDouble(), input.readDouble(), input.readDouble());
    }

}
