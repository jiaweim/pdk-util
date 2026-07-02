package pdk.util.data.fitting;

import org.apache.commons.rng.sampling.distribution.*;
import org.hipparchus.optim.nonlinear.vector.leastsquares.LeastSquaresOptimizer;
import org.hipparchus.random.RandomDataGenerator;
import org.junit.jupiter.api.Test;
import pdk.chart.Chart;
import pdk.chart.JChart;
import pdk.chart.XYChartType;
import pdk.chart.data.xy.XYSeries;
import pdk.chart.data.xy.XYSeriesCollection;
import pdk.util.data.Point;
import pdk.util.data.Point2D;
import pdk.util.data.WeightPoint2D;
import pdk.util.data.func.ExponentiallyModifiedGaussianFunc;
import pdk.util.data.func.Func2D;
import pdk.util.math.SamplingUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 28 Apr 2026, 2:47 PM
 */
class EMGFitterTest {

    @Test
    void testFunction() {
        double[] parameters = new double[]{1, 0, 1, 1};
        ExponentiallyModifiedGaussianFunc emg = ExponentiallyModifiedGaussianFunc.of(0, 1, 1);

        EMGFitter fitter = EMGFitter.create();
        double EPS = 1E-10;
        for (int i = 0; i < 100; i++) {
            double f1 = fitter.value(i, parameters);
            double f2 = emg.f(i);
            assertEquals(f1, f2, EPS);
        }
    }

    @Test
    void testFunction2() {
        // add Normalization
        double[] parameters = new double[]{2, 0, 1, 5};
        ExponentiallyModifiedGaussianFunc emg = ExponentiallyModifiedGaussianFunc.of(0, 1, 5);
        EMGFitter fitter = EMGFitter.create();
        double EPS = 1E-10;
        for (int i = 0; i < 100; i++) {
            double f1 = fitter.value(i, parameters);
            double f2 = emg.f(i);
            assertEquals(f1, f2 * 2, EPS);
        }
    }

    @Test
    void testFit() {
        double[] parameters = new double[]{33.4, -3.5, 2, 5};

        EMGFitter fitter = EMGFitter.create();
        final RandomDataGenerator dataGenerator = new RandomDataGenerator(9527);

        List<WeightPoint2D> obs = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            double x = dataGenerator.nextUniform(-100, 100);
            obs.add(new WeightPoint2D(x, fitter.value(x, parameters)));
        }

        // by moment
        LeastSquaresOptimizer.Optimum optimum1 = fitter.initGuess(EMGFitter.guess(obs)).fitDetail(obs);
        double[] parameters1 = optimum1.getPoint().toArray();
        assertArrayEquals(parameters, parameters1, 1E-8);
        assertEquals(13, optimum1.getIterations());

        // by shape
        LeastSquaresOptimizer.Optimum optimum2 = fitter.initGuess(EMGFitter.guessByShape(obs)).fitDetail(obs);
        double[] parameters2 = optimum2.getPoint().toArray();
        assertArrayEquals(parameters, parameters2, 1E-8);
        assertEquals(34, optimum2.getIterations()); // slower

        // 两种估计参数的方法都可以收敛到正确参数，但通过 momenet 估计的参数收敛更快
    }

    static void demo() {
        double a = 5;
        double mu = 0;
        double sigma = 2;
        double tao = 3;

        int sampleSize = 10000;

        NormalizedGaussianSampler normalizedGaussianSampler = BoxMullerNormalizedGaussianSampler.of(SamplingUtils.rng);
        SharedStateContinuousSampler sampler = GaussianSampler.of(normalizedGaussianSampler, mu, sigma);
        ZigguratSampler.Exponential exponential = ZigguratSampler.Exponential.of(SamplingUtils.rng, tao);

        double[] array1 = sampler.samples(sampleSize).toArray();
        double[] array2 = exponential.samples(sampleSize).toArray();

        double[] sample = new double[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            sample[i] = array1[i] + array2[i];
        }

        pdk.chart.data.statistics.HistogramDataset dataset = new pdk.chart.data.statistics.HistogramDataset();
        dataset.addSeries("", sample, 100);
        dataset.setType(pdk.chart.data.statistics.HistogramType.SCALE_AREA_TO_1);

        double[] x = new double[dataset.getItemCount(0)];
        double[] y = new double[dataset.getItemCount(0)];

        for (int i = 0; i < x.length; i++) {
            x[i] = dataset.getStartX(0, i).doubleValue();
            y[i] = dataset.getStartY(0, i).doubleValue();
        }

        EMGFitter fitter = EMGFitter.create();
        double[] parameters = fitter.fit(WeightPoint2D.convert(x, y));
        System.out.println(Arrays.toString(parameters));

        ExponentiallyModifiedGaussianFunc emg = ExponentiallyModifiedGaussianFunc.of(parameters[1], parameters[2], 1 / parameters[3]);
        Func2D func2D = x1 -> emg.f(x1) * parameters[0];
        List<Point2D> fitSample = func2D.sample(-10, 10, 100);
        XYSeries<String> series = new XYSeries<>("fitSample");
        for (Point2D point2D : fitSample) {
            series.add(point2D.getX(), point2D.getY());
        }
        XYSeriesCollection<String> dataset2 = new XYSeriesCollection<>(series);

        Chart chart = JChart.histogram(null, null, null, dataset);
        chart.getXYPlot()
                .addDataset(dataset2, XYChartType.LINE);
        chart.show();
    }

    static void demo2() {
        double start = -10;
        double end = 10;
        double area = 2;
        ExponentiallyModifiedGaussianFunc emg1 = ExponentiallyModifiedGaussianFunc.of(0, 2, 1 / 5.0);
        List<Point2D> sample = emg1.sample(start, end, 500);
        List<Point2D> realSample = new ArrayList<>(sample.size());
        for (Point2D point2D : sample) {
            realSample.add(Point.create(point2D.getX(), area * (point2D.getY() + point2D.getY() * Math.random() * 0.1)));
        }

        EMGFitter fitter = new EMGFitter(null, Integer.MAX_VALUE);
        double[] parameters = fitter.fit(WeightPoint2D.convert(realSample));

        System.out.println(Arrays.toString(parameters));
        ExponentiallyModifiedGaussianFunc emg = ExponentiallyModifiedGaussianFunc.of(parameters[1], parameters[2], 1 / parameters[3]);
        Func2D func2D = x -> emg.f(x) * parameters[0];
        List<Point2D> fitSample = func2D.sample(start, end, 100);

        XYSeriesCollection<String> dataset = new XYSeriesCollection<>();
        XYSeries<String> s1 = new XYSeries<>("μ=0, σ=1, λ=1");
        for (Point2D point2D : realSample) {
            s1.add(point2D.getX(), point2D.getY());
        }
        XYSeries<String> s2 = new XYSeries<>("fit");
        for (Point2D point2D : fitSample) {
            s2.add(point2D.getX(), point2D.getY());
        }
        dataset.addSeries(s1);
        dataset.addSeries(s2);

        Chart chart = JChart.line(dataset, "X", "f(x)");
        chart.getXYPlot()
                .getLineAndShapeRenderer()
                .seriesLineWidth(0, 3f)
                .seriesLineWidth(1, 3f);
        chart.show();
    }

    static void main() {
        demo();
    }
}