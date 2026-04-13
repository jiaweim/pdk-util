package pdk.util.chart;

import org.jfree.chart.JFreeChart;

/**
 * This interface is used to represent any chart.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 13 Apr 2026, 9:12 AM
 */
public interface Chart {

    /**
     * Returns the created JFreeChart.
     *
     * @return {@link JFreeChart} instance
     */
    JFreeChart getChart();

    /**
     * display the chart.
     */
    default void show() {
        ChartUtils.showChart(getChart());
    }
}
