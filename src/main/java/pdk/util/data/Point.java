package pdk.util.data;

import java.util.Comparator;

/**
 * A point in 3D space with x,y,z coordinates.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 26 Oct 2026, 1:42 PM
 */
public interface Point {

    /**
     * Create a 1D point
     *
     * @param value value
     * @return {@link Point1D}
     */
    static Point1D of(double value) {
        return new Point1D(value);
    }

    /**
     * Create a 2D point
     *
     * @param x x value
     * @param y y value
     * @return {@link Point2D} instance
     */
    static Point2D of(double x, double y) {
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
    static Point3D of(double x, double y, double z) {
        return new Point3D(x, y, z);
    }

    /**
     * A comparator that compares points by their x-coordinate in natural (ascending) order.
     */
    Comparator<Point> X_NATURAL = Comparator.comparingDouble(Point::getX);
    /**
     * A comparator that compares points by their y-coordinate in natural (ascending) order.
     */
    Comparator<Point> Y_NATURAL = Comparator.comparingDouble(Point::getY);
    /**
     * A comparator that compares points by their z-coordinate in natural (ascending) order.
     */
    Comparator<Point> Z_NATURAL = Comparator.comparingDouble(Point::getZ);

    /**
     * Return the x-value
     *
     * @return x-value
     */
    double getX();

    /**
     * Return the y-value
     *
     * @return y-value
     */
    double getY();

    /**
     * Return the z-value
     *
     * @return z-value
     */
    double getZ();

}
