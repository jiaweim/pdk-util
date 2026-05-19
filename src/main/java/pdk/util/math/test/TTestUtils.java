package pdk.util.math.test;

import org.apache.commons.statistics.distribution.TDistribution;
import pdk.util.math.StatUtils;
import pdk.util.tuple.Tuple;
import pdk.util.tuple.Tuple2;

/**
 * t-test.
 *
 * @author Jiawei Mao
 * @version 0.2.0
 * @since 06 May 2024, 10:26 AM
 */
public class TTestUtils {

    /**
     * Return the paired t-test t test statistic
     *
     * @param x1 values of sample 1
     * @param x2 values of sample 2
     * @return t test statistic
     * @since 2024-11-26⭐⭐
     */
    public static double getPairedT(double[] x1, double[] x2) {
        double[] d = new double[x1.length];
        for (int i = 0; i < x1.length; i++) {
            d[i] = x1[i] - x2[i];
        }
        double mean = StatUtils.mean(d);
        double variance = StatUtils.variance(d);
        return mean / Math.sqrt(variance / d.length);
    }

    /**
     * Compute the critical value for left-tailed test *
     *
     * @param degreesOfFreedom degrees of freedom, which is n-1, n is the sample size
     * @param alpha            level of significance
     * @return left-tailed test critical value
     */
    public static double getLeftTailedCriticalValue(int degreesOfFreedom, double alpha) {
        TDistribution distribution = TDistribution.of(degreesOfFreedom);
        return distribution.inverseCumulativeProbability(alpha);
    }

    /**
     * Compute the critical value for right-tailed test *
     *
     * @param degreesOfFreedom degrees of freedom
     * @param alpha            level of significance
     * @return right-tailed test critical value
     */
    public static double getRightTailedCriticalValue(int degreesOfFreedom, double alpha) {
        TDistribution distribution = TDistribution.of(degreesOfFreedom);
        return distribution.inverseCumulativeProbability(1 - alpha);
    }

    /**
     * Compute the critical values for two-tailed test
     *
     * @param degreesOfFreedom degrees of freedom
     * @param alpha            level of significance
     * @return two-tailed test critical values
     * @since 2024-11-26 ⭐
     */
    public static Tuple2<Double, Double> getTwoTailedCriticalValue(int degreesOfFreedom, double alpha) {
        TDistribution distribution = TDistribution.of(degreesOfFreedom);
        double leftValue = distribution.inverseCumulativeProbability(alpha / 2.0);
        double rightValue = 0 - leftValue;
        return Tuple.of(leftValue, rightValue);
    }

    /**
     * Compute the critical values for two-tailed test, assume the variance of two populations are not equal
     *
     * @param sampleSize1 size of sample 1
     * @param sampleSize2 size of sample 2
     * @param alpha       level of significance
     * @return two-tailed test critical values
     */
    public static Tuple2<Double, Double> getTwoTailedCriticalValue(int sampleSize1, int sampleSize2, double alpha) {
        int degreesOfFreedom = Math.min(sampleSize1 - 1, sampleSize2 - 1);
        TDistribution distribution = TDistribution.of(degreesOfFreedom);
        double leftValue = distribution.inverseCumulativeProbability(alpha / 2.0);
        double rightValue = 0 - leftValue;
        return Tuple.of(leftValue, rightValue);
    }

    /**
     * Compute the t-statistic *
     *
     * @param sampleMean     sample mean
     * @param mu             population mean
     * @param sampleVariance sample variance, not standard deviation
     * @param sampleSize     sample size
     * @return t-statistic
     */
    public static double getStatistic(final double sampleMean, final double mu,
            final double sampleVariance, final double sampleSize) {
        return (sampleMean - mu) / Math.sqrt(sampleVariance / sampleSize);
    }

    /**
     * Return the standard test statistic for two-sample t-test for mean.
     * (assume the variance of two population is not equal, the mean of two population is equal).
     *
     * @param sampleMean1     mean of sample 1
     * @param sampleMean2     mean of sample 2
     * @param sampleVariance1 variance of sample 1
     * @param sampleVariance2 variance of sample 2
     * @param sampleSize1     size of sample 1
     * @param sampleSize2     size of sample 2
     * @return standard test statistic
     * @since 2024-11-26⭐
     */
    public static double getStatistic(final double sampleMean1, final double sampleMean2,
            final double sampleVariance1, final double sampleVariance2, final int sampleSize1, final int sampleSize2) {
        return (sampleMean1 - sampleMean2)
                / Math.sqrt(sampleVariance1 / sampleSize1 + sampleVariance2 / sampleSize2);
    }

    /**
     * Return the standard test statistic for two-sample t-test for mean.
     * (assume the variances of two population are equal, the means of two population are equal).
     *
     * @param sampleMean1     mean of sample 1
     * @param sampleMean2     mean of sample 2
     * @param sampleVariance1 variance of sample 1
     * @param sampleVariance2 variance of sample 2
     * @param sampleSize1     size of sample 1
     * @param sampleSize2     size of sample 2
     * @return standard test statistic
     * @since 2024-11-26 ⭐
     */
    public static double getStatisticEqual(final double sampleMean1, final double sampleMean2,
            final double sampleVariance1, final double sampleVariance2, final int sampleSize1, final int sampleSize2) {
        double variance = ((sampleSize1 - 1) * sampleVariance1 + (sampleSize2 - 1) * sampleVariance2)
                * (1. / sampleSize1 + 1. / sampleSize2)
                / (sampleSize1 + sampleSize2 - 2);
        return (sampleMean1 - sampleMean2)
                / Math.sqrt(variance);
    }

    /**
     * Return the standard test statistic for two-sample t-test for mean
     * (assume the variance of two population is not equal).
     *
     * @param sampleMean1     mean of sample 1
     * @param sampleMean2     mean of sample 2
     * @param mean1           mean of population 1
     * @param mean2           mean of population 2
     * @param sampleVariance1 variance of sample 1
     * @param sampleVariance2 variance of sample 2
     * @param sampleSize1     size of sample 1
     * @param sampleSize2     size of sample 2
     * @return standard test statistic
     */
    public static double getStatistic(final double sampleMean1, final double sampleMean2, final double mean1, final double mean2,
            final double sampleVariance1, final double sampleVariance2, final int sampleSize1, final int sampleSize2) {
        return ((sampleMean1 - sampleMean2) - (mean1 - mean2))
                / Math.sqrt(sampleVariance1 / sampleSize1 + sampleVariance2 / sampleSize2);
    }

    /**
     * Compute p-value for 2-sided, 1-sample t-test *
     *
     * @param sampleMean     sample mean
     * @param mu             population mean, constant to test again
     * @param sampleVariance sample variance
     * @param sampleSize     sample size n
     * @return p-value
     */
    public static double getOneSampleTwoTailedPValue(final double sampleMean, final double mu,
            final double sampleVariance, final double sampleSize) {
        final double t = Math.abs(getStatistic(sampleMean, mu, sampleVariance, sampleSize));
        TDistribution distribution = TDistribution.of(sampleSize - 1);
        return 2.0 * distribution.cumulativeProbability(-t);
    }

    /**
     * Compute p-value for 1-sided, 1-sample t-test
     *
     * @param sampleMean     sample mean
     * @param mu             constant to test again
     * @param sampleVariance sample variance
     * @param sampleSize     sample size
     * @return p-value
     */
    public static double getOneSampleOneTailedPValue(final double sampleMean, final double mu,
            final double sampleVariance, final double sampleSize) {
        final double t = Math.abs(getStatistic(sampleMean, mu, sampleVariance, sampleSize));
        TDistribution distribution = TDistribution.of(sampleSize - 1);
        return distribution.cumulativeProbability(-t);
    }
}
