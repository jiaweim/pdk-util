package pdk.util.data.func;

import java.io.Serializable;
import java.util.Objects;

/**
 * A function of the form y = a*x^b
 *
 * @author Jiawei Mao
 * @version 1.0.0⭐
 * @since 03 Jul 2025, 10:58 AM
 */
public class PowerFunc2D implements Func2D, Serializable {

    private final double a;
    private final double b;

    /**
     * Create a power function
     *
     * @param a the coefficient term
     * @param b the exponential term
     */
    public PowerFunc2D(double a, double b) {
        this.a = a;
        this.b = b;
    }

    /**
     * the coefficient
     *
     * @return the coefficient term
     */
    public double getA() {
        return a;
    }

    /**
     * The exponential term
     *
     * @return the exponential coefficient
     */
    public double getB() {
        return b;
    }

    @Override
    public double f(double x) {
        return a * Math.pow(x, b);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PowerFunc2D that)) return false;
        return Double.compare(a, that.a) == 0 && Double.compare(b, that.b) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
