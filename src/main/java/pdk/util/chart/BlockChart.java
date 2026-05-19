package pdk.util.chart;

import org.jfree.chart.JFreeChart;
import pdk.util.IBuilder;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 06 May 2026, 5:35 PM
 */
public class BlockChart implements IBuilder<BlockChart>, Chart {


    @Override
    public BlockChart build() {
        return this;
    }


    @Override
    public JFreeChart getChart() {
        return null;
    }
}
