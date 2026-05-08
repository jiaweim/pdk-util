package pdk.util.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.chart.renderer.xy.XYShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import pdk.util.IBuilder;
import pdk.util.tuple.Tuple2;

import java.awt.*;

/**
 * Scatter chart.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 06 May 2026, 5:41 PM
 */
public class ScatterChart implements IBuilder<ScatterChart>, Chart {

    public static ScatterChart create() {
        return new ScatterChart();
    }

    private final JFreeChart chart_;
    private final XYPlot plot_;
    private final NumberAxis domainAxis_;
    private final NumberAxis rangeAxis_;
    private NumberAxis zAxis_;
    private final XYShapeRenderer renderer_;
    private LookupPaintScale lookupPaintScale_;
    private PaintScaleLegend paintScaleLegend_;

    private ScatterChart() {
        domainAxis_ = new NumberAxis();
        rangeAxis_ = new NumberAxis();

        renderer_ = new XYShapeRenderer();
        plot_ = new XYPlot(null, domainAxis_, rangeAxis_, renderer_);
        chart_ = new JFreeChart(null, DEFAULT_TITLE_FONT, plot_, false);
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
     * Whether to create and display the legend.
     *
     * @param createLegend true if add legend
     * @return this
     */
    public ScatterChart addLegend(boolean createLegend) {
        if (createLegend) {
            LegendTitle legend = new LegendTitle(this.plot_);
            legend.setMargin(new RectangleInsets(1.0, 1.0, 1.0, 1.0));
            legend.setBackgroundPaint(Color.WHITE);
            legend.setPosition(RectangleEdge.BOTTOM);
            chart_.addSubtitle(legend);
        }
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

    //region Plot Properties

    /**
     * Set the dataset to plot
     *
     * @param dataset {@link XYDataset}
     * @return this
     */
    public ScatterChart dataset(XYZDataset dataset) {
        plot_.setDataset(dataset);
        return this;
    }

    public ScatterChart dataset(XYDataset dataset) {
        plot_.setDataset(dataset);
        return this;
    }

    /**
     * Enables or disables panning of the plot along the domain axes.
     *
     * @param pannable the new flag value.
     */
    public ScatterChart domainPannable(boolean pannable) {
        plot_.setDomainPannable(pannable);
        return this;
    }

    /**
     * Enables or disables panning of the plot along the range axis/axes.
     *
     * @param pannable the new flag value.
     */
    public ScatterChart rangePannable(boolean pannable) {
        plot_.setRangePannable(pannable);
        return this;
    }

    /**
     * Sets the flag indicating whether the domain crosshair is visible.
     *
     * @param visible the new value of the flag.
     */
    public ScatterChart domainCrosshairVisible(boolean visible) {
        plot_.setDomainCrosshairVisible(visible);
        return this;
    }

    /**
     * Sets the flag indicating whether the range crosshair is visible.
     *
     * @param visible the new value of the flag.
     */
    public ScatterChart rangeCrosshairVisible(boolean visible) {
        plot_.setRangeCrosshairVisible(visible);
        return this;
    }

    /**
     * Sets the flag indicating whether the domain crosshair should
     * "lock-on" to actual data values.
     *
     * @param flag the flag.
     */
    public ScatterChart domainCrosshairLockedOnData(boolean flag) {
        plot_.setDomainCrosshairLockedOnData(flag);
        return this;
    }

    /**
     * Sets the flag indicating whether the range crosshair should
     * "lock-on" to actual data values.
     *
     * @param flag the flag.
     */
    public ScatterChart rangeCrosshairLockedOnData(boolean flag) {
        plot_.setRangeCrosshairLockedOnData(flag);
        return this;
    }

    /**
     * Sets the flag that controls whether the zero baseline is
     * displayed for the domain axis.
     *
     * @param visible the flag.
     */
    public ScatterChart domainZeroBaselineVisible(boolean visible) {
        plot_.setDomainZeroBaselineVisible(visible);
        return this;
    }

    /**
     * Sets the flag that controls whether the zero baseline is
     * displayed for the range axis.
     *
     * @param visible the flag.
     */
    public ScatterChart rangeZeroBaselineVisible(boolean visible) {
        plot_.setRangeZeroBaselineVisible(visible);
        return this;
    }

    /**
     * Whether grid-lines are drawn against the domain axis.
     *
     * @param showDomainGridlines true if show grid lines
     * @return this
     */
    public ScatterChart domainGridlinesVisible(boolean showDomainGridlines) {
        plot_.setDomainGridlinesVisible(showDomainGridlines);
        return this;
    }

    /**
     * Whether grid-lines are drawn against the range axis.
     *
     * @param showRangeGridlines true if show grid lines
     * @return this
     */
    public ScatterChart rangeGridlinesVisible(boolean showRangeGridlines) {
        plot_.setRangeGridlinesVisible(showRangeGridlines);
        return this;
    }

    //endregion

    //region Render Properties

    /**
     * Sets the paint used for a series outline.
     *
     * @param series the series index (zero-based).
     * @param paint  the paint.
     */
    public ScatterChart seriesOutlinePaint(int series, @Nullable Paint paint) {
        renderer_.setSeriesOutlinePaint(series, paint, false);
        useOutlinePaint(true);
        return this;
    }

    /**
     * Sets the flag that controls whether the outline paint is used to draw
     * shape outlines.
     *
     * @param flag the flag.
     */
    public ScatterChart useOutlinePaint(boolean flag) {
        renderer_.setUseOutlinePaint(flag);
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
    public ScatterChart drawOutlines(boolean flag) {
        renderer_.setDrawOutlines(flag);
        return this;
    }

    /**
     * Set the {@link LookupPaintScale}
     *
     * @param lowerBound   lookup lower bound
     * @param upperBound   lookup upper bound
     * @param defaultPaint {@link Paint} used when data exceeds the above range
     * @param lookupTable  Adds an entry to the lookup table.
     *                     Any values from n up to but not including the next value in the table take on the specified Paint.
     * @return this
     */
    @SafeVarargs
    public final ScatterChart paintScale(double lowerBound, double upperBound,
            Paint defaultPaint, Tuple2<Double, Paint>... lookupTable) {
        lookupPaintScale_ = new LookupPaintScale(lowerBound, upperBound, defaultPaint);
        for (Tuple2<Double, Paint> tuple2 : lookupTable) {
            lookupPaintScale_.add(tuple2._1, tuple2._2);
        }
        renderer_.setPaintScale(lookupPaintScale_);
        chart_.removeLegend();

        zAxis_ = new NumberAxis();
        paintScaleLegend_ = new PaintScaleLegend(lookupPaintScale_, zAxis_);
        chart_.addSubtitle(paintScaleLegend_);

        return this;
    }

    /**
     * Set the title of the paint scale
     *
     * @param title new title
     * @return this
     */
    public ScatterChart paintScaleTitle(String title) {
        if (zAxis_ == null) zAxis_ = new NumberAxis();
        zAxis_.setLabel(title);
        return this;
    }

    /**
     * Sets the source for obtaining standard tick units for the axis.
     * <p>
     * The axis will try to select the smallest tick unit from the source that does not cause
     * the tick labels to overlap.
     *
     * @param source the source for standard tick units.
     */
    public ScatterChart paintScaleTickUnits(@Nullable TickUnitSource source) {
        zAxis_.setStandardTickUnits(source);
        return this;
    }

    /**
     * Sets the position for the paintScale title.
     * <p>
     * This method should be called after adding paint scale by calling {@link #paintScale(double, double, Paint, Tuple2[])}
     *
     * @param rectangleEdge the position.
     */
    public ScatterChart paintScalePosition(@NonNull RectangleEdge rectangleEdge) {
        paintScaleLegend_.setPosition(rectangleEdge);
        return this;
    }

    /**
     * Sets the margin (use {@link RectangleInsets#ZERO_INSETS} for no
     * padding).
     *
     * @param margin the margin.
     */
    public ScatterChart paintScaleMargin(@NonNull RectangleInsets margin) {
        paintScaleLegend_.setMargin(margin);
        return this;
    }

    /**
     * Sets the axis location.
     *
     * @param axisLocation the location.
     */
    public ScatterChart paintScaleAxisLocation(@NonNull AxisLocation axisLocation) {
        paintScaleLegend_.setAxisLocation(axisLocation);
        return this;
    }

    /**
     * Sets the shape for a series.
     *
     * @param series the series index (zero based).
     * @param shape  the shape.
     */
    public ScatterChart seriesShape(int series, @Nullable Shape shape) {
        renderer_.setSeriesShape(series, shape, false);
        return this;
    }

    //endregion


    //region Axis Properties

    /**
     * Set X Axis title
     *
     * @param xAxisTitle x axis title
     * @return this
     */
    public ScatterChart xAxisName(String xAxisTitle) {
        domainAxis_.setLabel(xAxisTitle);
        return this;
    }

    /**
     * Set Y Axis title
     *
     * @param yAxisTitle y axis title
     * @return this
     */
    public ScatterChart yAxisName(String yAxisTitle) {
        rangeAxis_.setLabel(yAxisTitle);
        return this;
    }

    /**
     * Sets the flag that indicates whether the axis range, if
     * automatically calculated, is forced to include zero.
     * <p>
     * If the flag is changed to {@code true}, the axis range is
     * recalculated.
     *
     * @param xAxisIncludesZero the new value of the flag.
     */
    public ScatterChart xAxisAutoRangeIncludesZero(boolean xAxisIncludesZero) {
        if (domainAxis_ instanceof NumberAxis na) {
            na.setAutoRangeIncludesZero(xAxisIncludesZero);
        }
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
    public ScatterChart yAxisAutoRangeIncludesZero(boolean yAxisIncludesZero) {
        rangeAxis_.setAutoRangeIncludesZero(yAxisIncludesZero);
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
        return chart_;
    }
}
