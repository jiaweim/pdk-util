package pdk.util.data.func;

import org.apache.commons.math3.special.Erf;
import pdk.util.ArgUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * Exponentially modified Gaussian distribution function (ExGaussian or EMG):
 * https://en.wikipedia.org/wiki/Exponentially_modified_Gaussian_distribution
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 03 Jul 2025, 11:16 AM
 */
public class ExponentiallyModifiedGaussianFunc2D implements Func2D, Serializable {

    private final double mean;
    private final double sigma;
    private final double lambda;

    /**
     * precomputed values
     */
    private final double exp1;
    private final double sqrt2sigma;
    private final double erfc2;

    /**
     * Create a ExGaussian
     *
     * @param mu     mean of Gaussian
     * @param sigma  standard deviation of Gaussian
     * @param lambda exponential rate
     */
    public ExponentiallyModifiedGaussianFunc2D(double mu, double sigma, double lambda) {
        ArgUtils.checkArgument(lambda > 0);

        this.mean = mu;
        this.sigma = sigma;
        this.lambda = lambda;

        exp1 = lambda * mu + lambda * lambda * sigma * sigma * 0.5;
        sqrt2sigma = Math.sqrt(2.0) * sigma;
        erfc2 = (mu + lambda * sigma * sigma) / sqrt2sigma;
    }

    /**
     * mean of the normal distribution
     *
     * @return mean
     */
    public double getMu() {
        return mean;
    }

    /**
     * standard deviation of the normal distribution
     *
     * @return the standard deviation of the normal distribution
     */
    public double getSigma() {
        return sigma;
    }

    /**
     * Return lambda, e.g. exponential rate parameter
     *
     * @return exponential rate
     */
    public double getLambda() {
        return lambda;
    }

    /**
     * Return mean of ExGaussian
     *
     * @return mean of this distribution
     */
    public double getMean() {
        return mean + 1 / lambda;
    }

    /**
     * Variance of ExGaussian
     *
     * @return variance of this distribution
     */
    public double getVariance() {
        return sigma * sigma + 1 / (lambda * lambda);
    }

    @Override
    public double f(double x) {
        return lambda * 0.5 * Math.exp(exp1 - lambda * x) * Erf.erfc(erfc2 - x / sqrt2sigma);
    }


    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ExponentiallyModifiedGaussianFunc2D that)) return false;
        return Double.compare(mean, that.mean) == 0 && Double.compare(sigma, that.sigma) == 0
                && Double.compare(lambda, that.lambda) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mean, sigma, lambda);
    }
}
