package pdk.util.data.func;

import org.apache.commons.numbers.gamma.Erfc;
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
public class ExponentiallyModifiedGaussianFunc implements Func2D, Serializable {

    public static ExponentiallyModifiedGaussianFunc of(double mu, double sigma, double tau) {
        return new ExponentiallyModifiedGaussianFunc(mu, sigma, tau);
    }

    private final double mean_;
    private final double sigma_;
    private final double tau_;

    /**
     * precomputed values
     */
    private final double exp1;
    private final double sqrt2sigma;
    private final double erfc2;

    /**
     * Create a ExGaussian
     *
     * @param mu    mean of Gaussian
     * @param sigma standard deviation of Gaussian
     * @param tau   decay, or mean of the exponential part
     */
    public ExponentiallyModifiedGaussianFunc(double mu, double sigma, double tau) {
        ArgUtils.checkArgument(tau > 0);

        this.mean_ = mu;
        this.sigma_ = sigma;
        this.tau_ = tau;

        exp1 = mu / tau + sigma * sigma / (tau * tau * 2.0);
        sqrt2sigma = Math.sqrt(2.0) * sigma;
        erfc2 = (mu + sigma * sigma / tau) / sqrt2sigma;
    }

    /**
     * mean of the normal distribution
     *
     * @return mean
     */
    public double getMu() {
        return mean_;
    }

    /**
     * standard deviation of the normal distribution
     *
     * @return the standard deviation of the normal distribution
     */
    public double getSigma() {
        return sigma_;
    }

    /**
     * Return lambda, e.g. exponential rate parameter
     *
     * @return exponential rate
     */
    public double getLambda() {
        return 1 / tau_;
    }

    /**
     * Return tau, e.g. the mean of the exponential part
     *
     * @return tau
     */
    public double getTau() {
        return tau_;
    }

    /**
     * Return mean of ExGaussian
     *
     * @return mean of this distribution
     */
    public double getMean() {
        return mean_ + tau_;
    }

    /**
     * Variance of ExGaussian
     *
     * @return variance of this distribution
     */
    public double getVariance() {
        return sigma_ * sigma_ + tau_ * tau_;
    }

    @Override
    public double f(double x) {
        return 1 / (2 * tau_) * Math.exp(exp1 - x / tau_) * Erfc.value(erfc2 - x / sqrt2sigma);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ExponentiallyModifiedGaussianFunc that)) return false;
        return Double.compare(mean_, that.mean_) == 0 && Double.compare(sigma_, that.sigma_) == 0
                && Double.compare(tau_, that.tau_) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mean_, sigma_, tau_);
    }
}
