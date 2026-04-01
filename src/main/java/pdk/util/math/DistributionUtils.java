package pdk.util.math;

import org.apache.commons.statistics.distribution.*;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ui.TextAnchor;
import pdk.util.ArgUtils;
import pdk.util.chart.ChartUtils;
import pdk.util.chart.XYChartBuilder;
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

        JFreeChart chart1 = XYChartBuilder.start()
                .addLineChart("1", sample(t1, -6, 6, 500), 4.0)
                .addLineChart("2", sample(t2, -6, 6, 500), 4.0)
                .addLineChart("3", sample(t3, -6, 6, 500), 4.0)
                .addLineChart("4", sample(n1, -6, 6, 500), 4.0)
                .addPointerAnnotation("normal", 0, 0.4, Math.toRadians(0), 0, TextAnchor.CENTER_LEFT, Color.YELLOW)
                .showDomainGridLines(false)
                .showRangeGridLines(false)
                .build();
        ChartUtils.showChart(chart1);
    }
}
