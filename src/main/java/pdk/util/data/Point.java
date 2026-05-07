package pdk.util.data;

import pdk.util.ICopy;

import java.util.Comparator;
import java.util.Objects;

/**
 * Class for 1D data point.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 07 May 2026, 3:38 PM
 */
public class Point implements ICopy<Point>, Comparable<Point> {

    /**
     * Create a 1D point
     *
     * @param value value
     * @return {@link Point}
     */
    public static Point create(double value) {
        return new Point(value);
    }

    /**
     * Create a 2D point
     *
     * @param x x value
     * @param y y value
     * @return {@link Point2D} instance
     */
    public static Point2D create(double x, double y) {
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
    public static Point3D create(double x, double y, double z) {
        return new Point3D(x, y, z);
    }


    protected final double x;

    protected Point(double x) {
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
}
