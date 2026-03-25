package pdk.util;

import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import org.apache.commons.numbers.arrays.Selection;
import org.jetbrains.annotations.Nullable;
import pdk.util.tuple.Tuple;
import pdk.util.tuple.Tuple2;

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
     * Returns the code point of the character with the largest code point in the character array.
     *
     * @param array target character array
     * @return largest code point
     */
    public static int getMaxCodePoint(char[] array) {
        if (array == null || array.length == 0) return -1;

        int maxCodePoint = -1;
        for (int i = 0; i < array.length; ) {
            int codePoint = Character.codePointAt(array, i);
            if (codePoint > maxCodePoint) {
                maxCodePoint = codePoint;
            }
            i += Character.charCount(codePoint);
        }
        return maxCodePoint;
    }

    /**
     * Return true if the array is empty, which means it is null or length == 0.
     *
     * @param array array object
     * @param <T>   type of the array
     * @return true if the array is empty
     */
    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Array hash code that takes into account the array size
     *
     * @param a    the array
     * @param size number of elements to calculate hashCode
     * @return the hash code
     */
    public static int hashCode(double[] a, int size) {
        if (a == null)
            return 0;

        int result = 1;
        for (int i = 0; i < size; i++) {
            result = 31 * result + Double.hashCode(a[i]);
        }
        return result;
    }

    /**
     * Array hash code that takes into account the array size
     *
     * @param a    the array
     * @param size number of elements to calculate hashCode
     * @return the hash code
     */
    public static int hashCode(float[] a, int size) {
        if (a == null)
            return 0;

        int result = 1;
        for (int i = 0; i < size; i++) {
            result = 31 * result + Float.hashCode(a[i]);
        }
        return result;
    }

    /**
     * Creates an array with the specified values
     *
     * @param a int values
     * @return an int array
     * @since 2026-03-25⭐
     */
    public static int[] array(int... a) {
        return a;
    }

    /**
     * Create an int array with the specified size, filled with the given value.
     *
     * @param size      array size
     * @param fillValue fill value
     * @return int[]
     * @since 2026-03-25⭐
     */
    public static int[] array(int size, int fillValue) {
        int[] array = new int[size];
        Arrays.fill(array, fillValue);
        return array;
    }

    /**
     * Create a double array with the specified size, filled with the given double value
     *
     * @param size      array size
     * @param fillValue fill value
     * @return double[]
     * @since 2026-03-25⭐
     */
    public static double[] array(int size, double fillValue) {
        double[] array = new double[size];
        Arrays.fill(array, fillValue);
        return array;
    }

    /**
     * Wraps System.arrayCopy and creates a new array if required
     *
     * @param src     the src array
     * @param srcPos  the src position
     * @param dest    the dest array
     * @param destPos the dest position
     * @param length  the number of elements to copy
     * @return the dest array or the newly created array
     * @since 2026-03-25⭐
     */
    public static double[] copy(double[] src, int srcPos, double[] dest, int destPos, int length) {
        if (dest == null)
            dest = new double[destPos + length];
        System.arraycopy(src, srcPos, dest, destPos, length);
        return dest;
    }

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
    public static double greatestInplace(double[] data, int k) {
        checkArgument(k >= 1 && k <= data.length, "k should in range [1," + data.length + "]");

        int idx = data.length - k;
        Selection.select(data, idx);
        return data[idx];
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
     */
    public static double greatest(double[] data, int k) {
        Objects.requireNonNull(data);
        checkArgument(k >= 1 && k <= data.length, "k should in range [1," + data.length + "]");

        double[] copy = Arrays.copyOf(data, data.length);
        return greatestInplace(copy, k);
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
    public static int greatestInplace(int[] data, int k) {
        checkArgument(k >= 1 && k <= data.length, "k should in range [1," + data.length + "]");

        int idx = data.length - k;
        Selection.select(data, idx);
        return data[idx];
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
     * @since 2026-03-25⭐
     */
    public static int greatest(int[] data, int k) {
        Objects.requireNonNull(data);
        checkArgument(k >= 1 && k <= data.length, "k should in range [1," + data.length + "]");

        int[] copy = Arrays.copyOf(data, data.length);
        return greatestInplace(copy, k);
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
        if (array != null) {
            reverse(array, 0, array.length);
        }
    }

    /**
     * Reverse element order in specified range of the given array
     * <p>
     * This method does nothing for a {@code null} input array.
     * </p>
     *
     * @param array              the array to reverse, may be {@code null}
     * @param fromIndexInclusive the starting index.
     * @param toIndexExclusive   elements up to endIndex-1 are reversed in the array.
     * @since 2025-02-20 ⭐
     */
    public static void reverse(final char[] array, final int fromIndexInclusive, final int toIndexExclusive) {
        if (array == null) return;
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
     * @since 2026-03-24⭐
     */
    public static void reverse(int[] array, int fromIndexInclusive, int toIndexExclusive) {
        if (array == null) return;

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
        if (array != null) {
            reverse(array, 0, array.length);
        }
    }

    /**
     * Reverses the elements of {@code array}.
     *
     * @throws IndexOutOfBoundsException if {@code fromIndex < 0}, {@code toIndex > array.length}, or
     *                                   {@code toIndex > fromIndex}
     * @since 2026-03-24⭐
     */
    public static void reverse(double[] array) {
        if (array != null) {
            reverse(array, 0, array.length);
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
     * @since 2026-03-24⭐
     */
    public static void reverse(double[] array, int fromIndexInclusive, int toIndexExclusive) {
        if (array == null) return;
        checkFromToIndex(fromIndexInclusive, toIndexExclusive, array.length);

        for (int i = fromIndexInclusive, j = toIndexExclusive - 1; i < j; i++, j--) {
            double tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }

    /**
     * Reverse the elements of {@code array}.
     */
    public static <T> void reverse(@Nullable T[] array) {
        if (array == null) return;
        reverse(array, 0, array.length);
    }

    /**
     * Reverse the elements of {@code array} between {@code fromIndex} inclusive and {@code toIndex} exclusive.
     *
     * @param array              array to reverse
     * @param fromIndexInclusive start index (inclusive)
     * @param toIndexExclusive   end index (exclusive)
     * @since 2026-03-25⭐
     */
    public static <T> void reverse(@Nullable T[] array, int fromIndexInclusive, int toIndexExclusive) {
        if (array == null) return;
        checkFromToIndex(fromIndexInclusive, toIndexExclusive, array.length);

        for (int i = fromIndexInclusive, j = toIndexExclusive - 1; i < j; i++, j--) {
            T tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
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

    /**
     * Inserts the specified element at the given index in the array.
     *
     * @param value the value to insert
     * @param index the index in which the value is to be inserted
     * @param arr   the array to insert the value
     * @param size  The count of elements in the array, which cannot exceed the array length, otherwise throw {@link ArrayIndexOutOfBoundsException}
     * @since 2026-03-25⭐
     */
    public static void insert(double value, int index, double[] arr, int size) {
        System.arraycopy(arr, index, arr, index + 1, size - index);
        arr[index] = value;
    }

    /**
     * Inserts the specified element at the given index in the array.
     *
     * @param value the value to insert
     * @param index the index in which the value is to be inserted
     * @param arr   the array to insert the value
     * @param size  The count of elements in the array, which cannot exceed the array length, otherwise throw {@link ArrayIndexOutOfBoundsException}
     * @since 2026-03-25⭐
     */
    public static void insert(float value, int index, float[] arr, int size) {
        System.arraycopy(arr, index, arr, index + 1, size - index);
        arr[index] = value;
    }

    /**
     * Finds the minimum insertion index for a given value in a sorted array, considering equal
     * elements to yield the smallest possible position.
     *
     * @param array      a sorted array
     * @param comparator {@link Comparator} to compare elements
     * @param value      an element to insert
     * @param <C>        element type
     * @return minimum insertion index
     * @since 2026-03-25⭐
     */
    public static <C> int getMinInsertIndex(C[] array, Comparator<C> comparator, C value) {
        if (comparator.compare(value, array[0]) < 0)
            return 0;
        if (comparator.compare(value, array[array.length - 1]) > 0)
            return array.length;
        int index = Arrays.binarySearch(array, value, comparator);
        if (index < 0)
            index = -(index + 1);

        while (index > 0 && comparator.compare(array[index - 1], value) == 0) {
            index--;
        }
        return index;
    }

    /**
     * Finds the maximum insertion index for a given value in a sorted array, considering equal
     * elements to yield the largest possible position.
     *
     * @param array      a sorted array
     * @param comparator {@link Comparator} to compare elements
     * @param value      an element to insert
     * @param <C>        element type
     * @return maximum insertion index
     * @since 2026-03-25⭐
     */
    public static <C> int getMaxInsertIndex(C[] array, Comparator<C> comparator, C value) {
        if (comparator.compare(value, array[0]) < 0)
            return 0;
        if (comparator.compare(value, array[array.length - 1]) > 0)
            return array.length;
        int index = Arrays.binarySearch(array, value, comparator);
        if (index < 0)
            index = -(index + 1);

        while (index < array.length && comparator.compare(array[index], value) == 0) {
            index++;
        }
        return index;
    }

    /**
     * Find range of low and high, such that <code>low</code> and <code>high</code> are in range
     * {@code slice[left..right)}, that is:
     * <li>{@code slice[left] >= low && slice[left-1] < low || left == 0}</li>
     * <li>{@code slice[right] > high && slice[right-1] <= high || right=slice.length()}</li>
     * <p>
     * In fact, this method return the min insertion index for low {@link #getMinInsertIndex(Object[], Comparator, Object)}
     * and the maximum insertion index for high {@link #getMaxInsertIndex(Object[], Comparator, Object)}.
     *
     * @param slice      array to search, must be sorted
     * @param comparator {@link Comparator} to use
     * @param low        lower bound
     * @param high       upper bound
     * @return range [left, right) with values between low and high, left=right if no value in the array is between low..high
     * @since 2025-02-20 ⭐
     */
    public static <C> Tuple2<Integer, Integer> binarySearch(C[] slice, Comparator<C> comparator, C low, C high) {
        if (comparator.compare(low, slice[slice.length - 1]) > 0) // The target min value is greater than the max value of the array.
            return Tuple.of(slice.length, slice.length);

        if (comparator.compare(high, slice[0]) < 0)  // The target max value is less than the min value of the array.
            return Tuple.of(0, 0);

        int leftIdx = Arrays.binarySearch(slice, low, comparator);
        if (leftIdx < 0) leftIdx = -leftIdx - 1;
        // proceed back to pass equal values
        while (leftIdx > 0 && comparator.compare(slice[leftIdx - 1], low) == 0)
            leftIdx--;
        int rightIdx = Arrays.binarySearch(slice, leftIdx, slice.length, high, comparator);
        if (rightIdx < 0) rightIdx = -rightIdx - 1;
        while (rightIdx < slice.length && comparator.compare(slice[rightIdx], high) == 0) rightIdx++;

        return Tuple.of(leftIdx, rightIdx);
    }

    /**
     * Find range of low and high in given range, such that `low` and `high` are in range `slice[left..right]`
     *
     * @param slice              array to search
     * @param fromIndexInclusive the beginning index of the search range
     * @param toIndexExclusive   the end index of the search range
     * @param comparator         {@link Comparator} to use
     * @param low                lower bound
     * @param high               upper bound
     * @return value if [left, right)
     * @since 2026-03-25⭐
     */
    public static <C> Tuple2<Integer, Integer> binarySearch(C[] slice, int fromIndexInclusive, int toIndexExclusive,
            Comparator<C> comparator, C low, C high) {
        if (comparator.compare(low, slice[toIndexExclusive - 1]) > 0)
            return Tuple.of(toIndexExclusive, toIndexExclusive);

        if (comparator.compare(high, slice[fromIndexInclusive]) < 0)
            return Tuple.of(fromIndexInclusive, fromIndexInclusive);

        int leftIdx = Arrays.binarySearch(slice, fromIndexInclusive, toIndexExclusive, low, comparator);
        if (leftIdx < 0) leftIdx = -leftIdx - 1;
        // proceed back to pass equal values
        while (leftIdx > fromIndexInclusive && comparator.compare(slice[leftIdx - 1], low) == 0)
            leftIdx--;
        int rightIdx = Arrays.binarySearch(slice, leftIdx, toIndexExclusive, high, comparator);
        if (rightIdx < 0) rightIdx = -rightIdx - 1;
        while (rightIdx < toIndexExclusive && comparator.compare(slice[rightIdx], high) == 0)
            rightIdx++;

        return Tuple.of(leftIdx, rightIdx);
    }

    /**
     * Find range of low and high, such that <code>low</code> and <code>high</code> are in range
     * {@code slice[left..right)}, that is:
     * <li>{@code slice[left] >= low && slice[left-1] < low || left == 0}</li>
     * <li>{@code slice[right] > high && slice[right-1] <= high || right=slice.length()}</li>
     *
     * @param slice array to search, must be sorted
     * @param low   lower bound
     * @param high  upper bound
     * @return range [left, right) with values between low and high, if no value in the array is between low..high, return
     * the insert point, such as (0,0) or (slice.length, slice.length)
     * @since 2025-02-20 ⭐
     */
    public static Tuple2<Integer, Integer> binarySearch(double[] slice, double low, double high) {
        if (Double.compare(low, slice[slice.length - 1]) > 0)
            return Tuple.of(slice.length, slice.length);

        if (Double.compare(high, slice[0]) < 0)
            return Tuple.of(0, 0);

        int leftIdx = Arrays.binarySearch(slice, low);
        if (leftIdx < 0) leftIdx = -leftIdx - 1;
        while (leftIdx > 0 && Double.compare(slice[leftIdx - 1], low) == 0)
            leftIdx--;
        int rightIdx = Arrays.binarySearch(slice, leftIdx, slice.length, high);
        if (rightIdx < 0) rightIdx = -rightIdx - 1;
        while (rightIdx < slice.length && Double.compare(slice[rightIdx], high) == 0)
            rightIdx++;

        return Tuple.of(leftIdx, rightIdx);
    }

    /**
     * Find the longest subsequence such that elements in the subsequence are consecutive integers.
     *
     * @param array an int array
     * @return number of longest subsequence with consecutive integers⭐
     * @since 2026-03-25
     */
    public static int longestConsecutive(int[] array) {
        if (array == null || array.length == 0) return 0;

        // 1. 将所有数字放入 HashSet 中，实现 O(1) 查询并去重
        IntOpenHashSet numSet = new IntOpenHashSet(array.length);
        for (int num : array) {
            numSet.add(num);
        }

        int longestStreak = 0;
        // 2. 遍历集合中的每一个数字
        for (int num : numSet) {
            // 3. 只有当 num 是序列的起点时，才开始计算长度
            // 如果 num - 1 存在，说明 num 不是起点，直接跳过
            if (!numSet.contains(num - 1)) {
                int currentNum = num;
                int currentStreak = 1;
                while (numSet.contains(currentNum + 1)) {
                    currentNum++;
                    currentStreak++;
                }
                longestStreak = Math.max(longestStreak, currentStreak);
            }
        }

        return longestStreak;
    }
}
