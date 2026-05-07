package pdk.util.data;

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

    protected Point2D(double x, double y) {
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
}
