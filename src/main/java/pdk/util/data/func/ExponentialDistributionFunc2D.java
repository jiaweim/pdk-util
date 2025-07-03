package pdk.util.data.func;

import java.io.Serializable;
import java.util.Objects;

import static pdk.util.ArgUtils.checkArgument;

/**
 * Exponential distribution: https://en.wikipedia.org/wiki/Exponential_distribution
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 03 Jul 2025, 11:36 AM
 */
public class ExponentialDistributionFunc2D implements Func2D, Serializable {

    private final double lambda;

    /**
     * Create an Exponential distribution function
     *
     * @param lambda the rate parameter
     */
    public ExponentialDistributionFunc2D(double lambda) {
        checkArgument(lambda > 0);

        this.lambda = lambda;
    }

    /**
     * Return the mean of this distribution
     *
     * @return the mean
     */
    public double getMean() {
        return 1 / lambda;
    }

    /**
     * @return the variance of this exponential distribution
     */
    public double getVariance() {
        return 1 / lambda * lambda;
    }

    /**
     * @return the rate parameter
     */
    public double getLambda() {
        return lambda;
    }

    @Override
    public double f(double x) {
        if (x < 0)
            return 0;
        return lambda * Math.exp(-lambda * x);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ExponentialDistributionFunc2D that)) return false;
        return Double.compare(lambda, that.lambda) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(lambda);
    }
}
