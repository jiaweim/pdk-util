package pdk.util.data;

import pdk.util.Copyable;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * A two-dimensional point
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 03 Jul 2025, 10:00 AM
 */
public class Point2D implements Copyable<Point2D>, Serializable, Comparable<Point2D> {

    protected double x;
    protected double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
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
     * set the x-value for this point
     *
     * @param x the new x-value
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Return the y-value
     *
     * @return the y-value
     */
    public double getY() {
        return y;
    }

    /**
     * set the y-value for this point
     *
     * @param y the new y-value
     */
    public void setY(double y) {
        this.y = y;
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

    public static final Comparator<Point2D> X_NATURAL = Comparator.comparingDouble(o -> o.x);
    public static final Comparator<Point2D> Y_NATURAL = Comparator.comparingDouble(o -> o.y);

    @Override
    public int compareTo(Point2D o) {
        return X_NATURAL.compare(this, o);
    }

    @Override
    public Point2D copy() {
        return new Point2D(x, y);
    }
}
