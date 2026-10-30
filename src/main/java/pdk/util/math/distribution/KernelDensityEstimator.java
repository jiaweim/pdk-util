package pdk.util.math.distribution;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.apache.commons.statistics.distribution.ContinuousDistribution;
import org.apache.commons.statistics.distribution.NormalDistribution;
import pdk.chart.LineChart;
import pdk.chart.XYChart;
import pdk.chart.data.general.DatasetUtils;
import pdk.chart.data.statistics.HistogramDataset;
import pdk.chart.data.statistics.HistogramType;
import pdk.chart.data.xy.XYSeries;
import pdk.util.ArgUtils;
import pdk.util.ArrayUtils;
import pdk.util.SortUtils;
import pdk.util.math.StatUtils;

import java.util.Arrays;

/**
 * Gaussian kernel density estimator.
 *
 * @author Jiawei Mao
 * @version 1.0.0⭐
 * @since 19 May 2026, 10:04 AM
 */
public class KernelDensityEstimator implements ContinuousDistribution {

    private static final double SQRT2_PI = Math.sqrt(2 * Math.PI);
    private static final double KERNEL_CUTOFF = 5.0;
    private static final int MAX_ITERATIONS = 100;

    private final double[] x_;
    private final double mean_;
    private final double variance_;
    private final double sd_;
    private final double bandWidth_;
    private final double inverseBandWidth_;
    private final double densityFactor_;
    private final NormalDistribution kernel_;

    public KernelDensityEstimator(double[] x, double bandWidth) {
        ArgUtils.checkArgument(x != null && x.length > 0,
                "data must not be null or empty");
        ArgUtils.checkArgument(Double.isFinite(bandWidth) && bandWidth > 0,
                "bandWidth must be finite and > 0");

        this.x_ = x.clone();
        for (double value : x_) {
            ArgUtils.checkArgument(Double.isFinite(value),
                    "data must contain only finite values");
        }
        Arrays.sort(x_);

        double sum = StatUtils.sum(x_);
        this.mean_ = sum / x_.length;

        this.bandWidth_ = bandWidth;
        this.inverseBandWidth_ = 1.0 / bandWidth;
        this.variance_ = StatUtils.variance(x_);
        this.densityFactor_ = inverseBandWidth_ / SQRT2_PI;
        this.sd_ = Math.sqrt(variance_);
        this.kernel_ = NormalDistribution.of(0, bandWidth);
    }

    public KernelDensityEstimator(double[] x) {
        this(x, silverman(x));
    }

    /**
     * @return bandwidth of the kernel
     */
    public double getBandWidth() {
        return bandWidth_;
    }

    /**
     * The gaussian kernel is truncated at +/- 5 bandwidths.
     *
     * @param x Point at which the PDF is evaluated.
     */
    @Override
    public double density(double x) {
        if (Double.isNaN(x)) {
            return Double.NaN;
        }
        // The effective support of the Gaussian kernel is infinite,
        // yet the density value approaches zero when the distance from the center exceeds 5h.
        // Here, binary search is used to find samples within the interval \([x-5h, x+5h]\),
        // and only these samples are summed, which greatly improves computational efficiency.
        double radius = KERNEL_CUTOFF * bandWidth_;
        int start = SortUtils.getLowerBound(x_, x - radius);
        int end = SortUtils.getUpperBound(x_, x + radius);

        double sum = 0.0;
        for (int i = start; i < end; i++) {
            double z = (x_[i] - x) * inverseBandWidth_;
            sum += Math.exp(-0.5 * z * z);
        }

        return densityFactor_ * sum / x_.length;
    }

    @Override
    public double cumulativeProbability(double x) {
        if (Double.isNaN(x)) {
            return Double.NaN;
        }
        double sum = 0.0;
        for (double xi : x_) {
            sum += kernel_.cumulativeProbability(x - xi);
        }
        return sum / this.x_.length;
    }

    @Override
    public double inverseCumulativeProbability(double p) {
        if (p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException("p must be in range [0.0, 1.0]");
        }
        if (p == 0.0) {
            return Double.NEGATIVE_INFINITY;
        }
        if (p == 1.0) {
            return Double.POSITIVE_INFINITY;
        }

        double step = bandWidth_ * 8.0;

        double lower = x_[0] - step;
        double upper = x_[x_.length - 1] + step;

        // expand the lower bound until F(lower) <= p
        while (cumulativeProbability(lower) > p) {
            step *= 2;
            lower = x_[0] - step;
        }
        step = bandWidth_ * 8.0;

        // expand upper bound until F(upper) >= p
        while (cumulativeProbability(upper) < p) {
            step *= 2;
            upper = x_[x_.length - 1] + step;
        }

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            double mid = lower * 0.5 + upper * 0.5;
            if (mid == lower || mid == upper) {
                break;
            }
            double cdf = cumulativeProbability(mid);
            if (cdf < p) {
                lower = mid;
            } else {
                upper = mid;
            }
        }

        return lower * 0.5 + upper * 0.5;
    }

    @Override
    public double getMean() {
        return mean_;
    }

    @Override
    public double getVariance() {
        return variance_;
    }

    @Override
    public double getSupportLowerBound() {
        return Double.NEGATIVE_INFINITY;
    }

    @Override
    public double getSupportUpperBound() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public Sampler createSampler(UniformRandomProvider rng) {
        Sampler sampler = kernel_.createSampler(rng);

        return () -> {
            int index = rng.nextInt(x_.length);
            return x_[index] + sampler.sample();
        };
    }

    public double getSD() {
        return sd_;
    }

    /**
     * Estimate bandwidth using Silverman's rule of thumb.
     * <p>
     * h=1.06×σ×n^{-1/5}
     *
     * @param data data
     * @return bandwidth
     */
    public static double silverman(double[] data) {
        int n = data.length;
        double sd = StatUtils.standardDeviation(data);
        return 1.06 * sd * Math.pow(n, -0.2);
    }

    /**
     * Estimate bandwidth using robust Silverman's rule.
     * <p>
     * h = 0.9 * min(sigma, IQR / 1.34) * n^(-1/5)
     */
    public static double robustSilverman(double[] data) {
        double[] sorted = data.clone();
        Arrays.sort(sorted);

        double mean = 0.0;
        for (double value : sorted) {
            mean += value;
        }
        mean /= sorted.length;

        double sum = 0.0;
        for (double value : sorted) {
            double d = value - mean;
            sum += d * d;
        }

        double sd = Math.sqrt(sum / sorted.length);

        double iqr = quantile(sorted, 0.75) -
                quantile(sorted, 0.25);

        double scale = Math.min(sd, iqr / 1.34);

        if (!(scale > 0.0)) {
            throw new IllegalArgumentException(
                    "Cannot estimate bandwidth: zero robust scale");
        }

        return 0.9 * scale * Math.pow(sorted.length, -0.2);
    }

    private static double quantile(double[] sorted, double p) {
        double index = p * (sorted.length - 1);
        int lower = (int) index;
        int upper = Math.min(lower + 1, sorted.length - 1);

        double fraction = index - lower;

        return sorted[lower] +
                fraction * (sorted[upper] - sorted[lower]);
    }

    static void main() {
        NormalDistribution gaussian = NormalDistribution.of(0, 1);
        Sampler sampler = gaussian.createSampler(RandomSource.XO_RO_SHI_RO_128_PP.create());

        double[] dataset = sampler.samples(1000).toArray();
        KernelDensityEstimator kde = new KernelDensityEstimator(dataset, robustSilverman(dataset));

        HistogramDataset dataset1 = new HistogramDataset();
        dataset1.addSeries("Histogram", dataset, 30);
        dataset1.setType(HistogramType.SCALE_AREA_TO_1);

        double[] x = ArrayUtils.linspace(-5, 5, 1000);
        double[] y = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            y[i] = kde.density(x[i]);
        }

        XYSeries<String> series = new XYSeries<>("KDE", x, y);
        LineChart chart = new LineChart(DatasetUtils.createXY(series));
        chart.getRenderer().withSeriesStrokeWidth(0, 4f);
        chart.addDataset(dataset1, XYChart.Type.BAR);
        chart.show();
    }
}
