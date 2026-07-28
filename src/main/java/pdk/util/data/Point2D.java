package pdk.util.data;

import pdk.util.PDKRuntimeException;
import pdk.util.io.ImmutableBinary;
import pdk.util.io.Input;
import pdk.util.io.Output;

import java.util.Objects;

/**
 * A two-dimensional point
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 03 Jul 2025, 10:00 AM
 */
public class Point2D extends Point {

    protected final double y;

    public Point2D(double x, double y) {
        super(x);
        this.y = y;
    }

    /**
     * Return the y-value
     *
     * @return the y-value
     */
    @Override
    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Point2D point2D)) return false;
        return Double.compare(x, point2D.x) == 0 && Double.compare(y, point2D.y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public Point2D copy() {
        return new Point2D(x, y);
    }

    @Override
    public void write(Output output) throws PDKRuntimeException {
        output.writeDouble(x);
        output.writeDouble(y);
    }

    /**
     * Deserializes a {@link Point2D} from the given {@link Input}.
     * <p>
     * This static factory method reads two {@code double} values
     * and creates a new immutable Point2D. It is the counterpart to
     * {@link #write(Output)} and is automatically invoked by
     * {@link BinaryIO} when reading an {@link ImmutableBinary} type.
     *
     * @param input the input stream to read binary data from
     * @return a new {@code Point} instance with the deserialized x-coordinate
     * @throws PDKRuntimeException if an I/O error occurs or the data is insufficient
     */
    public static Point2D fromBinary(Input input) {
        return new Point2D(input.readDouble(), input.readDouble());
    }
}
