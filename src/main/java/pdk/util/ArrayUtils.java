package pdk.util;

import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.ints.IntComparators;
import org.apache.commons.numbers.arrays.Selection;

import java.util.*;

import static java.util.Objects.checkFromToIndex;
import static java.util.Objects.requireNonNull;
import static pdk.util.ArgUtils.checkArgument;

/**
 * Array utilities
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 20 Jul 2025, 9:56 PM
 */
public final class ArrayUtils {

    private ArrayUtils() {}

    /**
     * Natural comparator for double
     */
    public static final Comparator<Double> DOUBLE_NATURAL_COMPARATOR = Double::compare;
    /**
     * Opposite comparator for double
     */
    public static final Comparator<Double> DOUBLE_OPPOSITE_COMPARATOR = (o1, o2) -> Double.compare(o2, o1);

    /**
     * An empty immutable {@code double} array.
     */
    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
    /**
     * An empty immutable {@code char} array.
     */
    public static final char[] EMPTY_CHAR_ARRAY = {};

    /**
     * Return a list of int in given range
     *
     * @param startInclusive start inclusive
     * @param endInclusive   end inclusive
     * @return list of int
     */
    public static List<Integer> rangeClosed(int startInclusive, int endInclusive) {
        List<Integer> list = new ArrayList<>(endInclusive - startInclusive + 1);
        for (int i = startInclusive; i <= endInclusive; i++) {
            list.add(i);
        }
        return list;
    }

    /**
     * Repeat the specified double value {@code k} times to obtain an array
     *
     * @param v value to repeat
     * @param k k times
     * @return double array
     * @since 2025-12-17⭐
     */
    public static double[] repeat(double v, int k) {
        double[] array = new double[k];
        Arrays.fill(array, v);
        return array;
    }

    /**
     * Take a sample from given dataset
     *
     * @param dataset       dataset
     * @param sampleIndexes indexes in the {@code dataset} of the sample
     * @return sample data
     */
    public static int[] sample(int[] dataset, int[] sampleIndexes) {
        int[] sample = new int[sampleIndexes.length];
        for (int i = 0; i < sampleIndexes.length; i++) {
            sample[i] = dataset[sampleIndexes[i]];
        }
        return sample;
    }

    /**
     * Return the element for which its rank is k in descending order, that is the kth largest element.
     * Its behavior is consistent with a fully sorted array.
     * <p>
     * <b>Warning, this operation will change the order of the array elements</b>
     *
     * @param data double array
     * @param k    element
     * @return the kth largest element (1-based)
     * @since 2025-11-05⭐
     */
    public static double greatest(double[] data, int k) {
        checkArgument(k >= 1 && k <= data.length, "k should in range [1," + data.length + "]");

        int idx = data.length - k;
        Selection.select(data, idx);
        return data[idx];
    }

    /**
     * Return the element for which its rank is {@code k} in descending order, that is the kth largest element.
     * Its behavior is consistent with a fully sorted array.
     * <p>
     * <b>Warning, this operation will change the order of the array elements</b>
     *
     * @param data double array
     * @param k    element
     * @return the kth largest element (1-based)
     * @since 2025-11-17⭐
     */
    public static int greatest(int[] data, int k) {
        checkArgument(k >= 1 && k <= data.length, "k should in range [1," + data.length + "]");

        int idx = data.length - k;
        Selection.select(data, idx);
        return data[idx];
    }

    /**
     * Return the kth least element.
     * <p>
     * <b>Warning, this operation will change the order of the array elements</b>
     *
     * @param data double array
     * @param k    1-based rank
     * @return the pivot value at {@code k}
     * @since 2025-11-05⭐
     */
    public static double least(double[] data, int k) {
        checkArgument(k >= 1 && k <= data.length, "k should in range [1," + data.length + "]");
        int idx = k - 1;
        Selection.select(data, idx);
        return data[idx];
    }

    /**
     * Return the kth least element.
     * <p>
     * <b>Warning, this operation will change the order of the array elements</b>
     *
     * @param data double array
     * @param k    1-based rank
     * @return the pivot value at {@code k}
     * @since 2025-11-05⭐
     */
    public static int least(int[] data, int k) {
        checkArgument(k >= 1 && k <= data.length, "k should in range [1," + data.length + "]");
        int idx = k - 1;
        Selection.select(data, idx);
        return data[idx];
    }

    /**
     * Convert a double array of object type to primitive type.
     *
     * @param values Double array
     * @return double array
     */
    public static double[] toPrimitive(Double[] values) {
        requireNonNull(values);

        double[] array = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i] = values[i];
        }
        return array;
    }

    /**
     * Convert a double collection of object type to primitive array.
     *
     * @param values double Collection
     * @return double array
     */
    public static double[] toPrimitive(Collection<Double> values) {
        requireNonNull(values);

        double[] array = new double[values.size()];
        int i = 0;
        for (Double value : values) {
            array[i] = value;
            i++;
        }
        return array;
    }

    /**
     * Reverse element order in specified array
     * <p>
     * This method does nothing for a {@code null} input array.
     * </p>
     *
     * @param array the array to reverse, may be {@code null}
     * @since 2025-02-20 ⭐
     */
    public static void reverse(final char[] array) {
        reverse(array, 0, array.length);
    }

    /**
     * Reverse element order in specified range of the given array
     * <p>
     * This method does nothing for a {@code null} input array.
     * </p>
     *
     * @param array              the array to reverse, may be {@code null}
     * @param fromIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue (&gt;array.length)
     *                           results in no change.
     * @param toIndexExclusive   elements up to endIndex-1 are reversed in the array. Undervalue (&lt; start index)
     *                           results in no change. Overvalue (&gt;array.length) is demoted to array length.
     * @since 2025-02-20 ⭐
     */
    public static void reverse(final char[] array, final int fromIndexInclusive, final int toIndexExclusive) {
        if (array == null)
            return;
        checkFromToIndex(fromIndexInclusive, toIndexExclusive, array.length);

        char tmp;
        for (int i = fromIndexInclusive, j = toIndexExclusive - 1; i < j; i++, j--) {
            tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }


    /**
     * Reverses the elements of {@code array} between {@code fromIndex} inclusive and {@code toIndex}
     * exclusive. This is equivalent to {@code
     * Collections.reverse(IntUtils.asList(array).subList(fromIndex, toIndex))}, but is likely to be more
     * efficient.
     *
     * @throws IndexOutOfBoundsException if {@code fromIndex < 0}, {@code toIndex > array.length}, or
     *                                   {@code toIndex > fromIndex}
     */
    public static void reverse(int[] array, int fromIndexInclusive, int toIndexExclusive) {
        if (array == null)
            return;

        checkFromToIndex(fromIndexInclusive, toIndexExclusive, array.length);

        for (int i = fromIndexInclusive, j = toIndexExclusive - 1; i < j; i++, j--) {
            int tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }

    /**
     * Reverses the elements of {@code array}. This is equivalent to {@code
     * Collections.reverse(IntUtils.asList(array))}, but is likely to be more
     * efficient.
     *
     * @throws IndexOutOfBoundsException if {@code fromIndex < 0}, {@code toIndex > array.length}, or
     *                                   {@code toIndex > fromIndex}
     * @since 2026-03-24⭐
     */
    public static void reverse(int[] array) {
        reverse(array, 0, array.length);
    }

    /**
     * Sorts the elements of {@code array} between {@code fromIndex} inclusive and {@code toIndex}
     * exclusive in descending order.
     *
     * @param array     array to sort
     * @param fromIndex from index (inclusive)
     * @param toIndex   to index (exclusive)
     */
    public static void sortDescending(int[] array, int fromIndex, int toIndex) {
        requireNonNull(array);
        checkFromToIndex(fromIndex, toIndex, array.length);

        Arrays.sort(array, fromIndex, toIndex);
        reverse(array, fromIndex, toIndex);
    }

    /**
     * Sorts the elements of {@code array} in descending order.
     */
    public static void sortDescending(int[] array) {
        requireNonNull(array);
        IntArrays.stableSort(array, IntComparators.OPPOSITE_COMPARATOR);
    }

}
