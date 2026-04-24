package pdk.util.chart;

import org.apache.commons.statistics.distribution.ExponentialDistribution;
import org.jfree.data.xy.XYDataset;
import pdk.util.math.DistributionUtils;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 24 Apr 2026, 3:35 PM
 */
public class ExponentialDistributionDemo {
    static void main() {
        ExponentialDistribution d1 = ExponentialDistribution.of(0.5);
        ExponentialDistribution d2 = ExponentialDistribution.of(1.0);

        XYDataset dataset = Data.xyDataset()
                .addSeries("λ=0.5", DistributionUtils.sample(d1, 0.0, 4.0, 500))
                .addSeries("λ=1.0", DistributionUtils.sample(d2, 0.0, 4.0, 500))
                .build();
        LineChart chart = LineChart.lineChart()
                .dataset(dataset)
                .xAxisName("X")
                .yAxisName("Probability density")
                .build();
        chart.show();
    }
}
