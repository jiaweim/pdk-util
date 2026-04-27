package pdk.util.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.CategoryDataset;
import org.jspecify.annotations.Nullable;
import pdk.util.IBuilder;

import java.awt.*;

/**
 * Bar Chart with category domain axis.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 14 Apr 2026, 3:45 PM
 */
public class CategoryBarChart implements IBuilder<CategoryBarChart>, Chart {

    public static CategoryBarChart create() {
        return new CategoryBarChart();
    }

    private final JFreeChart chart_;
    private PlotOrientation orientation_ = PlotOrientation.VERTICAL;
    private final CategoryAxis domainAxis_;
    private final NumberAxis rangeAxis_;
    private final BarRenderer renderer_;
    private final CategoryPlot plot_;

    private CategoryBarChart() {
        domainAxis_ = new CategoryAxis();
        rangeAxis_ = new NumberAxis();
        renderer_ = new BarRenderer();
        plot_ = new CategoryPlot(null, domainAxis_, rangeAxis_, renderer_);
        chart_ = new JFreeChart(null, DEFAULT_TITLE_FONT, plot_, false);
        DEFAULT_THEME.apply(chart_);
    }

    /**
     * Set the dataset for this plot
     *
     * @param dataset {@link CategoryDataset} instance
     * @return this
     */
    public CategoryBarChart dataset(CategoryDataset dataset) {
        plot_.setDataset(dataset);
        return this;
    }

    /**
     * Set the chart title.
     *
     * @param title new title
     * @return this
     */
    public CategoryBarChart title(String title) {
        chart_.setTitle(title);
        return this;
    }

    /**
     * Set the chart orientation.
     *
     * @param orientation {@link PlotOrientation}
     * @return this
     */
    public CategoryBarChart orientation(PlotOrientation orientation) {
        plot_.setOrientation(this.orientation_);
        return this;
    }

    /**
     * Set X Axis title
     *
     * @param xAxisTitle x axis title
     * @return this
     */
    public CategoryBarChart xAxisName(String xAxisTitle) {
        domainAxis_.setLabel(xAxisTitle);
        return this;
    }

    /**
     * Set X Axis title
     *
     * @param yAxisTitle y axis title
     * @return this
     */
    public CategoryBarChart yAxisName(String yAxisTitle) {
        rangeAxis_.setLabel(yAxisTitle);
        return this;
    }

    /**
     * Whether to create and display the legend.
     *
     * @param createLegend true if add legend
     * @return this
     */
    public CategoryBarChart addLegend(boolean createLegend) {
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
     * Set the legend.
     *
     * @param legendTitle {@link LegendTitle}
     * @return this
     */
    public CategoryBarChart legend(LegendTitle legendTitle) {
        chart_.addSubtitle(0, legendTitle);
        return this;
    }

    /**
     * configure chart to generate tool tips
     *
     * @param addTooltip true if generate tool tips
     * @return this
     */
    public CategoryBarChart addTooltips(boolean addTooltip) {
        if (addTooltip) {
            renderer_.setDefaultToolTipGenerator(new StandardCategoryToolTipGenerator());
        }
        return this;
    }

    /**
     * Whether grid-lines are drawn against the domain axis.
     *
     * @param showDomainGridlines true if show grid lines
     * @return this
     */
    public CategoryBarChart domainGridlinesVisible(boolean showDomainGridlines) {
        plot_.setDomainGridlinesVisible(showDomainGridlines);
        return this;
    }

    /**
     * Whether grid-lines are drawn against the range axis.
     *
     * @param showRangeGridlines true if show grid lines
     * @return this
     */
    public CategoryBarChart rangeGridlinesVisible(boolean showRangeGridlines) {
        plot_.setRangeGridlinesVisible(showRangeGridlines);
        return this;
    }

    /**
     * Set the amount of space reserved between categories.
     *
     * @param categoryMargin a percentage of the axis length, default to be 0.2
     * @return this
     */
    public CategoryBarChart categoryMargin(double categoryMargin) {
        domainAxis_.setCategoryMargin(categoryMargin);
        return this;
    }

    /**
     * Set the margin between items (bars) within a category.
     *
     * @param itemMargin a percentage of the available space for all bars.
     * @return this
     */
    public CategoryBarChart itemMargin(double itemMargin) {
        renderer_.setItemMargin(itemMargin);
        return this;
    }

    /**
     * Sets the paint used for a series.
     *
     * @param series the series index (zero-based).
     * @param paint  the paint.
     */
    public CategoryBarChart seriesPaint(int series, @Nullable Paint paint) {
        renderer_.setSeriesPaint(series, paint, false);
        return this;
    }

    @Override
    public CategoryBarChart build() {
        if (orientation_ == PlotOrientation.HORIZONTAL) {
            ItemLabelPosition position1 = new ItemLabelPosition(
                    ItemLabelAnchor.OUTSIDE3, TextAnchor.CENTER_LEFT);
            renderer_.setDefaultPositiveItemLabelPosition(position1);
            ItemLabelPosition position2 = new ItemLabelPosition(
                    ItemLabelAnchor.OUTSIDE9, TextAnchor.CENTER_RIGHT);
            renderer_.setDefaultNegativeItemLabelPosition(position2);
        } else if (orientation_ == PlotOrientation.VERTICAL) {
            ItemLabelPosition position1 = new ItemLabelPosition(
                    ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER);
            renderer_.setDefaultPositiveItemLabelPosition(position1);
            ItemLabelPosition position2 = new ItemLabelPosition(
                    ItemLabelAnchor.OUTSIDE6, TextAnchor.TOP_CENTER);
            renderer_.setDefaultNegativeItemLabelPosition(position2);
        }

        return this;
    }

    @Override
    public JFreeChart getChart() {
        return chart_;
    }
}
