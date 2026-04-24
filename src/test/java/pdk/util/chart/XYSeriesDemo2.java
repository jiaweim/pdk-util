package pdk.util.chart;

import org.jfree.data.xy.XYDataset;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 24 Apr 2026, 1:56 PM
 */
public class XYSeriesDemo2 {
    static void main() {
        XYDataset dataset = Data.createXYDataset("Flat Data",
                new double[]{1.0, 5.0, 4.0, 12.5, 17.3, 21.2, 21.9, 25.6, 30.0},
                new double[]{100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0, 100.0}
        );

        LineChart chart = LineChart.chart()
                .dataset(dataset)
                .title("XY Series Demo 2")
                .xAxisName("X")
                .yAxisName("Y")
                .addLegend(true)
                .addTooltips(true)
                .yAxisAutoRangeIncludesZero(false)
                .yAxisAutoRangeMinimumSize(1.0)
                .build();
        chart.show();
    }
}
