package pdk.util;

import org.apache.commons.math3.stat.StatUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;

import static pdk.util.ArgUtils.checkArgument;
import static pdk.util.ArgUtils.checkNotNull;

/**
 * Math utilities
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 20 Jul 2025, 7:01 PM
 */
public final class MathUtils {

    private MathUtils() {}

    static final double[] factorial = new double[128];
    static final double[] logFactorial = new double[128];

    static {
        double fac = 1.0;
        for (int i = 1; i < 128; i++) {
            fac *= i;
            factorial[i] = fac;
            logFactorial[i] = Math.log(fac);
        }
    }

    /**
     * return the factorial of given positive integer
     *
     * @param n positive number, at most 127
     * @return factorial
     */
    public static double factorial(int n) {
        return factorial[n];
    }

    /**
     * return the ln(factorial) of given positive integer
     *
     * @param n positive number, at most 127
     * @return ln(factorial)
     */
    public static double lnFactorial(int n) {
        return logFactorial[n];
    }

    /**
     * Return true if the given integer is odd
     *
     * @param x integer
     * @return true if the integer is odd
     */
    public static boolean isOdd(int x) {
        return x % 2 == 1;
    }

    /**
     * Return the next largest power of two
     * <ul>
     *     <li>negative integer will return 0</li>
     *     <li>0 -> 0</li>
     *     <li>1 -> 1</li>
     *     <li>2 -> 2</li>
     *     <li>3 -> 4</li>
     *     <li>...</li>
     * </ul>
     *
     * @param value an integer
     * @return next largest power of two
     * @since 2024-07-01 ⭐
     */
    public static int nextPowerOfTwo(int value) {
        int n = value;
        n--;
        n |= n >> 1;
        n |= n >> 2;
        n |= n >> 4;
        n |= n >> 8;
        n |= n >> 16;
        n++;
        return n;
    }

    /**
     * Return true if two double value equals within given accuracy.
     *
     * @param value1 a double value
     * @param value2 a double value
     * @param delta  accuracy
     * @return true if the two double value equals within the accuracy
     */
    public static boolean isEqual(double value1, double value2, double delta) {
        if (Double.isNaN(delta) || delta < 0) {
            throw new IllegalArgumentException("delta with value " + delta + " is not allowed");
        }
        return (Double.doubleToLongBits(value1) == Double.doubleToLongBits(value2))
                || (Math.abs(value1 - value2) <= delta);
    }

    /**
     * Return sum of values in array.
     * <p>
     * If there are no values in the dataset, 0 is returned. If any of the values are {@link Double#NaN},
     * {@link Double#NaN} is returned.
     *
     * @param values a Double array
     * @return sum
     */
    public static double sum(Double[] values) {
        double total = 0;
        for (Double value : values) {
            total += value;
        }
        return total;
    }

    /**
     * Return the sum of the array.
     *
     * @param values double array
     * @return sum of all values.
     */
    public static double sum(double[] values) {
        checkNotNull(values);

        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum;
    }

    /**
     * Return the sum of double values
     *
     * @param values dataset
     * @return sum of data in the dataset
     */
    public static double sum(Iterable<Double> values) {
        checkNotNull(values);

        double sum = 0;
        for (Double value : values) {
            sum += value;
        }
        return sum;
    }

    /**
     * The weighted sum of the entries in the specified input array.
     * Uses the formula,
     * <pre>
     *  weighted sum = &Sigma;(values[i] * weights[i])
     * </pre>
     *
     * @param values  the input array
     * @param weights the weight array
     * @return weighted sum
     */
    public static double sum(final double[] values, final double[] weights) {
        checkNotNull(values);
        checkNotNull(weights);

        if (values.length != weights.length) {
            throw new IllegalArgumentException("The length of values and weights should be the same.");
        }

        double sum = 0.;
        for (int i = 0; i < values.length; i++) {
            double weight = weights[i];
            if (Double.isNaN(weight))
                throw new IllegalArgumentException("NaN weight at " + i);
            if (Double.isInfinite(weight))
                throw new IllegalArgumentException("Infinite value at " + i);
            sum += values[i] * weights[i];
        }
        return sum;
    }

    public static double mean(Collection<Double> values) {
        checkArgument(values != null && !values.isEmpty());

        double sum = sum(values);
        return sum / values.size();
    }

    /**
     * Return the mean of the values.
     *
     * @param values a Double array.
     * @return mean of the array values.
     */
    public static double mean(Double[] values) {
        return sum(values) / values.length;
    }

    /**
     * Return the mean value of a given dataset
     *
     * @param values dataset
     * @return mean value
     */
    public static double mean(double[] values) {
        return sum(values) / values.length;
    }

    /**
     * Return variance of the dataset.
     *
     * @param values data set
     * @param mean   data set mean
     * @param isBias true calculate sample variance
     * @return variance of the data.
     */
    public static double variance(final Collection<Double> values, final double mean, boolean isBias) {
        if (values.isEmpty())
            return Double.NaN;
        int len = values.size();
        if (len == 1) {
            return 0.;
        } else {
            double sum = 0.;
            double dev;
            for (Double value : values) {
                dev = value - mean;
                sum += dev * dev;
            }
            if (isBias) {
                return sum / (len - 1.);
            } else
                return sum / len;
        }
    }

    /**
     * Return variance of a sample dataset
     *
     * @param values data set
     * @return variance of the data.
     */
    public static double variance(final Collection<Double> values) {
        return variance(values, true);
    }

    /**
     * Return variance of the data
     *
     * @param values data set
     * @return variance of the data.
     */
    public static double variance(final Collection<Double> values, boolean isBias) {
        double mean = mean(values);
        return variance(values, mean, isBias);
    }

    /**
     * Calculate the variance.
     *
     * @param values double array
     * @param mean   mean value
     * @param begin  start index
     * @param length data number
     * @param isBias Whether bias correction is applied when computing the value of the statistics.
     * @return variance of the data, return {@link Double#NaN} if the data is empty.
     */
    public static double variance(final double[] values, final double mean, final int begin, final int length, final boolean isBias) {
        double var = Double.NaN;
        if (length == 1) {
            var = 0.;
        } else if (length > 1) {
            double sum = 0.;
            double dev;
            for (int i = begin; i < begin + length; i++) {
                dev = values[i] - mean;
                sum += dev * dev;
            }
            if (isBias) {
                var = sum / (length - 1.0);
            } else
                var = sum / (length);
        }
        return var;
    }

    /**
     * Calculate the variance of the double array.
     *
     * @param values double array
     * @param bias   Whether bias correction is applied when computing the value of the statistics.
     * @return variance of the data.
     */
    public static double variance(final double[] values, boolean bias) {
        double mean = mean(values);
        return variance(values, mean, 0, values.length, bias);
    }

    /**
     * Calculate the variance of the double array.
     *
     * @param values double array
     * @param mean   mean
     * @return variance of the data.
     */
    public static double variance(final double[] values, double mean) {
        return variance(values, mean, 0, values.length, true);
    }

    /**
     * Calculate the variance of the double array.
     *
     * @param values double array
     * @return variance of the data.
     */
    public static double variance(final double[] values) {
        double mean = mean(values);
        return variance(values, mean, 0, values.length, true);
    }

    /**
     * Return the standard deviation of given double values
     *
     * @param values double values
     * @return standard deviation
     */
    public static double standardDeviation(Collection<Double> values) {
        return Math.sqrt(variance(values));
    }

    /**
     * Return the standard deviation of given sample data.
     *
     * @param values values
     * @return standard deviation.
     */
    public static double standardDeviation(final double[] values) {
        return Math.sqrt(variance(values));
    }

    /**
     * Return the relative standard deviation (RSD) of given double array.
     *
     * @param values a {@link Double} array.
     * @return relative standard deviation of the double array.
     */
    public static double rsd(double[] values) {
        double mean = mean(values);
        double variance = variance(values, mean, 0, values.length, true);
        return Math.sqrt(variance) / mean;
    }

    /**
     * Return the relative standard deviation (RSD) of given double values
     *
     * @param values double values
     * @return relative standard deviation
     */
    public static double rsd(Collection<Double> values) {
        double mean = mean(values);
        double variance = variance(values, mean, true);
        return Math.sqrt(variance) / mean;
    }

    /**
     * Rounds a double value to the wanted number of decimal places.
     *
     * @param value         the value to scale
     * @param decimalPlaces the number of decimal places
     * @return a scaled double to the indicated decimal places
     */
    public static double round(double value, int decimalPlaces) {
        checkArgument(decimalPlaces >= 0);

        BigDecimal bd = BigDecimal.valueOf(value).setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Find the first quantile (1/4) of an array
     *
     * @param values double array
     * @return the first quantile
     */
    public static double q1(double[] values) {
        return StatUtils.percentile(values, 25);
    }

    /**
     * Find the median of an array
     *
     * @param values double array
     * @return median
     */
    public static double median(double[] values) {
        return StatUtils.percentile(values, 50);
    }

    public static double q3(double[] values) {
        return StatUtils.percentile(values, 75);
    }
}
