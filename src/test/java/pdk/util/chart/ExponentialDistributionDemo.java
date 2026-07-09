package pdk.util.chart;

import org.apache.commons.statistics.distribution.ExponentialDistribution;
import pdk.chart.Chart;
import pdk.chart.Data;
import pdk.chart.JChart;
import pdk.chart.data.xy.XYSeries;
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

        XYSeries<String> series1 = new XYSeries<>("λ=0.5");
        XYSeries<String> series2 = new XYSeries<>("λ=1.0");
        for (int i = 0; i < sample1.size(); i++) {
            Point2D p1 = sample1.get(i);
            Point2D p2 = sample2.get(i);
            series1.add(p1.getX(), p1.getY());
            series2.add(p2.getX(), p2.getY());
        }

        Chart chart = JChart.line(Data.createXY(series1, series2), "X", "Probability density");
        chart.show();
    }
}
