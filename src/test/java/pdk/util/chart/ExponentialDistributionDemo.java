package pdk.util.chart;

import org.apache.commons.statistics.distribution.ExponentialDistribution;
import pdk.chart.Chart;
import pdk.chart.JChart;
import pdk.chart.data.xy.XYSeriesCollection;
import pdk.util.data.Point2D;
import pdk.util.math.DistributionUtils;

import java.util.ArrayList;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 24 Apr 2026, 3:35 PM
 */
public class ExponentialDistributionDemo {
    static void main() {
        ExponentialDistribution d1 = ExponentialDistribution.of(0.5);
        ExponentialDistribution d2 = ExponentialDistribution.of(1.0);

        ArrayList<Point2D> sample1 = DistributionUtils.sample(d1, 0.0, 4.0, 500);
        ArrayList<Point2D> sample2 = DistributionUtils.sample(d2, 0.0, 4.0, 500);

        pdk.chart.data.xy.XYSeries<String> series1 = new pdk.chart.data.xy.XYSeries<>("λ=0.5");
        pdk.chart.data.xy.XYSeries<String> series2 = new pdk.chart.data.xy.XYSeries<>("λ=1.0");
        for (int i = 0; i < sample1.size(); i++) {
            Point2D p1 = sample1.get(i);
            Point2D p2 = sample2.get(i);
            series1.add(p1.getX(), p1.getY());
            series2.add(p2.getX(), p2.getY());
        }

        XYSeriesCollection<String> dataset1 = new XYSeriesCollection<>();
        dataset1.addSeries(series1);
        dataset1.addSeries(series2);

        Chart chart = JChart.line(null, "X", "Probability density", dataset1);
        chart.show();
    }
}
