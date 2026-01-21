package pdk.util.math;

import org.junit.jupiter.api.Test;
import pdk.util.data.Point2D;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 20 Jul 2025, 7:12 PM
 */
class MathsTest {

    private static final double EPS = 1E-15;


    @Test
    void linearInterpolateX() {
        Point2D p1 = new Point2D(1, 1);
        Point2D p2 = new Point2D(2, 2);
        assertEquals(3.0, Maths.linearInterpolateX(p1, p2, 3));
    }

    @Test
    void linearInterpolateY() {
        Point2D p1 = new Point2D(1, 1);
        Point2D p2 = new Point2D(2, 2);
        assertEquals(3.0, Maths.linearInterpolateY(p1, p2, 3));
    }

    @Test
    void factorial() {
        assertEquals(1.0, Maths.factorial(1), EPS);
        assertEquals(120.0, Maths.factorial(5), EPS);
    }

    @Test
    void isOdd() {
        assertTrue(Maths.isOdd(1));
        assertFalse(Maths.isOdd(2));
    }

    @Test
    void nextPowerOfTwo() {
        assertEquals(0, Maths.nextPowerOfTwo(0));
        assertEquals(1, Maths.nextPowerOfTwo(1));
        assertEquals(2, Maths.nextPowerOfTwo(2));
        assertEquals(4, Maths.nextPowerOfTwo(3));
        assertEquals(8, Maths.nextPowerOfTwo(6));
        assertEquals(0, Maths.nextPowerOfTwo(-1));
        assertEquals(0, Maths.nextPowerOfTwo(-100));
        assertEquals(64, Maths.nextPowerOfTwo(63));
        assertEquals(64, Maths.nextPowerOfTwo(64));
    }

    @Test
    void isEqual() {
        assertTrue(Maths.isEqual(100, 99, 1));
        assertTrue(Maths.isEqual(100.001, 100.0009, 0.00011));
    }

    @Test
    void getCombinations() {
        int[] array = new int[]{3, 2, 1};
        List<int[]> list = Maths.getCombinations(array);
        assertArrayEquals(new int[]{0, 0, 0}, list.get(0));
        assertArrayEquals(new int[]{1, 0, 0}, list.get(1));
        assertArrayEquals(new int[]{2, 0, 0}, list.get(2));
        assertArrayEquals(new int[]{0, 1, 0}, list.get(3));
        assertArrayEquals(new int[]{1, 1, 0}, list.get(4));
        assertArrayEquals(new int[]{2, 1, 0}, list.get(5));

        int[] array1 = new int[]{1, 2, 3};
        list = Maths.getCombinations(array1);
        assertArrayEquals(new int[]{0, 0, 0}, list.get(0));
        assertArrayEquals(new int[]{0, 1, 0}, list.get(1));
        assertArrayEquals(new int[]{0, 0, 1}, list.get(2));
        assertArrayEquals(new int[]{0, 1, 1}, list.get(3));
        assertArrayEquals(new int[]{0, 0, 2}, list.get(4));
        assertArrayEquals(new int[]{0, 1, 2}, list.get(5));

        array1 = new int[]{2};
        list = Maths.getCombinations(array1);
        assertArrayEquals(new int[]{0}, list.get(0));
        assertArrayEquals(new int[]{1}, list.get(1));
    }

    @Test
    void permutation() {
        int[] arr = {1, 2, 3, 4, 5};
        List<int[]> list = Maths.permutation(arr, 3);

        assertEquals(10, list.size());
        assertArrayEquals(new int[]{1, 2, 3}, list.get(0));
        assertArrayEquals(new int[]{1, 2, 4}, list.get(1));
        assertArrayEquals(new int[]{1, 3, 4}, list.get(2));
        assertArrayEquals(new int[]{2, 3, 4}, list.get(3));
        assertArrayEquals(new int[]{1, 2, 5}, list.get(4));
        assertArrayEquals(new int[]{1, 3, 5}, list.get(5));
        assertArrayEquals(new int[]{2, 3, 5}, list.get(6));
        assertArrayEquals(new int[]{1, 4, 5}, list.get(7));
        assertArrayEquals(new int[]{2, 4, 5}, list.get(8));
        assertArrayEquals(new int[]{3, 4, 5}, list.get(9));

        arr = new int[]{5, 3, 4, 2};
        list = Maths.permutation(arr, 3);
        assertEquals(4, list.size());

        assertArrayEquals(new int[]{5, 3, 4}, list.get(0));
        assertArrayEquals(new int[]{5, 3, 2}, list.get(1));
        assertArrayEquals(new int[]{5, 4, 2}, list.get(2));
        assertArrayEquals(new int[]{3, 4, 2}, list.get(3));

        arr = new int[]{1, 4, 6, 11, 12, 21, 22, 25, 27, 29, 31, 32, 33, 34};
        list = Maths.permutation(arr, 2);
        assertEquals(91, list.size());

        assertArrayEquals(new int[]{1, 4}, list.get(0));
        assertArrayEquals(new int[]{33, 34}, list.get(90));
    }

    @Test
    void permutationDup() {
        int[] arr = {1, 2, 2, 4, 5};
        int k = 3;
        List<int[]> list = Maths.permutationDup(arr, k);
        assertEquals(7, list.size());
        assertArrayEquals(new int[]{1, 2, 2}, list.get(0));
        assertArrayEquals(new int[]{1, 2, 4}, list.get(1));
        assertArrayEquals(new int[]{1, 2, 5}, list.get(2));
        assertArrayEquals(new int[]{1, 4, 5}, list.get(3));
        assertArrayEquals(new int[]{2, 2, 4}, list.get(4));
        assertArrayEquals(new int[]{2, 2, 5}, list.get(5));
        assertArrayEquals(new int[]{2, 4, 5}, list.get(6));
    }

    @Test
    void permutationDupException() {
        int[] arr = {1, 2, 2, 4, 5};
        assertThrows(IllegalArgumentException.class, () -> Maths.permutationDup(arr, 6));
    }

    @Test
    void permutationDupOrder() {
        int[] arr = {4, 5, 1, 1, 4, 3};
        List<int[]> list = Maths.permutationDup(arr, 3);
        assertEquals(10, list.size());
        assertArrayEquals(new int[]{1, 1, 3}, list.get(0));
        assertArrayEquals(new int[]{1, 1, 4}, list.get(1));
        assertArrayEquals(new int[]{1, 1, 5}, list.get(2));
        assertArrayEquals(new int[]{1, 3, 4}, list.get(3));
        assertArrayEquals(new int[]{1, 3, 5}, list.get(4));
        assertArrayEquals(new int[]{1, 4, 4}, list.get(5));
        assertArrayEquals(new int[]{1, 4, 5}, list.get(6));
        assertArrayEquals(new int[]{3, 4, 4}, list.get(7));
        assertArrayEquals(new int[]{3, 4, 5}, list.get(8));
        assertArrayEquals(new int[]{4, 4, 5}, list.get(9));
    }

    @Test
    void permutationDup3() {
        int[] arr = {1, 2, 2, 2, 4, 5};
        int k = 2;
        List<int[]> list = Maths.permutationDup(arr, k);
        assertEquals(7, list.size());
        assertArrayEquals(new int[]{1, 2}, list.get(0));
        assertArrayEquals(new int[]{1, 4}, list.get(1));
        assertArrayEquals(new int[]{1, 5}, list.get(2));
        assertArrayEquals(new int[]{2, 2}, list.get(3));
        assertArrayEquals(new int[]{2, 4}, list.get(4));
        assertArrayEquals(new int[]{2, 5}, list.get(5));
        assertArrayEquals(new int[]{4, 5}, list.get(6));
    }
}