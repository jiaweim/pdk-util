package pdk.util.math;

import org.apache.commons.statistics.distribution.TDistribution;

import java.util.Arrays;

import static pdk.util.ArgUtils.checkNonNull;

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
     * Computes the Kullback–Leibler divergence from a true probability
     * distribution to an approximation.
     * <p>
     * For discrete distributions the KL divergence is defined as
     * <pre>
     *     D<sub>KL</sub>(P || Q) = Σ<sub>i</sub> P(i) · log(P(i) / Q(i))
     * </pre>
     * where {@code P} is the true distribution and {@code Q} is the
     * approximation.  The divergence is non‑negative and is zero if and only if
     * the two distributions are identical almost everywhere.  It is <em>not</em>
     * symmetric; swapping the arguments generally yields a different value.
     * <p>
     * <b>Requirements and edge cases:</b>
     * <ul>
     *   <li>Both arrays must be non‑{@code null} and have the same length;
     *       otherwise an exception is thrown.</li>
     *   <li>Every element is expected to be non‑negative.  If a negative value
     *       is encountered, an {@link IllegalArgumentException} is thrown.</li>
     *   <li>Terms where {@code P(i) == 0} are treated as zero by convention
     *       ({@code 0 · log(0)} = 0).</li>
     *   <li>If for any index {@code P(i) > 0} but {@code Q(i) == 0}, the
     *       divergence is infinite and {@link Double#POSITIVE_INFINITY} is
     *       returned.</li>
     *   <li>If both arrays contain only zeros, the method returns {@code 0.0}
     *       (the divergence between two degenerate distributions).</li>
     * </ul>
     *
     * @param distribution  the true probability distribution {@code P};
     *                      must not be {@code null}
     * @param approximation the approximate distribution {@code Q};
     *                      must not be {@code null}
     * @return the KL divergence {@code D}<sub>KL</sub>(P || Q), or
     * {@link Double#POSITIVE_INFINITY} when {@code P(i) > 0} and
     * {@code Q(i) == 0} for some {@code i}
     * @throws NullPointerException     if either array is {@code null}
     * @throws IllegalArgumentException if the arrays have different lengths,
     *                                  or if any element is negative
     * @since 2026-07-21⭐
     */
    public static double getKLDivergence(double[] distribution, double[] approximation) {
        checkNonNull(distribution);
        checkNonNull(approximation);

        if (distribution.length != approximation.length) {
            throw new IllegalArgumentException("distribution and approximation must have same length");
        }

        double kl = 0;
        for (int i = 0; i < distribution.length; i++) {
            double px = distribution[i];
            double qx = approximation[i];

            if (px < 0.0 || qx < 0.0) {
                throw new IllegalArgumentException("Probabilities must be non-negative");
            }
            if (px == 0.0)
                continue;

            if (qx == 0.0) {
                return Double.POSITIVE_INFINITY;
            }

            kl += px * Math.log(px / qx);
        }
        return kl;
    }

    /**
     * Computes the Pearson product-moment correlation coefficient between two
     * paired vectors of equal length.
     * <p>
     * The Pearson correlation measures the strength and direction of the linear
     * relationship between two variables. It is defined as:
     * <pre>
     *     r = (n·Σx<sub>i</sub>y<sub>i</sub> - Σx<sub>i</sub>·Σy<sub>i</sub>)
     *          / √((n·Σx<sub>i</sub>² - (Σx<sub>i</sub>)²) · (n·Σy<sub>i</sub>² - (Σy<sub>i</sub>)²))
     * </pre>
     * The result ranges from {@code -1.0} (perfect negative correlation) through
     * {@code 0.0} (no linear correlation) to {@code 1.0} (perfect positive
     * correlation).
     * <p>
     * <b>Numerical strategy:</b> To prevent overflow for vectors containing
     * extreme magnitudes, the arrays are first scaled by their respective
     * maximum absolute values. All statistics are accumulated on the scaled
     * data, and the scaling factors cancel out in the final ratio. The
     * computation is exact and requires only two passes over the data.
     * <p>
     * <b>Special floating-point values and edge cases:</b>
     * <ul>
     *   <li>If any element in either array is {@link Double#NaN}, the method
     *       immediately returns {@link Double#NaN}.</li>
     *   <li>If the length is 0 or 1, the correlation is undefined and
     *       {@link Double#NaN} is returned.</li>
     *   <li>If either vector is a zero vector or has zero variance (all
     *       elements equal), the correlation is undefined and
     *       {@link Double#NaN} is returned.</li>
     *   <li>If either vector contains an infinite component (and no
     *       {@code NaN}), {@link Double#NaN} is returned.</li>
     * </ul>
     *
     * @param xs the first array; must not be {@code null}
     * @param ys the second array; must not be {@code null}
     * @return the Pearson correlation coefficient in {@code [-1.0, 1.0]}, or
     * {@link Double#NaN} for degenerate inputs
     * @throws NullPointerException     if either array is {@code null}
     * @throws IllegalArgumentException if the arrays have different lengths
     * @since 2026-07-21⭐
     */
    public static double getPearsonCorrelation(double[] xs, double[] ys) {
        checkNonNull(xs);
        checkNonNull(ys);

        if (xs.length != ys.length) {
            throw new IllegalArgumentException("Arrays must have the same length");
        }
        int n = xs.length;
        if (n <= 1) {
            return Double.NaN;
        }

        // first pass: detect NaN, find max absolute values
        double maxX = 0.0;
        double maxY = 0.0;
        for (int i = 0; i < n; i++) {
            double x = xs[i];
            double y = ys[i];
            if (Double.isNaN(x) || Double.isNaN(y)) {
                return Double.NaN;
            }
            maxX = Math.max(maxX, Math.abs(x));
            maxY = Math.max(maxY, Math.abs(y));
        }
        if (maxX == 0.0 || maxY == 0.0) {
            return Double.NaN;
        }
        if (Double.isInfinite(maxX) || Double.isInfinite(maxY)) {
            return Double.NaN;
        }

        double sumX = 0.0;
        double sumY = 0.0;
        double sumXX = 0.0;
        double sumYY = 0.0;
        double sumXY = 0.0;
        for (int i = 0; i < n; i++) {
            double sx = xs[i] / maxX;
            double sy = ys[i] / maxY;

            sumX += sx;
            sumY += sy;

            sumXX += sx * sx;
            sumYY += sy * sy;
            sumXY += sx * sy;
        }
        double numerator = n * sumXY - sumX * sumY;
        double denominatorX = n * sumXX - sumX * sumX;
        double denominatorY = n * sumYY - sumY * sumY;

        if (denominatorX <= 0.0 || denominatorY <= 0.0) {
            return Double.NaN;
        }

        return numerator / Math.sqrt(denominatorX * denominatorY);
    }

    /**
     * Computes the Pearson correlation coefficient for a contiguous window of
     * elements from two paired arrays.
     * <p>
     * The window starts at {@code fromIndex} (inclusive) and includes exactly
     * {@code length} elements from each array.  The method validates that the
     * requested window lies entirely within both arrays and that
     * {@code length >= 2} (otherwise the correlation is undefined and
     * {@link Double#NaN} is returned).
     * <p>
     * The same numerical safeguards and edge‑case handling as in
     * {@link #getPearsonCorrelation(double[], double[])} are applied (scaling
     * to prevent overflow, detection of NaN/Infinity/zero variance).
     *
     * @param xs        the first array; must not be {@code null}
     * @param ys        the second array; must not be {@code null}
     * @param fromIndex the starting index (inclusive)
     * @param length    the number of elements to process; must be ≥ 2 and
     *                  {@code fromIndex + length <= xs.length} and
     *                  {@code fromIndex + length <= ys.length}
     * @return the Pearson correlation coefficient for the window,
     * or {@link Double#NaN} if the result is undefined
     * @throws NullPointerException      if either {@code xs} or {@code ys}
     *                                   is {@code null}
     * @throws IllegalArgumentException  if {@code length} is less than 2
     * @throws IndexOutOfBoundsException if the window exceeds the bounds
     *                                   of either array
     * @since 2026-07-21
     */
    public static double getPearsonCorrelation(double[] xs, double[] ys,
            int fromIndex, int length) {
        checkNonNull(xs);
        checkNonNull(ys);

        if (length < 2) {
            // Correlation requires at least 2 data points
            throw new IllegalArgumentException("length must be at least 2, was " + length);
        }
        int toIndex = fromIndex + length;
        if (fromIndex < 0 || toIndex > xs.length || toIndex > ys.length) {
            throw new IndexOutOfBoundsException(
                    String.format("Window [%d, %d) exceeds array bounds: "
                                    + "xs.length=%d, ys.length=%d",
                            fromIndex, toIndex, xs.length, ys.length));
        }

        int n = length;

        // First pass: detect NaN, find max absolute values within the window
        double maxX = 0.0, maxY = 0.0;
        for (int i = fromIndex; i < toIndex; i++) {
            double x = xs[i];
            double y = ys[i];
            if (Double.isNaN(x) || Double.isNaN(y)) {
                return Double.NaN;
            }
            maxX = Math.max(maxX, Math.abs(x));
            maxY = Math.max(maxY, Math.abs(y));
        }

        if (maxX == 0.0 || maxY == 0.0) {
            return Double.NaN;
        }
        if (Double.isInfinite(maxX) || Double.isInfinite(maxY)) {
            return Double.NaN;
        }

        // Second pass: accumulate scaled sums
        double sumX = 0.0, sumY = 0.0;
        double sumXX = 0.0, sumYY = 0.0, sumXY = 0.0;
        for (int i = fromIndex; i < toIndex; i++) {
            double sx = xs[i] / maxX;
            double sy = ys[i] / maxY;
            sumX += sx;
            sumY += sy;
            sumXX += sx * sx;
            sumYY += sy * sy;
            sumXY += sx * sy;
        }

        double numerator = n * sumXY - sumX * sumY;
        double denominatorX = n * sumXX - sumX * sumX;
        double denominatorY = n * sumYY - sumY * sumY;

        if (denominatorX <= 0.0 || denominatorY <= 0.0) {
            return Double.NaN;
        }
        return numerator / Math.sqrt(denominatorX * denominatorY);
    }

    /**
     * Computes the two-tailed p-value for the Pearson correlation coefficient
     * between two paired arrays.
     * <p>
     * The test statistic is
     * <pre>
     *     t = |r| · √((n - 2) / (1 - r²))
     * </pre>
     * where {@code r} is the Pearson correlation coefficient returned by
     * {@link #getPearsonCorrelation(double[], double[])} and {@code n} is the
     * number of data points.  The p-value is then calculated as
     * <pre>
     *     p = 2 · P(T<sub>n-2</sub> ≤ -t)
     * </pre>
     * using Student's t-distribution with {@code n - 2} degrees of freedom.
     * <p>
     * <b>Requirements and edge cases:</b>
     * <ul>
     *   <li>The arrays must have the same length (enforced by the Pearson
     *       correlation calculation).</li>
     *   <li>At least 3 observations are needed; otherwise an
     *       {@link IllegalArgumentException} is thrown because the degrees of
     *       freedom would be ≤ 0.</li>
     *   <li>If the Pearson correlation is {@link Double#NaN} (e.g., due to zero
     *       variance or a constant vector), this method returns
     *       {@link Double#NaN}.</li>
     *   <li>The correlation coefficient is clamped to {@code [-1.0, 1.0]}
     *       via {@link Math#clamp(double, double, double)} to guard against
     *       floating‑point rounding errors that could produce a value slightly
     *       outside the valid range, which would otherwise lead to a negative
     *       argument inside the square root.</li>
     * </ul>
     *
     * @param x the first array; must not be {@code null}
     * @param y the second array; must not be {@code null}
     * @return the two-tailed p-value for the Pearson correlation test
     * @throws NullPointerException     if either {@code x} or {@code y} is
     *                                  {@code null}
     * @throws IllegalArgumentException if the arrays have different lengths
     *                                  (thrown by the underlying correlation method), or if the length
     *                                  is less than 3
     * @see #getPearsonCorrelation(double[], double[])
     * @since 2026-07-21⭐
     */
    public static double getTwoTailedPValue(double[] x, double[] y) {
        int n = x.length;
        if (n < 3) {
            throw new IllegalArgumentException("Need at least 3 values for correlation test");
        }

        double r = getPearsonCorrelation(x, y);
        if (Double.isNaN(r)) {
            return Double.NaN;
        }

        r = Math.clamp(r, -1.0, 1.0);
        int df = n - 2;
        double t = Math.abs(r / Math.sqrt((1 - r * r) / df));
        TDistribution distribution = TDistribution.of(df);
        return 2 * distribution.cumulativeProbability(-t);
    }

    /**
     * Computes the cosine similarity between two vectors (normalized dot product).
     * <p>
     * Cosine similarity measures the cosine of the angle between two non-zero
     * vectors in an inner product space. It is defined as:
     * <pre>
     *     cos(θ) = (A · B) / (||A||<sub>2</sub> ||B||<sub>2</sub>)
     * </pre>
     * where {@code ·} denotes the dot product and {@code ||·||}<sub>2</sub> is the
     * Euclidean (L2) norm. The result ranges from {@code -1.0} (exactly opposite)
     * to {@code 1.0} (exactly the same direction), with {@code 0.0} indicating
     * orthogonality.
     * <p>
     * <b>Numerical strategy:</b> To avoid overflow and underflow when components
     * span extreme orders of magnitude, the implementation first finds the maximum
     * absolute value in each vector. Every component is then divided by its vector’s
     * maximum before accumulating the dot product and squared sums. The scaling
     * factors cancel out in the final quotient, returning the exact cosine without
     * intermediate overflow.
     * <p>
     * <b>Special floating-point values and edge cases:</b>
     * <ul>
     *   <li>If any component in either array is {@link Double#NaN}, the method
     *       immediately returns {@link Double#NaN} (IEEE 754 requirement).</li>
     *   <li>If both vectors are exactly the zero vector, the cosine is mathematically
     *       undefined. This implementation returns <b>{@code 1.0}</b> by convention.
     *       Callers should be aware of this contract.</li>
     *   <li>If only one vector is the zero vector (and the other is non-zero),
     *       the method returns {@code 0.0}.</li>
     *   <li>If either vector contains an infinite component (and no {@code NaN}),
     *       the direction cannot be determined, and {@link Double#NaN} is
     *       returned.</li>
     * </ul>
     *
     * @param xs the first array, representing a vector; must not be {@code null}
     * @param ys the second array, representing a vector; must not be {@code null}
     * @return the cosine similarity in the range {@code [-1.0, 1.0]}, or a special
     * value ({@code NaN} or {@code 0.0}) for the edge cases documented above
     * @throws IllegalArgumentException if either {@code xs} or {@code ys} is
     *                                  {@code null}, or if the two arrays have different lengths
     * @since 2026-07-21⭐
     */
    public static double getCosine(double[] xs, double[] ys) {
        checkNonNull(xs);
        checkNonNull(ys);

        if (xs.length != ys.length) {
            throw new IllegalArgumentException("Array length should equal");
        }
        double maxX = 0.0;
        double maxY = 0.0;
        for (int i = 0; i < xs.length; i++) {
            if (Double.isNaN(xs[i]) || Double.isNaN(ys[i])) {
                return Double.NaN;
            }
            maxX = Math.max(maxX, Math.abs(xs[i]));
            maxY = Math.max(maxY, Math.abs(ys[i]));
        }
        // 全 0 向量
        if (maxX == 0.0 && maxY == 0.0) {
            return 1.0;
        } else if (maxX == 0.0 || maxY == 0.0) {
            return 0.0;
        }

        if (Double.isInfinite(maxX) || Double.isInfinite(maxY)) {
            return Double.NaN;
        }

        double dot = 0.0;
        double sumX2 = 0.0;
        double sumY2 = 0.0;
        for (int i = 0; i < xs.length; i++) {
            double sx = xs[i] / maxX;
            double sy = ys[i] / maxY;
            dot += sx * sy;
            sumX2 += sx * sx;
            sumY2 += sy * sy;
        }

        if (sumX2 == 0.0 || sumY2 == 0.0) {
            return 0.0;
        }

        return dot / (Math.sqrt(sumX2) * Math.sqrt(sumY2));
    }

    /**
     * Calculates the normalized contrast angle between two vectors.
     * <p>
     * The normalized contrast angle is derived from the cosine similarity
     * returned by {@link #getCosine(double[], double[])} and is defined as
     * <pre>
     *     NCA = 1 − (2 / π) · arccos(cosine)
     * </pre>
     * It maps the cosine range {@code [-1, 1]} to {@code [-1, 1]} while
     * spreading out values near {@code 1.0}. This makes it more sensitive to
     * small directional differences when vectors are already highly similar.
     * <p>
     * <b>Numerical safeguards:</b> The cosine value is clamped to
     * {@code [-1.0, 1.0]} via {@link Math#clamp(double, double, double)}
     * before computing the arc cosine, guarding against floating‑point
     * rounding errors that could otherwise produce a value slightly outside
     * that interval and cause {@link Math#acos(double)} to return
     * {@link Double#NaN}.
     * <p>
     * <b>Special cases propagated from {@code getCosine}:</b>
     * <ul>
     *   <li>If either array is {@code null}, the underlying method throws a
     *       {@link NullPointerException}.</li>
     *   <li>If the arrays differ in length, an
     *       {@link IllegalArgumentException} is thrown.</li>
     *   <li>If the cosine is {@link Double#NaN} (because one of the vectors
     *       contains {@code NaN}, is infinite, etc.), this method returns
     *       {@link Double#NaN}.</li>
     *   <li>For two zero vectors ({@code cosine = 1.0} by convention),
     *       the result is {@code 1.0}.</li>
     *   <li>For one zero vector ({@code cosine = 0.0} by convention),
     *       the result is {@code 0.0}.</li>
     * </ul>
     *
     * @param x the first vector; must not be {@code null}
     * @param y the second vector; must not be {@code null}
     * @return the normalized contrast angle in the range {@code [-1.0, 1.0]},
     * or {@link Double#NaN} when the underlying cosine is undefined
     * @throws NullPointerException     if either {@code x} or {@code y} is
     *                                  {@code null}
     * @throws IllegalArgumentException if the arrays have different lengths
     * @see #getCosine(double[], double[])
     * @since 2026-01-05
     */
    public static double getNormalizedContrastAngle(double[] x, double[] y) {
        double cosine = getCosine(x, y);
        cosine = Math.clamp(cosine, -1.0, 1.0);
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

}
