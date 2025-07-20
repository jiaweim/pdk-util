package pdk.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 20 Jul 2025, 7:12 PM
 */
class MathUtilsTest {

    @Test
    void isOdd() {
        assertTrue(MathUtils.isOdd(1));
        assertFalse(MathUtils.isOdd(2));
    }

    @Test
    void mean() {
        double[] value = new double[]{1, 2, 3., 4};
        double mean = MathUtils.mean(value);
        assertEquals(2.5, mean, 1E-4);
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
}