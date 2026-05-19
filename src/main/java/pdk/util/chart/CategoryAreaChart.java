package pdk.util.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.AreaRendererEndType;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.Title;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import pdk.util.IBuilder;

import java.awt.*;

/**
 * Area Chart with category domain axis.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 19 May 2026, 4:46 PM
 */
public class CategoryAreaChart implements IBuilder<CategoryAreaChart>, Chart {

    public static CategoryAreaChart create() {
        return new CategoryAreaChart();
    }

    private final JFreeChart chart_;
    private final CategoryPlot plot_;
    private final CategoryAxis domainAxis_;
    private final NumberAxis rangeAxis_;
    private final AreaRenderer renderer_;

    private CategoryAreaChart() {
        domainAxis_ = new CategoryAxis();
        domainAxis_.setCategoryMargin(0.0);
        rangeAxis_ = new NumberAxis();
        renderer_ = new AreaRenderer();
        renderer_.setEndType(AreaRendererEndType.LEVEL);
        plot_ = new CategoryPlot(null, domainAxis_, rangeAxis_, renderer_);
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
    public CategoryAreaChart title(String title) {
        chart_.setTitle(title);
        return this;
    }

    /**
     * Whether to create and display the legend.
     *
     * @param createLegend true if add legend
     * @return this
     */
    public CategoryAreaChart addLegend(boolean createLegend) {
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
     * @param paint the paint.
     */
    public CategoryAreaChart backgroundPaint(@Nullable Paint paint) {
        chart_.setBackgroundPaint(paint);
        return this;
    }

    /**
     * Adds a chart subtitle.
     *
     * @param subtitle the subtitle.
     */
    public CategoryAreaChart addSubtitle(@NonNull Title subtitle) {
        chart_.addSubtitle(subtitle);
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
    public CategoryAreaChart dataset(CategoryDataset dataset) {
        plot_.setDataset(dataset);
        return this;
    }

    /**
     * Set the chart orientation.
     *
     * @param orientation {@link PlotOrientation}
     * @return this
     */
    public CategoryAreaChart orientation(PlotOrientation orientation) {
        plot_.setOrientation(orientation);
        return this;
    }

    /**
     * Sets the alpha-transparency for the plot.
     *
     * @param alpha the new alpha transparency.
     */
    public CategoryAreaChart foregroundAlpha(float alpha) {
        plot_.setForegroundAlpha(alpha);
        return this;
    }

    /**
     * Sets the axis offsets (gap between the data area and the axes).
     *
     * @param offset the offset ({@code null} not permitted).
     */
    public CategoryAreaChart axisOffset(RectangleInsets offset) {
        plot_.setAxisOffset(offset);
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
    public CategoryAreaChart xAxisName(String xAxisTitle) {
        domainAxis_.setLabel(xAxisTitle);
        return this;
    }

    /**
     * Set Y Axis title
     *
     * @param yAxisTitle y axis title
     * @return this
     */
    public CategoryAreaChart yAxisName(String yAxisTitle) {
        rangeAxis_.setLabel(yAxisTitle);
        return this;
    }

    /**
     * Sets the lower margin for the axis.
     *
     * @param margin the margin as a percentage of the axis length (for
     *               example, 0.05 is five percent).
     */
    public CategoryAreaChart domainAxisLowerMargin(double margin) {
        domainAxis_.setLowerMargin(margin);
        return this;
    }

    /**
     * Sets the upper margin for the axis.
     *
     * @param margin the margin as a percentage of the axis length (for
     *               example, 0.05 is five percent).
     */
    public CategoryAreaChart domainAxisUpperMargin(double margin) {
        domainAxis_.setUpperMargin(margin);
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
    public CategoryAreaChart addTooltips(boolean addTooltip) {
        if (addTooltip) {
            renderer_.setDefaultToolTipGenerator(new StandardCategoryToolTipGenerator());
        }
        return this;
    }
    //endregion


    @Override
    public CategoryAreaChart build() {
        return this;
    }

    @Override
    public JFreeChart getChart() {
        return chart_;
    }
}
