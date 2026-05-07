package pdk.util.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.chart.renderer.xy.XYShapeRenderer;
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
    private final XYPlot xyPlot_;
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

    //region Plot Properties

    /**
     * Set the dataset to plot
     *
     * @param dataset {@link XYDataset}
     * @return this
     */
    public ScatterChart dataset(XYZDataset dataset) {
        xyPlot_.setDataset(dataset);
        return this;
    }

    /**
     * Enables or disables panning of the plot along the domain axes.
     *
     * @param pannable the new flag value.
     */
    public ScatterChart domainPannable(boolean pannable) {
        xyPlot_.setDomainPannable(pannable);
        return this;
    }

    /**
     * Enables or disables panning of the plot along the range axis/axes.
     *
     * @param pannable the new flag value.
     */
    public ScatterChart rangePannable(boolean pannable) {
        xyPlot_.setRangePannable(pannable);
        return this;
    }

    /**
     * Sets the flag indicating whether the domain crosshair is visible.
     *
     * @param visible the new value of the flag.
     */
    public ScatterChart domainCrosshairVisible(boolean visible) {
        xyPlot_.setDomainCrosshairVisible(visible);
        return this;
    }

    /**
     * Sets the flag indicating whether the range crosshair is visible.
     *
     * @param visible the new value of the flag.
     */
    public ScatterChart rangeCrosshairVisible(boolean visible) {
        xyPlot_.setRangeCrosshairVisible(visible);
        return this;
    }

    /**
     * Sets the flag indicating whether the domain crosshair should
     * "lock-on" to actual data values.
     *
     * @param flag the flag.
     */
    public ScatterChart domainCrosshairLockedOnData(boolean flag) {
        xyPlot_.setDomainCrosshairLockedOnData(flag);
        return this;
    }

    /**
     * Sets the flag indicating whether the range crosshair should
     * "lock-on" to actual data values.
     *
     * @param flag the flag.
     */
    public ScatterChart rangeCrosshairLockedOnData(boolean flag) {
        xyPlot_.setRangeCrosshairLockedOnData(flag);
        return this;
    }

    //endregion

    //region Render Properties

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
