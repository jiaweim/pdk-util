package pdk.util.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 10 Apr 2026, 5:26 PM
 */
class CorrelationUtilsTest {

    @Test
    void normalizeToUnitVector() {
        double[] values = new double[]{1.0, 2.0, 3.0, 4.0};
        double[] normalized = CorrelationUtils.normalizeToUnitVector(values);
        assertArrayEquals(new double[]{0.1, 0.2, 0.3, 0.4}, normalized, 1E-15);
    }

    @Test
    void getCosineSimilarity() {
        double[] x = new double[]{1, 1, 2, 1, 1, 1, 0, 0, 0};
        double[] y = new double[]{1, 1, 1, 0, 1, 1, 1, 1, 1};
        double similarity = CorrelationUtils.getCosineSimilarity(x, y);
        assertEquals(0.7071, similarity, 1E-4);

        double sc = CorrelationUtils.getCosineSimilarity(new double[]{1, 5}, new double[]{-1, 3});
        assertEquals(0.868, sc, 1E-3);
    }

    @Test
    void testCosine() {
        double[] v1 = new double[]{1, 2, 3};
        assertEquals(1.0, CorrelationUtils.getCosineSimilarity(v1, v1));

        double[] v2 = new double[]{-3.0, 3.0, -1.0};
        assertEquals(0.0, CorrelationUtils.getCosineSimilarity(v1, v2));
    }

    @Test
    void testZeroCosine() {
        double[] v1 = {1.0, 0.0, 2.0, 0.0, 3.0, 0.0};
        double[] v2 = {0.0, 1.0, 0.0, 1.0, 0.0, 3.0};
        assertEquals(0.0, CorrelationUtils.getCosineSimilarity(v1, v2));
    }

    @Test
    void getNormalizedContrastAngle() {
        var v0 = new double[]{0.0, 10.0};
        var v1 = new double[]{0.1, 10.0};
        var v2 = new double[]{1.0, 10.0};
        var v3 = new double[]{2.0, 10.0};
        var v4 = new double[]{5.0, 10.0};
        var v5 = new double[]{10.0, 10.0};

        assertEquals(1.0, CorrelationUtils.getCosineSimilarity(v0, v1), 0.01);
        assertEquals(0.99, CorrelationUtils.getNormalizedContrastAngle(v0, v1), 0.01);

        assertEquals(0.99, CorrelationUtils.getCosineSimilarity(v0, v2), 0.01);
        assertEquals(0.94, CorrelationUtils.getNormalizedContrastAngle(v0, v2), 0.01);

        assertEquals(0.98, CorrelationUtils.getCosineSimilarity(v0, v3), 0.01);
        assertEquals(0.87, CorrelationUtils.getNormalizedContrastAngle(v0, v3), 0.01);

        assertEquals(0.89, CorrelationUtils.getCosineSimilarity(v0, v4), 0.01);
        assertEquals(0.7, CorrelationUtils.getNormalizedContrastAngle(v0, v4), 0.01);

        assertEquals(0.7, CorrelationUtils.getCosineSimilarity(v0, v5), 0.01);
        assertEquals(0.5, CorrelationUtils.getNormalizedContrastAngle(v0, v5), 0.01);

        System.out.println(CorrelationUtils.getCosineSimilarity(v0, v2));
        System.out.println(CorrelationUtils.getNormalizedContrastAngle(v0, v2));
    }

    @Test
    void testDot() {
        double[] v1 = new double[]{1, 1, 1};
        double[] v2 = new double[]{0.25, 0.25, 0.25};
        assertEquals(0.75, CorrelationUtils.dotProduct(v1, v2), 1E-15);
        assertEquals(0.0, CorrelationUtils.dotProduct(new double[]{1, 1, 1}, new double[]{}), 1E-15);
    }

    @Test
    void testPValue() {
        double[] x = new double[]{
                1.8, 1.3, 2.4, 1.5, 3.9, 2.1, 0.9, 1.4, 3.0, 4.6
        };
        double[] y = new double[]{
                604.4, 434.2, 544.0, 370.4, 742.3, 340.5, 232.0, 262.3, 441.9, 1157.7
        };
        double pValue = CorrelationUtils.getTwoTailedPValue(x, y);
        assertEquals(9.5516561E-4, pValue, 1E-10);
    }

    @Test
    void getCorrelationCoefficient() {
        double[] x = new double[]{
                1.8, 1.3, 2.4, 1.5, 3.9, 2.1, 0.9, 1.4, 3.0, 4.6
        };
        double[] y = new double[]{
                604.4, 434.2, 544.0, 370.4, 742.3, 340.5, 232.0, 262.3, 441.9, 1157.7
        };
        double r = CorrelationUtils.getPearsonCorrelationCoefficient(x, y);
        assertEquals(0.874, r, 1E-3);
    }

    @Test
    void testPearson() {
        double[] hoursStudied = {2.5, 4.0, 3.5, 5.0, 1.5, 6.0};
        double[] examScores = {65, 80, 75, 90, 55, 95};
        double pearson = CorrelationUtils.getPearsonCorrelationCoefficient(hoursStudied, examScores);
        assertEquals(0.99497, pearson, 1E-5);
    }

    @Test
    void test2() {
        double[] x = new double[]{
                1.80, 1.82, 1.90, 1.93, 1.98, 2.05, 2.13, 2.30, 2.37, 2.82,
                3.13, 3.27, 3.65, 3.78, 3.83, 3.88, 4.10, 4.27, 4.30, 4.43,
                4.47, 4.53, 4.55, 4.60, 4.63
        };

        double[] y = new double[]{
                56, 58, 62, 56, 57, 57, 60, 57, 61, 73,
                76, 77, 77, 79, 85, 80, 89, 90, 89, 89,
                86, 89, 86, 92, 91
        };

        double r = CorrelationUtils.getPearsonCorrelationCoefficient(x, y);
        assertEquals(0.979, r, 1E-3);
    }
}