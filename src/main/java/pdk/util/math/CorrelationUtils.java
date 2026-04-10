package pdk.util.math;

import org.apache.commons.statistics.distribution.TDistribution;
import org.hipparchus.stat.correlation.PearsonsCorrelation;

import java.util.Arrays;

/**
 * Correlation utilities.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 10 Apr 2026, 5:22 PM
 */
public final class CorrelationUtils {

    private CorrelationUtils() {}

    /**
     * Normalize the set of numbers to a unit vector
     *
     * @param values values to normalize
     * @return normalized values
     * @since 2026-01-05
     */
    public static double[] normalizeToUnitVector(double[] values) {
        double[] normalized = new double[values.length];
        double sum = StatUtils.sum(values);
        if (sum == 0) {
            Arrays.fill(normalized, Double.NaN);
        } else {
            for (int i = 0; i < values.length; i++) {
                normalized[i] = values[i] / sum;
            }
        }
        return normalized;
    }

    /**
     * Calculate the KL divergence.
     * <p>
     * $$
     * KL = \sum P(x) \log{\frac{P(x)}{Q(x)}}
     * $$
     * <p>
     * where, P(x) is the true distribution, Q(x) is the approximation.
     *
     * @param distribution  probability distribution
     * @param approximation approximation distribution
     * @return KL divergence
     */
    public static double getKLDivergence(double[] distribution, double[] approximation) {
        double kl = 0;
        for (int i = 0; i < distribution.length; i++) {
            double px = distribution[i];
            double qx = approximation[i];

            if (px != 0 && qx != 0) {
                kl += px * Math.log(px / qx);
            }
        }
        return kl;
    }

    /**
     * Computes the Pearson's correlation coefficient between two arrays.
     *
     * @param x first data array
     * @param y second data array
     * @return Pearson's correlation coefficient for the two arrays
     * @since 2024-12-09⭐
     */
    public static double getPearsonCorrelationCoefficient(double[] x, double[] y) {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        return correlation.correlation(x, y);
    }

    /**
     * Calculate the p-value for correlation coefficient t-test
     *
     * @param x x values
     * @param y y values
     * @return two tailed test p-value
     * @since 2024-12-09⭐
     */
    public static double getTwoTailedPValue(double[] x, double[] y) {
        int df = x.length - 2;
        double r = getPearsonCorrelationCoefficient(x, y);
        double t = Math.abs(r / Math.sqrt((1 - r * r) / df));
        TDistribution distribution = TDistribution.of(df);
        return 2 * distribution.cumulativeProbability(-t);
    }

    /**
     * Calculate the cosine similarity of two double array.
     * <p>
     * The cosine value in formula:
     * <pre>
     *     $a\cdot b=\lVert a\rVert \lVert b\rVert cos\theta$
     * </pre>
     *
     * @param xs an array
     * @param ys an array
     * @return cosine similarity
     * @since 2025-2-13 ⭐
     */
    public static double getCosineSimilarity(double[] xs, double[] ys) {
        if (xs.length != ys.length) {
            throw new IllegalArgumentException("Array length should equal");
        }
        double dot = 0;
        for (int i = 0; i < xs.length; i++) {
            dot += xs[i] * ys[i];
        }

        double xNorm = getL2Norm(xs);
        double yNorm = getL2Norm(ys);

        if (xNorm == 0 && yNorm == 0) {
            return 1.0;
        } else if (xNorm == 0 || yNorm == 0) {
            return 0.0;
        }

        return dot / (xNorm * yNorm);
    }

    /**
     * Calculate the normalized contrast angle between two vector. which is more sensitive for small vectors
     * than cos(angle) implemented by {@link #getCosineSimilarity(double[], double[])}
     *
     * @param x a vector
     * @param y a vector
     * @return normalized contrast angle
     * @since 2026-01-05
     */
    public static double getNormalizedContrastAngle(double[] x, double[] y) {
        double cosine = getCosineSimilarity(x, y);
        return 1 - 2 * Math.acos(cosine) / Math.PI;
    }

    /**
     * Calculate the dot product of two array
     *
     * @param x an array
     * @param y an array
     * @return dot product
     * @since 2026-01-07⭐
     */
    public static double dotProduct(double[] x, double[] y) {
        double dot = 0;
        for (int i = 0; i < Math.min(x.length, y.length); i++) {
            dot += x[i] * y[i];
        }
        return dot;
    }

    /**
     * Return the L2 norm of given array
     *
     * @param xs double array
     * @return the L2 norm
     */
    public static double getL2Norm(double[] xs) {
        double sum = 0;
        for (final double value : xs) {
            sum += value * value;
        }
        return Math.sqrt(sum);
    }

}
