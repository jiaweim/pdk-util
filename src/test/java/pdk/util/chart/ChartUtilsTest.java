package pdk.util.chart;

import org.apache.commons.statistics.distribution.NormalDistribution;
import org.jfree.chart.JFreeChart;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 11 Dec 2025, 1:23 PM
 */
class ChartUtilsTest {

    static void createChart() {
        double mean = 20.6;
        JFreeChart chart = ChartUtils.pdfChart(NormalDistribution.of(mean, 1.62), mean - 10, mean + 10, mean - 10, 18, 500);
        ChartUtils.showChart(chart);
    }

    static void main() {
        createChart();
    }
}