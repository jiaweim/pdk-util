package pdk.util.data.func;

import pdk.util.ArgUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * Normal distribution: https://en.wikipedia.org/wiki/Normal_distribution
 *
 * @author Jiawei Mao
 * @version 1.0.0⭐
 * @since 03 Jul 2025, 10:17 AM
 */
public class NormalDistributionFunc2D implements Func2D, Serializable {

    private final double mean;
    private final double std;

    /**
     * precomputed factor
     */
    private final double factor;
    private final double denominator;

    public NormalDistributionFunc2D(double mean, double std) {
        ArgUtils.checkArgument(std > 0, "Standard deviation should > 0");
        this.mean = mean;
        this.std = std;
        this.factor = 1 / (std * Math.sqrt(2.0 * Math.PI));
        this.denominator = 2 * std * std;
    }

    /**
     * Return the mean of this normal distribution
     *
     * @return the mean
     */
    public double getMean() {
        return mean;
    }

    /**
     * Returns the standard deviation of this normal distribution
     *
     * @return the standard deviation
     */
    public double getStandardDeviation() {
        return std;
    }

    @Override
    public double f(double x) {
        double z = x - mean;
        return factor * Math.exp(-z * z / denominator);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof NormalDistributionFunc2D that)) return false;
        return Double.compare(mean, that.mean) == 0 && Double.compare(std, that.std) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mean, std);
    }
}
