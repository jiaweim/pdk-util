package pdk.util.data.func;

import java.io.Serializable;
import java.util.Objects;

/**
 * A func in the form y=ax+b⭐
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 03 Jul 2025, 9:28 AM
 */
public class LineFunc2D implements Func2D, Serializable {

    /**
     * slope
     */
    private final double a;
    /**
     * intercept
     */
    private final double b;

    public LineFunc2D(double a, double b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Return the slope, e.g. the coefficient `a`
     *
     * @return the slope
     */
    public double getSlope() {
        return a;
    }

    /**
     * Return the intercept, e.g. the coefficient `b`
     *
     * @return intercept
     */
    public double getIntercept() {
        return b;
    }

    @Override
    public double f(double x) {
        return a * x + b;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof LineFunc2D that)) return false;
        return Double.compare(a, that.a) == 0
                && Double.compare(b, that.b) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
