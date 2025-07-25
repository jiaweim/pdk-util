package pdk.util.math;

import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 20 Jul 2025, 7:12 PM
 */
class MathUtilsTest {

    @Test
    void percentile() {
        double median = MathUtils.percentile(new double[]{223, 235, 235, 268, 274, 285, 290}, 50);
        assertEquals(268, median, 0.1);

        median = MathUtils.percentile(List.of(223.0, 235.0, 235.0, 268.0, 274.0, 285.0, 290.0), 50);
        assertEquals(268, median, 0.1);

        double median2 = MathUtils.percentile(new double[]{223, 235, 235, 268, 274, 290}, 50);
        assertEquals(median2, 251.5, 0.01);

        double[] x = null;

        // test null
        try {
            MathUtils.percentile(x, .25);
            fail("null is not a valid data array.");
        } catch (NullArgumentException ex) {
            // success
        }

        try {
            MathUtils.percentile(x, 0, 4, 0.25);
            fail("null is not a valid data array.");
        } catch (NullArgumentException ex) {
            // success
        }

        // test empty
        x = new double[]{};
        assertEquals(Double.NaN, MathUtils.percentile(x, 25), EPS);
        assertEquals(Double.NaN, MathUtils.percentile(x, 0, 0, 25), EPS);

        // test one
        x = new double[]{2.0};
        assertEquals(2.0, MathUtils.percentile(x, 25), EPS);
        assertEquals(2.0, MathUtils.percentile(x, 0, 1, 25), EPS);

        // test many
        x = new double[]{1.0, 2.0, 2.0, 3.0};
        assertEquals(2.5, MathUtils.percentile(x, 70), EPS);
        assertEquals(2.5, MathUtils.percentile(x, 1, 3, 62.5), EPS);
    }

    @Test
    void isOdd() {
        assertTrue(MathUtils.isOdd(1));
        assertFalse(MathUtils.isOdd(2));
    }

    private static final double EPS = 10E-15;

    @Test
    void mean() {
        double[] value = new double[]{1, 2, 3., 4};
        double mean = MathUtils.mean(value);
        assertEquals(2.5, mean, 1E-4);

        mean = MathUtils.mean(274, 235, 223, 268, 290, 285, 235);
        assertEquals(258.6, mean, 1e-1);

        double[] x = null;

        try {
            MathUtils.mean(x, 0, 4);
            fail("null is not a valid data array.");
        } catch (NullPointerException ex) {
            // success
        }

        // test empty
        x = new double[]{};
        assertEquals(Double.NaN, MathUtils.mean(x, 0, 0), EPS);

        // test one
        x = new double[]{2.0};
        assertEquals(2.0, MathUtils.mean(x, 0, 1), EPS);

        // test many
        x = new double[]{1.0, 2.0, 2.0, 3.0};
        assertEquals(2.5, MathUtils.mean(x, 2, 2), EPS);
    }

    @Test
    void weightedMean() {
        double[] values = new double[]{0.8, 0.6, 0.5, 0.8};
        double[] weights = new double[]{0.4, 0.2, 0.3, 0.1};
        double v = MathUtils.mean(values, weights);
        assertEquals(0.67, v, 1E-2);

        double v1 = MathUtils.mean(new double[]{19.99, 13.99, 25}, new double[]{5, 3, 2});
        assertEquals(19.192, v1, 1E-3);

        double mean = MathUtils.mean(new double[]{2, 2, 1, 4, 2, 3}, new double[]{3, 4, 1, 3, 2, 3});
        assertEquals(2.5, mean, EPS);
    }

    @Test
    void variance() {
        double[] values = new double[]{1, 2, 3, 3, 9, 10};
        assertEquals(12.22222, MathUtils.variance(values, false), 1E-5);
        assertEquals(14.667, MathUtils.variance(values), 1E-3);
        assertEquals(3.83, MathUtils.standardDeviation(values), 0.01);
        assertEquals(4.667, MathUtils.mean(values), 0.001);
    }

    @Test
    void standardDeviation() {
        double[] values = new double[]{4, 7, 6, 7, 9, 5, 8, 10, 9, 8, 7, 10};
        double variance = MathUtils.variance(values);
        assertEquals(3.5, variance, 0.1);
        assertEquals(1.9, MathUtils.standardDeviation(values), 0.1);

        values = new double[]{
                51, 30, 15,
                47, 14, 87,
                33, 11, 35,
                74, 42, 51,
                24, 40, 26,
                36, 22, 40,
                41, 35, 36,
                42, 29, 24
        };
        double v = MathUtils.standardDeviation(values);
        assertEquals(17.39143342, v, 1E-8);
    }

    @Test
    void nextPowerOfTwo() {
        assertEquals(0, MathUtils.nextPowerOfTwo(0));
        assertEquals(1, MathUtils.nextPowerOfTwo(1));
        assertEquals(2, MathUtils.nextPowerOfTwo(2));
        assertEquals(4, MathUtils.nextPowerOfTwo(3));
        assertEquals(8, MathUtils.nextPowerOfTwo(6));
        assertEquals(0, MathUtils.nextPowerOfTwo(-1));
        assertEquals(0, MathUtils.nextPowerOfTwo(-100));
        assertEquals(64, MathUtils.nextPowerOfTwo(63));
        assertEquals(64, MathUtils.nextPowerOfTwo(64));
    }


    @Test
    void round() {
        double a = 46.656;
        double scale = MathUtils.round(a, 2);
        assertEquals(46.66, scale, 0.0001);
    }

    @Test
    void quantile() {
        double[] d = new double[]{1, 3, 2, 4};//  p * (length + 1)
        double v = MathUtils.q1(d);
        System.out.println(v);

    }


    @Test
    void mode() {
        final double[] singleMode = {0, 1, 0, 2, 7, 11, 12};
        final double[] modeSingle = MathUtils.mode(singleMode);
        assertEquals(0, modeSingle[0], Double.MIN_VALUE);
        assertEquals(1, modeSingle.length);

        final double[] twoMode = {0, 1, 2, 0, 2, 3, 7, 11};
        final double[] modeDouble = MathUtils.mode(twoMode);
        assertEquals(0, modeDouble[0], Double.MIN_VALUE);
        assertEquals(2, modeDouble[1], Double.MIN_VALUE);
        assertEquals(2, modeDouble.length);

        final double[] nanInfested = {0, 0, 0, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 2, 2, 2, 3, 5};
        final double[] modeNan = MathUtils.mode(nanInfested);
        assertEquals(0, modeNan[0], Double.MIN_VALUE);
        assertEquals(2, modeNan[1], Double.MIN_VALUE);
        assertEquals(2, modeNan.length);

        final double[] infInfested = {0, 0, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
                Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 2, 2, 3, 5};
        final double[] modeInf = MathUtils.mode(infInfested);
        assertEquals(Double.NEGATIVE_INFINITY, modeInf[0], Double.MIN_VALUE);
        assertEquals(0, modeInf[1], Double.MIN_VALUE);
        assertEquals(2, modeInf[2], Double.MIN_VALUE);
        assertEquals(Double.POSITIVE_INFINITY, modeInf[3], Double.MIN_VALUE);
        assertEquals(4, modeInf.length);

        final double[] noData = {};
        final double[] modeNodata = MathUtils.mode(noData);
        assertEquals(0, modeNodata.length);

        final double[] nansOnly = {Double.NaN, Double.NaN};
        final double[] modeNansOnly = MathUtils.mode(nansOnly);
        assertEquals(0, modeNansOnly.length);

        final double[] nullArray = null;
        try {
            MathUtils.mode(nullArray);
            fail("Expecting NullArgumentException");
        } catch (NullPointerException ex) {
            // Expected
        }
    }

    @Test
    void populationStandardDeviation() {
        double[] values = new double[]{41, 38, 39, 45, 47, 41, 44, 41, 37, 42};
        double variance = MathUtils.populationVariance(values);
        assertEquals(8.9, variance, 1E-1);
        double standardDeviation = MathUtils.populationStandardDeviation(values);
        assertEquals(3.0, standardDeviation, 1E-1);
    }

}