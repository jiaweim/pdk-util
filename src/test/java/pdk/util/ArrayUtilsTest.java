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