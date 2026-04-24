package pdk.util.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.event.AxisChangeEvent;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.Layer;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.xy.XYDataset;
import pdk.util.IBuilder;

import java.awt.*;

/**
 * Line Chart and scatter chart.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 13 Apr 2026, 9:06 AM
 */
public class LineChart implements IBuilder<LineChart>, Chart {

    public static LineChart chart() {
        return new LineChart();
    }

    /**
     * Create line chart
     *
     * @return {@link LineChart}
     */
    public static LineChart lineChart() {
        return new LineChart().showLine(true).showShape(false);
    }

    /**
     * Create scatter chart
     *
     * @return {@link LineChart}
     */
    public static LineChart scatterChart() {
        return new LineChart().showLine(false).showShape(true);
    }

    private final JFreeChart chart_;
    private final XYPlot xyPlot_;
    private final NumberAxis domainAxis_;
    private final NumberAxis rangeAxis_;
    private final XYLineAndShapeRenderer renderer_;

    private LineChart() {
        domainAxis_ = new NumberAxis();
        rangeAxis_ = new NumberAxis();
        renderer_ = new XYLineAndShapeRenderer(true, false);

        xyPlot_ = new XYPlot(null, domainAxis_, rangeAxis_, renderer_);
        chart_ = new JFreeChart(null, DEFAULT_TITLE_FONT, xyPlot_, false);
    }

    /**
     * Set true to create line chart
     *
     * @param showLine whether show lines between data points
     * @return this
     */
    public LineChart showLine(boolean showLine) {
        renderer_.setDefaultLinesVisible(showLine);
        return this;
    }

    /**
     * Set true to create scatter chart
     *
     * @param showShape whether show shapes of data points
     * @return this
     */
    public LineChart showShape(boolean showShape) {
        renderer_.setDefaultShapesVisible(showShape);
        return this;
    }

    /**
     * Sets the shape used for a series.
     *
     * @param series the series index (zero-based).
     * @param shape  the shape ({@code null} permitted).
     */
    public LineChart seriesShape(int series, Shape shape) {
        renderer_.setSeriesShape(series, shape);
        return this;
    }

    /**
     * Sets the shape used for a series.
     *
     * @param dataset dataset index
     * @param series  the series index (zero-based).
     * @param shape   the shape ({@code null} permitted).
     */
    public LineChart seriesShape(int dataset, int series, Shape shape) {
        XYItemRenderer renderer = xyPlot_.getRenderer(dataset);
        if (renderer == null) {
            throw new IllegalArgumentException("Dataset of index " + dataset + " not exist!");
        }
        renderer.setSeriesShape(series, shape);
        return this;
    }

    /**
     * Sets the paint used for a series.
     *
     * @param series the series index (zero-based).
     * @param paint  the paint ({@code null} permitted).
     */
    public LineChart seriesPaint(int series, Paint paint) {
        renderer_.setSeriesPaint(series, paint);
        return this;
    }

    /**
     * Sets the paint used for a series.
     *
     * @param dataset dataset index
     * @param series  the series index (zero-based).
     * @param paint   the paint ({@code null} permitted).
     */
    public LineChart seriesPaint(int dataset, int series, Paint paint) {
        XYItemRenderer renderer = xyPlot_.getRenderer(dataset);
        if (renderer == null) {
            throw new IllegalArgumentException("Dataset of index " + dataset + " not exist!");
        }
        renderer.setSeriesPaint(series, paint);
        return this;
    }

    /**
     * Sets the paint used for a series fill.
     *
     * @param series the series index (zero-based).
     * @param paint  the paint ({@code null} permitted).
     */
    public LineChart seriesFillPaint(int series, Paint paint) {
        renderer_.setSeriesFillPaint(series, paint);
        return this;
    }

    /**
     * Sets the paint used for a series fill.
     *
     * @param dataset dataset index
     * @param series  the series index (zero-based).
     * @param paint   the paint ({@code null} permitted).
     */
    public LineChart seriesFillPaint(int dataset, int series, Paint paint) {
        XYItemRenderer renderer = xyPlot_.getRenderer(dataset);
        if (renderer == null) {
            throw new IllegalArgumentException("Dataset of index " + dataset + " not exist!");
        }
        renderer.setSeriesFillPaint(series, paint);
        return this;
    }

    /**
     * Sets the paint used for a series outline.
     *
     * @param series the series index (zero-based).
     * @param paint  the paint ({@code null} permitted).
     */
    public LineChart seriesOutlinePaint(int series, Paint paint) {
        renderer_.setSeriesOutlinePaint(series, paint, false);
        return this;
    }

    /**
     * Sets the paint used for a series outline.
     *
     * @param dataset dataset index
     * @param series  the series index (zero-based).
     * @param paint   the paint ({@code null} permitted).
     */
    public LineChart seriesOutlinePaint(int dataset, int series, Paint paint) {
        XYItemRenderer renderer = xyPlot_.getRenderer(dataset);
        if (renderer == null) {
            throw new IllegalArgumentException("Dataset of index " + dataset + " not exist!");
        }
        renderer.setSeriesOutlinePaint(series, paint);
        return this;
    }

    /**
     * Sets the flag that controls whether outlines are drawn for shapes.
     * <p>
     * In some cases, shapes look better if they do NOT have an outline, but
     * this flag allows you to set your own preference.
     *
     * @param flag the flag.
     */
    public LineChart drawOutlines(boolean flag) {
        renderer_.setDrawOutlines(flag);
        return this;
    }

    /**
     * Sets the flag that controls whether outlines are drawn for shapes.
     * <p>
     * In some cases, shapes look better if they do NOT have an outline, but
     * this flag allows you to set your own preference.
     *
     * @param dataset dataset index
     * @param flag    the flag.
     */
    public LineChart drawOutlines(int dataset, boolean flag) {
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) xyPlot_.getRenderer(dataset);
        if (renderer == null) {
            throw new IllegalArgumentException("Dataset of index " + dataset + " not exist!");
        }
        renderer.setDrawOutlines(flag);
        return this;
    }

    /**
     * Set the line width of a given series
     *
     * @param series series index
     * @param width  line width
     * @return this
     */
    public LineChart seriesLinesWidth(int series, float width) {
        renderer_.setSeriesStroke(series, new BasicStroke(width));
        return this;
    }

    /**
     * Sets the 'lines visible' flag for a series.
     *
     * @param series  the series index (zero-based).
     * @param visible the flag.
     */
    public LineChart seriesLinesVisible(int series, boolean visible) {
        renderer_.setSeriesLinesVisible(series, visible);
        return this;
    }

    /**
     * Sets the 'shapes visible' flag for a series.
     *
     * @param series  the series index (zero-based).
     * @param visible the flag.
     */
    public LineChart seriesShapesVisible(int series, boolean visible) {
        renderer_.setSeriesShapesVisible(series, visible);
        return this;
    }

    /**
     * Sets the flag that controls whether the outline paint is used to draw
     * shape outlines.
     *
     * @param flag the flag.
     */
    public LineChart useOutlinePaint(boolean flag) {
        renderer_.setUseOutlinePaint(flag);
        return this;
    }

    /**
     * Sets the flag that controls whether the fill paint is used to fill
     * shapes.
     *
     * @param flag the flag.
     */
    public LineChart useFillPaint(boolean flag) {
        renderer_.setUseFillPaint(flag);
        return this;
    }

    /**
     * Sets the flag that controls whether the fill paint is used to fill
     * shapes.
     *
     * @param dataset dataset index.
     * @param flag    the flag.
     */
    public LineChart useFillPaint(int dataset, boolean flag) {
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) xyPlot_.getRenderer(dataset);
        if (renderer == null) {
            throw new IllegalArgumentException("Dataset of index " + dataset + " not exist!");
        }
        renderer.setUseFillPaint(flag);
        return this;
    }

    /**
     * Sets the flag that controls whether the outline paint is used to draw
     * shape outlines.
     *
     * @param dataset dataset index
     * @param flag    the flag
     */
    public LineChart useOutlinePaint(int dataset, boolean flag) {
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) xyPlot_.getRenderer(dataset);
        if (renderer == null) {
            throw new IllegalArgumentException("Dataset of index " + dataset + " not exist!");
        }
        renderer.setUseOutlinePaint(flag);
        return this;
    }

    /**
     * Set the dataset to plot
     *
     * @param dataset {@link XYDataset}
     * @return this
     */
    public LineChart dataset(XYDataset dataset) {
        xyPlot_.setDataset(dataset);
        return this;
    }

    /**
     * Add a new dataset to the plot
     *
     * @param dataset {@link XYDataset} instance
     * @return this
     */
    public LineChart addDataset(XYDataset dataset) {
        int datasetCount = xyPlot_.getDatasetCount();
        return addDataset(datasetCount, dataset);
    }

    /**
     * Add a new dataset to the plot
     *
     * @param index   index of the dataset
     * @param dataset {@link XYDataset} instance
     * @return this
     */
    public LineChart addDataset(int index, XYDataset dataset) {
        XYDataset preDataset = xyPlot_.getDataset(index);
        if (preDataset != null) {
            throw new IllegalStateException("Dataset with index " + index + " already exists!");
        }
        xyPlot_.setDataset(index, dataset);
        xyPlot_.setRenderer(index, new XYLineAndShapeRenderer());
        return this;
    }

    /**
     * Set the chart title.
     *
     * @param title new title
     * @return this
     */
    public LineChart title(String title) {
        chart_.setTitle(title);
        return this;
    }

    /**
     * Set the chart orientation.
     *
     * @param orientation {@link PlotOrientation}
     * @return this
     */
    public LineChart orientation(PlotOrientation orientation) {
        xyPlot_.setOrientation(orientation);
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
    public LineChart addRangeMarker(Marker marker, Layer layer) {
        xyPlot_.addRangeMarker(marker, layer);
        return this;
    }

    /**
     * Set X Axis title
     *
     * @param xAxisTitle x axis title
     * @return this
     */
    public LineChart xAxisName(String xAxisTitle) {
        domainAxis_.setLabel(xAxisTitle);
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
    public LineChart xAxisAutoRangeIncludesZero(boolean xAxisIncludesZero) {
        domainAxis_.setAutoRangeIncludesZero(xAxisIncludesZero);
        return this;
    }

    /**
     * Sets the range for the axis and sends a change event to all registered
     * listeners.  As a side-effect, the auto-range flag is set to
     * {@code false}.
     *
     * @param lower the lower axis limit.
     * @param upper the upper axis limit.
     */
    public LineChart xAxisRange(double lower, double upper) {
        domainAxis_.setRange(lower, upper);
        return this;
    }

    /**
     * Sets the range for the axis and sends a change event to all registered
     * listeners. As a side-effect, the auto-range flag is set to
     * {@code false}.
     *
     * @param lower the lower axis limit.
     * @param upper the upper axis limit.
     */
    public LineChart yAxisRange(double lower, double upper) {
        rangeAxis_.setRange(lower, upper);
        return this;
    }

    /**
     * Enables or disables panning of the plot along the domain axes.
     *
     * @param pannable the new flag value.
     */
    public LineChart domainPannable(boolean pannable) {
        xyPlot_.setDomainPannable(pannable);
        return this;
    }

    /**
     * Set X Axis title
     *
     * @param yAxisTitle y axis title
     * @return this
     */
    public LineChart yAxisName(String yAxisTitle) {
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
     * @param yAxisIncludesZero the new value of the flag.
     */
    public LineChart yAxisAutoRangeIncludesZero(boolean yAxisIncludesZero) {
        rangeAxis_.setAutoRangeIncludesZero(yAxisIncludesZero);
        return this;
    }

    /**
     * Sets the auto range minimum size.
     * <p>
     * The minimum display range of the axis values.
     *
     * @param size the size.
     */
    public LineChart yAxisAutoRangeMinimumSize(double size) {
        rangeAxis_.setAutoRangeMinimumSize(size, false);
        return this;
    }

    /**
     * Enables or disables panning of the plot along the range axis/axes.
     *
     * @param pannable the new flag value.
     */
    public LineChart rangePannable(boolean pannable) {
        xyPlot_.setRangePannable(pannable);
        return this;
    }

    /**
     * Sets the flag that controls whether the zero baseline is
     * displayed for the domain axis.
     *
     * @param visible the flag.
     */
    public LineChart domainZeroBaselineVisible(boolean visible) {
        xyPlot_.setDomainZeroBaselineVisible(visible);
        return this;
    }

    /**
     * Sets the flag that controls whether the zero baseline is
     * displayed for the range axis.
     *
     * @param visible the flag.
     */
    public LineChart rangeZeroBaselineVisible(boolean visible) {
        xyPlot_.setRangeZeroBaselineVisible(visible);
        return this;
    }

    /**
     * Whether to create and display the legend.
     *
     * @param createLegend true if add legend
     * @return this
     */
    public LineChart addLegend(boolean createLegend) {
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
    public LineChart addTooltips(boolean addTooltip) {
        if (addTooltip) {
            renderer_.setDefaultToolTipGenerator(new StandardXYToolTipGenerator());
        }
        return this;
    }

    /**
     * Whether grid-lines are drawn against the domain axis.
     *
     * @param showDomainGridlines true if show grid lines
     * @return this
     */
    public LineChart showDomainGridlines(boolean showDomainGridlines) {
        xyPlot_.setDomainGridlinesVisible(showDomainGridlines);
        return this;
    }

    /**
     * Whether grid-lines are drawn against the range axis.
     *
     * @param showRangeGridlines true if show grid lines
     * @return this
     */
    public LineChart showRangeGridlines(boolean showRangeGridlines) {
        xyPlot_.setRangeGridlinesVisible(showRangeGridlines);
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
    public LineChart addPointerAnnotation(String label, double x, double y,
            double angle, double labelOffset,
            TextAnchor textAnchor, Color backgroundColor) {
        XYPointerAnnotation annotation = new XYPointerAnnotation(label, x, y, angle);
        annotation.setLabelOffset(labelOffset);
        annotation.setTextAnchor(textAnchor);
        annotation.setBackgroundPaint(backgroundColor);
        xyPlot_.addAnnotation(annotation);
        return this;
    }

    @Override
    public LineChart build() {
        DEFAULT_THEME.apply(chart_);
        return this;
    }

    @Override
    public JFreeChart getChart() {
        return chart_;
    }
}
