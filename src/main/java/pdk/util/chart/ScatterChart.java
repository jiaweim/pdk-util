package pdk.util.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYShapeRenderer;
import pdk.util.IBuilder;

import java.awt.*;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 06 May 2026, 5:41 PM
 */
public class ScatterChart implements IBuilder<ScatterChart>, Chart {

    private final JFreeChart chart_;
    private final XYPlot xyPlot_;
    private final NumberAxis domainAxis_;
    private final NumberAxis rangeAxis_;
    private final XYShapeRenderer renderer_;

    private ScatterChart() {
        domainAxis_ = new NumberAxis();
        rangeAxis_ = new NumberAxis();

        renderer_ = new XYShapeRenderer();
        xyPlot_ = new XYPlot(null, domainAxis_, rangeAxis_, renderer_);
        chart_ = new JFreeChart(null, DEFAULT_TITLE_FONT, xyPlot_, false);
        DEFAULT_THEME.apply(chart_);
    }

    //region Chart Properties

    /**
     * Set the chart title.
     *
     * @param title new title
     * @return this
     */
    public ScatterChart title(String title) {
        chart_.setTitle(title);
        return this;
    }

    /**
     * Sets the paint used to fill the chart background.
     *
     * @param paint the paint ({@code null} permitted).
     */
    public ScatterChart backgroundPaint(Paint paint) {
        chart_.setBackgroundPaint(paint);
        return this;
    }
    //endregion

    @Override
    public ScatterChart build() {
        return this;
    }

    /**
     * Returns the created JFreeChart.
     *
     * @return {@link JFreeChart} instance
     */
    @Override
    public JFreeChart getChart() {
        return null;
    }
}
