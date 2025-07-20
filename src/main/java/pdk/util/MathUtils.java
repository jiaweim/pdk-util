package pdk.util;

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

    public static double mean(Collection<Double> values) {
        checkArgument(values != null && !values.isEmpty());

        double sum = sum(values);
        return sum / values.size();
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
     * Return the standard deviation of given double values
     *
     * @param values double values
     * @return standard deviation
     */
    public static double standardDeviation(Collection<Double> values) {
        return Math.sqrt(variance(values));
    }
}
