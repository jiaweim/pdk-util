package pdk.util.math;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.apache.commons.math3.stat.StatUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static pdk.util.ArgUtils.*;

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
     * Return the sum of entries in specified portion of the input array, or 0 if the subarray is empty
     *
     * @param values the input array
     * @param begin  the index of the first array element to include
     * @param length the number of elements to include
     * @return sum of the values or 0 if length=0
     */
    public static double sum(final double[] values, final int begin, final int length) {
        double sum = 0.0;
        for (int i = begin; i < begin + length; i++) {
            sum += values[i];
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
     * Return the arithmetic mean of the entries in the specified portion of the input array.
     *
     * @param values the input array
     * @param begin  index of the first element to include (0-based)
     * @param length number of elements to include
     * @return the mean of the values
     * @since 2025-02-18 ⭐
     */
    public static double mean(final double[] values, final int begin, final int length) {
        checkNotNull(values);
        checkNonNegative("index", begin);
        checkNonNegative("length", length);

        if (begin + length > values.length) {
            throw new IllegalArgumentException("End index " + (begin + length) + " exceed the array length " + values.length);
        }
        double sum = sum(values, begin, length);
        return sum / length;
    }

    /**
     * Return the mean value of an array
     *
     * @param values array of values
     * @return mean
     */
    public static double mean(double... values) {
        return mean(values, 0, values.length);
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
        if (values.length != weights.length)
            throw new IllegalArgumentException("The number of values and weights should be same");

        double numerator = 0;
        double denominator = 0;
        for (int i = 0; i < values.length; i++) {
            numerator += values[i] * weights[i];
            denominator += weights[i];
        }
        return numerator / denominator;
    }

    /**
     * calculate the weighted average of a given sample
     *
     * @param values  values
     * @param weights weight values
     * @param indexes sample indexes
     * @return weighted average, {@link Double#NaN} if the sum of weights is zero
     */
    public static double mean(double[] values, double[] weights, int[] indexes) {
        if (values.length != weights.length)
            throw new IllegalArgumentException("The number of values and weights should be same");

        double sum = 0;
        double weightSum = 0;
        for (int index : indexes) {
            sum += values[index] * weights[index];
            weightSum += weights[index];
        }
        return sum / weightSum;
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
     * Calculate the sample variance of the double array.
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
     * Returns the standard deviation of the entries in the input array, or
     * <code>Double.NaN</code> if the array is empty.
     * <p>
     * This method returns the bias-corrected sample standard deviation (using {@code n - 1} in
     * the denominator). Use {@link #populationVariance(double[])} for the non-bias-corrected
     * population variance.
     * <p>
     * Returns 0 for a single-value (i.e. length = 1) sample.
     * <p>
     * Throws <code>MathIllegalArgumentException</code> if the array is null.
     *
     * @param values the input array
     * @return the variance of the values or Double.NaN if the array is empty
     * @since 2025-02-18
     */
    public static double standardDeviation(double[] values, double mean) {
        double variance = variance(values, mean);
        return Math.sqrt(variance);
    }

    /**
     * Returns the <a href="http://en.wikibooks.org/wiki/Statistics/Summary/Variance">
     * population variance</a> of the entries in the input array, or
     * <code>Double.NaN</code> if the array is empty.
     * <p>
     * Returns 0 for a single-value (i.e. length = 1) sample.
     *
     * @param values the input array
     * @return the population variance of the values or Double.NaN if the array is empty
     */
    public static double populationVariance(double[] values) {
        return variance(values, false);
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
        double variance = populationVariance(values);
        return Math.sqrt(variance);
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

    /**
     * return the median of the values
     *
     * @param values array of input values
     * @param begin  the first element to include
     * @param length number of array element to include
     * @return median
     */
    public static double median(double[] values, int begin, int length) {
        return StatUtils.percentile(values, begin, length, 50);
    }

    /**
     * Find the third quantile (3/4) of an array
     *
     * @param values double array
     * @return the third quantile
     */
    public static double q3(double[] values) {
        return StatUtils.percentile(values, 75);
    }

    /**
     * Returns an estimate of the <code>p</code>th percentile of the values
     * in the <code>values</code> array.
     * <p>
     * Calls to this method do not modify the internal <code>quantile</code>
     * state of this statistic.</p>
     * <ul>
     * <li>Returns <code>Double.NaN</code> if <code>values</code> has length
     * <code>0</code></li>
     * <li>Returns (for any value of <code>p</code>) <code>values[0]</code>
     *  if <code>values</code> has length <code>1</code></li>
     * <li>Throws <code>MathIllegalArgumentException</code> if <code>values</code>
     * is null or p is not a valid quantile value (p must be greater than 0
     * and less than or equal to 100) </li>
     * </ul>
     * <p>
     *
     * @param values input array of values
     * @param p      the percentile value to compute
     * @return the percentile value or Double.NaN if the array is empty
     * @since 2025-02-18⭐
     */
    public static double percentile(double[] values, final double p) {
        return StatUtils.percentile(values, p);
    }

    /**
     * same as {@link #percentile(double[], double)}
     *
     * @param values values
     * @param p      the percentile value
     * @return percentile
     */
    public static double percentile(Collection<Double> values, final double p) {
        double[] vs = new double[values.size()];
        int i = 0;
        for (Double value : values) {
            vs[i] = value;
            i++;
        }
        return percentile(vs, p);
    }

    /**
     * Return an estimate of the pth percentile of the values.
     *
     * @param values array of input values
     * @param begin  the first element to include (0-based)
     * @param length number of array element to include
     * @param p      the percentile to compute
     * @return the percentile value
     * @since 2025-02-18 ⭐
     */
    public static double percentile(final double[] values, final int begin, final int length, final double p) {
        return StatUtils.percentile(values, begin, length, p);
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
            if (array[i] > max) {
                max = array[i];
            }
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
            if (array[i] > max) {
                max = array[i];
            }
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
            if (array[i] < min) {
                min = array[i];
            }
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
            if (array[i] < min) {
                min = array[i];
            }
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
        checkNotNull(sample);
        checkNonNegative("index", begin);
        checkNonNegative("length", length);

        return getMode(sample, begin, length);
    }

    /**
     * @param values input date
     * @param begin  begin index (0-based)
     * @param length number of elements to include
     * @return array of most frequently occuring element(s) sorted in ascending order.
     */
    private static double[] getMode(double[] values, final int begin, final int length) {
        Counter<Double> counter = new Counter<>();
        for (int i = begin; i < begin + length; i++) {
            double value = values[i];
            if (!Double.isNaN(value)) {
                counter.increase(value);
            }
        }
        List<Double> modes = counter.getMode();
        modes.sort(Comparator.naturalOrder());

        return modes.stream().mapToDouble(Double::doubleValue).toArray();
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
     * Calculation all combinations of choose K element from input. Duplicate value is not allowed.
     * This algorithm adapt from
     * http://www.geeksforgeeks.org/print-all-possible-combinations-of-r-elements-in-a-given-array-of-size-n/
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

        List<int[]> resultList = new ArrayList<>();
        int[] data = new int[k];
        permutation(input, data, 0, input.length - 1, 0, k, resultList);
        return resultList;
    }

    /**
     * Generate all combinations of given input data.
     *
     * @param input      input data.
     * @param data       temporary array to store current combination.
     * @param start      start index in input
     * @param end        end index in input
     * @param index      current index in data
     * @param k          size of a combination
     * @param resultList List to store the result permutations.⭐
     */
    private static void permutation(int[] input, int[] data, int start, int end, int index, int k,
            List<int[]> resultList) {
        if (index == k) {
            resultList.add(Arrays.copyOf(data, k));
            return;
        }

        // replace index with all possible elements. The condition "end-i+1 >= r-index" makes sure that including
        // one element at index will make a combination with remaining elements at remaining positions
        for (int i = start; i <= end && end - i + 1 >= k - index; i++) {
            data[index] = input[i];
            permutation(input, data, i + 1, end, index + 1, k, resultList);
        }
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
     * @return all combinations.⭐
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
