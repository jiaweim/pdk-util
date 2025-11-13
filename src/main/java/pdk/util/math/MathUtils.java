package pdk.util.math;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleComparators;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.apache.commons.numbers.combinatorics.BinomialCoefficient;
import org.apache.commons.numbers.combinatorics.Combinations;
import org.apache.commons.numbers.combinatorics.Factorial;
import org.apache.commons.numbers.core.Precision;
import org.apache.commons.numbers.core.Sum;
import org.apache.commons.statistics.descriptive.*;
import pdk.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static pdk.util.ArgUtils.*;

/**
 * Math utilities.
 * <p>
 * Most statistics are calculated using commons-statistics, which uses more precise floating point calculations.
 *
 * @author Jiawei Mao
 * @version 1.1.0
 * @since 20 Jul 2025, 7:01 PM
 */
public final class MathUtils {

    private MathUtils() {}

    /**
     * Return the factorial of <code>n</code>.
     * <p>
     * The result should be small enough to fit into a {@code double}: The largest {@code n} for which {@code n!}
     * does not exceed {@code Double.MAX_VALUE} is 170. {@code Double.POSITIVE_INFINITY} is returned for {@code n > 170}.
     *
     * @param n positive number, at most 170
     * @return {@code n!}
     */
    public static double factorial(int n) {
        return Factorial.doubleValue(n);
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
     * @return true if the two double value equals within the accuracy⭐
     */
    public static boolean isEqual(double value1, double value2, double delta) {
        return Precision.equals(value1, value2, delta);
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
        requireNonNull(values);
        Sum sum = Sum.create();
        for (Double value : values) {
            sum.add(value);
        }
        return sum.getAsDouble();
    }

    /**
     * Return the sum of the array.
     *
     * @param values double array
     * @return sum of all values.⭐
     */
    public static double sum(double[] values) {
        requireNonNull(values);
        return Sum.of(values).getAsDouble();
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
        requireNonNull(values);
        Sum sum = Sum.create();
        for (int i = begin; i < begin + length; i++) {
            sum.add(values[i]);
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
     * Rounds a double value to the wanted number of decimal places.
     *
     * @param value         the value to scale
     * @param decimalPlaces the number of decimal places
     * @return a scaled double to the indicated decimal places
     */
    public static double round(double value, int decimalPlaces) {
        return Precision.round(value, decimalPlaces);
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


    /**
     * The input is an array of box, and the array value is the number of balls.
     * we need to get a ball from each of the box, with all combinations. such as array:
     * [1, 2, 3]
     * <p>
     * It is mean the 1st box has 1 ball, the 2ed box has 2 balls, the 3rd box has 3 balls, possible combinations are:
     * 1 * 2 * 3 = 6
     * <p>
     * [0, 0, 0], [0, 0, 1], [0, 0, 2], [0, 1, 0], [0, 1, 1], [0, 1, 2]
     * <p>
     * the values of the combinations can be treated as the index of the ball in the box.
     *
     * @param input input array.
     * @return all combinations.⭐
     */
    public static List<int[]> getCombinations(int[] input) {
        List<int[]> resultList = new ArrayList<>();
        int size = input.length;

        // check argument, values less than 0 is not allowed.
        if (size < 1)
            return resultList;

        for (int value : input) {
            if (value <= 0)
                return resultList;
        }

        List<IntList> results = new ArrayList<>();
        int first = input[0];
        for (int i = 0; i < first; i++) {
            IntList list = new IntArrayList();
            list.add(i);
            results.add(list);
        }

        if (size > 1) {
            for (int boxId = 1; boxId < size; boxId++) {
                List<IntList> tmpList = new ArrayList<>();
                for (int ballId = 0; ballId < input[boxId]; ballId++) {
                    for (IntList list : results) {
                        IntList newList = new IntArrayList(list);
                        newList.add(ballId);
                        tmpList.add(newList);
                    }
                }

                results = tmpList;
            }
        }

        for (IntList list : results) {
            resultList.add(list.toIntArray());
        }
        return resultList;
    }


    /**
     * Calculation all combinations of choose K element from input. Duplicate values result in duplicate combinations
     * <p>
     * for array [1, 2, 3]:
     * permutation(array, 2) could generate [1, 2], [1, 3], [2, 3]
     *
     * @param input input data, duplicate value is not allowed.
     * @param k     K value
     * @return all combinations.⭐
     */
    public static List<int[]> permutation(int[] input, int k) {
        checkArgument(k > 0, "K should > 0");

        List<int[]> resultList = new ArrayList<>((int) BinomialCoefficient.value(input.length, k));
        for (int[] combine : Combinations.of(input.length, k)) {
            int[] sample = ArrayUtils.sample(input, combine);
            resultList.add(sample);
        }
        return resultList;
    }

    /**
     * Calculation all combinations of choose K element from input. Duplicate values are allowed.
     * This algorithm adopt from
     * http://www.geeksforgeeks.org/print-all-possible-combinations-of-r-elements-in-a-given-array-of-size-n/
     * <p>
     * <pre>
     * {@code
     *  int arr[] = {1, 2, 2, 4, 5};
     *  int k = 3;
     *  List<int[]> list = MathUtils.permutationDup(arr, k);
     *  assertEquals(7, list.size());
     *  assertArrayEquals(new int[]{1, 2, 2}, list.get(0));
     *  assertArrayEquals(new int[]{1, 2, 4}, list.get(1));
     *  assertArrayEquals(new int[]{1, 2, 5}, list.get(2));
     *  assertArrayEquals(new int[]{1, 4, 5}, list.get(3));
     *  assertArrayEquals(new int[]{2, 2, 4}, list.get(4));
     *  assertArrayEquals(new int[]{2, 2, 5}, list.get(5));
     *  assertArrayEquals(new int[]{2, 4, 5}, list.get(6));
     * }
     * </pre>
     *
     * @param input input data, duplicate values are allowed.
     * @param k     K value, should > 0.
     * @return all combinations.⭐⭐
     */
    public static List<int[]> permutationDup(int[] input, int k) {
        checkArgument(k > 0, "K should > 0");
        checkArgument(k <= input.length, "The K should <= the input length.");

        List<int[]> resultList = new ArrayList<>();
        int[] data = new int[k];
        Arrays.sort(input);

        permutationDup(input, data, 0, input.length - 1, 0, k, resultList);
        return resultList;
    }

    /**
     * Generate all combinations of given input data, duplicate values are allowed.
     *
     * @param input      input data, should in ascending order.
     * @param data       temporary array to store current combination.
     * @param start      start index in input
     * @param end        end index in input
     * @param index      current index in data
     * @param k          size of a combination
     * @param resultList List to store the result permutations.
     */
    private static void permutationDup(int[] input, int[] data, int start, int end, int index, int k, List<int[]>
            resultList) {
        if (index == k) {
            resultList.add(Arrays.copyOf(data, k));
            return;
        }

        // replace index with all possible elements. The condition "end-i+1 >= r-index" makes sure that including
        // one element at index will make a combination with remaining elements at remaining positions
        for (int i = start; i <= end && end - i + 1 >= k - index; i++) {
            data[index] = input[i];
            permutationDup(input, data, i + 1, end, index + 1, k, resultList);

            while (i < end && input[i] == input[i + 1])
                i++;
        }
    }
}
