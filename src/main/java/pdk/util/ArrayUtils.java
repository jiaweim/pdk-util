package pdk.util;

import org.apache.commons.numbers.arrays.Selection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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
}
