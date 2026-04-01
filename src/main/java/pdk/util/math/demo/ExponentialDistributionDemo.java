package pdk.util.math.demo;

import org.apache.commons.statistics.distribution.ExponentialDistribution;
import org.jfree.chart.JFreeChart;
import pdk.util.chart.ChartUtils;
import pdk.util.chart.XYChartBuilder;
import pdk.util.chart.XYChartType;
import pdk.util.math.DistributionUtils;

/**
 * Display two exponential distributions with different parameters.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 31 Mar 2026, 4:15 PM
 */
public class ExponentialDistributionDemo {
    static void main() {
        ExponentialDistribution d1 = ExponentialDistribution.of(0.5);
        ExponentialDistribution d2 = ExponentialDistribution.of(1.0);
        JFreeChart chart = XYChartBuilder.start()
                .addSeries("λ=0.5", DistributionUtils.sample(d1, 0.0, 4.0, 500), XYChartType.LINE)
                .addSeries("λ=1.0", DistributionUtils.sample(d2, 0.0, 4.0, 500), XYChartType.LINE)
                .xAxisName("X")
                .yAxisName("Probability density")
                .build();
        ChartUtils.showChart(chart);

//        JFreeChart chart = ChartUtils.pdfChart(d1, "λ=0.5", d2, "λ=1.0", 0.0, 4.0, 100);
//        ChartUtils.showChart(chart);
    }
}
