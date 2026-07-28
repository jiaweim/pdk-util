package pdk.util.data;

import pdk.util.ICopy;
import pdk.util.PDKRuntimeException;
import pdk.util.io.ImmutableBinary;
import pdk.util.io.Input;
import pdk.util.io.Output;

import java.util.Comparator;
import java.util.Objects;

/**
 * Class for 1D data point.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 07 May 2026, 3:38 PM
 */
public class Point implements ICopy<Point>, Comparable<Point>, ImmutableBinary {

    /**
     * Create a 1D point
     *
     * @param value value
     * @return {@link Point}
     */
    public static Point of(double value) {
        return new Point(value);
    }

    /**
     * Create a 2D point
     *
     * @param x x value
     * @param y y value
     * @return {@link Point2D} instance
     */
    public static Point2D of(double x, double y) {
        return new Point2D(x, y);
    }

    /**
     * Create a 3D point
     *
     * @param x x value
     * @param y y value
     * @param z z value
     * @return {@link Point3D} instance
     */
    public static Point3D of(double x, double y, double z) {
        return new Point3D(x, y, z);
    }

    protected final double x;

    public Point(double x) {
        this.x = x;
    }

    /**
     * Return the x-value
     *
     * @return the x-value
     */
    public double getX() {
        return x;
    }

    /**
     * Return the y-value
     *
     * @return the y-value
     */
    public double getY() {
        throw new UnsupportedOperationException();
    }

    /**
     * Return the z-value
     *
     * @return the z-value
     */
    public double getZ() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Point copy() {
        return new Point(x);
    }

    public static final Comparator<Point> X_NATURAL = Comparator.comparingDouble(o -> o.x);
    public static final Comparator<Point> Y_NATURAL = Comparator.comparingDouble(Point::getY);

    @Override
    public int compareTo(Point o) {
        return X_NATURAL.compare(this, o);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point point)) return false;
        return Double.compare(x, point.x) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(x);
    }

    @Override
    public void write(Output output) throws PDKRuntimeException {
        output.writeDouble(x);
    }

    /**
     * Deserializes a {@link Point} from the given {@link Input}.
     * <p>
     * This static factory method reads a single {@code double} value
     * and creates a new immutable 1D point. It is the counterpart to
     * {@link #write(Output)} and is automatically invoked by
     * {@link BinaryIO} when reading an {@link ImmutableBinary} type.
     *
     * @param input the input stream to read binary data from
     * @return a new {@code Point} instance with the deserialized x-coordinate
     * @throws PDKRuntimeException if an I/O error occurs or the data is insufficient
     */
    public static Point fromBinary(Input input) {
        return new Point(input.readDouble());
    }
}
