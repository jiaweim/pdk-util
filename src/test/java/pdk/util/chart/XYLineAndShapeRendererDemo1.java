package pdk.util.chart;

import org.jfree.data.xy.XYDataset;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 24 Apr 2026, 12:56 PM
 */
public class XYLineAndShapeRendererDemo1 {
    static void main() {
        XYDataset dataset = Data.xyDataset()
                .addSeries("Series 1", new double[]{1, 2, 3}, new double[]{3.3, 4.4, 1.7})
                .addSeries("Series 2", new double[]{1, 2, 3, 4}, new double[]{7.3, 6.8, 9.6, 5.6})
                .build();
        LineChart chart = LineChart.chart()
                .dataset(dataset)
                .xAxisName("X")
                .yAxisName("Y")
                .title("XYLineAndShapeRenderer Demo 1")
                .addLegend(true)
                .addTooltips(true)
                .seriesLinesVisible(0, true)
                .seriesShapesVisible(0, false)
                .seriesLinesVisible(1, false)
                .seriesShapesVisible(1, true)
                .build();
        chart.show();
    }
}
