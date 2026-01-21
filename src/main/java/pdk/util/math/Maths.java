package pdk.util.math;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.apache.commons.numbers.combinatorics.BinomialCoefficient;
import org.apache.commons.numbers.combinatorics.Combinations;
import org.apache.commons.numbers.combinatorics.Factorial;
import org.apache.commons.numbers.core.Precision;
import pdk.util.ArrayUtils;
import pdk.util.data.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static pdk.util.ArgUtils.checkArgument;

/**
 * Math utilities.
 * <p>
 * Most statistics are calculated using commons-statistics, which uses more precise floating point calculations.
 *
 * @author Jiawei Mao
 * @version 1.1.0
 * @since 20 Jul 2025, 7:01 PM
 */
public final class Maths {

    private Maths() {}

    private static final double LN2 = Math.log(2.0);

    /**
     * Returns the base 2 logarithm of a double value.
     *
     * <p>Special cases:
     *
     * <ul>
     *   <li>If {@code x} is NaN or less than zero, the result is NaN.
     *   <li>If {@code x} is positive infinity, the result is positive infinity.
     *   <li>If {@code x} is positive or negative zero, the result is negative infinity.
     * </ul>
     *
     * <p>The computed result is within 1 ulp of the exact result.
     *
     * @return base 2 log
     */
    public static double log2(double x) {
        return Math.log(x) / LN2;
    }

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
     * Round a double value to the closest int
     *
     * @param value the value to round
     * @return the closed int value
     * @since 2026-01-21⭐
     */
    public static int roundInt(double value) {
        return Math.toIntExact(Math.round(value));
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

    /**
     * Based on the line determined by two points, determine the y value of the third point
     *
     * @param point1 a {@link Point2D}
     * @param point2 a {@link Point2D}
     * @param x3     the x value of the third point
     * @return y value of the third point
     */
    public static double linearInterpolateY(Point2D point1, Point2D point2, double x3) {
        double k = (point2.getY() - point1.getY()) / (point2.getX() - point1.getX());
        return k * (x3 - point1.getX()) + point1.getY();
    }

    /**
     * Based on the line determined by two points, determine the x value of the third point
     *
     * @param point1 a {@link Point2D}
     * @param point2 a {@link Point2D}
     * @param y3     the y value of the third point
     * @return x value of the third point
     */
    public static double linearInterpolateX(Point2D point1, Point2D point2, double y3) {
        double k = (point2.getY() - point1.getY()) / (point2.getX() - point1.getX());
        return (y3 - point1.getY()) / k + point1.getX();
    }
}
