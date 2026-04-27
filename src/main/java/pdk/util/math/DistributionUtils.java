package pdk.util.math;

import org.apache.commons.statistics.distribution.*;
import org.hipparchus.special.Gamma;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.xy.XYDataset;
import pdk.util.ArgUtils;
import pdk.util.chart.Data;
import pdk.util.chart.LineChart;
import pdk.util.data.Point2D;

import java.awt.*;
import java.util.ArrayList;

/**
 * Class for statistics distributions.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 11 Dec 2025, 11:42 AM
 */
public final class DistributionUtils {

    private DistributionUtils() {}

    private static final NormalDistribution NORMAL = NormalDistribution.of(0, 1);

    /**
     * Computes the probability density of Normal distribution at x.
     *
     * @param mean   The mean (μ) of the normal distribution.
     * @param stddev The standard deviation (σ) of the normal distribution. Range: σ ≥ 0.
     * @param x      The location at which to compute the density.
     * @return the density at x.
     */
    public static double getNormalPDF(double mean, double stddev, double x) {
        if (stddev < 0.0) {
            throw new IllegalArgumentException("Invalid parametrization for the distribution.");
        }
        double num = (x - mean) / stddev;
        return NORMAL.density(num);
    }

    /**
     * Computes the probability density of the TDistribution at x.
     *
     * @param mean    the location (μ) of the TDistribution
     * @param scale   the scale (σ) of the TDistribution. Range: σ>0.
     * @param freedom the degrees of freedom (ν) for the TDistribution. Range: ν>0.
     * @param x       the location at which to compute the density.
     * @return density at x.
     * @since 2026-04-03⭐
     */
    public static double getStudentTPDF(double mean, double scale, double freedom, double x) {
        if (scale <= 0.0 || freedom <= 0.0) {
            throw new IllegalArgumentException("Invalid parametrization for the distribution.");
        }
        if (freedom >= 100000000.0) {
            return getNormalPDF(mean, scale, x);
        }
        double num = (x - mean) / scale;
        return Math.exp(Gamma.logGamma((freedom + 1.0) / 2.0) - Gamma.logGamma(freedom / 2.0))
                * Math.pow(1.0 + num * num / freedom, -0.5 * (freedom + 1.0))
                / Math.sqrt(freedom * Math.PI) / scale;
    }

    /**
     * Creates a uniform continuous distribution.
     *
     * @param lower Lower bound of this distribution (inclusive).
     * @param upper Upper bound of this distribution (inclusive).
     * @return the distribution
     * @throws IllegalArgumentException if {@code lower >= upper} or the range between the bounds
     *                                  is not finite
     */
    public static UniformContinuousDistribution uniform(double lower, double upper) {
        return UniformContinuousDistribution.of(lower, upper);
    }

    /**
     * Creates a normal distribution.
     *
     * @param mean              Mean for this distribution.
     * @param standardDeviation Standard deviation for this distribution.
     * @return the distribution
     * @throws IllegalArgumentException if {@code sd <= 0}.
     */
    public static NormalDistribution normal(double mean, double standardDeviation) {
        return NormalDistribution.of(mean, standardDeviation);
    }

    /**
     * Creates a beta distribution.
     *
     * @param alpha First shape parameter (must be positive).
     * @param beta  Second shape parameter (must be positive).
     * @return the distribution
     * @throws IllegalArgumentException if {@code alpha <= 0} or {@code beta <= 0}.
     */
    public static BetaDistribution beta(double alpha, double beta) {
        return BetaDistribution.of(alpha, beta);
    }

    /**
     * Sampling uniformly from a {@link ContinuousDistribution}
     *
     * @param distribution {@link ContinuousDistribution} instance
     * @param start        start x
     * @param end          end x
     * @param samples      number of samples
     * @return samples
     */
    public static ArrayList<Point2D> sample(ContinuousDistribution distribution, double start, double end, int samples) {
        ArgUtils.checkArgument(start < end, "Require start < end");
        double step = (end - start) / (samples - 1);
        ArrayList<Point2D> list = new ArrayList<>(samples);
        for (int i = 0; i < samples; i++) {
            double x = start + (i * step);
            double y = distribution.density(x);
            list.add(new Point2D(x, y));
        }
        return list;
    }

    static void main() {
        TDistribution t1 = TDistribution.of(1);
        TDistribution t2 = TDistribution.of(2);
        TDistribution t3 = TDistribution.of(5);
        NormalDistribution n1 = NormalDistribution.of(0, 1);

        XYDataset dataset = Data.xyDataset()
                .addSeries("1", sample(t1, -6, 6, 500))
                .addSeries("2", sample(t2, -6, 6, 500))
                .addSeries("3", sample(t3, -6, 6, 500))
                .addSeries("4", sample(n1, -6, 6, 500))
                .build();
        LineChart chart = LineChart
                .lineChart()
                .dataset(dataset)
                .seriesLinesWidth(0, 4F)
                .seriesLinesWidth(1, 4F)
                .seriesLinesWidth(2, 4F)
                .seriesLinesWidth(3, 4F)
                .addPointerAnnotation("normal", 0, 0.4, Math.toRadians(0), 0, TextAnchor.CENTER_LEFT, Color.YELLOW)
                .domainGridlinesVisible(false)
                .showRangeGridlines(false)
                .build();
        chart.show();
    }
}
