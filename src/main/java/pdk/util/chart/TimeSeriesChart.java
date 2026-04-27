package pdk.util.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import pdk.util.IBuilder;

/**
 * XYChart with time domain axis
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 24 Apr 2026, 7:13 PM
 */
public class TimeSeriesChart implements IBuilder<TimeSeriesChart>, Chart {

    private final JFreeChart chart_;
    private final DateAxis domainAxis_;
    private final NumberAxis rangeAxis_;
    private final XYPlot xyPlot_;
    private final XYLineAndShapeRenderer renderer_;

    private TimeSeriesChart() {
        domainAxis_ = new DateAxis();
        rangeAxis_ = new NumberAxis();
        renderer_ = new XYLineAndShapeRenderer(true, false);
        xyPlot_ = new XYPlot(null, domainAxis_, rangeAxis_, renderer_);

        chart_ = new JFreeChart(null, DEFAULT_TITLE_FONT, xyPlot_, false);
    }

    @Override
    public TimeSeriesChart build() {
        return this;
    }

    @Override
    public JFreeChart getChart() {
        return null;
    }
}
