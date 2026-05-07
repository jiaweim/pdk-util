package pdk.util.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.labels.BoxAndWhiskerToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import pdk.util.IBuilder;

import java.awt.*;

/**
 * Box-Whisker chart.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 07 May 2026, 12:54 PM
 */
public class BoxWhiskerChart implements IBuilder<BoxWhiskerChart>, Chart {

    public static BoxWhiskerChart create() {
        return new BoxWhiskerChart();
    }

    private final JFreeChart chart_;
    private final CategoryPlot plot_;
    private final CategoryAxis domainAxis_;
    private final NumberAxis rangeAxis_;
    private final BoxAndWhiskerRenderer renderer_;

    private BoxWhiskerChart() {
        domainAxis_ = new CategoryAxis();
        rangeAxis_ = new NumberAxis();

        renderer_ = new BoxAndWhiskerRenderer();
        plot_ = new CategoryPlot(null, domainAxis_, rangeAxis_, renderer_);
        chart_ = new JFreeChart(null, DEFAULT_TITLE_FONT, plot_, false);
        DEFAULT_THEME.apply(chart_);
    }

    //region Chart properties

    /**
     * Set the chart title.
     *
     * @param title new title
     * @return this
     */
    public BoxWhiskerChart title(String title) {
        chart_.setTitle(title);
        return this;
    }

    /**
     * Sets the paint used to fill the chart background.
     *
     * @param paint the paint.
     */
    public BoxWhiskerChart backgroundPaint(@Nullable Paint paint) {
        chart_.setBackgroundPaint(paint);
        return this;
    }

    /**
     * Whether to create and display the legend.
     *
     * @param createLegend true if add legend
     * @return this
     */
    public BoxWhiskerChart addLegend(boolean createLegend) {
        if (createLegend) {
            LegendTitle legend = new LegendTitle(this.plot_);
            legend.setMargin(new RectangleInsets(1.0, 1.0, 1.0, 1.0));
            legend.setBackgroundPaint(Color.WHITE);
            legend.setPosition(RectangleEdge.BOTTOM);
            chart_.addSubtitle(legend);
        }
        return this;
    }

    //endregion

    //region Plot properties

    /**
     * Sets the dataset for the plot, replacing the existing dataset, if there
     * is one.
     *
     * @param dataset the dataset.
     */
    public BoxWhiskerChart dataset(@Nullable BoxAndWhiskerCategoryDataset dataset) {
        plot_.setDataset(dataset);
        return this;
    }

    /**
     * Sets the background color of the plot area.
     *
     * @param paint the paint.
     */
    public BoxWhiskerChart plotBackgroundPaint(@Nullable Paint paint) {
        plot_.setBackgroundPaint(paint);
        return this;
    }

    /**
     * Whether grid-lines are drawn against the domain axis.
     *
     * @param showDomainGridlines true if show grid lines
     * @return this
     */
    public BoxWhiskerChart domainGridlinesVisible(boolean showDomainGridlines) {
        plot_.setDomainGridlinesVisible(showDomainGridlines);
        return this;
    }

    /**
     * Whether grid-lines are drawn against the range axis.
     *
     * @param showRangeGridlines true if show grid lines
     * @return this
     */
    public BoxWhiskerChart rangeGridlinesVisible(boolean showRangeGridlines) {
        plot_.setRangeGridlinesVisible(showRangeGridlines);
        return this;
    }

    /**
     * Sets the paint used to draw the grid-lines (if any) against the domain
     * axis.
     *
     * @param paint the paint.
     */
    public BoxWhiskerChart domainGridlinePaint(@NonNull Paint paint) {
        plot_.setDomainGridlinePaint(paint);
        return this;
    }

    /**
     * Sets the paint used to draw the grid lines against the range axis.
     *
     * @param paint the paint.
     */
    public BoxWhiskerChart rangeGridlinePaint(@NonNull Paint paint) {
        plot_.setRangeGridlinePaint(paint);
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
    public BoxWhiskerChart xAxisName(String xAxisTitle) {
        domainAxis_.setLabel(xAxisTitle);
        return this;
    }

    /**
     * Set Y Axis title
     *
     * @param yAxisTitle y axis title
     * @return this
     */
    public BoxWhiskerChart yAxisName(String yAxisTitle) {
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
     * @param yAxisIncludesZero the new value of the flag.
     */
    public BoxWhiskerChart yAxisAutoRangeIncludesZero(boolean yAxisIncludesZero) {
        rangeAxis_.setAutoRangeIncludesZero(yAxisIncludesZero);
        return this;
    }

    /**
     * Sets the source for obtaining standard tick units for the axis.  The axis will
     * try to select the smallest tick unit from the source that does not cause
     * the tick labels to overlap.
     *
     * @param source the source for standard tick units.
     */
    public BoxWhiskerChart rangeAxisStandardTickUnits(@Nullable TickUnitSource source) {
        rangeAxis_.setStandardTickUnits(source);
        return this;
    }

    //endregion

    //region Renderer Properties

    /**
     * configure chart to generate tool tips
     *
     * @param addTooltip true if generate tool tips
     * @return this
     */
    public BoxWhiskerChart addTooltips(boolean addTooltip) {
        if (addTooltip) {
            renderer_.setDefaultToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
        }
        return this;
    }

    /**
     * Sets the flag that controls whether the minimum outlier is drawn
     * for each item.
     *
     * @param visible the new flag value. default true.
     */
    public BoxWhiskerChart minOutlierVisible(boolean visible) {
        renderer_.setMinOutlierVisible(visible);
        return this;
    }

    /**
     * Sets the flag that controls whether the maximum outlier is drawn
     * for each item.
     *
     * @param visible the new flag value. default true.
     */
    public BoxWhiskerChart maxOutlierVisible(boolean visible) {
        renderer_.setMaxOutlierVisible(visible);
        return this;
    }
    //endregion

    @Override
    public BoxWhiskerChart build() {
        return this;
    }

    @Override
    public JFreeChart getChart() {
        return chart_;
    }
}
