package pdk.util.chart;

import org.jfree.data.xy.XYDataset;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 24 Apr 2026, 1:52 PM
 */
public class XYSeriesDemo1 {
    static void main() {
        XYDataset dataset = Data.createXYDataset("Random data",
                new double[]{1.0, 5.0, 4.0, 12.5, 17.3, 21.2, 21.9, 25.6, 30.0},
                new double[]{500.2, 694.1, 100.0, 734.4, 453.2, 500.2, Double.NaN, 734.4, 453.2}
        );
        LineChart chart = LineChart.chart()
                .title("XY Series Demo")
                .xAxisName("X")
                .yAxisName("Y")
                .addLegend(true)
                .addTooltips(true)
                .dataset(dataset)
                .build();
        chart.show();
    }
}
