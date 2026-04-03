package pdk.util;

import org.apache.commons.numbers.arrays.Selection;
import org.junit.jupiter.api.Test;
import pdk.util.tuple.Tuple;
import pdk.util.tuple.Tuple2;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 17 Dec 2025, 11:18 AM
 */
class ArrayUtilsTest {

    @Test
    void concat() {
        double[] array = ArrayUtils.concat(new double[]{1, 2, 3}, new double[]{4, 5});
        assertArrayEquals(new double[]{1, 2, 3, 4, 5}, array);
    }

    @Test
    void linspace() {
        double[] array = ArrayUtils.linspace(2, 3, 5);
        assertArrayEquals(new double[]{2.0, 2.25, 2.5, 2.75, 3.0}, array, 1E-10);
    }

    @Test
    void reverseChar() {
        char[] a = new char[]{'a', 'b', 'c', 'd'};
        ArrayUtils.reverse(a, 1, 3);
        assertArrayEquals(new char[]{'a', 'c', 'b', 'd'}, a);

        a = new char[]{'a', 'b', 'c', 'd'};
        ArrayUtils.reverse(a);
        assertArrayEquals(new char[]{'d', 'c', 'b', 'a'}, a);

        char[] values = new char[]{'h', 'e', 'l', 'l', 'o'};
        ArrayUtils.reverse(values, 1, 4);
        assertArrayEquals(new char[]{'h', 'l', 'l', 'e', 'o'}, values);
    }

    @Test
    void reverseInt() {
        int[] a = new int[]{1, 2, 3, 4, 5};
        ArrayUtils.reverse(a);
        assertArrayEquals(new int[]{5, 4, 3, 2, 1}, a);

        a = new int[]{1, 2, 3, 4, 5};
        ArrayUtils.reverse(a, 1, 3);
        assertArrayEquals(new int[]{1, 3, 2, 4, 5}, a);
    }

    @Test
    void reverse() {
        Double[] v = new Double[]{1.0, 2.0, 3.0};
        ArrayUtils.reverse(v, 0, v.length);
        assertArrayEquals(new Double[]{3.0, 2.0, 1.0}, v);
    }

    @Test
    void repeat() {
        double[] array = ArrayUtils.repeat(1.0, 2);
        assertArrayEquals(new double[]{1.0, 1.0}, array);
    }

    @Test
    void leastDouble() {
        double[] a = {7, 10, 4, 3, 20, 15};

        Selection.select(a, 0);
        assertEquals(3.0, a[0]);

        Selection.select(a, 1);
        assertEquals(4.0, a[1]);

        Selection.select(a, 2);
        assertEquals(7.0, a[2]);

        assertEquals(3.0, ArrayUtils.least(a, 1));
        assertEquals(4.0, ArrayUtils.least(a, 2));
        assertEquals(7.0, ArrayUtils.least(a, 3));
    }

    @Test
    void leastDouble2() {
        double[] data = {4, 9, 16, 25, 36};
        assertEquals(4, ArrayUtils.least(data, 1));
        assertEquals(9, ArrayUtils.least(data, 2));
        assertEquals(16, ArrayUtils.least(data, 3));
        assertEquals(25, ArrayUtils.least(data, 4));
        assertEquals(36, ArrayUtils.least(data, 5));

        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.least(data, 0));
        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.least(data, data.length + 1));
    }

    @Test
    void leastDoubleEqual() {
        double[] data = {16, 16, 25, 25, 25, 4, 4, 9, 9, 36};

        assertEquals(4, ArrayUtils.least(data, 1));
        assertEquals(4, ArrayUtils.least(data, 2));
        assertEquals(9, ArrayUtils.least(data, 3));
        assertEquals(9, ArrayUtils.least(data, 4));
        assertEquals(16, ArrayUtils.least(data, 5));
        assertEquals(16, ArrayUtils.least(data, 6));
        assertEquals(25, ArrayUtils.least(data, 7));
        assertEquals(25, ArrayUtils.least(data, 8));
        assertEquals(25, ArrayUtils.least(data, 9));
        assertEquals(36, ArrayUtils.least(data, 10));

        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.least(data, 0));
        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.least(data, data.length + 1));
    }

    @Test
    void greatestInplaceDouble() {
        double[] data = {4, 9, 16, 25, 36};
        assertEquals(36, ArrayUtils.greatestInplace(data, 1));
        assertEquals(25, ArrayUtils.greatestInplace(data, 2));
        assertEquals(16, ArrayUtils.greatestInplace(data, 3));
        assertEquals(9, ArrayUtils.greatestInplace(data, 4));
        assertEquals(4, ArrayUtils.greatestInplace(data, 5));
    }

    @Test
    void greatestInplaceInt() {
        int[] data = {4, 9, 16, 25, 36};
        assertEquals(36, ArrayUtils.greatestInplace(data, 1));
        assertEquals(25, ArrayUtils.greatestInplace(data, 2));
        assertEquals(16, ArrayUtils.greatestInplace(data, 3));
        assertEquals(9, ArrayUtils.greatestInplace(data, 4));
        assertEquals(4, ArrayUtils.greatestInplace(data, 5));
    }

    @Test
    void greatestInplaceEqual() {
        double[] data = {16, 16, 25, 25, 25, 4, 4, 9, 9, 36};

        assertEquals(36, ArrayUtils.greatestInplace(data, 1));
        assertEquals(25, ArrayUtils.greatestInplace(data, 2));
        assertEquals(25, ArrayUtils.greatestInplace(data, 3));
        assertEquals(25, ArrayUtils.greatestInplace(data, 4));
        assertEquals(16, ArrayUtils.greatestInplace(data, 5));
        assertEquals(16, ArrayUtils.greatestInplace(data, 6));
        assertEquals(9, ArrayUtils.greatestInplace(data, 7));
        assertEquals(9, ArrayUtils.greatestInplace(data, 8));
        assertEquals(4, ArrayUtils.greatestInplace(data, 9));
        assertEquals(4, ArrayUtils.greatestInplace(data, 10));

        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.greatestInplace(data, 0));
        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.greatestInplace(data, data.length + 1));
    }

    @Test
    void greatestInplaceIntEquals() {
        int[] data = {16, 16, 25, 25, 25, 4, 4, 9, 9, 36};

        assertEquals(36, ArrayUtils.greatestInplace(data, 1));
        assertEquals(25, ArrayUtils.greatestInplace(data, 2));
        assertEquals(25, ArrayUtils.greatestInplace(data, 3));
        assertEquals(25, ArrayUtils.greatestInplace(data, 4));
        assertEquals(16, ArrayUtils.greatestInplace(data, 5));
        assertEquals(16, ArrayUtils.greatestInplace(data, 6));
        assertEquals(9, ArrayUtils.greatestInplace(data, 7));
        assertEquals(9, ArrayUtils.greatestInplace(data, 8));
        assertEquals(4, ArrayUtils.greatestInplace(data, 9));
        assertEquals(4, ArrayUtils.greatestInplace(data, 10));

        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.greatestInplace(data, 0));
        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.greatestInplace(data, data.length + 1));
    }

    @Test
    void greatest() {
        int[] values1 = new int[]{3, 2, 1, 5, 6, 4};
        assertEquals(5, ArrayUtils.greatest(values1, 2));

        int[] values2 = new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6}; // equal values before target
        assertEquals(4, ArrayUtils.greatest(values2, 4));

        double[] values3 = new double[]{1., 2., 3., 4., 5., 6.};
        assertEquals(5.0, ArrayUtils.greatest(values3, 2));
    }

    @Test
    void leastInt() {
        int[] a = {7, 10, 4, 3, 20, 15};
        Selection.select(a, 3);

        int[] b = a.clone();
        Arrays.sort(b);

        assertEquals(b[3], a[3]);
        assertEquals(ArrayUtils.least(a, 3), b[2]);
    }

    @Test
    void leastSameInt() {
        int[] a = {7, 10, 4, 3, 10, 20, 15};
        Selection.select(a, 3);
        assertEquals(10, a[3]);
        assertEquals(7, ArrayUtils.least(a, 3));
        assertEquals(10, ArrayUtils.least(a, 4));
        assertEquals(10, ArrayUtils.least(a, 5));
    }

    @Test
    void array() {
        int[] a = ArrayUtils.array(1, 2, 3);
        assertArrayEquals(new int[]{1, 2, 3}, a);
    }

    @Test
    void arrayFill() {
        int[] array = ArrayUtils.array(3, 2);
        assertArrayEquals(new int[]{2, 2, 2}, array);

        assertArrayEquals(new double[]{2.0, 2.0, 2.0}, ArrayUtils.array(3, 2.0));
    }

    @Test
    void insert() {
        double[] data = {1., 2., 3., 4., 5., 1.};

        ArrayUtils.insert(9.0, 3, data, 5);
        assertArrayEquals(new double[]{1.0, 2.0, 3.0, 9.0, 4.0, 5.0}, data);
    }

    @Test
    void insertFloat() {
        float[] vals = new float[]{1, 2, 3, 4, 5, 0};
        ArrayUtils.insert(2.5f, 2, vals, vals.length - 1);
        assertArrayEquals(new float[]{1, 2, 2.5f, 3, 4, 5}, vals);
    }

    @Test
    void getMinInsertIndex() {
        Double[] data = new Double[]{1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0};
        Comparator<Double> comparator = Comparator.comparingDouble(o -> (double) o);

        // left end
        assertEquals(0, ArrayUtils.getMinInsertIndex(data, comparator, 0.5));

        // equal left
        assertEquals(0, ArrayUtils.getMinInsertIndex(data, comparator, 1.0));

        // between
        assertEquals(1, ArrayUtils.getMinInsertIndex(data, comparator, 1.1));
        assertEquals(1, ArrayUtils.getMinInsertIndex(data, comparator, 1.2));
        assertEquals(1, ArrayUtils.getMinInsertIndex(data, comparator, 1.45));
        assertEquals(1, ArrayUtils.getMinInsertIndex(data, comparator, 1.5));

        assertEquals(2, ArrayUtils.getMinInsertIndex(data, comparator, 1.6));
        assertEquals(2, ArrayUtils.getMinInsertIndex(data, comparator, 2.0));

        assertEquals(6, ArrayUtils.getMinInsertIndex(data, comparator, 3.6));
        assertEquals(6, ArrayUtils.getMinInsertIndex(data, comparator, 4.0));
        assertEquals(7, ArrayUtils.getMinInsertIndex(data, comparator, 4.1));

        Double[] data2 = new Double[]{1.0, 1.5, 1.5, 1.5, 1.5, 2.0, 2.5, 3.0, 3.0, 3.5, 4.0};

        assertEquals(1, ArrayUtils.getMinInsertIndex(data2, comparator, 1.5));
        assertEquals(7, ArrayUtils.getMinInsertIndex(data2, comparator, 3.0));

        assertEquals(0, ArrayUtils.getMinInsertIndex(data2, comparator, 0.0));
        assertEquals(0, ArrayUtils.getMinInsertIndex(data2, comparator, 1.0));
        assertEquals(1, ArrayUtils.getMinInsertIndex(data2, comparator, 1.2));
        assertEquals(5, ArrayUtils.getMinInsertIndex(data2, comparator, 1.6));
        assertEquals(5, ArrayUtils.getMinInsertIndex(data2, comparator, 2.0));
        assertEquals(10, ArrayUtils.getMinInsertIndex(data2, comparator, 4.0));
    }

    @Test
    void getMaxInsertIndex() {
        Double[] data = new Double[]{1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0};
        Comparator<Double> comparator = Comparator.comparingDouble(o -> (double) o);

        assertEquals(0, ArrayUtils.getMaxInsertIndex(data, comparator, 0.5));
        assertEquals(1, ArrayUtils.getMaxInsertIndex(data, comparator, 1.0));
        assertEquals(1, ArrayUtils.getMaxInsertIndex(data, comparator, 1.2));
        assertEquals(2, ArrayUtils.getMaxInsertIndex(data, comparator, 1.5));
        assertEquals(3, ArrayUtils.getMaxInsertIndex(data, comparator, 2.0));
        assertEquals(3, ArrayUtils.getMaxInsertIndex(data, comparator, 2.2));
        assertEquals(4, ArrayUtils.getMaxInsertIndex(data, comparator, 2.5));
        assertEquals(7, ArrayUtils.getMaxInsertIndex(data, comparator, 4.0));
        assertEquals(7, ArrayUtils.getMaxInsertIndex(data, comparator, 4.1));

        Double[] data2 = new Double[]{1.0, 1.5, 1.5, 1.5, 1.5, 2.0, 2.5, 3.0, 3.0, 3.5, 4.0};
        assertEquals(0, ArrayUtils.getMaxInsertIndex(data2, comparator, 0.0));
        assertEquals(1, ArrayUtils.getMaxInsertIndex(data2, comparator, 1.0));
        assertEquals(1, ArrayUtils.getMaxInsertIndex(data2, comparator, 1.2));
        assertEquals(5, ArrayUtils.getMaxInsertIndex(data2, comparator, 1.5));
        assertEquals(7, ArrayUtils.getMaxInsertIndex(data2, comparator, 2.9));
        assertEquals(9, ArrayUtils.getMaxInsertIndex(data2, comparator, 3.0));
        assertEquals(9, ArrayUtils.getMaxInsertIndex(data2, comparator, 3.1));
        assertEquals(11, ArrayUtils.getMaxInsertIndex(data2, comparator, 4.0));
        assertEquals(11, ArrayUtils.getMaxInsertIndex(data2, comparator, 4.5));
    }

    @Test
    void binarySearch() {
        Double[] data = new Double[]{1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0};
        Comparator<Double> comparator = Comparator.comparingDouble(o -> (double) o);

        Tuple2<Integer, Integer> range = ArrayUtils.binarySearch(data, comparator, 1.75, 3.5);
        assertEquals(Tuple.of(2, 6), range);
        assertEquals(range._1, ArrayUtils.getMinInsertIndex(data, comparator, 1.75));
        assertEquals(range._2, ArrayUtils.getMaxInsertIndex(data, comparator, 3.5));

        // left end
        range = ArrayUtils.binarySearch(data, comparator, 0.5, 3.0);
        assertEquals(Tuple.of(0, 5), range);
        assertEquals(range._1, ArrayUtils.getMinInsertIndex(data, comparator, 0.5));
        assertEquals(range._2, ArrayUtils.getMaxInsertIndex(data, comparator, 3.0));

        // right end
        range = ArrayUtils.binarySearch(data, comparator, 1.75, 6.0);
        assertEquals(Tuple.of(2, 7), range);
        assertEquals(range._1, ArrayUtils.getMinInsertIndex(data, comparator, 1.75));
        assertEquals(range._2, ArrayUtils.getMaxInsertIndex(data, comparator, 6.0));

        // both end
        range = ArrayUtils.binarySearch(data, comparator, 0.0, 5.0);
        assertEquals(range, Tuple.of(0, data.length));
        assertEquals(range._1, ArrayUtils.getMinInsertIndex(data, comparator, 0.0));
        assertEquals(range._2, ArrayUtils.getMaxInsertIndex(data, comparator, 5.0));

        // out
        range = ArrayUtils.binarySearch(data, comparator, 0.1, 0.3);
        assertEquals(Tuple.of(0, 0), range);
        assertEquals(range._1, ArrayUtils.getMinInsertIndex(data, comparator, 0.1));
        assertEquals(range._2, ArrayUtils.getMaxInsertIndex(data, comparator, 0.3));

        range = ArrayUtils.binarySearch(data, comparator, 4.1, 4.2);
        assertEquals(Tuple.of(data.length, data.length), range);
        assertEquals(range._1, ArrayUtils.getMinInsertIndex(data, comparator, 4.1));
        assertEquals(range._2, ArrayUtils.getMaxInsertIndex(data, comparator, 4.2));

        // between
        range = ArrayUtils.binarySearch(data, comparator, 1.6, 1.8);
        assertEquals(Tuple.of(2, 2), range);
        assertEquals(range._1, ArrayUtils.getMinInsertIndex(data, comparator, 1.6));
        assertEquals(range._2, ArrayUtils.getMaxInsertIndex(data, comparator, 1.8));
    }

    @Test
    void binarySearchEqual() {
        Double[] data = new Double[]{1.0, 1.5, 1.5, 1.5, 1.5, 2.0, 2.5, 3.0, 3.0, 3.5, 4.0};

        Comparator<Double> comparator = Comparator.comparingDouble(o -> (double) o);

        // low equal
        Tuple2<Integer, Integer> range = ArrayUtils.binarySearch(data, comparator, 1.5, 3.25);
        assertEquals(Tuple.of(1, 9), range);
        assertEquals(range._1, ArrayUtils.getMinInsertIndex(data, comparator, 1.5));
        assertEquals(range._2, ArrayUtils.getMaxInsertIndex(data, comparator, 3.25));

        assertArrayEquals(new Double[]{1.5, 1.5, 1.5, 1.5, 2.0, 2.5, 3.0, 3.0},
                Arrays.copyOfRange(data, range._1, range._2));

        // high equal
        range = ArrayUtils.binarySearch(data, comparator, 1.6, 3.0);
        assertEquals(Tuple.of(5, 9), range);
        assertEquals(range._1, ArrayUtils.getMinInsertIndex(data, comparator, 1.6));
        assertEquals(range._2, ArrayUtils.getMaxInsertIndex(data, comparator, 3.0));

        // bot equal
        range = ArrayUtils.binarySearch(data, comparator, 1.5, 3.0);
        assertEquals(Tuple.of(1, 9), range);
        assertEquals(range._1, ArrayUtils.getMinInsertIndex(data, comparator, 1.5));
        assertEquals(range._2, ArrayUtils.getMaxInsertIndex(data, comparator, 3.0));

        // both not equal
        range = ArrayUtils.binarySearch(data, comparator, 1.6, 3.2);
        assertEquals(Tuple.of(5, 9), range);
        assertEquals(range._1, ArrayUtils.getMinInsertIndex(data, comparator, 1.6));
        assertEquals(range._2, ArrayUtils.getMaxInsertIndex(data, comparator, 3.2));
    }

    @Test
    void binarySearchBoundary() {
        Double[] data = new Double[]{1.0, 1.5, 1.5, 1.5, 1.5, 2.0, 2.5, 3.0, 3.0, 3.5, 4.0};

        Tuple2<Integer, Integer> range = ArrayUtils.binarySearch(data, Comparator.comparingDouble(o -> o), 1.5, 4.);
        assertEquals(Tuple.of(1, 11), range);
        assertEquals(range._1, ArrayUtils.getMinInsertIndex(data, Comparator.comparingDouble(o -> o), 1.5));
        assertEquals(range._2, ArrayUtils.getMaxInsertIndex(data, Comparator.comparingDouble(o -> o), 4.));

        range = ArrayUtils.binarySearch(data, Comparator.comparingDouble(o -> o), 1.0, 3.);
        assertEquals(Tuple.of(0, 9), range);
        assertEquals(range._1, ArrayUtils.getMinInsertIndex(data, Comparator.comparingDouble(o -> o), 1.0));
        assertEquals(range._2, ArrayUtils.getMaxInsertIndex(data, Comparator.comparingDouble(o -> o), 3.));

        range = ArrayUtils.binarySearch(data, Comparator.comparingDouble(o -> o), 1.0, 4.);
        assertEquals(Tuple.of(0, 11), range);
        assertEquals(range._1, ArrayUtils.getMinInsertIndex(data, Comparator.comparingDouble(o -> o), 1.0));
        assertEquals(range._2, ArrayUtils.getMaxInsertIndex(data, Comparator.comparingDouble(o -> o), 4.0));

        range = ArrayUtils.binarySearch(data, Comparator.comparingDouble(o -> o), 1.0, 5.);
        assertEquals(Tuple.of(0, 11), range);
        assertEquals(range._1, ArrayUtils.getMinInsertIndex(data, Comparator.comparingDouble(o -> o), 1.0));
        assertEquals(range._2, ArrayUtils.getMaxInsertIndex(data, Comparator.comparingDouble(o -> o), 5.0));
    }

    @Test
    void binarySearchDouble() {
        double[] data = new double[]{1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0};
        Tuple2<Integer, Integer> range = ArrayUtils.binarySearch(data, 1.75, 3.5);
        assertEquals(Tuple.of(2, 6), range);

        range = ArrayUtils.binarySearch(data, 0.0, 5.0);
        assertEquals(Tuple.of(0, data.length), range);
    }

    @Test
    void binarySearchDoubleBounds() {
        double[] data = new double[]{1.0, 1.5, 1.5, 1.5, 1.5, 2.0, 2.5, 3.0, 3.0, 3.5, 4.0};
        Tuple2<Integer, Integer> range = ArrayUtils.binarySearch(data, 1.5, 3.25);
        assertEquals(Tuple.of(1, 9), range);
    }

    @Test
    void binarySearchDoubleOut() {
        assertEquals(Tuple.of(10, 11),
                ArrayUtils.binarySearch(new double[]{1.0, 1.5, 1.5, 1.5, 1.5, 2.0, 2.5, 3.0, 3.0, 3.5, 4.0},
                        4.0, 6.0));
        assertEquals(Tuple.of(8, 10),
                ArrayUtils.binarySearch(new double[]{1.0, 1.5, 1.5, 1.5, 1.5, 2.0, 2.5, 3.0, 4.0, 4.0},
                        4.0, 6.0));
    }


    @Test
    void binarySearchOut() {
        double[] data = new double[]{1.0, 1.5, 1.5, 1.5, 1.5, 2.0, 2.5, 3.0, 3.0, 3.5, 4.0};
        // low
        Tuple2<Integer, Integer> range = ArrayUtils.binarySearch(data, 0.1, 0.2);
        assertEquals(Tuple.of(0, 0), range);
        assertEquals(Tuple.of(data.length, data.length), ArrayUtils.binarySearch(data, 4.1, 4.2));

        assertEquals(Tuple.of(0, 1), ArrayUtils.binarySearch(data, 0.1, 1.0));
        assertEquals(Tuple.of(0, 5), ArrayUtils.binarySearch(data, 0.1, 1.5));
        assertEquals(Tuple.of(0, 9), ArrayUtils.binarySearch(data, 1.0, 3.0));

        assertEquals(Tuple.of(7, 11), ArrayUtils.binarySearch(data, 3.0, 4.0));
        assertEquals(Tuple.of(7, 11), ArrayUtils.binarySearch(data, 3.0, 4.1));
        assertEquals(Tuple.of(10, 11), ArrayUtils.binarySearch(data, 4.0, 4.0));
    }

    @Test
    void binarySearchRange() {
        Double[] data1 = new Double[]{1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0};
        Double[] data2 = new Double[]{1.0, 1.5, 1.5, 1.5, 1.5, 2.0, 2.5, 3.0, 3.0, 3.5, 4.0};
        Comparator<Double> comparator = Comparator.comparingDouble(o -> (double) o);

        assertEquals(Tuple.of(2, 6), ArrayUtils.binarySearch(data1, 2, 6, comparator, 0.0, 10.0));
        assertEquals(Tuple.of(3, 5), ArrayUtils.binarySearch(data1, 2, 6, comparator, 2.1, 3.4));

        assertEquals(Tuple.of(2, 9), ArrayUtils.binarySearch(data2, 2, 9, comparator, 0.0, 10.0));
        assertEquals(Tuple.of(2, 9), ArrayUtils.binarySearch(data2, 2, 9, comparator, 1.4, 10.0));
        assertEquals(Tuple.of(2, 9), ArrayUtils.binarySearch(data2, 2, 9, comparator, 1.5, 10.0));
        assertEquals(Tuple.of(5, 9), ArrayUtils.binarySearch(data2, 2, 9, comparator, 1.51, 10.0));
        assertEquals(Tuple.of(5, 9), ArrayUtils.binarySearch(data2, 2, 9, comparator, 1.51, 3.5));
        assertEquals(Tuple.of(5, 9), ArrayUtils.binarySearch(data2, 2, 9, comparator, 1.51, 3.0));
        assertEquals(Tuple.of(5, 7), ArrayUtils.binarySearch(data2, 2, 9, comparator, 1.51, 2.9));
        assertEquals(Tuple.of(5, 7), ArrayUtils.binarySearch(data2, 2, 9, comparator, 1.51, 2.5));
        assertEquals(Tuple.of(5, 6), ArrayUtils.binarySearch(data2, 2, 9, comparator, 1.51, 2.4));
        assertEquals(Tuple.of(5, 6), ArrayUtils.binarySearch(data2, 2, 9, comparator, 1.51, 2.0));
        assertEquals(Tuple.of(5, 5), ArrayUtils.binarySearch(data2, 2, 9, comparator, 1.51, 1.9));
    }

    @Test
    void copy() {
        double[] src = new double[]{1, 2, 3, 4, 7};
        double[] dest = new double[4];
        ArrayUtils.copy(src, 2, dest, 1, 3);
        assertArrayEquals(new double[]{0, 3, 4, 7}, dest);
    }

    @Test
    void longestConsecutive() {
        int[] data = {100, 4, 200, 1, 3, 2};
        assertEquals(4, ArrayUtils.longestConsecutive(data)); // 最长连续序列是 [1, 2, 3, 4]，长度为 4

        assertEquals(6, ArrayUtils.longestConsecutive(new int[]{2, 6, 1, 9, 4, 5, 3})); // [1,2,3,4,5,6]
        assertEquals(2, ArrayUtils.longestConsecutive(new int[]{5, 7, 9, 11, 12})); // [11,12]
        assertEquals(7, ArrayUtils.longestConsecutive(new int[]{4, 5, 7, 8, 9, 10, 11, 12, 13, 13})); // [7,8,9,10,11,12,13]

        int[] val = {1, 2, 3, 3, 3};
        assertEquals(3, ArrayUtils.longestConsecutive(val));
        assertEquals(3, ArrayUtils.longestConsecutive(new int[]{1, 2, 3, 3, 3, 5, 5}));
    }
}