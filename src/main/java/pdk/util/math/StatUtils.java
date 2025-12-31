package pdk.util.math;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleComparators;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import org.apache.commons.numbers.core.Sum;
import org.apache.commons.statistics.descriptive.*;
import pdk.util.tuple.Tuple;
import pdk.util.tuple.Tuple2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static pdk.util.ArgUtils.*;

/**
 * Statistic utilities.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 31 Dec 2025, 5:10 PM
 */
public final class StatUtils {

    private StatUtils() {}

    /**
     * Returns the least value present in {@code array}.
     *
     * @param array a <i>nonempty</i> array of {@code int} values
     * @return the value present in {@code array} that is less than or equal to every other value in
     * the array
     * @throws IllegalArgumentException if {@code array} is empty
     */
    public static int min(int... array) {
        checkArgument(array.length > 0);
        int min = array[0];
        for (int i = 1; i < array.length; i++) {
            min = Math.min(min, array[i]);
        }
        return min;
    }

    /**
     * Returns the least value present in {@code array}.
     *
     * @param array a <i>nonempty</i> array of {@code long} values
     * @return the value present in {@code array} that is less than or equal to every other value in
     * the array
     * @throws IllegalArgumentException if {@code array} is empty
     */
    public static long min(long... array) {
        checkArgument(array.length > 0);
        long min = array[0];
        for (int i = 1; i < array.length; i++) {
            min = Math.min(min, array[i]);
        }
        return min;
    }


    /**
     * Returns the greatest value present in {@code collection}.
     *
     * @param collection a collection of double values
     * @return the value present in {@code collection} that is greater than or equal to every other value
     * in the collection
     */
    public static double max(Collection<Double> collection) {
        checkNotNull(collection);
        checkArgument(!collection.isEmpty());
        double max = Double.NEGATIVE_INFINITY;
        for (Double value : collection) {
            max = Math.max(max, value);
        }
        return max;
    }


    /**
     * Returns the greatest value present in {@code array}.
     *
     * @param array a <i>nonempty</i> array of {@code int} values
     * @return the value present in {@code array} that is greater than or equal to every other value
     * in the array
     * @throws IllegalArgumentException if {@code array} is empty
     */
    public static int max(int... array) {
        checkArgument(array.length > 0);

        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            max = Math.max(max, array[i]);
        }
        return max;
    }

    /**
     * Returns the greatest value present in {@code array}.
     *
     * @param array a <i>nonempty</i> array of {@code long} values
     * @return the value present in {@code array} that is greater than or equal to every other value
     * in the array
     * @throws IllegalArgumentException if {@code array} is empty
     */
    public static long max(long... array) {
        checkArgument(array.length > 0);

        long max = array[0];
        for (int i = 1; i < array.length; i++) {
            max = Math.max(max, array[i]);
        }
        return max;
    }

    /**
     * Return the greatest value present in {@code values} with its index
     *
     * @param values a list of double values
     * @return the greatest value with its index
     */
    public static Tuple2<Integer, Double> maxIndex(List<Double> values) {
        double max = Double.NEGATIVE_INFINITY;
        int index = -1;
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) > max) {
                max = values.get(i);
                index = i;
            }
        }
        return Tuple.of(index, max);
    }

    /**
     * Return the greatest value present in {@code values} with its index
     *
     * @param values a list of double values
     * @return the greatest value with its index
     */
    public static Tuple2<Integer, Double> maxIndex(double[] values) {
        double max = Double.NEGATIVE_INFINITY;
        int index = -1;
        for (int i = 0; i < values.length; i++) {
            if (values[i] > max) {
                max = values[i];
                index = i;
            }
        }
        return Tuple.of(index, max);
    }


    /**
     * Return the mean of double collections
     *
     * @param values dataset
     * @return mean⭐
     */
    public static double mean(Collection<Double> values) {
        checkArgument(values != null && !values.isEmpty());
        Mean mean = Mean.create();
        for (Double value : values) {
            mean.accept(value);
        }
        return mean.getAsDouble();
    }

    /**
     * Return the mean of the values.
     *
     * @param values a Double array.
     * @return mean of the array values.
     */
    public static double mean(Double[] values) {
        requireNonNull(values);
        Mean mean = Mean.create();
        for (Double value : values) {
            mean.accept(value);
        }
        return mean.getAsDouble();
    }

    /**
     * Return the arithmetic mean of the entries in the specified portion of the input array.
     *
     * @param values the input array
     * @param begin  index of the first element to include (0-based)
     * @param length number of elements to include
     * @return the mean of the values
     * @since 2025-02-18 ⭐⭐
     */
    public static double mean(final double[] values, final int begin, final int length) {
        requireNonNull(values);
        checkNonNegative(begin, "index");
        checkNonNegative(length, "length");

        if (begin + length > values.length) {
            throw new IllegalArgumentException("End index " + (begin + length) + " exceed the array length " + values.length);
        }
        Mean mean = Mean.create();
        for (int i = begin; i < (begin + length); i++) {
            mean.accept(values[i]);
        }
        return mean.getAsDouble();
    }

    /**
     * Return the mean value of an array
     *
     * @param values array of values
     * @return mean⭐
     */
    public static double mean(double... values) {
        return Mean.of(values).getAsDouble();
    }

    /**
     * calculate the weighted average: \sum{xw}/\sum{w}
     *
     * @param values  values
     * @param weights weight values
     * @return weighted average, {@link Double#NaN} if the sum of weights is zero
     * @since 2025-02-14 ⭐
     */
    public static double mean(double[] values, double[] weights) {
        double weightedSum = weightedSum(values, weights);
        double sum = sum(weights);
        return weightedSum / sum;
    }

    /**
     * Return sum of values in array.
     * <p>
     * If there are no values in the dataset, 0 is returned.
     *
     * @param array an int array
     * @return sum⭐
     */
    public static int sum(int[] array) {
        int sum = 0;
        for (int j : array) {
            sum += j;
        }
        return sum;
    }

    /**
     * Return the sum of the array.
     *
     * @param values double array
     * @return sum of all values.⭐
     */
    public static double sum(double[] values) {
        checkNotNull(values);

        return Sum.of(values).getAsDouble();
    }

    /**
     * Return sum of values in array.
     * <p>
     * If there are no values in the dataset, 0 is returned. If any of the values are {@link Double#NaN},
     * {@link Double#NaN} is returned.
     *
     * @param values a Double array
     * @return sum⭐
     */
    public static double sum(Double[] values) {
        checkNotNull(values);

        Sum sum = Sum.create();
        for (Double value : values) {
            sum.add(value);
        }
        return sum.getAsDouble();
    }


    /**
     * Return the sum of double values
     *
     * @param values dataset
     * @return sum of data in the dataset⭐
     */
    public static double sum(Iterable<Double> values) {
        requireNonNull(values);

        Sum sum = Sum.create();
        for (Double value : values) {
            sum.add(value);
        }
        return sum.getAsDouble();
    }

    /**
     * Return the sum of entries in specified portion of the input array, or 0 if the subarray is empty
     *
     * @param values the input array
     * @param begin  the index of the first array element to include
     * @param length the number of elements to include
     * @return sum of the values or 0 if length=0⭐
     */
    public static double sum(final double[] values, final int begin, final int length) {
        checkNotNull(values);

        Sum sum = Sum.create();
        for (int i = begin; i < begin + length; i++) {
            sum.add(values[i]);
        }
        return sum.getAsDouble();
    }

    /**
     * calculate the weighted average of a given sample
     *
     * @param values  values
     * @param weights weight values
     * @param indexes sample indexes
     * @return weighted average, {@link Double#NaN} if the sum of weights is zero
     */
    public static double weightedMean(double[] values, double[] weights, int[] indexes) {
        if (values.length != weights.length)
            throw new IllegalArgumentException("The number of values and weights should be same");

        Sum weightSum = Sum.create();
        Sum sum = Sum.create();
        for (int index : indexes) {
            sum.addProduct(values[index], weights[index]);
            weightSum.add(weights[index]);
        }
        return sum.getAsDouble() / weightSum.getAsDouble();
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
     * @return weighted sum⭐
     */
    public static double weightedSum(final double[] values, final double[] weights) {
        requireNonNull(values);
        requireNonNull(weights);

        return Sum.ofProducts(values, weights).getAsDouble();
    }


    /**
     * Return variance of the data
     *
     * @param values data set
     * @param isBias false means use (n-1)
     * @return variance of the data.
     */
    public static double variance(final Collection<Double> values, boolean isBias) {
        Variance variance = Variance.create().setBiased(isBias);
        for (Double value : values) {
            variance.accept(value);
        }
        return variance.getAsDouble();
    }

    /**
     * Calculate the sample variance of the double array.
     * <p>
     * The unbiased (n-1) is used for calculation.
     *
     * @param values double array
     * @return variance of the data.
     */
    public static double variance(final double[] values) {
        return Variance.of(values).getAsDouble();
    }

    /**
     * Calculate the variance of the double array.
     *
     * @param values double array
     * @param bias   Whether bias correction is applied when computing the value of the statistics, false means using the
     *               unbiased (n-1)
     * @return variance of the data.
     */
    public static double variance(final double[] values, boolean bias) {
        return Variance.of(values).setBiased(bias).getAsDouble();
    }

    /**
     * Return variance of a sample
     *
     * @param values data set
     * @return variance of the data.
     */
    public static double variance(final Collection<Double> values) {
        Variance variance = Variance.create();
        for (Double value : values) {
            variance.accept(value);
        }
        return variance.getAsDouble();
    }


    /**
     * Return the standard deviation of given double values
     *
     * @param values double values
     * @return standard deviation
     */
    public static double standardDeviation(Collection<Double> values) {
        StandardDeviation standardDeviation = StandardDeviation.create();
        for (Double value : values) {
            standardDeviation.accept(value);
        }
        return standardDeviation.getAsDouble();
    }

    /**
     * Return the standard deviation of given sample.
     *
     * @param values sample data
     * @return standard deviation.
     */
    public static double standardDeviation(final double[] values) {
        return StandardDeviation.of(values).getAsDouble();
    }


    /**
     * Returns the <a href="http://en.wikibooks.org/wiki/Statistics/Summary/Variance">
     * population variance</a> of the entries in the input array, or
     * <code>Double.NaN</code> if the array is empty.
     *
     * @param values the input array
     * @return the population variance of the values or Double.NaN if the array is empty
     */
    public static double populationVariance(double[] values) {
        return Variance.of(values).setBiased(true).getAsDouble();
    }

    /**
     * Returns the standard deviation of the entries in the input array, or
     * <code>Double.NaN</code> if the array is empty.
     * <p>
     * Returns 0 for a single-value (i.e. length = 1) sample.
     * <p>
     * Throws <code>MathIllegalArgumentException</code> if the array is null.
     *
     * @param values the input array
     * @return the variance of the values or Double.NaN if the array is empty
     */
    public static double populationStandardDeviation(double[] values) {
        return StandardDeviation.of(values).setBiased(true).getAsDouble();
    }


    /**
     * Return the relative standard deviation (RSD) of given double array.
     *
     * @param values a {@link Double} array.
     * @return relative standard deviation of the double array.
     */
    public static double rsd(double[] values) {
        DoubleStatistics statistics = statistics(values, Statistic.MEAN, Statistic.STANDARD_DEVIATION);
        return statistics.getAsDouble(Statistic.STANDARD_DEVIATION) / statistics.getAsDouble(Statistic.MEAN);
    }


    /**
     * Calculate a set of {@link Statistic}, support:
     * <p>
     * <ul>
     *     <li>{@link Statistic#GEOMETRIC_MEAN}</li>
     *     <li>{@link Statistic#KURTOSIS}</li>
     *     <li>{@link Statistic#MAX}</li>
     *     <li>{@link Statistic#MEAN}</li>
     *     <li>{@link Statistic#MIN}</li>
     *     <li>{@link Statistic#PRODUCT}</li>
     *     <li>{@link Statistic#SKEWNESS}</li>
     *     <li>{@link Statistic#STANDARD_DEVIATION}</li>
     *     <li>{@link Statistic#SUM}</li>
     *     <li>{@link Statistic#SUM_OF_LOGS}</li>
     *     <li>{@link Statistic#SUM_OF_SQUARES}</li>
     *     <li>{@link Statistic#VARIANCE}</li>
     * </ul>
     *
     * @param values     dataset used for calculation
     * @param statistics {@link Statistic} to calculate
     * @return {@link DoubleStatistics}
     */
    public static DoubleStatistics statistics(double[] values, Statistic... statistics) {
        return DoubleStatistics.builder(statistics).build(values);
    }


    /**
     * Return the sample mode(s).
     * <p>
     * The mode is the most frequently occurring value in the sample.
     * If there is a unique value with maximum frequency, this value is returned as the
     * only element of the output array. Otherwise, the returned array contains the
     * maximum frequency elements in natural order.
     * <p>
     * NaN values are ignored when computing the mode.
     *
     * @param sample input data
     * @return array of the most frequently occurring elements sorted in natural order.
     * @since 2025-02-18⭐
     */
    public static double[] mode(double[] sample) {
        checkNotNull(sample);

        return getMode(sample, 0, sample.length);
    }

    /**
     * Return the sample mode(s).
     * <p>
     * The mode is the most frequently occurring value in the sample.
     * If there is a unique value with maximum frequency, this value is returned as the
     * only element of the output array. Otherwise, the returned array contains the
     * maximum frequency elements in natural order.
     * <p>
     * NaN values are ignored when computing the mode.
     *
     * @param sample input data
     * @param begin  index (0-based)
     * @param length number of elements to include
     * @return array of the most frequently occurring elements sorted in natural order.
     * @since 2025-02-18⭐
     */
    public static double[] mode(double[] sample, final int begin, final int length) {
        requireNonNull(sample);
        checkNonNegative(begin, "index");
        checkNonNegative(length, "length");

        return getMode(sample, begin, length);
    }

    /**
     * @param values input date
     * @param begin  begin index (0-based)
     * @param length number of elements to include
     * @return array of most frequently occurring element(s) sorted in ascending order.
     */
    private static double[] getMode(double[] values, final int begin, final int length) {
        Multiset<Double> set = HashMultiset.create();
        for (int i = begin; i < begin + length; i++) {
            double value = values[i];
            if (!Double.isNaN(value)) {
                set.add(value);
            }
        }
        int maxCount = 0;
        for (Multiset.Entry<Double> entry : set.entrySet()) {
            int count = entry.getCount();
            maxCount = Math.max(maxCount, count);
        }
        DoubleList modeList = new DoubleArrayList();
        for (Multiset.Entry<Double> entry : set.entrySet()) {
            if (entry.getCount() == maxCount) {
                modeList.add(entry.getElement());
            }
        }

        modeList.sort(DoubleComparators.NATURAL_COMPARATOR);
        return modeList.toDoubleArray();
    }

    /**
     * Return modes in a {@link Multiset}, {@link Multiset} holds element types and counts.
     *
     * @param set {@link Multiset}
     * @param <E> element type
     * @return list of all modes (with same maximum count)
     */
    public static <E> List<E> getMode(Multiset<E> set) {
        int maxCount = 0;
        for (Multiset.Entry<E> entry : set.entrySet()) {
            int count = entry.getCount();
            maxCount = Math.max(maxCount, count);
        }
        List<E> modeList = new ArrayList<>();
        for (Multiset.Entry<E> entry : set.entrySet()) {
            if (entry.getCount() == maxCount) {
                modeList.add(entry.getElement());
            }
        }
        return modeList;
    }
}
