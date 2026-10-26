package pdk.util.data;

import pdk.util.ICopy;
import pdk.util.PDKRuntimeException;
import pdk.util.io.ImmutableBinary;
import pdk.util.io.Input;
import pdk.util.io.Output;

import java.util.Comparator;

/**
 * Class for 1D data point.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 07 May 2026, 3:38 PM
 */
public class Point1D implements Point, ICopy<Point1D>, Comparable<Point1D>, ImmutableBinary {

    protected final double x;

    public Point1D(double x) {
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
    public Point1D copy() {
        return new Point1D(x);
    }

    public static final Comparator<Point1D> X_NATURAL = Comparator.comparingDouble(o -> o.x);
    public static final Comparator<Point1D> Y_NATURAL = Comparator.comparingDouble(Point1D::getY);

    @Override
    public int compareTo(Point1D o) {
        return X_NATURAL.compare(this, o);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point1D point)) return false;
        return Double.compare(x, point.x) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(x);
    }

    @Override
    public void write(Output output) throws PDKRuntimeException {
        output.writeDouble(x);
    }

    /**
     * Deserializes a {@link Point1D} from the given {@link Input}.
     *
     * @param input the input stream to read binary data from
     * @return a new {@code Point} instance with the deserialized x-coordinate
     * @throws PDKRuntimeException if an I/O error occurs or the data is insufficient
     */
    public static Point1D fromBinary(Input input) {
        return new Point1D(input.readDouble());
    }
}
