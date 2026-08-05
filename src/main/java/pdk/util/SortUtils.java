package pdk.util;

import it.unimi.dsi.fastutil.ints.IntArrays;
import org.jspecify.annotations.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static pdk.util.ArgUtils.checkNonNull;

/**
 * Sorting-related utility class.
 *
 * @author Jiawei Mao
 * @version 1.0.0⭐
 * @since 08 May 2026, 2:25 PM
 */
public final class SortUtils {

    private SortUtils() {}

    /**
     * Returns index of the first element not less than the key (&ge;).
     * <p>
     * Considering equal elements to yield the smallest possible position.
     *
     * <ul>
     *     <li>If the array contains the value, return the index of its first occurrence.</li>
     *     <li>If the array does not contain the value, return the index of the first element that is larger than the target value.</li>
     *     <li>If all elements are smaller than the target value, return the length of the array.</li>
     *     <li>If all elements are greater than the target value, return 0, e.g. the index of the first element larger than the target value</li>
     *     <li>if the array is empty, return -1</li>
     * </ul>
     *
     * @param array      a sorted array
     * @param comparator {@link Comparator} to compare elements
     * @param value      an element to insert
     * @param <C>        element type
     * @return index of the lower bound
     */
    public static <C> int getLowerBound(@NonNull C[] array, @NonNull Comparator<C> comparator, C value) {
        requireNonNull(array, "array is null");
        requireNonNull(comparator, "comparator is null");
        if (array.length == 0) {
            return -1;
        }

        int lo = 0;
        int hi = array.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (comparator.compare(array[mid], value) < 0) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    /**
     * Returns index of the first element not less than the key (&ge;).
     * <p>
     * Considering equal elements to yield the smallest possible position.
     *
     * <ul>
     *     <li>If the list contains the value, return the index of its first occurrence.</li>
     *     <li>If the list does not contain the value, return the index of the first element that is larger than the target value.</li>
     *     <li>If all elements are smaller than the target value, return the size of the list.</li>
     *     <li>If all elements are greater than the target value, return 0, e.g. the index of the first element larger than the target value</li>
     *     <li>if the list is empty, return -1</li>
     * </ul>
     *
     * @param list       a sorted list
     * @param comparator {@link Comparator} to compare elements
     * @param value      an element to insert
     * @param <C>        element type
     * @return index of the lower bound
     */
    public static <C> int getLowerBound(List<? extends C> list, @NonNull Comparator<? super C> comparator, C value) {
        requireNonNull(list, "array is null");
        requireNonNull(comparator, "comparator is null");
        if (list.isEmpty()) {
            return -1;
        }

        int lo = 0;
        int hi = list.size();
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (comparator.compare(list.get(mid), value) < 0) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    /**
     * Returns index of the first element not less than the key (&ge;).
     * <p>
     * Considering equal elements to yield the smallest possible position.
     *
     * <ul>
     *     <li>If the array contains the value, return the index of its first occurrence.</li>
     *     <li>If the array does not contain the value, return the index of the first element that is larger than the target value.</li>
     *     <li>If all elements are smaller than the target value, return the length of the array.</li>
     *     <li>If all elements are greater than the target value, return 0, e.g. the index of the first element larger than the target value</li>
     *     <li>if the array is empty, return -1</li>
     * </ul>
     *
     * @param array      a sorted array
     * @param comparator {@link Comparator} to compare elements
     * @param value      an element to insert
     * @param <C>        element type
     * @return index of the lower bound
     */
    public static <C> int getLowerBound(@NonNull C[] array, int fromInclusive, int toExclusive, @NonNull Comparator<C> comparator, C value) {
        requireNonNull(array, "array is null");
        requireNonNull(comparator, "comparator is null");
        if (array.length == 0) {
            return -1;
        }

        int lo = fromInclusive;
        int hi = toExclusive;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (comparator.compare(array[mid], value) < 0) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    /**
     * Returns index of the first element not less than the key (&ge;).
     * <p>
     * Considering equal elements to yield the smallest possible position.
     *
     * <ul>
     *     <li>If the array contains the value, return the index of its first occurrence.</li>
     *     <li>If the array does not contain the value, return the index of the first element that is larger than the target value.</li>
     *     <li>If all elements are smaller than the target value, return the length of the array.</li>
     *     <li>If all elements are greater than the target value, return 0, e.g. the index of the first element larger than the target value</li>
     *     <li>if the array is empty, return -1</li>
     * </ul>
     *
     * @param array a sorted array
     * @param value an element to insert
     * @return index of the lower bound
     */
    public static int getLowerBound(double[] array, double value) {
        requireNonNull(array, "array is null");
        if (array.length == 0) {
            return -1;
        }

        int lo = 0;
        int hi = array.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (Double.compare(array[mid], value) < 0) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    /**
     * Returns index of the first element not less than the key (&ge;).
     * <p>
     * <ul>
     *     <li>If the array contains the value, return the index of its first occurrence.</li>
     *     <li>If the array does not contain the value, return the index of the first element that is larger than the target value.</li>
     *     <li>If all elements are smaller than the target value, return the length of the array.</li>
     *     <li>If all elements are greater than the target value, return 0, e.g. the index of the first element larger than the target value</li>
     *     <li>if the array is empty, return -1</li>
     * </ul>
     *
     * @param array a sorted array
     * @param value an element to insert
     * @return index of the lower bound
     */
    public static int getLowerBound(int[] array, int value) {
        requireNonNull(array, "array is null");
        if (array.length == 0) {
            return -1;
        }

        int lo = 0;
        int hi = array.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (array[mid] < value) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    /**
     * Returns index of the first element not less than the key (&ge;).
     *
     * @param array a sorted array
     * @param value an element to search
     * @param range Search only within the subarray [0, range)
     * @return index of the lower bound
     */
    public static int getLowerBound(int[] array, int value, int range) {
        requireNonNull(array, "array is null");
        if (range == 0) {
            return -1;
        }

        int lo = 0;
        int hi = Math.min(range, array.length);
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (array[mid] < value) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    /**
     * Returns index of the first element greater than the key (&gt;).
     * <p>
     * Considering equal elements to yield the largest possible position.
     *
     * <ul>
     *     <li>If the array contains the value, return the index of its last occurrence + 1.</li>
     *     <li>If the array does not contain the value, return the index of the first element that is larger than the target value.</li>
     *     <li>If all elements are smaller than the target value, return the length of the array.</li>
     *     <li>If all elements are greater than the target value, return 0, e.g. the index of the first element larger than the target value</li>
     *     <li>if the array is empty, return -1</li>
     * </ul>
     *
     * @param array      a sorted array
     * @param comparator {@link Comparator} to compare elements
     * @param value      an element to insert
     * @param <C>        element type
     * @return index of the lower bound
     */
    public static <C> int getUpperBound(@NonNull C[] array, @NonNull Comparator<C> comparator, C value) {
        requireNonNull(array, "array is null");
        requireNonNull(comparator, "comparator is null");
        if (array.length == 0) {
            return -1;
        }

        int lo = 0;
        int hi = array.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (comparator.compare(array[mid], value) <= 0) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    /**
     * Returns index of the first element greater than the key (&gt;).
     * <p>
     * Considering equal elements to yield the largest possible position.
     *
     * <ul>
     *     <li>If the list contains the value, return the index of its last occurrence + 1.</li>
     *     <li>If the list does not contain the value, return the index of the first element that is larger than the target value.</li>
     *     <li>If all elements are smaller than the target value, return the size of the list.</li>
     *     <li>If all elements are greater than the target value, return 0, e.g. the index of the first element larger than the target value</li>
     *     <li>if the list is empty, return -1</li>
     * </ul>
     *
     * @param list       a sorted array
     * @param comparator {@link Comparator} to compare elements
     * @param value      an element to insert
     * @param <C>        element type
     * @return index of the lower bound
     */
    public static <C> int getUpperBound(List<? extends C> list, @NonNull Comparator<? super C> comparator, C value) {
        requireNonNull(list, "array is null");
        requireNonNull(comparator, "comparator is null");
        if (list.isEmpty()) {
            return -1;
        }

        int lo = 0;
        int hi = list.size();
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (comparator.compare(list.get(mid), value) <= 0) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    /**
     * Returns index of the first element greater than the key (&gt;).
     * <p>
     * Considering equal elements to yield the largest possible position.
     *
     * <ul>
     *     <li>If the array contains the value, return the index of its last occurrence + 1.</li>
     *     <li>If the array does not contain the value, return the index of the first element that is larger than the target value.</li>
     *     <li>If all elements are smaller than the target value, return the length of the array.</li>
     *     <li>If all elements are greater than the target value, return 0, e.g. the index of the first element larger than the target value</li>
     *     <li>if the array is empty, return -1</li>
     * </ul>
     *
     * @param array         a sorted array
     * @param comparator    {@link Comparator} to compare elements
     * @param value         an element to insert
     * @param fromInclusive from index (inclusive)
     * @param toExclusive   to index (exclusive)
     * @param <C>           element type
     * @return index of the lower bound
     */
    public static <C> int getUpperBound(@NonNull C[] array, int fromInclusive, int toExclusive,
            @NonNull Comparator<C> comparator, C value) {
        int lo = fromInclusive;
        int hi = toExclusive;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (comparator.compare(array[mid], value) <= 0) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    /**
     * Returns index of the first element greater than the key (&gt;).
     * <p>
     * Considering equal elements to yield the largest possible position.
     *
     * <ul>
     *     <li>If the array contains the value, return the index of its last occurrence + 1.</li>
     *     <li>If the array does not contain the value, return the index of the first element that is larger than the target value.</li>
     *     <li>If all elements are smaller than the target value, return the length of the array.</li>
     *     <li>If all elements are greater than the target value, return 0, e.g. the index of the first element larger than the target value</li>
     *     <li>if the array is empty, return -1</li>
     * </ul>
     *
     * @param array     a sorted array
     * @param value     an element to insert
     * @param fromIndex from index (inclusive)
     * @param toIndex   to index (exclusive)
     * @return index of the lower bound
     */
    public static int getUpperBound(double[] array, int fromIndex, int toIndex, double value) {
        int lo = fromIndex;
        int hi = toIndex;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (Double.compare(array[mid], value) <= 0) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    /**
     * Returns index of the first element greater than the key (&gt;).
     * <p>
     * Considering equal elements to yield the largest possible position.
     *
     * <ul>
     *     <li>If the array contains the value, return the index of its last occurrence + 1.</li>
     *     <li>If the array does not contain the value, return the index of the first element that is larger than the target value.</li>
     *     <li>If all elements are smaller than the target value, return the length of the array.</li>
     *     <li>If all elements are greater than the target value, return 0, e.g. the index of the first element larger than the target value</li>
     *     <li>if the array is empty, return -1</li>
     * </ul>
     *
     * @param array a sorted array
     * @param value an element to insert
     * @return index of the lower bound
     */
    public static int getUpperBound(double[] array, double value) {
        requireNonNull(array, "array is null");
        if (array.length == 0) {
            return -1;
        }

        int lo = 0;
        int hi = array.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (Double.compare(array[mid], value) <= 0) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    /**
     * Return a half-open interval [low,high) that covers all elements between the two given values
     * (inclusive of the smaller, exclusive of the larger).
     * <p>
     * More precisely, it computes:
     *
     * <ul>
     *     <li>{@code low} = {@link #getLowerBound(Object[], Comparator, Object)} of the <b>smaller</b></li>
     *     <li>{@code high}= {@link #getUpperBound(Object[], Comparator, Object)} of the <b>larger</b></li>
     * </ul>
     *
     * @param array      sorted array
     * @param comparator comparator
     * @param low        the smaller element
     * @param high       the larger element
     * @param <C>        element type
     * @return index array of size 2, where [0]=low, and [1]=high
     */
    public static <C> int[] getBound(@NonNull C[] array, @NonNull Comparator<? super C> comparator, C low, C high) {
        Objects.requireNonNull(array);
        Objects.requireNonNull(comparator);
        if (array.length == 0) {
            return new int[]{-1, -1};
        }

        int lo = getLowerBound(array, comparator, low);
        if (lo == array.length) {
            return new int[]{lo, lo};
        }
        int hi = getUpperBound(array, lo, array.length, comparator, high);
        return new int[]{lo, hi};
    }

    /**
     * Return a half-open interval [low,high) that covers all elements between the two given values
     * (inclusive of the smaller, exclusive of the larger) in a given range.
     * <p>
     * More precisely, it computes:
     *
     * <ul>
     *     <li>{@code low} = {@link #getLowerBound(Object[], Comparator, Object)} of the <b>smaller</b></li>
     *     <li>{@code high}= {@link #getUpperBound(Object[], Comparator, Object)} of the <b>larger</b></li>
     * </ul>
     *
     * @param array         sorted array
     * @param fromInclusive start searching index
     * @param toExclusive   end starting index
     * @param comparator    comparator
     * @param low           the smaller element
     * @param high          the larger element
     * @param <C>           element type
     * @return index array of size 2, where [0]=low, and [1]=high
     */
    public static <C> int[] getBound(@NonNull C[] array, int fromInclusive, int toExclusive,
            @NonNull Comparator<C> comparator, C low, C high) {
        Objects.requireNonNull(array);
        Objects.requireNonNull(comparator);
        if (array.length == 0) {
            return new int[]{-1, -1};
        }

        int lo = getLowerBound(array, fromInclusive, toExclusive, comparator, low);
        if (lo == toExclusive) {
            return new int[]{lo, lo};
        }
        int hi = getUpperBound(array, lo, toExclusive, comparator, high);
        return new int[]{lo, hi};
    }

    /**
     * Return a half-open interval [low,high) that covers all elements between the two given values
     * (inclusive of the smaller, exclusive of the larger).
     * <p>
     * More precisely, it computes:
     *
     * <ul>
     *     <li>{@code low} = {@link #getLowerBound(double[], double)} of the <b>smaller</b></li>
     *     <li>{@code high}= {@link #getUpperBound(double[], double)} of the <b>larger</b></li>
     * </ul>
     *
     * @param array sorted array
     * @param low   the smaller element
     * @param high  the larger element
     * @return index array of size 2, where [0]=low, and [1]=high
     */
    public static int[] getBound(double[] array, double low, double high) {
        Objects.requireNonNull(array);
        if (array.length == 0) {
            return new int[]{-1, -1};
        }

        int lo = getLowerBound(array, low);
        if (lo == array.length) {
            return new int[]{lo, lo};
        }
        int hi = getUpperBound(array, lo, array.length, high);
        return new int[]{lo, hi};
    }


    /**
     * Returns the permutation of indices that sorts the specified array in
     * ascending order.
     *
     * <p>The sorting is stable. If two elements have equal values, their original
     * relative order is preserved.</p>
     *
     * <p>The original array is not modified. If {@code indices} is the returned
     * array, then:</p>
     *
     * <pre>{@code
     * array[indices[0]] <= array[indices[1]] <= ...
     * }</pre>
     *
     * <p>NaN ordering follows {@link Double#compare(double, double)}.</p>
     *
     * @param array input array
     * @return sorted index permutation
     * @throws NullPointerException if {@code array} is {@code null}
     * @since 2026-08-05⭐
     */
    public static int[] argsort(double[] array) {
        checkNonNull(array);

        int n = array.length;
        int[] indices = new int[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }

        if (n < 2) {
            return indices;
        }

        IntArrays.stableSort(indices, (k1, k2) -> Double.compare(array[k1], array[k2]));
        return indices;
    }

    /**
     * Returns the permutation of indices that sorts the specified array in
     * descending order.
     *
     * <p>The sorting is stable. If two elements have equal values, their original
     * relative order is preserved.</p>
     *
     * <p>The original array is not modified.</p>
     *
     * <p>NaN ordering follows {@link Double#compare(double, double)}. Since the
     * comparison order is reversed, NaN values are placed before other values.</p>
     *
     * @param array input array
     * @return descending sorted index permutation
     * @throws NullPointerException if {@code array} is {@code null}
     * @since 2026-08-05⭐
     */
    public static int[] argsortDescending(double[] array) {
        checkNonNull(array);

        int n = array.length;
        int[] indices = new int[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }

        if (n < 2) {
            return indices;
        }

        IntArrays.stableSort(
                indices,
                (k1, k2) -> Double.compare(array[k2], array[k1])
        );

        return indices;
    }

    /**
     * Returns the permutation of indices that sorts the specified array in
     * ascending order.
     *
     * <p>The sorting is stable. If two elements have equal values, their original
     * relative order is preserved.</p>
     *
     * <p>The original array is not modified. If {@code indices} is the returned
     * array, then:</p>
     *
     * <pre>{@code
     * array[indices[0]] <= array[indices[1]] <= ...
     * }</pre>
     *
     * @param array input array
     * @return sorted index permutation
     * @throws NullPointerException if {@code array} is {@code null}
     * @since 2026-08-05⭐
     */
    public static int[] argsort(int[] array) {
        checkNonNull(array);

        int n = array.length;
        int[] indices = new int[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }

        if (n < 2) {
            return indices;
        }

        IntArrays.stableSort(indices,
                (k1, k2) -> Integer.compare(array[k1], array[k2]));

        return indices;
    }

    /**
     * Returns the permutation of indices that sorts the specified array in
     * ascending order.
     *
     * <p>The sorting is stable. If two elements have equal values, their original
     * relative order is preserved.</p>
     *
     * <p>The original array is not modified. If {@code indices} is the returned
     * array, then:</p>
     *
     * <pre>{@code
     * array[indices[0]] <= array[indices[1]] <= ...
     * }</pre>
     *
     * <p>NaN ordering follows {@link Float#compare(float, float)}.</p>
     *
     * @param array input array
     * @return sorted index permutation
     * @throws NullPointerException if {@code array} is {@code null}
     * @since 2026-08-05⭐
     */
    public static int[] argsort(float[] array) {
        checkNonNull(array);

        int n = array.length;
        int[] indices = new int[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }

        if (n < 2) {
            return indices;
        }

        IntArrays.stableSort(
                indices,
                (k1, k2) -> Float.compare(array[k1], array[k2])
        );

        return indices;
    }

    /**
     * Returns the permutation of indices that sorts the specified array in
     * descending order.
     *
     * <p>The sorting is stable. If two elements have equal values, their original
     * relative order is preserved.</p>
     *
     * <p>NaN ordering follows {@link Float#compare(float, float)}.</p>
     *
     * @param array input array
     * @return descending sorted index permutation
     * @throws NullPointerException if {@code array} is {@code null}
     * @since 2026-08-05⭐
     */
    public static int[] argsortDescending(float[] array) {
        checkNonNull(array);

        int n = array.length;
        int[] indices = new int[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }

        if (n < 2) {
            return indices;
        }

        IntArrays.stableSort(
                indices,
                (k1, k2) -> Float.compare(array[k2], array[k1])
        );

        return indices;
    }

    /**
     * Returns the permutation of indices that sorts the specified array in
     * descending order.
     *
     * <p>The sorting is stable. If two elements have equal values, their original
     * relative order is preserved.</p>
     *
     * @param array input array
     * @return descending sorted index permutation
     * @throws NullPointerException if {@code array} is {@code null}
     * @since 2026-08-05⭐
     */
    public static int[] argsortDescending(int[] array) {
        checkNonNull(array);

        int n = array.length;
        int[] indices = new int[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }

        if (n < 2) {
            return indices;
        }

        IntArrays.stableSort(
                indices,
                (k1, k2) -> Integer.compare(array[k2], array[k1])
        );

        return indices;
    }

    /**
     * Returns the permutation of indices that sorts the specified array according
     * to the given comparator.
     *
     * <p>The sorting is stable. If two elements are considered equal by the
     * comparator, their original relative order is preserved.</p>
     *
     * <p>The original array is not modified. If {@code indices} is the returned
     * array, then:</p>
     *
     * <pre>{@code
     * comparator.compare(array[indices[i]], array[indices[i + 1]]) <= 0
     * }</pre>
     *
     * <p>The comparator defines the ordering of the elements.</p>
     *
     * @param <T>        element type
     * @param array      input array
     * @param comparator comparator used for ordering elements
     * @return sorted index permutation
     * @throws NullPointerException if {@code array} or {@code comparator} is
     *                              {@code null}
     * @since 2026-08-05⭐
     */
    public static <T> int[] argsort(T[] array, Comparator<? super T> comparator) {
        checkNonNull(array);
        checkNonNull(comparator);

        int n = array.length;
        int[] indices = new int[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }

        if (n < 2) {
            return indices;
        }

        IntArrays.stableSort(
                indices,
                (k1, k2) -> comparator.compare(array[k1], array[k2])
        );

        return indices;
    }

    /**
     * Returns the permutation of indices that sorts the specified array according
     * to the natural ordering of its elements.
     *
     * @param <T>   element type implementing natural ordering
     * @param array input array
     * @return sorted index permutation
     * @throws NullPointerException if {@code array} is {@code null}
     * @since 2026-08-05⭐
     */
    public static <T extends Comparable<? super T>> int[] argsort(T[] array) {
        return argsort(array, Comparator.naturalOrder());
    }

    /**
     * Returns the permutation of indices that sorts the specified array in
     * descending order according to the given comparator.
     *
     * <p>The sorting is stable. If two elements are considered equal by the
     * comparator, their original relative order is preserved.</p>
     *
     * @param <T>        element type
     * @param array      input array
     * @param comparator comparator defining ascending order
     * @return descending sorted index permutation
     * @throws NullPointerException if {@code array} or {@code comparator} is null
     * @since 2026-08-05⭐
     */
    public static <T> int[] argsortDescending(
            T[] array,
            Comparator<? super T> comparator) {

        checkNonNull(array);
        checkNonNull(comparator);

        int n = array.length;
        int[] indices = new int[n];

        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }

        if (n < 2) {
            return indices;
        }

        IntArrays.stableSort(
                indices,
                (k1, k2) -> comparator.compare(array[k2], array[k1])
        );

        return indices;
    }
}
