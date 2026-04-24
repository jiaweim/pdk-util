package pdk.util.chart;

import org.apache.commons.statistics.distribution.NormalDistribution;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import pdk.util.data.Point2D;
import pdk.util.math.DistributionUtils;

import java.util.ArrayList;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 24 Apr 2026, 4:05 PM
 */
public class XYChartDemo {
    static void main() {
        NormalDistribution distribution = NormalDistribution.of(20.6, 1.62);
        ArrayList<Point2D> samples = DistributionUtils.sample(distribution, 20.6 - 10, 20.6 + 10, 500);
        XYSeries lineSeries = new XYSeries("Line");
        XYSeries areaSeries = new XYSeries("Area");
        for (Point2D point2D : samples) {
            double x = point2D.getX();
            lineSeries.add(x, point2D.getY());
            if (x >= 10.6 && x <= 18) {
                areaSeries.add(x, point2D.getY());
            }
        }

        XYDataset dataset1 = Data.xyDataset().addSeries(lineSeries).build();
        XYDataset dataset2 = Data.xyDataset().addSeries(areaSeries).build();

        XYChart chart = XYChart.chart()
                .xAxisName("X")
                .yAxisName("Probability Density")
                .addDataset(0, dataset1, XYChartType.LINE)
                .addDataset(1, dataset2, XYChartType.AREA)
                .addLegend(false)
                .build();
        chart.show();
    }
}
