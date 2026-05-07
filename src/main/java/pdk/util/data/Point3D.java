package pdk.util.data;

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
    protected Point3D(double x, double y, double z) {
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
}
