package pdk.util.math.test;

import org.apache.commons.statistics.inference.TTest;
import org.junit.jupiter.api.Test;
import pdk.util.tuple.Tuple2;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 19 May 2026, 9:37 AM
 */
class TTestUtilsTest {

    @Test
    void getStatisticTwoSample() {
        double t = TTestUtils.getStatistic(473, 459, 39.7 * 39.7, 24.5 * 24.5, 8, 18);
        assertEquals(0.922, t, 1e-3);

        double[] s1 = new double[]{24, 22, 25, 28, 35, 32, 30, 27};
        double[] s2 = new double[]{26, 25, 25, 29, 33, 34, 35, 30};

        double pairedT = TTestUtils.getPairedT(s1, s2);
        assertEquals(-2.333, pairedT, 1e-3);
    }

    @Test
    void pairedT() {
        double[] s1 = new double[]{4.85, 4.90, 5.08, 4.72, 4.62, 4.54, 5.25, 5.18, 4.81, 4.57, 4.63, 4.77};
        double[] s2 = new double[]{4.78, 4.90, 5.05, 4.65, 4.64, 4.50, 5.24, 5.27, 4.75, 4.43, 4.61, 4.82};
        double pairedT = TTestUtils.getPairedT(s2, s1);
        double alpha = 0.05;
        double criticalValue = TTestUtils.getLeftTailedCriticalValue(s1.length - 1, alpha);

        double[] x1 = new double[]{60, 54, 78, 84, 91, 25, 50, 65, 68, 81, 75, 45, 62, 79, 58, 63};
        double[] x2 = new double[]{56, 48, 70, 60, 85, 40, 40, 55, 80, 75, 78, 50, 50, 85, 53, 60};

        Tuple2<Double, Double> criticalValue1 = TTestUtils.getTwoTailedCriticalValue(15, 0.01);
        assertEquals(-2.947, criticalValue1._1(), 1e-3);
        assertEquals(2.947, criticalValue1._2(), 1e-3);

        double pairedT1 = TTestUtils.getPairedT(x1, x2);
        assertEquals(1.369, pairedT1, 1e-3);
    }

    @Test
    void getOneSampleOneTailedPValue() {
        double pValue = TTestUtils.getOneSampleOneTailedPValue(13, 14, 3.5 * 3.5, 10);
        System.out.println(pValue);
    }

    @Test
    void testGetLeftTailedCriticalValue() {
        double criticalValue = TTestUtils.getLeftTailedCriticalValue(20, 0.05);
        assertEquals(criticalValue, -1.725, 1e-3);
    }

    @Test
    void getRightTailedCriticalValue() {
        double criticalValue = TTestUtils.getRightTailedCriticalValue(16, 0.01);
        assertEquals(criticalValue, 2.583, 1e-3);
    }

    @Test
    void testGetTwoTailedCriticalValue() {
        Tuple2<Double, Double> criticalValue = TTestUtils.getTwoTailedCriticalValue(25, 0.1);
        assertEquals(criticalValue._1(), -1.708, 1e-3);
        assertEquals(criticalValue._2(), 1.708, 1e-3);
    }

    @Test
    void testLeft() {
        double criticalValue = TTestUtils.getLeftTailedCriticalValue(13, 0.05);
        System.out.println(criticalValue);
    }

    @Test
    void testStatistic() {
        double statistic = TTestUtils.getStatistic(19189, 21000, 2950 * 2950, 14);
        assertEquals(statistic, -2.297, 1e-3);
    }

    @Test
    void testRejectionRegion() {
        double mu = 6.8;
        int n = 39; // > 30
        double mean = 6.7;
        double s = 0.35;
        double a = 0.05;

        Tuple2<Double, Double> criticalValue = TTestUtils.getTwoTailedCriticalValue(n - 1, a);
        assertEquals(criticalValue._1(), -2.024, 1e-3);
        assertEquals(criticalValue._2(), 2.024, 1e-3);
        double t = TTestUtils.getStatistic(mean, mu, s * s, n);
        assertEquals(t, -1.784, 1e-3);

        double pValue = TTestUtils.getOneSampleTwoTailedPValue(mean, mu, s * s, n);
        assertEquals(pValue, 0.082, 1e-3);
    }

    @Test
    void testDifferentSigma() {
        double[] sample1 = new double[]{50, 47, 42, 43, 39, 51, 43, 38, 44, 37};
        double[] sample2 = new double[]{36, 38, 37, 38, 36, 39, 37, 35, 33, 37};
        TTest tTest = TTest.withDefaults();
        double tValue = tTest.statistic(sample1, sample2);
        System.out.println(tValue);
    }

    @Test
    void sample5() {
        double mean = 6.8;
        int n = 39;
        double sampleMean = 6.7;
        double sampleSigma = 0.35;
        double alpha = 0.05;

    }
}