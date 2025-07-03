package pdk.util.data.func;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * A function int the form y=a0+a1*x+a2*x^2+ ... + an*x^n
 *
 * @author Jiawei Mao
 * @version 1.0.0⭐
 * @since 03 Jul 2025, 10:31 AM
 */
public class PolynomialFunc2D implements Func2D, Serializable {
    /**
     * the coefficients
     */
    private final double[] coefficients;

    /**
     * Constructors a new polynomial function with form {@code y=a0+a1*x+a2*x^2+...+an*x^n}
     *
     * @param coefficients an array with coefficients [a0,a1,...,an]
     */
    public PolynomialFunc2D(double[] coefficients) {
        Objects.requireNonNull(coefficients);
        this.coefficients = coefficients.clone();
    }

    /**
     * Return a copy of the coefficients array
     *
     * @return the coefficients
     */
    public double[] getCoefficients() {
        return coefficients.clone();
    }

    /**
     * Return the order of the polynomial
     *
     * @return the order
     */
    public int getOrder() {
        return coefficients.length - 1;
    }

    @Override
    public double f(double x) {
        double y = 0;
        for (int i = 0; i < coefficients.length; i++) {
            y += coefficients[i] * Math.pow(x, i);
        }
        return y;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PolynomialFunc2D that)) return false;
        return Objects.deepEquals(coefficients, that.coefficients);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(coefficients);
    }
}
