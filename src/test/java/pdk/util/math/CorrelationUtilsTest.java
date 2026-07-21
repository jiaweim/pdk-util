package pdk.util.math;

import org.apache.commons.statistics.distribution.TDistribution;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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

    @Nested
    class KLDivergence {
        // ---------- Normal cases ----------

        @Test
        void testIdenticalDistributions() {
            double[] p = {0.3, 0.7};
            double[] q = {0.3, 0.7};
            assertEquals(0.0, CorrelationUtils.getKLDivergence(p, q), 1e-12);
        }

        @Test
        void testKnownDivergence() {
            double[] p = {0.5, 0.5};
            double[] q = {0.9, 0.1};
            // KL = 0.5 * log(0.5/0.9) + 0.5 * log(0.5/0.1) = 0.5 * log(25/9)
            double expected = 0.5 * Math.log(25.0 / 9.0);
            assertEquals(expected, CorrelationUtils.getKLDivergence(p, q), 1e-12);
        }

        @Test
        void testAsymmetric() {
            double[] p = {0.5, 0.5};
            double[] q = {0.9, 0.1};
            double klPQ = CorrelationUtils.getKLDivergence(p, q);
            double klQP = CorrelationUtils.getKLDivergence(q, p);
            // The two should be different (non‑symmetric)
            assertNotEquals(klPQ, klQP, 1e-12);
            assertTrue(klPQ > 0.0);
            assertTrue(klQP > 0.0);
        }

        @Test
        void testZeroProbabilityInTrueDistribution() {
            double[] p = {0.0, 0.5, 0.5};
            double[] q = {0.1, 0.45, 0.45};
            // The term with p=0 contributes 0
            double expected = 0.5 * Math.log(0.5 / 0.45) + 0.5 * Math.log(0.5 / 0.45);
            assertEquals(expected, CorrelationUtils.getKLDivergence(p, q), 1e-12);
        }

        @Test
        void testZeroProbabilityInBothMatching() {
            double[] p = {0.4, 0.0, 0.6};
            double[] q = {0.4, 0.0, 0.6};
            assertEquals(0.0, CorrelationUtils.getKLDivergence(p, q), 1e-12);
        }

        // ---------- Infinite divergence ----------

        @Test
        void testTruePositiveApproximationZero() {
            double[] p = {0.5, 0.5};
            double[] q = {0.0, 1.0};   // q[0] = 0 while p[0] > 0
            assertTrue(Double.isInfinite(CorrelationUtils.getKLDivergence(p, q)));
            assertEquals(Double.POSITIVE_INFINITY,
                    CorrelationUtils.getKLDivergence(p, q));
        }

        @Test
        void testTruePositiveApproximationZeroAtLaterIndex() {
            double[] p = {0.7, 0.3};
            double[] q = {0.7, 0.0};   // q[1] = 0, p[1] > 0
            assertEquals(Double.POSITIVE_INFINITY,
                    CorrelationUtils.getKLDivergence(p, q));
        }

        // ---------- Degenerate / zero distributions ----------

        @Test
        void testBothAllZeros() {
            double[] p = {0.0, 0.0, 0.0};
            double[] q = {0.0, 0.0, 0.0};
            assertEquals(0.0, CorrelationUtils.getKLDivergence(p, q), 0.0);
        }

        @Test
        void testTrueDistributionAllZeros() {
            double[] p = {0.0, 0.0};
            double[] q = {0.5, 0.5};
            // All terms have p=0, so divergence is 0
            assertEquals(0.0, CorrelationUtils.getKLDivergence(p, q), 0.0);
        }

        // ---------- Invalid inputs ----------

        @Test
        void testNegativeValueInDistribution() {
            double[] p = {0.5, -0.5};
            double[] q = {0.5, 0.5};
            assertThrows(IllegalArgumentException.class,
                    () -> CorrelationUtils.getKLDivergence(p, q));
        }

        @Test
        void testNegativeValueInApproximation() {
            double[] p = {0.5, 0.5};
            double[] q = {0.5, -0.5};
            assertThrows(IllegalArgumentException.class,
                    () -> CorrelationUtils.getKLDivergence(p, q));
        }

        @Test
        void testNullDistribution() {
            double[] q = {0.5, 0.5};
            assertThrows(NullPointerException.class,
                    () -> CorrelationUtils.getKLDivergence(null, q));
        }

        @Test
        void testNullApproximation() {
            double[] p = {0.5, 0.5};
            assertThrows(NullPointerException.class,
                    () -> CorrelationUtils.getKLDivergence(p, null));
        }

        @Test
        void testLengthMismatch() {
            double[] p = {0.5, 0.5};
            double[] q = {0.3, 0.3, 0.4};
            assertThrows(IllegalArgumentException.class,
                    () -> CorrelationUtils.getKLDivergence(p, q));
        }

        // ---------- Large vectors ----------

        @Test
        void testLargeDistributions() {
            int n = 1000;
            double[] p = new double[n];
            double[] q = new double[n];
            for (int i = 0; i < n; i++) {
                p[i] = 1.0 / n;
                q[i] = (i < n / 2) ? 2.0 / n : 0.0;   // second half has q=0 while p>0 → infinite
            }
            assertEquals(Double.POSITIVE_INFINITY,
                    CorrelationUtils.getKLDivergence(p, q));
        }

        @Test
        void testHighDimensionFinite() {
            int n = 1000;
            double[] p = new double[n];
            double[] q = new double[n];
            for (int i = 0; i < n; i++) {
                p[i] = 1.0 / n;
                q[i] = 1.0 / n;
            }
            assertEquals(0.0, CorrelationUtils.getKLDivergence(p, q), 1e-12);
        }
    }

    @Nested
    class Cosine {
        @Test
        void testIdenticalVectors() {
            double[] xs = {1.0, 2.0, 3.0};
            double[] ys = {1.0, 2.0, 3.0};
            assertEquals(1.0, CorrelationUtils.getCosine(xs, ys), 1e-15);
        }

        @Test
        void testOppositeVectors() {
            double[] xs = {1.0, 2.0};
            double[] ys = {-1.0, -2.0};
            assertEquals(-1.0, CorrelationUtils.getCosine(xs, ys), 1e-15);
        }

        @Test
        void testOrthogonalVectors() {
            double[] xs = {1.0, 0.0};
            double[] ys = {0.0, 1.0};
            assertEquals(0.0, CorrelationUtils.getCosine(xs, ys), 1e-15);
        }

        @Test
        void testArbitraryVectors() {
            double[] xs = {3.0, 4.0, 0.0};
            double[] ys = {0.0, 3.0, 4.0};
            // dot = 0*3 + 4*3 + 0*4 = 12
            // norm xs = 5, norm ys = 5, cosine = 12/25 = 0.48
            assertEquals(0.48, CorrelationUtils.getCosine(xs, ys), 1e-15);
        }

        @Test
        void testNegativeValues() {
            double[] xs = {-1.0, -2.0, -3.0};
            double[] ys = {-4.0, -5.0, -6.0};
            double expected = 32.0 / (Math.sqrt(14) * Math.sqrt(77));
            assertEquals(expected, CorrelationUtils.getCosine(xs, ys), 1e-15);
        }

        @Test
        void testBothZeroVectors() {
            double[] xs = {0.0, 0.0};
            double[] ys = {0.0, 0.0};
            assertEquals(1.0, CorrelationUtils.getCosine(xs, ys), 0.0);
        }

        @Test
        void testOneZeroVector() {
            double[] xs = {0.0, 0.0};
            double[] ys = {1.0, 2.0};
            assertEquals(0.0, CorrelationUtils.getCosine(xs, ys), 0.0);
        }

        @Test
        void testEmptyArrays() {
            double[] xs = {};
            double[] ys = {};
            assertEquals(1.0, CorrelationUtils.getCosine(xs, ys), 0.0);
        }

        @Test
        void testEmptyAndNonEmpty() {
            double[] xs = {};
            double[] ys = {1.0};
            assertThrows(IllegalArgumentException.class, () -> CorrelationUtils.getCosine(xs, ys));
        }

        // ---- NaN ----

        @Test
        void testNaNInFirstVector() {
            double[] xs = {1.0, Double.NaN};
            double[] ys = {1.0, 2.0};
            assertTrue(Double.isNaN(CorrelationUtils.getCosine(xs, ys)));
        }

        @Test
        void testNaNInSecondVector() {
            double[] xs = {1.0, 2.0};
            double[] ys = {1.0, Double.NaN};
            assertTrue(Double.isNaN(CorrelationUtils.getCosine(xs, ys)));
        }

        @Test
        void testNaNInBothVectors() {
            double[] xs = {Double.NaN, 1.0};
            double[] ys = {Double.NaN, 2.0};
            assertTrue(Double.isNaN(CorrelationUtils.getCosine(xs, ys)));
        }

        @Test
        void testPositiveInfinityInVector() {
            double[] xs = {1.0, Double.POSITIVE_INFINITY};
            double[] ys = {2.0, 3.0};
            assertTrue(Double.isNaN(CorrelationUtils.getCosine(xs, ys)));
        }

        @Test
        void testNegativeInfinityInVector() {
            double[] xs = {1.0, 2.0};
            double[] ys = {-1.0, Double.NEGATIVE_INFINITY};
            assertTrue(Double.isNaN(CorrelationUtils.getCosine(xs, ys)));
        }

        @Test
        void testInfinityCombinedWithNaN() {
            double[] xs = {Double.POSITIVE_INFINITY, Double.NaN};
            double[] ys = {1.0, 2.0};
            assertTrue(Double.isNaN(CorrelationUtils.getCosine(xs, ys)));
        }

        @Test
        void testExtremeLargeValues() {
            double big = 1e200;
            double[] xs = {big, big * 2};
            double[] ys = {big * 3, big * 4};
            double expected = (3.0 + 8.0) / (Math.sqrt(1.0 + 4.0) * Math.sqrt(9.0 + 16.0));
            assertEquals(expected, CorrelationUtils.getCosine(xs, ys), expected * 1e-15);
        }

        @Test
        void testExtremeSmallValues() {
            double tiny = 1e-200;
            double[] xs = {tiny, tiny * 2};
            double[] ys = {tiny * 3, tiny * 4};
            double expected = (3.0 + 8.0) / (Math.sqrt(1.0 + 4.0) * Math.sqrt(9.0 + 16.0));
            assertEquals(expected, CorrelationUtils.getCosine(xs, ys), expected * 1e-15);
        }

        @Test
        void testMixedLargeAndSmall() {
            double[] xs = {1e200, 1e-200};
            double[] ys = {1e200, 1e-200};
            assertEquals(1.0, CorrelationUtils.getCosine(xs, ys), 1e-12);
        }

        // ---- Large dimension ----

        @Test
        void testLargeDimension() {
            int n = 10_000;
            double[] xs = new double[n];
            double[] ys = new double[n];
            for (int i = 0; i < n; i++) {
                xs[i] = 1.0;
                ys[i] = 1.0;
            }
            assertEquals(1.0, CorrelationUtils.getCosine(xs, ys), 1e-12);
        }

        @Test
        void testNullFirstArgument() {
            assertThrows(NullPointerException.class, () -> CorrelationUtils.getCosine(null, new double[]{1.0}));
        }

        @Test
        void testNullSecondArgument() {
            assertThrows(NullPointerException.class, () -> CorrelationUtils.getCosine(new double[]{1.0}, null));
        }

        @Test
        void testLengthMismatch() {
            assertThrows(IllegalArgumentException.class, () -> CorrelationUtils.getCosine(new double[]{1.0}, new double[]{1.0, 2.0}));
        }
    }

    @Nested
    class Pearson {
        @Test
        void testPearson() {
            double[] hoursStudied = {2.5, 4.0, 3.5, 5.0, 1.5, 6.0};
            double[] examScores = {65, 80, 75, 90, 55, 95};
            double pearson = CorrelationUtils.getPearsonCorrelation(hoursStudied, examScores);
            assertEquals(0.99497, pearson, 1E-5);
        }

        @Test
        void getCorrelationCoefficient() {
            double[] x = new double[]{
                    1.8, 1.3, 2.4, 1.5, 3.9, 2.1, 0.9, 1.4, 3.0, 4.6
            };
            double[] y = new double[]{
                    604.4, 434.2, 544.0, 370.4, 742.3, 340.5, 232.0, 262.3, 441.9, 1157.7
            };
            double r = CorrelationUtils.getPearsonCorrelation(x, y);
            assertEquals(0.874, r, 1E-3);
        }

        // ---- Normal cases ----

        @Test
        void testPerfectPositiveCorrelation() {
            double[] xs = {1.0, 2.0, 3.0};
            double[] ys = {4.0, 5.0, 6.0};
            assertEquals(1.0, CorrelationUtils.getPearsonCorrelation(xs, ys), 1e-15);
        }

        @Test
        void testPerfectNegativeCorrelation() {
            double[] xs = {1.0, 2.0, 3.0};
            double[] ys = {10.0, 8.0, 6.0};
            assertEquals(-1.0, CorrelationUtils.getPearsonCorrelation(xs, ys), 1e-12);
        }

        @Test
        void testNoCorrelation() {
            // x and y are independent, r should be close to 0
            double[] xs = {1.0, 2.0, 3.0, 4.0};
            double[] ys = {3.0, 1.0, 4.0, 2.0};  // manual permutation for near-zero
            double r = CorrelationUtils.getPearsonCorrelation(xs, ys);
            assertTrue(Math.abs(r) < 0.5); // not exactly 0 but low
        }

        @Test
        void testExactZeroCorrelation() {
            // Orthogonal after centering: xs centered = {-1,0,1}, ys = {0,0,0}? not good.
            // Use y values such that covariance is 0: y = {-1, 2, -1} with mean 0, dot = 1*(-1)+0*2+1*(-1)=-2? need proper.
            // Instead use known result: x={-1,0,1}, y={1, -2, 1} gives cov = (-1-0)*(1-0) + (0-0)*(-2-0) + (1-0)*(1-0) = -1+0+1=0.
            double[] xs = {-1.0, 0.0, 1.0};
            double[] ys = {1.0, -2.0, 1.0};
            assertEquals(0.0, CorrelationUtils.getPearsonCorrelation(xs, ys), 1e-15);
        }

        @Test
        void testGeneralPositiveCorrelation() {
            double[] xs = {1.0, 2.0, 3.0, 4.0, 5.0};
            double[] ys = {2.0, 4.0, 5.0, 4.0, 5.0};
            double expected = 3.0 / Math.sqrt(15.0); // precomputed
            assertEquals(expected, CorrelationUtils.getPearsonCorrelation(xs, ys), 1e-14);
        }

        @Test
        void testSameValuesRepeated() {
            // x = y, should be 1.0
            double[] xs = {5.0, 5.0, 5.0, 5.0};
            double[] ys = {5.0, 5.0, 5.0, 5.0};
            // Both have zero variance → undefined, should return NaN
            assertTrue(Double.isNaN(CorrelationUtils.getPearsonCorrelation(xs, ys)));
        }

        @Test
        void testOneVectorConstant() {
            double[] xs = {1.0, 2.0, 3.0};
            double[] ys = {7.0, 7.0, 7.0};
            assertTrue(Double.isNaN(CorrelationUtils.getPearsonCorrelation(xs, ys)));
        }

        // ---- Edge cases for length ----

        @Test
        void testSingleElement() {
            double[] xs = {5.0};
            double[] ys = {10.0};
            assertTrue(Double.isNaN(CorrelationUtils.getPearsonCorrelation(xs, ys)));
        }

        @Test
        void testEmptyArrays() {
            double[] xs = {};
            double[] ys = {};
            assertTrue(Double.isNaN(CorrelationUtils.getPearsonCorrelation(xs, ys)));
        }

        // ---- NaN ----

        @Test
        void testNaNInFirstVector() {
            double[] xs = {1.0, Double.NaN};
            double[] ys = {2.0, 3.0};
            assertTrue(Double.isNaN(CorrelationUtils.getPearsonCorrelation(xs, ys)));
        }

        @Test
        void testNaNInSecondVector() {
            double[] xs = {1.0, 2.0};
            double[] ys = {Double.NaN, 3.0};
            assertTrue(Double.isNaN(CorrelationUtils.getPearsonCorrelation(xs, ys)));
        }

        @Test
        void testNaNInBothVectors() {
            double[] xs = {Double.NaN, 1.0};
            double[] ys = {Double.NaN, 2.0};
            assertTrue(Double.isNaN(CorrelationUtils.getPearsonCorrelation(xs, ys)));
        }

        // ---- Infinity ----

        @Test
        void testPositiveInfinity() {
            double[] xs = {1.0, Double.POSITIVE_INFINITY};
            double[] ys = {2.0, 3.0};
            assertTrue(Double.isNaN(CorrelationUtils.getPearsonCorrelation(xs, ys)));
        }

        @Test
        void testNegativeInfinity() {
            double[] xs = {1.0, 2.0};
            double[] ys = {Double.NEGATIVE_INFINITY, 3.0};
            assertTrue(Double.isNaN(CorrelationUtils.getPearsonCorrelation(xs, ys)));
        }

        @Test
        void testInfinityCombinedWithNaN() {
            double[] xs = {Double.POSITIVE_INFINITY, Double.NaN};
            double[] ys = {1.0, 2.0};
            assertTrue(Double.isNaN(CorrelationUtils.getPearsonCorrelation(xs, ys)));
        }

        // ---- Extreme values (no overflow) ----

        @Test
        void testExtremeLargeValues() {
            double big = 1e200;
            double[] xs = {big, big * 2, big * 3};
            double[] ys = {big * 4, big * 5, big * 6};
            // Both are perfectly collinear with positive slope => r = 1.0
            assertEquals(1.0, CorrelationUtils.getPearsonCorrelation(xs, ys), 1e-12);
        }

        @Test
        void testExtremeSmallValues() {
            double tiny = 1e-200;
            double[] xs = {tiny, tiny * 2, tiny * 3};
            double[] ys = {tiny * 10, tiny * 20, tiny * 30};
            assertEquals(1.0, CorrelationUtils.getPearsonCorrelation(xs, ys), 1e-12);
        }

        @Test
        void testMixedDynamicRange() {
            double[] xs = {1e200, 1e-200, 1e100};
            double[] ys = {2e200, 2e-200, 2e100};
            // y = 2*x exactly => r = 1.0
            assertEquals(1.0, CorrelationUtils.getPearsonCorrelation(xs, ys), 1e-12);
        }

        // ---- Zero variance due to underflow ----

        @Test
        void testUnderflowCausesZeroVariance() {
            double tiny = 1e-310;
            // values are so small that after scaling, differences vanish
            double[] xs = {tiny, tiny * 2};
            double[] ys = {tiny * 3, tiny * 4};
            // In this particular case, there is a linear relationship but due to
            // underflow of the small component the variance may be computed as zero,
            // leading to NaN. This is acceptable behavior.
            double result = CorrelationUtils.getPearsonCorrelation(xs, ys);
            // It could be 1.0 if the scaling works; if underflow makes denominator zero, NaN
            assertTrue(Double.isNaN(result) || result == 1.0);
        }

        // ---- Large datasets ----

        @Test
        void testLargeDataset() {
            int n = 10_000;
            double[] xs = new double[n];
            double[] ys = new double[n];
            for (int i = 0; i < n; i++) {
                xs[i] = i * 0.01;
                ys[i] = 3.0 * xs[i] + 5.0;  // perfect linear
            }
            assertEquals(1.0, CorrelationUtils.getPearsonCorrelation(xs, ys), 1e-12);
        }

        // ---- Null and length mismatch ----

        @Test
        void testNullFirstArgument() {
            assertThrows(NullPointerException.class,
                    () -> CorrelationUtils.getPearsonCorrelation(null, new double[]{1.0}));
        }

        @Test
        void testNullSecondArgument() {
            assertThrows(NullPointerException.class,
                    () -> CorrelationUtils.getPearsonCorrelation(new double[]{1.0}, null));
        }

        @Test
        void testLengthMismatch() {
            assertThrows(IllegalArgumentException.class,
                    () -> CorrelationUtils.getPearsonCorrelation(
                            new double[]{1.0, 2.0}, new double[]{1.0}));
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

            double r = CorrelationUtils.getPearsonCorrelation(x, y);
            assertEquals(0.979, r, 1E-3);
        }


        @Test
        void testRange() {
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

            for (int startIndex = 0; startIndex < x.length - 3; startIndex++) {
                int maxCount = x.length - startIndex;
                for (int len = 3; len <= maxCount; len++) {
                    double p1 = CorrelationUtils.getPearsonCorrelation(x, y, startIndex, len);
                    double[] xCopy = Arrays.copyOfRange(x, startIndex, startIndex + len);
                    double[] yCopy = Arrays.copyOfRange(y, startIndex, startIndex + len);
                    double p2 = CorrelationUtils.getPearsonCorrelation(xCopy, yCopy);
                    assertEquals(p1, p2, 1E-5);
                }
            }
        }
    }


    @Test
    void getNormalizedContrastAngle() {
        var v0 = new double[]{0.0, 10.0};
        var v1 = new double[]{0.1, 10.0};
        var v2 = new double[]{1.0, 10.0};
        var v3 = new double[]{2.0, 10.0};
        var v4 = new double[]{5.0, 10.0};
        var v5 = new double[]{10.0, 10.0};

        assertEquals(1.0, CorrelationUtils.getCosine(v0, v1), 0.01);
        assertEquals(0.99, CorrelationUtils.getNormalizedContrastAngle(v0, v1), 0.01);

        assertEquals(0.99, CorrelationUtils.getCosine(v0, v2), 0.01);
        assertEquals(0.94, CorrelationUtils.getNormalizedContrastAngle(v0, v2), 0.01);

        assertEquals(0.98, CorrelationUtils.getCosine(v0, v3), 0.01);
        assertEquals(0.87, CorrelationUtils.getNormalizedContrastAngle(v0, v3), 0.01);

        assertEquals(0.89, CorrelationUtils.getCosine(v0, v4), 0.01);
        assertEquals(0.7, CorrelationUtils.getNormalizedContrastAngle(v0, v4), 0.01);

        assertEquals(0.7, CorrelationUtils.getCosine(v0, v5), 0.01);
        assertEquals(0.5, CorrelationUtils.getNormalizedContrastAngle(v0, v5), 0.01);

        System.out.println(CorrelationUtils.getCosine(v0, v2));
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

    @Nested
    class TwoTailedPValue {
        /**
         * Helper method to compute the expected two-tailed p-value using the same t-statistic
         * and TDistribution for cross-validation.
         */
        private static double referencePValue(double[] x, double[] y) {
            double r = CorrelationUtils.getPearsonCorrelation(x, y);
            if (Double.isNaN(r)) return Double.NaN;
            r = Math.clamp(r, -1.0, 1.0);
            int n = x.length;
            int df = n - 2;
            double t = Math.abs(r / Math.sqrt((1 - r * r) / df));
            TDistribution dist = TDistribution.of(df);
            return 2 * dist.cumulativeProbability(-t);
        }

        @Test
        void testBasicPositiveCorrelation() {
            double[] xs = {1, 2, 3, 4, 5};
            double[] ys = {2, 4, 5, 4, 5};
            double expected = referencePValue(xs, ys);
            double actual = CorrelationUtils.getTwoTailedPValue(xs, ys);
            assertEquals(expected, actual, 1e-10);
        }

        @Test
        void testPerfectPositiveCorrelation() {
            double[] xs = {1, 2, 3, 4};
            double[] ys = {3, 5, 7, 9}; // y = 2x+1
            double actual = CorrelationUtils.getTwoTailedPValue(xs, ys);
            // For perfect linearity r = 1.0, t → ∞, p ≈ 0
            assertEquals(0.0, actual, 1e-10);
        }

        @Test
        void testPerfectNegativeCorrelation() {
            double[] xs = {1, 2, 3, 4};
            double[] ys = {10, 8, 6, 4}; // y = -2x+12
            double actual = CorrelationUtils.getTwoTailedPValue(xs, ys);
            assertEquals(0.0, actual, 1e-10);
        }

        @Test
        void testUncorrelatedData() {
            double[] xs = {1, 2, 3, 4};
            double[] ys = {4, 1, 3, 2};
            double expected = referencePValue(xs, ys);
            double actual = CorrelationUtils.getTwoTailedPValue(xs, ys);
            assertEquals(expected, actual, 1e-10);
        }

        @Test
        void testExactlyThreeObservations() {
            double[] xs = {1, 2, 3};
            double[] ys = {2, 4.5, 7};
            // Should not throw, df = 1
            assertDoesNotThrow(() -> CorrelationUtils.getTwoTailedPValue(xs, ys));
            double expected = referencePValue(xs, ys);
            double actual = CorrelationUtils.getTwoTailedPValue(xs, ys);
            assertEquals(expected, actual, 1e-10);
        }

        @Test
        void testNotEnoughObservations() {
            double[] xs = {1, 2};
            double[] ys = {3, 4};
            assertThrows(IllegalArgumentException.class,
                    () -> CorrelationUtils.getTwoTailedPValue(xs, ys));
        }

        @Test
        void testEmptyArray() {
            double[] xs = {};
            double[] ys = {};
            // Length 0 → n < 3 → IllegalArgumentException
            assertThrows(IllegalArgumentException.class,
                    () -> CorrelationUtils.getTwoTailedPValue(xs, ys));
        }

        @Test
        void testNaNCorrelationReturnsNaN() {
            double[] xs = {1, 2, 3};
            double[] ys = {5, 5, 5};  // constant ys → r = NaN
            assertTrue(Double.isNaN(CorrelationUtils.getTwoTailedPValue(xs, ys)));
        }

        @Test
        void testClampWorksForCorrelationAboveOne() {
            // Simulate a case where getPearsonCorrelation might return slightly > 1
            // We can't easily force that without mocking, so we verify the method
            // doesn't crash and produces a finite p-value for perfect correlation.
            double[] xs = {1, 2, 3, 4, 5};
            double[] ys = {2, 4, 6, 8, 10}; // perfect r = 1
            double actual = CorrelationUtils.getTwoTailedPValue(xs, ys);
            assertFalse(Double.isNaN(actual));
            assertEquals(0.0, actual, 1e-10);
        }

        @Test
        void testNullFirstArgument() {
            assertThrows(NullPointerException.class,
                    () -> CorrelationUtils.getTwoTailedPValue(null, new double[]{1, 2, 3}));
        }

        @Test
        void testNullSecondArgument() {
            assertThrows(NullPointerException.class,
                    () -> CorrelationUtils.getTwoTailedPValue(new double[]{1, 2, 3}, null));
        }

        @Test
        void testLengthMismatch() {
            // The Pearson correlation method will throw IllegalArgumentException for length mismatch
            assertThrows(IllegalArgumentException.class,
                    () -> CorrelationUtils.getTwoTailedPValue(new double[]{1, 2, 3}, new double[]{1, 2}));
        }
    }

}