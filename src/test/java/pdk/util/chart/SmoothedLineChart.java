package pdk.util.chart;

import org.jfree.data.xy.XYDataset;
import pdk.util.chart.util.Data;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 19 May 2026, 4:43 PM
 */
public class SmoothedLineChart {
    static void main() {
        XYDataset dataset = Data.xyDataset().addSeries("line",
                        new double[]{1, 2, 3, 4, 5, 6, 7},
                        new double[]{820, 932, 901, 934, 1290, 1330, 1320})
                .build();
        LineChart chart = LineChart.create(false, true)
                .dataset(dataset)
                .domainGridlinesVisible(false)
                .build();
        chart.show();
    }
}
