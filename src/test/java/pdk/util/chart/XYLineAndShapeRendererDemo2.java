package pdk.util.chart;

import org.jfree.data.xy.XYDataset;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 24 Apr 2026, 1:04 PM
 */
public class XYLineAndShapeRendererDemo2 {
    static void main() {
        XYDataset dataset1 = Data.createXYDataset("Series 1", new double[]{1, 2, 3}, new double[]{1, 1, 1});
        XYDataset dataset2 = Data.createXYDataset("Series 2", new double[]{1, 2, 3}, new double[]{2, 2, 2});
        XYDataset dataset3 = Data.createXYDataset("Series 3", new double[]{1, 2, 3}, new double[]{3, 3, 3});
        XYDataset dataset4 = Data.createXYDataset("Series 4", new double[]{1, 2, 3}, new double[]{4, 4, 4});
        XYDataset dataset5 = Data.createXYDataset("Series 5", new double[]{1, 2, 3}, new double[]{5, 5, 5});

        Shape shape = new Ellipse2D.Double(-4.0, -4.0, 8.0, 8.0);

        LineChart chart = LineChart.chart()
                .title("XYLineAndShapeRendererDemo2")
                .dataset(dataset1)
                .addDataset(dataset2)
                .addDataset(dataset3)
                .addDataset(dataset4)
                .addDataset(dataset5)
                .xAxisName("X")
                .yAxisName("Y")
                .showShape(true)
                .showLine(true)
                .seriesShape(0, shape)
                .seriesPaint(0, Color.RED)
                .seriesFillPaint(0, Color.YELLOW)
                .seriesOutlinePaint(0, Color.GRAY)

                .seriesShape(1, 0, shape)
                .seriesPaint(1, 0, Color.RED)
                .seriesFillPaint(1, 0, Color.YELLOW)
                .seriesOutlinePaint(1, 0, Color.GRAY)
                .useFillPaint(1, true)

                .seriesShape(2, 0, shape)
                .seriesPaint(2, 0, Color.RED)
                .seriesFillPaint(2, 0, Color.YELLOW)
                .seriesOutlinePaint(2, 0, Color.GRAY)
                .useOutlinePaint(2, true)

                .seriesShape(3, 0, shape)
                .seriesPaint(3, 0, Color.RED)
                .seriesFillPaint(3, 0, Color.YELLOW)
                .seriesOutlinePaint(3, 0, Color.GRAY)
                .useFillPaint(3, true)
                .useOutlinePaint(3, true)

                .seriesShape(4, 0, shape)
                .seriesPaint(4, 0, Color.RED)
                .seriesFillPaint(4, 0, Color.YELLOW)
                .seriesOutlinePaint(4, 0, Color.GRAY)
                .useOutlinePaint(4, true)
                .useFillPaint(4, true)
                .drawOutlines(4, false)

                .addLegend(true)
                .addTooltips(true)
                .xAxisAutoRangeIncludesZero(false)
                .yAxisAutoRangeIncludesZero(false);
        chart.show();
    }
}
