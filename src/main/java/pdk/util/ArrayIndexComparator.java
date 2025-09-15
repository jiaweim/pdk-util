package pdk.util;

import it.unimi.dsi.fastutil.ints.IntArrays;

import java.util.Comparator;

import static java.util.Objects.requireNonNull;

/**
 * Compare values at two indexes of two backing arrays.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 07 Apr 2025, 3:29 PM
 */
public interface ArrayIndexComparator {

    /**
     * Return the index array with backing array double values in descending order
     *
     * @param array backing double array
     * @return index array
     * @since 2025-04-07 ⭐
     */
    static int[] sortDescending(int[] array) {
        requireNonNull(array);

        IntDescending comparator = new IntDescending(array);
        return sort(comparator);
    }

    /**
     * Return the index array with backing array double values in ascending order
     *
     * @param array backing double array
     * @return index array
     * @since 2025-04-07 ⭐
     */
    static int[] sortAscending(int[] array) {
        requireNonNull(array);

        IntAscending comparator = new IntAscending(array);
        return sort(comparator);
    }

    /**
     * Return the index array with backing array double values in descending order
     *
     * @param array backing double array
     * @return index array
     * @since 2025-04-07 ⭐
     */
    static int[] sortDescending(double[] array) {
        requireNonNull(array);

        DoubleDescending comparator = new DoubleDescending(array);
        return sort(comparator);
    }

    /**
     * Return the index array with backing array double values in ascending order
     *
     * @param array backing double array
     * @return index array
     * @since 2025-04-07 ⭐
     */
    static int[] sortAscending(double[] array) {
        requireNonNull(array);

        DoubleAscending comparator = new DoubleAscending(array);
        return sort(comparator);
    }

    /**
     * Return the index array with backing array values sort according to given {@link Comparator}
     *
     * @param array      backing array
     * @param comparator {@link Comparator}
     * @return index array ⭐
     */
    static <T> int[] sort(T[] array, Comparator<? super T> comparator) {
        requireNonNull(array);
        requireNonNull(comparator);

        DelegateComparator<T> delegateComparator = new DelegateComparator<>(array, comparator);
        return sort(delegateComparator);
    }

    /**
     * sort and return the sorted indexes
     *
     * @param comparator {@link ArrayIndexComparator} instance
     * @return sorted indexes
     */
    static int[] sort(ArrayIndexComparator comparator) {
        requireNonNull(comparator);

        int[] indexArray = comparator.indexArray();
        IntArrays.stableSort(indexArray, comparator::compare);
        return indexArray;
    }

    class IntAscending implements ArrayIndexComparator {
        private final int[] array;

        public IntAscending(int[] array) {
            this.array = array;
        }

        @Override
        public int compare(int index1, int index2) {
            return Integer.compare(array[index1], array[index2]);
        }

        @Override
        public int size() {
            return array.length;
        }
    }

    class IntDescending implements ArrayIndexComparator {

        private final int[] array;

        public IntDescending(int[] array) {
            this.array = array;
        }

        @Override
        public int compare(int index1, int index2) {
            return Integer.compare(array[index2], array[index1]);
        }

        @Override
        public int size() {
            return array.length;
        }
    }

    class DoubleAscending implements ArrayIndexComparator {

        private final double[] array;

        public DoubleAscending(double[] array) {
            this.array = array;
        }

        @Override
        public int compare(int index1, int index2) {
            return Double.compare(array[index1], array[index2]);
        }

        @Override
        public int size() {
            return array.length;
        }
    }

    class DoubleDescending implements ArrayIndexComparator {

        private final double[] array;

        public DoubleDescending(double[] array) {
            this.array = array;
        }

        @Override
        public int compare(int index1, int index2) {
            return Double.compare(array[index2], array[index1]);
        }

        @Override
        public int size() {
            return array.length;
        }
    }

    class DelegateComparator<T> implements ArrayIndexComparator {
        private final T[] array;
        private final Comparator<? super T> comparator;

        public DelegateComparator(T[] array, Comparator<? super T> comparator) {
            this.array = array;
            this.comparator = comparator;
        }

        @Override
        public int compare(int index1, int index2) {
            return comparator.compare(array[index1], array[index2]);
        }

        @Override
        public int size() {
            return array.length;
        }
    }

    /**
     * @param index1 first index
     * @param index2 second index
     * @return result after compare
     */
    int compare(int index1, int index2);

    /**
     * @return size of the backing array
     */
    int size();

    /**
     * @return index array
     */
    default int[] indexArray() {
        int[] array = new int[size()];
        for (int i = 0; i < size(); i++) {
            array[i] = i;
        }
        return array;
    }
}
