package pdk.util.bayesian;

import org.jfree.data.xy.XYDataset;
import pdk.util.chart.Data;
import pdk.util.chart.LineChart;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 24 Apr 2026, 7:31 PM
 */
public class XICChartDemo {
    static void main() {
        XYDataset dataset = Data.xyDataset()
                .addSeries("TIC",
                        new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0},
                        new double[]{100.0, 250.0, 500.0, 800.0, 600.0, 300.0, 150.0, 50.0})
                .build();

        LineChart chart = LineChart.lineChart()
                .dataset(dataset)
                .title("色谱图")
                .xAxisName("Time (min)")
                .yAxisName("强度").build();
        chart.show();
    }
}
