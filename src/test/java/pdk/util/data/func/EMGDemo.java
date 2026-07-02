package pdk.util.data.func;

import pdk.chart.Chart;
import pdk.chart.JChart;
import pdk.chart.api.RectangleEdge;
import pdk.chart.data.xy.XYSeries;
import pdk.chart.data.xy.XYSeriesCollection;
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

    private static XYSeries<String> toSeries(String name, List<Point2D> points) {
        XYSeries<String> series = new XYSeries<>(name);
        for (Point2D point : points) {
            series.add(point.getX(), point.getY());
        }
        return series;
    }

    static void main() {

        ExponentiallyModifiedGaussianFunc emg1 = ExponentiallyModifiedGaussianFunc.of(0, 1, 1);
        ExponentiallyModifiedGaussianFunc emg2 = ExponentiallyModifiedGaussianFunc.of(-2, 1, 1);
        ExponentiallyModifiedGaussianFunc emg3 = ExponentiallyModifiedGaussianFunc.of(0, 3, 1);
        ExponentiallyModifiedGaussianFunc emg4 = ExponentiallyModifiedGaussianFunc.of(-3, 1, 0.25);
        List<Point2D> sample1 = emg1.sample(-10, 10, 500);
        List<Point2D> sample2 = emg2.sample(-10, 10, 500);
        List<Point2D> sample3 = emg3.sample(-10, 10, 500);
        List<Point2D> sample4 = emg4.sample(-10, 10, 500);

        XYSeriesCollection<String> dataset = new XYSeriesCollection<>();
        dataset.addSeries(toSeries("μ=0, σ=1, τ=1", sample1));
        dataset.addSeries(toSeries("μ=-2, σ=1, τ=1", sample2));
        dataset.addSeries(toSeries("μ=0, σ=3, τ=1", sample3));
        dataset.addSeries(toSeries("μ=-3, σ=1, τ=0.25", sample4));

        Chart chart = JChart.line(dataset, "X", "Y");
        chart.getXYPlot()
                .getLineAndShapeRenderer()
                .seriesLineWidth(0, 4f)
                .seriesLineWidth(1, 4f)
                .seriesLineWidth(2, 4f)
                .seriesLineWidth(3, 4f);
        chart.getLegend().setPosition(RectangleEdge.RIGHT);
        chart.show();
    }
}
