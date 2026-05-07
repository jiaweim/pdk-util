package pdk.util.chart;

import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.xy.DefaultXYZDataset;
import pdk.util.tuple.Tuple;

import java.awt.*;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 07 May 2026, 10:22 AM
 */
public class XYShapeRendererDemo1 {
    static void main() {
        DefaultXYZDataset dataset = new DefaultXYZDataset();
        double[] x = new double[]{2.1, 2.3, 2.3, 2.2, 2.2, 1.8, 1.8, 1.9, 2.3, 2.8};
        double[] y = new double[]{14.1, 17.1, (double) 10.0F, 8.8, 8.7, 8.4, 5.4, 4.1, 4.1, (double) 25.0F};
        double[] z = new double[]{2.4, 2.7, 1.7, 2.2, 1.3, 2.2, 2.1, 3.2, 1.6, 3.4};
        double[][] series = new double[][]{x, y, z};
        dataset.addSeries("Series 1", series);

        ScatterChart scatterChart = ScatterChart.create()
                .dataset(dataset)
                .xAxisName("X")
                .yAxisName("Y")
                .xAxisAutoRangeIncludesZero(false)
                .yAxisAutoRangeIncludesZero(false)
                .paintScale(1.0, 4.0, new Color(0, 0, 255),
                        Tuple.of(2.0, new Color(100, 100, 255)),
                        Tuple.of(3.0, new Color(200, 200, 255)))
                .paintScaleTitle("Score")
                .paintScalePosition(RectangleEdge.RIGHT)
                .paintScaleMargin(new RectangleInsets(4.0, 4.0, 40.0, 4.0))
                .paintScaleAxisLocation(AxisLocation.BOTTOM_OR_RIGHT)
                .paintScaleTickUnits(NumberAxis.createIntegerTickUnits())
                .domainPannable(true)
                .rangePannable(true)
                .domainCrosshairVisible(true)
                .rangeCrosshairVisible(true)
                .domainCrosshairLockedOnData(true)
                .rangeCrosshairLockedOnData(true)
                .title("XYShapeRendererDemo1").build();
        scatterChart.show();
    }
}
