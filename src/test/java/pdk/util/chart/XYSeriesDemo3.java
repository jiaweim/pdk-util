package pdk.util.chart;

import org.jfree.chart.plot.Marker;
import org.jfree.chart.ui.Layer;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.xy.XYDataset;

import java.awt.*;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 24 Apr 2026, 2:04 PM
 */
public class XYSeriesDemo3 {
    static void main() {
        XYDataset dataset = Data.createXYDataset("Random Data",
                new double[]{1.0, 5.0, 4.0, 12.5, 17.3, 21.2, 21.9, 25.6, 30.0},
                new double[]{400.2, 294.1, 100.0, 734.4, 453.2, 500.2, Double.NaN, 734.4, 453.2}
        );

        Marker marker = DataMarker.range(400, 700)
                .label("Target Range")
                .labelFont(new Font("SansSerif", Font.ITALIC, 11))
                .labelAnchor(RectangleAnchor.LEFT)
                .labelTextAnchor(TextAnchor.CENTER_LEFT)
                .paint(new Color(222, 222, 255, 128))
                .getMarker();

        BarChart chart = BarChart.chart()
                .dataset(dataset)
                .title("XY Series Demo")
                .xAxisName("X")
                .yAxisName("Y")
                .addLegend(true)
                .addTooltips(true)
                .addRangeMarker(marker, Layer.BACKGROUND)
                .build();
        chart.show();
    }
}
