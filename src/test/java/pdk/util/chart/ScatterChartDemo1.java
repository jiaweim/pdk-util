package pdk.util.chart;

import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.util.ShapeUtils;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 08 May 2026, 11:28 AM
 */
public class ScatterChartDemo1 {

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
        Shape[] shapeSequence = DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE;
        ScatterChart scatterChart = ScatterChart.create()
                .xAxisName("X")
                .yAxisName("Y")
                .dataset(create(4, 40))
                .seriesOutlinePaint(0, Color.BLACK)
                .seriesShape(0, shapeSequence[0])
                .seriesShape(1, shapeSequence[1])
                .seriesShape(2, shapeSequence[2])
                .seriesShape(3, shapeSequence[3])
                .useOutlinePaint(true)
                .drawOutlines(true)
                .xAxisAutoRangeIncludesZero(false)
                .domainZeroBaselineVisible(true)
                .rangeZeroBaselineVisible(true)
                .addLegend(true)
                .domainGridlinesVisible(false)
                .rangeGridlinesVisible(false)
                .build();
        scatterChart.show();
    }
}
