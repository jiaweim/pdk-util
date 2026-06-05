package pdk.util.math.distribution;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.apache.commons.statistics.distribution.ContinuousDistribution;
import org.apache.commons.statistics.distribution.NormalDistribution;
import pdk.chart.LineChart;
import pdk.chart.data.xy.XYSeries;
import pdk.chart.data.xy.XYSeriesCollection;
import pdk.util.ArgUtils;
import pdk.util.ArrayUtils;
import pdk.util.math.StatUtils;

import java.util.Arrays;

/**
 * KDE implementation.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 19 May 2026, 10:04 AM
 */
public class KernelDensityEstimator implements ContinuousDistribution {

    private final double[] x_;
    private double mean_;
    private double variance_;
    private double sd_;
    /**
     * bandwidth
     */
    private double bandWidth_;
    /**
     * Gaussian kernel
     */
    private NormalDistribution kernel_;

    public KernelDensityEstimator(double[] x, double bandWidth) {
        ArgUtils.checkArgument(bandWidth > 0, "bandWidth must be > 0");

        this.x_ = x;
        this.bandWidth_ = bandWidth;
        this.mean_ = StatUtils.mean(x_);
        this.variance_ = StatUtils.variance(x_);
        this.sd_ = Math.sqrt(variance_);
        this.kernel_ = NormalDistribution.of(0, bandWidth);
        Arrays.sort(x_);
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

    @Override
    public double density(double x) {
        // The effective support of the Gaussian kernel is infinite,
        // yet the density value approaches zero when the distance from the center exceeds 5h.
        // Here, binary search is used to find samples within the interval \([x-5h, x+5h]\),
        // and only these samples are summed, which greatly improves computational efficiency.
        int start = Arrays.binarySearch(x_, x - 5 * bandWidth_);
        if (start < 0) {
            start = -start - 1;
        }
        int end = Arrays.binarySearch(x_, x + 5 * bandWidth_);
        if (end < 0) {
            end = -end - 1;
        }
        double p = 0;
        for (int i = start; i < end; i++) {
            p += kernel_.density(this.x_[i] - x);
        }
        return p / this.x_.length;
    }

    @Override
    public double cumulativeProbability(double x) {
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
        double xmin = this.x_[0] - 5 * bandWidth_;
        double xmax = this.x_[this.x_.length - 1] + 5 * bandWidth_;
        return 0;
    }

    public double getMean() {
        return mean_;
    }

    public double getVariance() {
        return variance_;
    }

    @Override
    public double getSupportLowerBound() {
        return 0;
    }

    @Override
    public double getSupportUpperBound() {
        return 0;
    }

    @Override
    public Sampler createSampler(UniformRandomProvider rng) {
        return null;
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

    static void main() {
        NormalDistribution gaussian = NormalDistribution.of(0, 1);
        Sampler sampler = gaussian.createSampler(RandomSource.XO_RO_SHI_RO_128_PP.create());

        double[] dataset = sampler.samples(1000).toArray();
        KernelDensityEstimator kde = new KernelDensityEstimator(dataset);

        pdk.chart.data.statistics.HistogramDataset dataset1 = new pdk.chart.data.statistics.HistogramDataset();
        dataset1.addSeries("Histogram", dataset, 30);
        dataset1.setType(pdk.chart.data.statistics.HistogramType.SCALE_AREA_TO_1);

        double[] x = ArrayUtils.linspace(-5, 5, 1000);
        double[] y = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            y[i] = kde.density(x[i]);
        }

        XYSeries<String> series = new XYSeries<>("KDE", x, y);
        XYSeriesCollection<String> dataset2 = new XYSeriesCollection<>(series);

        LineChart chart = new LineChart();
        chart.addDataset(1, dataset1, pdk.chart.XYChartType.HISTOGRAM)
                .addDataset(0, dataset2, pdk.chart.XYChartType.LINE);
        chart.show();
    }
}
