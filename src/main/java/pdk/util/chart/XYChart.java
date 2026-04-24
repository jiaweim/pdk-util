package pdk.util.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.event.AxisChangeEvent;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.xy.XYDataset;
import pdk.util.IBuilder;

import java.awt.*;

/**
 * A generic chart type
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 24 Apr 2026, 3:40 PM
 */
public class XYChart implements IBuilder<XYChart>, Chart {

    public static XYChart chart() {
        return new XYChart();
    }

    private final NumberAxis domainAxis_;
    private final NumberAxis rangeAxis_;
    private final XYPlot xyPlot_;
    private final JFreeChart chart_;

    private XYChart() {
        domainAxis_ = new NumberAxis();
        rangeAxis_ = new NumberAxis();
        xyPlot_ = new XYPlot(null, domainAxis_, rangeAxis_, null);
        chart_ = new JFreeChart(null, DEFAULT_TITLE_FONT, xyPlot_, false);
    }

    /**
     * Set the chart title.
     *
     * @param title new title
     * @return this
     */
    public XYChart title(String title) {
        chart_.setTitle(title);
        return this;
    }

    /**
     * Whether to create and display the legend.
     *
     * @param createLegend true if add legend
     * @return this
     */
    public XYChart addLegend(boolean createLegend) {
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
     * configure chart to generate tool tips
     *
     * @param addTooltip true if generate tool tips
     * @return this
     */
    public XYChart addTooltips(boolean addTooltip) {
        if (addTooltip) {
            xyPlot_.getRenderer().setDefaultToolTipGenerator(new StandardXYToolTipGenerator());
        }
        return this;
    }

    /**
     * Set the dataset to plot
     *
     * @param dataset   {@link XYDataset}
     * @param chartType {@link XYChartType}
     * @return this
     */
    public XYChart dataset(XYDataset dataset, XYChartType chartType) {
        return addDataset(0, dataset, chartType);
    }

    /**
     * Add a new dataset to the plot
     *
     * @param index     index of the dataset to insert
     * @param dataset   {@link XYDataset}
     * @param chartType {@link XYChartType}
     * @return this
     */
    public XYChart addDataset(int index, XYDataset dataset, XYChartType chartType) {
        XYDataset preDataset = xyPlot_.getDataset(index);
        if (preDataset != null) {
            throw new IllegalStateException("Dataset with index " + index + " already exists!");
        }
        xyPlot_.setDataset(index, dataset);
        XYItemRenderer renderer = null;
        switch (chartType) {
            case SCATTER -> renderer = new XYLineAndShapeRenderer(false, true);
            case LINE -> renderer = new XYLineAndShapeRenderer(true, false);
            case AREA -> renderer = new XYAreaRenderer(XYAreaRenderer.AREA);
            case HISTOGRAM -> renderer = new XYBarRenderer();
        }
        if (renderer != null)
            xyPlot_.setRenderer(index, renderer);
        return this;
    }

    /**
     * Sets the flag that controls whether the zero baseline is
     * displayed for the domain axis.
     *
     * @param visible the flag.
     */
    public XYChart domainZeroBaselineVisible(boolean visible) {
        xyPlot_.setDomainZeroBaselineVisible(visible);
        return this;
    }

    /**
     * Sets the flag that controls whether the zero baseline is
     * displayed for the range axis.
     *
     * @param visible the flag.
     */
    public XYChart rangeZeroBaselineVisible(boolean visible) {
        xyPlot_.setRangeZeroBaselineVisible(visible);
        return this;
    }

    /**
     * Set the chart orientation.
     *
     * @param orientation {@link PlotOrientation}
     * @return this
     */
    public XYChart orientation(PlotOrientation orientation) {
        xyPlot_.setOrientation(orientation);
        return this;
    }

    /**
     * Sets the flag that controls whether the domain grid-lines are
     * visible.
     *
     * @param visible the new value of the flag.
     */
    public XYChart domainGridlinesVisible(boolean visible) {
        xyPlot_.setDomainGridlinesVisible(visible);
        return this;
    }

    /**
     * Sets the flag that controls whether the range axis grid lines
     * are visible.
     *
     * @param visible the new value of the flag.
     */
    public XYChart rangeGridlinesVisible(boolean visible) {
        xyPlot_.setRangeGridlinesVisible(visible);
        return this;
    }

    /**
     * Set X Axis title
     *
     * @param xAxisTitle x axis title
     * @return this
     */
    public XYChart xAxisName(String xAxisTitle) {
        domainAxis_.setLabel(xAxisTitle);
        return this;
    }

    /**
     * Set X Axis title
     *
     * @param yAxisTitle y axis title
     * @return this
     */
    public XYChart yAxisName(String yAxisTitle) {
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
    public XYChart xAxisAutoRangeIncludesZero(boolean xAxisIncludesZero) {
        domainAxis_.setAutoRangeIncludesZero(xAxisIncludesZero);
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
     * @param yAxisIncludesZero the new value of the flag.
     */
    public XYChart yAxisAutoRangeIncludesZero(boolean yAxisIncludesZero) {
        rangeAxis_.setAutoRangeIncludesZero(yAxisIncludesZero);
        return this;
    }

    /**
     * Add a new {@link XYTextAnnotation}
     *
     * @param label      annotation text
     * @param x          the x-coordinate (measured against the chart's domain axis).
     * @param y          the y-coordinate (measured against the chart's range axis).
     * @param font       the font (null not permitted).
     * @param textAnchor the anchor point (null not permitted).
     * @return this
     */
    public XYChart addTextAnnotation(String label, double x, double y, Font font, TextAnchor textAnchor) {
        XYTextAnnotation annotation = new XYTextAnnotation(label, x, y);
        annotation.setFont(font);
        annotation.setTextAnchor(textAnchor);
        xyPlot_.addAnnotation(annotation, false);
        return this;
    }

    /**
     * Add a XYPointer Annotation
     *
     * @param label           annotation text
     * @param x               the x-coordinate (measured against the chart's domain axis).
     * @param y               the y-coordinate (measured against the chart's range axis).
     * @param angle           the angle of the arrow's line (in radians).
     * @param labelOffset     the label offset (distance between arrows and annotation text)
     * @param textAnchor      the text anchor (the point on the text bounding rectangle that is aligned to the (x, y) coordinate of the annotation)
     * @param backgroundColor the background paint for the annotation
     * @return this
     */
    public XYChart addPointerAnnotation(String label, double x, double y, double angle, double labelOffset,
            TextAnchor textAnchor, Color backgroundColor) {
        XYPointerAnnotation annotation = new XYPointerAnnotation(label, x, y, angle);
        annotation.setLabelOffset(labelOffset);
        annotation.setTextAnchor(textAnchor);
        annotation.setBackgroundPaint(backgroundColor);
        xyPlot_.addAnnotation(annotation, false);
        return this;
    }

    /**
     * Return the {@link XYItemRenderer} for a given dataset
     *
     * @param index index
     * @return {@link XYItemRenderer}
     */
    public XYItemRenderer getRenderer(int index) {
        XYItemRenderer renderer = xyPlot_.getRenderer(index);
        return renderer;
    }

    @Override
    public XYChart build() {
        DEFAULT_THEME.apply(chart_);
        return this;
    }

    @Override
    public JFreeChart getChart() {
        return chart_;
    }
}
