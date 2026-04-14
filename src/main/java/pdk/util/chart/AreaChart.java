package pdk.util.chart;

import org.jfree.chart.JFreeChart;
import pdk.util.IBuilder;

/**
 * Area Chart.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 14 Apr 2026, 3:43 PM
 */
public class AreaChart implements IBuilder<AreaChart>, Chart {

    private JFreeChart chart_;

    @Override
    public AreaChart build() {

        return null;
    }

    @Override
    public JFreeChart getChart() {
        return chart_;
    }
}
