package pdk.util.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.event.AxisChangeEvent;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.Layer;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.xy.XYDataset;
import pdk.util.IBuilder;

import java.awt.*;

/**
 * Bar chart.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 24 Apr 2026, 2:16 PM
 */
public class BarChart implements IBuilder<BarChart>, Chart {

    public static BarChart chart() {
        return new BarChart();
    }

    private final JFreeChart chart_;
    private final XYPlot xyPlot_;
    private final NumberAxis domainAxis_;
    private final NumberAxis rangeAxis_;
    private final XYBarRenderer renderer_;

    private BarChart() {
        domainAxis_ = new NumberAxis();
        rangeAxis_ = new NumberAxis();
        renderer_ = new XYBarRenderer();
        domainAxis_.setAutoRangeIncludesZero(false);

        xyPlot_ = new XYPlot(null, domainAxis_, rangeAxis_, renderer_);
        chart_ = new JFreeChart(null, DEFAULT_TITLE_FONT, xyPlot_, false);
    }


    /**
     * Set the chart title.
     *
     * @param title new title
     * @return this
     */
    public BarChart title(String title) {
        chart_.setTitle(title);
        return this;
    }

    /**
     * configure chart to generate tool tips
     *
     * @param addTooltip true if generate tool tips
     * @return this
     */
    public BarChart addTooltips(boolean addTooltip) {
        if (addTooltip) {
            renderer_.setDefaultToolTipGenerator(new StandardXYToolTipGenerator());
        }
        return this;
    }

    /**
     * Adds a marker for the range axis in the specified layer.
     * <p>
     * Typically a marker will be drawn by the renderer as a line perpendicular
     * to the range axis, however this is entirely up to the renderer.
     *
     * @param marker the marker ({@code null} not permitted).
     * @param layer  the layer (foreground or background).
     */
    public BarChart addRangeMarker(Marker marker, Layer layer) {
        xyPlot_.addRangeMarker(marker, layer);
        return this;
    }

    /**
     * Whether to create and display the legend.
     *
     * @param createLegend true if add legend
     * @return this
     */
    public BarChart addLegend(boolean createLegend) {
        if (createLegend) {
            LegendTitle legend = new LegendTitle(this.xyPlot_);
            legend.setMargin(new RectangleInsets(1.0, 1.0, 1.0, 1.0));
            legend.setBackgroundPaint(Color.WHITE);
            legend.setPosition(RectangleEdge.BOTTOM);
            chart_.addSubtitle(legend);
        }
        return this;
    }

    /**
     * Set X Axis title
     *
     * @param xAxisTitle x axis title
     * @return this
     */
    public BarChart xAxisName(String xAxisTitle) {
        domainAxis_.setLabel(xAxisTitle);
        return this;
    }

    /**
     * Set X Axis title
     *
     * @param yAxisTitle y axis title
     * @return this
     */
    public BarChart yAxisName(String yAxisTitle) {
        rangeAxis_.setLabel(yAxisTitle);
        return this;
    }

    /**
     * Sets the flag that indicates whether the axis range, if
     * automatically calculated, is forced to include zero.
     * <p>
     * If the flag is changed to {@code true}, the axis range is
     * recalculated.
     * <p>
     * Any change to the flag will trigger an {@link AxisChangeEvent}.
     *
     * @param xAxisIncludesZero the new value of the flag.
     */
    public BarChart xAxisAutoRangeIncludesZero(boolean xAxisIncludesZero) {
        domainAxis_.setAutoRangeIncludesZero(xAxisIncludesZero);
        return this;
    }

    /**
     * Sets the flag that indicates whether the axis range, if
     * automatically calculated, is forced to include zero.
     * <p>
     * If the flag is changed to {@code true}, the axis range is
     * recalculated.
     *
     * @param yAxisIncludesZero the new value of the flag.
     */
    public BarChart yAxisAutoRangeIncludesZero(boolean yAxisIncludesZero) {
        rangeAxis_.setAutoRangeIncludesZero(yAxisIncludesZero);
        return this;
    }

    /**
     * Set the dataset to plot
     *
     * @param dataset {@link XYDataset}
     * @return this
     */
    public BarChart dataset(XYDataset dataset) {
        xyPlot_.setDataset(dataset);
        return this;
    }

    /**
     * Set the chart orientation.
     *
     * @param orientation {@link PlotOrientation}
     * @return this
     */
    public BarChart orientation(PlotOrientation orientation) {
        xyPlot_.setOrientation(orientation);
        return this;
    }

    @Override
    public BarChart build() {
        DEFAULT_THEME.apply(chart_);
        return this;
    }

    @Override
    public JFreeChart getChart() {
        return chart_;
    }
}
