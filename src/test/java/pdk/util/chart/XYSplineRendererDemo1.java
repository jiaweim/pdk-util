package pdk.util.chart;

import org.jfree.data.xy.XYDataset;
import pdk.util.chart.util.Data;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 19 May 2026, 4:36 PM
 */
public class XYSplineRendererDemo1 {
    static void main() {
        XYDataset dataset = Data.xyDataset()
                .addSeries("Series 1",
                        new double[]{2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0},
                        new double[]{56.27, 41.32, 31.45, 30.05, 24.69, 19.78, 20.94, 16.73, 14.21, 12.44})
                .addSeries("Series 2",
                        new double[]{11.0, 10.0F, 9.0F, 8.0F, 7.0F, 6.0F, 5.0F, 4.0F, 3.0F, 2.0F},
                        new double[]{56.27, 41.32, 31.45, 30.05, 24.69, 19.78, 20.94, 16.73, 14.21, 12.44})
                .build();
        LineChart chart = LineChart.create(false, true)
                .dataset(dataset)
                .xAxisName("X")
                .yAxisName("Y")
                .addLegend(true)
                .build();
        chart.show();
    }
}
