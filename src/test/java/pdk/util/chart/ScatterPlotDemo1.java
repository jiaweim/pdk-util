package pdk.util.chart;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 23 Apr 2026, 12:52 PM
 */
public class ScatterPlotDemo1 {

    private static final double DEFAULT_RANGE = 200;

    public static XYDataset create(int seriesCount, int itemCount) {
        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        XYSeries[] seriesArray = new XYSeries[seriesCount];
        for (int i = 0; i < seriesCount; i++) {
            seriesArray[i] = new XYSeries("Sample " + i);
        }

        for (int series = 0; series < seriesCount; series++) {
            for (int item = 0; item < itemCount; item++) {
                double x = (Math.random() - 0.5) * DEFAULT_RANGE;
                if (x < minX) {
                    minX = x;
                }
                if (x > maxX) {
                    maxX = x;
                }

                double y = (Math.random() + 0.5) * 6 * x + x;
                if (y < minY) {
                    minY = y;
                }
                if (y > maxY) {
                    maxY = y;
                }
                seriesArray[series].add(x, y);
            }
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (XYSeries series : seriesArray) {
            dataset.addSeries(series);
        }
        return dataset;
    }

    static void main() {
        LineChart lineChart = LineChart.chart()
                .dataset(create(4, 40))
                .showLine(false)
                .showShape(true)
                .seriesOutlinePaint(0, Color.BLACK)
                .useOutlinePaint(true)
                .xAxisAutoRangeIncludesZero(false)
                .domainZeroBaselineVisible(true)
                .rangeZeroBaselineVisible(true)
                .addLegend(true)
                .showRangeGridlines(false)
                .showDomainGridlines(false)
                .build();
        lineChart.show();
    }
}
