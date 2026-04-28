package pdk.util.data.fitting;

import org.apache.commons.rng.sampling.distribution.*;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.XYDataset;
import org.junit.jupiter.api.Test;
import pdk.util.chart.Data;
import pdk.util.chart.LineChart;
import pdk.util.chart.XYChart;
import pdk.util.chart.XYChartType;
import pdk.util.data.Point2D;
import pdk.util.data.func.ExponentiallyModifiedGaussianFunc;
import pdk.util.data.func.Func2D;
import pdk.util.math.SamplingUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 28 Apr 2026, 2:47 PM
 */
class EMGCurveFitterTest {

    @Test
    void testFunction() {
        double[] parameters = new double[]{1, 0, 1, 1};
        ExponentiallyModifiedGaussianFunc emg = ExponentiallyModifiedGaussianFunc.of(0, 1, 1);

        double EPS = 1E-10;
        for (int i = 0; i < 100; i++) {
            double f1 = EMGCurveFitter.FUNCTION.value(i, parameters);
            double f2 = emg.f(i);
            assertEquals(f1, f2, EPS);
        }
    }

    @Test
    void testFit() {
        ExponentiallyModifiedGaussianFunc emg1 = ExponentiallyModifiedGaussianFunc.of(0, 1, 1);
        List<Point2D> sample = emg1.sample(-10, 10, 500);
        for (Point2D point2D : sample) {
            point2D.setY(point2D.getY() + point2D.getY() * Math.random() * 0.1);
        }

        EMGCurveFitter fitter = new EMGCurveFitter(null, Integer.MAX_VALUE);
        double[] parameters = fitter.fit(DataUtils.createDataset(sample).toList());
        System.out.println(Arrays.toString(parameters));
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

        HistogramDataset dataset = Data.histogramDataset().addSeries("", sample, 100)
                .type(HistogramType.SCALE_AREA_TO_1)
                .build();
        double[] x = new double[dataset.getItemCount(0)];
        double[] y = new double[dataset.getItemCount(0)];

        for (int i = 0; i < x.length; i++) {
            x[i] = dataset.getStartX(0, i).doubleValue();
            y[i] = dataset.getStartY(0, i).doubleValue();
        }

        EMGCurveFitter fitter = EMGCurveFitter.create();
        double[] parameters = fitter.fit(DataUtils.createDataset(x, y));
        System.out.println(Arrays.toString(parameters));

        ExponentiallyModifiedGaussianFunc emg = ExponentiallyModifiedGaussianFunc.of(parameters[1], parameters[2], 1 / parameters[3]);
        Func2D func2D = new Func2D() {
            @Override
            public double f(double x) {
                return emg.f(x) * parameters[0];
            }
        };
        List<Point2D> fitSample = func2D.sample(-10, 10, 100);

        XYDataset dataset2 = Data.xyDataset().addSeries("fitSample", fitSample).build();

        XYChart chart = XYChart.chart()
                .addDataset(0, dataset, XYChartType.HISTOGRAM)
                .addDataset(1, dataset2, XYChartType.LINE)
                .addLegend(true)
                .build();
        chart.show();
    }

    static void demo2() {
        double start = -10;
        double end = 10;
        double area = 2;
        ExponentiallyModifiedGaussianFunc emg1 = ExponentiallyModifiedGaussianFunc.of(0, 2, 1 / 5.0);
        List<Point2D> sample = emg1.sample(start, end, 500);
        for (Point2D point2D : sample) {
            point2D.setY(area * (point2D.getY() + point2D.getY() * Math.random() * 0.1));
        }

        EMGCurveFitter fitter = new EMGCurveFitter(null, Integer.MAX_VALUE);
        double[] parameters = fitter.fit(DataUtils.createDataset(sample).toList());

        System.out.println(Arrays.toString(parameters));
        ExponentiallyModifiedGaussianFunc emg = ExponentiallyModifiedGaussianFunc.of(parameters[1], parameters[2], 1 / parameters[3]);
        Func2D func2D = x -> emg.f(x) * parameters[0];
        List<Point2D> fitSample = func2D.sample(start, end, 100);

        XYDataset dataset = Data.xyDataset()
                .addSeries("μ=0, σ=1, λ=1", sample)
                .addSeries("fit", fitSample)
                .build();
        LineChart chart = LineChart.lineChart()
                .addLegend(true)
                .xAxisName("X")
                .yAxisName("f(x)")
                .seriesLinesWidth(0, 3F)
                .seriesLinesWidth(1, 3F)
                .dataset(dataset).build();
        chart.show();
    }

    static void main() {
        demo();
    }
}