package pdk.util.math;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 31 Dec 2025, 5:12 PM
 */
class StatUtilsTest {

    private static final double EPS = 1E-15;

    @Test
    void sum() {
        double[] values = new double[]{1, 2, 3, 9};
        assertEquals(15.0, StatUtils.sum(values), EPS);

        Double[] vs = new Double[]{1., 2., 3., 9.};
        assertEquals(15.0, StatUtils.sum(vs), EPS);
    }

    @Test
    void meanCollection() {
        List<Double> data = List.of(1.0, 2.0, 3.0, 4.0);
        assertEquals(2.5, StatUtils.mean(data), EPS);
    }

    @Test
    void mean() {
        double[] value = new double[]{1, 2, 3., 4};
        assertEquals(2.5, StatUtils.mean(value), EPS);

        double mean = StatUtils.mean(274, 235, 223, 268, 290, 285, 235);
        assertEquals(258.5714286, mean, 1E-7);

        double[] x = null;

        try {
            StatUtils.mean(x, 0, 4);
            fail("null is not a valid data array.");
        } catch (NullPointerException ex) {
            // success
        }

        // test empty
        x = new double[]{};
        assertEquals(Double.NaN, StatUtils.mean(x, 0, 0), EPS);

        // test one
        x = new double[]{2.0};
        assertEquals(2.0, StatUtils.mean(x, 0, 1), EPS);

        // test many
        x = new double[]{1.0, 2.0, 2.0, 3.0};
        assertEquals(2.5, StatUtils.mean(x, 2, 2), EPS);
    }


    @Test
    void weightedMean() {
        double[] values = new double[]{0.8, 0.6, 0.5, 0.8};
        double[] weights = new double[]{0.4, 0.2, 0.3, 0.1};
        double v = StatUtils.mean(values, weights);
        assertEquals(0.67, v, 1E-2);

        double v1 = StatUtils.mean(new double[]{19.99, 13.99, 25}, new double[]{5, 3, 2});
        assertEquals(19.192, v1, 1E-3);

        double mean = StatUtils.mean(new double[]{2, 2, 1, 4, 2, 3}, new double[]{3, 4, 1, 3, 2, 3});
        assertEquals(2.5, mean, EPS);
    }

    @Test
    void variance() {
        double[] values = new double[]{1, 2, 3, 3, 9, 10};
        assertEquals(12.22222, StatUtils.variance(values, true), 1E-5);
        assertEquals(14.667, StatUtils.variance(values), 1E-3);
        assertEquals(3.83, StatUtils.standardDeviation(values), 0.01);
        assertEquals(4.667, StatUtils.mean(values), 0.001);
    }

    @Test
    void standardDeviation() {
        double[] values = new double[]{4, 7, 6, 7, 9, 5, 8, 10, 9, 8, 7, 10};
        double variance = StatUtils.variance(values);
        assertEquals(3.5, variance, 0.1);
        assertThat(StatUtils.standardDeviation(values)).isCloseTo(1.9, offset(0.1));

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
        double v = StatUtils.standardDeviation(values);
        assertEquals(17.39143342, v, 1E-8);
    }


    @Test
    void sum2() {
        double[] values = new double[]{1, 2, 3, 5, 9};
        assertEquals(10, StatUtils.sum(values, 1, 3), EPS);

        List<Double> data = List.of(1.0, 2.0, 3.0, 5.0);
        assertEquals(11.0, StatUtils.sum(data), EPS);
    }

    @Test
    void weightedSum() {
        double[] values = new double[]{1, 2, 3};
        double[] weights = new double[]{0.3, 0.8, 0.7};
        assertEquals(4.0, StatUtils.weightedSum(values, weights), EPS);
    }

    @Test
    void round() {
        double a = 46.656;
        double scale = MathUtils.round(a, 2);
        assertEquals(46.66, scale, 0.0001);
    }


    @Test
    void mode() {
        final double[] singleMode = {0, 1, 0, 2, 7, 11, 12};
        final double[] modeSingle = StatUtils.mode(singleMode);
        assertEquals(0, modeSingle[0], Double.MIN_VALUE);
        assertEquals(1, modeSingle.length);

        final double[] twoMode = {0, 1, 2, 0, 2, 3, 7, 11};
        final double[] modeDouble = StatUtils.mode(twoMode);
        assertEquals(0, modeDouble[0], Double.MIN_VALUE);
        assertEquals(2, modeDouble[1], Double.MIN_VALUE);
        assertEquals(2, modeDouble.length);

        final double[] nanInfested = {0, 0, 0, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 2, 2, 2, 3, 5};
        final double[] modeNan = StatUtils.mode(nanInfested);
        assertEquals(0, modeNan[0], Double.MIN_VALUE);
        assertEquals(2, modeNan[1], Double.MIN_VALUE);
        assertEquals(2, modeNan.length);

        final double[] infInfested = {0, 0, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
                Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 2, 2, 3, 5};
        final double[] modeInf = StatUtils.mode(infInfested);
        assertEquals(Double.NEGATIVE_INFINITY, modeInf[0], Double.MIN_VALUE);
        assertEquals(0, modeInf[1], Double.MIN_VALUE);
        assertEquals(2, modeInf[2], Double.MIN_VALUE);
        assertEquals(Double.POSITIVE_INFINITY, modeInf[3], Double.MIN_VALUE);
        assertEquals(4, modeInf.length);

        final double[] noData = {};
        final double[] modeNodata = StatUtils.mode(noData);
        assertEquals(0, modeNodata.length);

        final double[] nansOnly = {Double.NaN, Double.NaN};
        final double[] modeNansOnly = StatUtils.mode(nansOnly);
        assertEquals(0, modeNansOnly.length);

        final double[] nullArray = null;
        try {
            StatUtils.mode(nullArray);
            fail("Expecting NullArgumentException");
        } catch (NullPointerException ex) {
            // Expected
        }
    }

    @Test
    void populationStandardDeviation() {
        double[] values = new double[]{41, 38, 39, 45, 47, 41, 44, 41, 37, 42};
        double variance = StatUtils.populationVariance(values);
        assertEquals(8.9, variance, 1E-1);
        double standardDeviation = StatUtils.populationStandardDeviation(values);
        assertEquals(3.0, standardDeviation, 1E-1);
    }


}