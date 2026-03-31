package pdk.util.math.demo;

import org.apache.commons.statistics.distribution.ExponentialDistribution;
import org.jfree.chart.JFreeChart;
import pdk.util.chart.ChartUtils;

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
        JFreeChart chart = ChartUtils.pdfChart(d1, "λ=0.5", d2, "λ=1.0", 0.0, 4.0, 100);
        ChartUtils.showChart(chart);
    }
}
