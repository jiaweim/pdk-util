package pdk.util.math;

import org.apache.commons.statistics.distribution.*;
import org.hipparchus.special.Gamma;
import pdk.chart.Chart;
import pdk.chart.JChart;
import pdk.chart.XYChartType;
import pdk.chart.data.xy.XYSeries;
import pdk.chart.data.xy.XYSeriesCollection;
import pdk.util.ArgUtils;
import pdk.util.data.Point;
import pdk.util.data.Point2D;

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
            list.add(Point.create(x, y));
        }
        return list;
    }

    /**
     * Create a {@link Chart} to show the probability density for a {@link ContinuousDistribution}.
     *
     * @param distribution {@link ContinuousDistribution}
     * @param start        start x
     * @param end          end x
     * @param samples      number of data points
     * @return {@link Chart}
     */
    public static Chart pdfChart(ContinuousDistribution distribution, double start, double end, int samples) {
        ArrayList<Point2D> sample = sample(distribution, start, end, samples);
        XYSeries<String> series = createSeries("", sample);
        XYSeriesCollection<String> dataset = new XYSeriesCollection<>(series);

        return JChart.line(dataset, "X", "Probability Density");
    }

    /**
     * Create a {@link Chart} for a {@link ContinuousDistribution}.
     * </p>
     *
     * @param distribution {@link ContinuousDistribution}
     * @param start        start x
     * @param end          end x
     * @param areaStart    start x of shaded area
     * @param areaEnd      end x of shaded area
     * @param samples      number of data points
     * @return {@link Chart}
     */
    public static Chart pdfChart(ContinuousDistribution distribution, double start, double end,
            double areaStart, double areaEnd, int samples) {

        double shadowStart = Math.max(start, areaStart);
        double shadowEnd = Math.min(end, areaEnd);

        ArrayList<Point2D> sample = sample(distribution, start, end, samples);
        XYSeries<String> lineSeries = new XYSeries<>("Line");
        XYSeries<String> areaSeries = new XYSeries<>("Area");
        for (Point2D point : sample) {
            double x = point.getX();
            lineSeries.add(x, point.getY());
            if (x >= shadowStart && x <= shadowEnd) {
                areaSeries.add(x, point.getY());
            }
        }
        XYSeriesCollection<String> dataset1 = new XYSeriesCollection<>(lineSeries);
        XYSeriesCollection<String> dataset2 = new XYSeriesCollection<>(areaSeries);

        Chart chart = JChart.line(dataset1, "X", "Probability Density");
        chart.getXYPlot()
                .addDataset(dataset2, XYChartType.AREA);

        return chart;
    }

    private static XYSeries<String> createSeries(String name, ArrayList<Point2D> points) {
        XYSeries<String> series = new XYSeries<>(name);
        for (Point2D point : points) {
            series.add(point.getX(), point.getY());
        }
        return series;
    }

    static void main() {
        TDistribution t1 = TDistribution.of(1);
        TDistribution t2 = TDistribution.of(2);
        TDistribution t3 = TDistribution.of(5);
        NormalDistribution n1 = NormalDistribution.of(0, 1);

        XYSeriesCollection<String> dataset = new XYSeriesCollection<>();
        dataset.addSeries(createSeries("1", sample(t1, -6, 6, 500)));
        dataset.addSeries(createSeries("2", sample(t2, -6, 6, 500)));
        dataset.addSeries(createSeries("3", sample(t3, -6, 6, 500)));
        dataset.addSeries(createSeries("4", sample(n1, -6, 6, 500)));

//        pdk.chart.LineChart lineChart = new pdk.chart.LineChart();
//        lineChart.dataset(dataset)
//                .seriesLineWidth(0, 4F)
//                .seriesLineWidth(1, 4F)
//                .seriesLineWidth(2, 4F)
//                .seriesLineWidth(3, 4F)
//                .addPointerAnnotation("normal", 0, 0.4, Math.toRadians(0), 0, pdk.chart.text.TextAnchor.CENTER_LEFT, Color.YELLOW)
//                .domainGridlinesVisible(false)
//                .rangeGridlinesVisible(false);
//        lineChart.show();

        ExponentialDistribution exponentialDistribution = ExponentialDistribution.of(29);

//        NormalDistribution distribution = NormalDistribution.of(mean, 1.62);
//        System.out.println(distribution.probability(mean - 6 * sigma, mean + 6 * sigma));

//        pdfChart(exponentialDistribution, 1, 200, 10, 20, 500).show();

        pdfChart(exponentialDistribution, 1, 200, 500).show();
    }
}
