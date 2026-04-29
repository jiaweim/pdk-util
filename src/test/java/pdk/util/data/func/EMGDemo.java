package pdk.util.data.func;

import org.jfree.data.xy.XYDataset;
import pdk.util.chart.Data;
import pdk.util.chart.LineChart;
import pdk.util.chart.util.LegendTitleBuilder;
import pdk.util.data.Point2D;

import java.util.List;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 28 Apr 2026, 12:27 PM
 */
public class EMGDemo {
    static void main() {

        ExponentiallyModifiedGaussianFunc emg1 = ExponentiallyModifiedGaussianFunc.of(0, 1, 1);
        ExponentiallyModifiedGaussianFunc emg2 = ExponentiallyModifiedGaussianFunc.of(-2, 1, 1);
        ExponentiallyModifiedGaussianFunc emg3 = ExponentiallyModifiedGaussianFunc.of(0, 3, 1);
        ExponentiallyModifiedGaussianFunc emg4 = ExponentiallyModifiedGaussianFunc.of(-3, 1, 0.25);
        List<Point2D> sample1 = emg1.sample(-10, 10, 500);
        List<Point2D> sample2 = emg2.sample(-10, 10, 500);
        List<Point2D> sample3 = emg3.sample(-10, 10, 500);
        List<Point2D> sample4 = emg4.sample(-10, 10, 500);

        XYDataset dataset = Data.xyDataset()
                .addSeries("μ=0, σ=1, τ=1", sample1)
                .addSeries("μ=-2, σ=1, τ=1", sample2)
                .addSeries("μ=0, σ=3, τ=1", sample3)
                .addSeries("μ=-3, σ=1, τ=0.25", sample4)
                .build();
        LineChart chart = LineChart.lineChart()
                .addLegend(true)
                .xAxisName("X")
                .yAxisName("f(x)")
                .seriesLinesWidth(0, 4F)
                .seriesLinesWidth(1, 4F)
                .seriesLinesWidth(2, 4F)
                .seriesLinesWidth(3, 4F)
                .dataset(dataset).build();
        chart.show();
    }
}
