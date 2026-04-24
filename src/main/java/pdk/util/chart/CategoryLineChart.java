package pdk.util.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.CategoryDataset;
import org.jspecify.annotations.NonNull;
import pdk.util.IBuilder;

import java.awt.*;

/**
 * Line Chart with category domain axis.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 24 Apr 2026, 3:08 PM
 */
public class CategoryLineChart implements IBuilder<CategoryLineChart>, Chart {

    public static CategoryLineChart chart() {
        return new CategoryLineChart();
    }

    private final CategoryAxis domainAxis_;
    private final NumberAxis rangeAxis_;
    private final CategoryPlot plot_;
    private final LineAndShapeRenderer renderer_;
    private final JFreeChart chart_;

    private CategoryLineChart() {
        domainAxis_ = new CategoryAxis();
        rangeAxis_ = new NumberAxis();
        renderer_ = new LineAndShapeRenderer();

        plot_ = new CategoryPlot(null, domainAxis_, rangeAxis_, renderer_);
        chart_ = new JFreeChart(null, DEFAULT_TITLE_FONT, plot_, false);
    }


    /**
     * Set the chart title.
     *
     * @param title new title
     * @return this
     */
    public CategoryLineChart title(String title) {
        chart_.setTitle(title);
        return this;
    }

    /**
     * Set the dataset to plot
     *
     * @param dataset {@link CategoryDataset}
     * @return this
     */
    public CategoryLineChart dataset(CategoryDataset dataset) {
        plot_.setDataset(dataset);
        return this;
    }

    /**
     * configure chart to generate tool tips
     *
     * @param addTooltip true if generate tool tips
     * @return this
     */
    public CategoryLineChart addTooltips(boolean addTooltip) {
        if (addTooltip) {
            renderer_.setDefaultToolTipGenerator(new StandardCategoryToolTipGenerator());
        }
        return this;
    }

    /**
     * Whether to create and display the legend.
     *
     * @param createLegend true if add legend
     * @return this
     */
    public CategoryLineChart addLegend(boolean createLegend) {
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
     * Sets the category label position specification for the axis.
     *
     * @param positions the positions.
     */
    public CategoryLineChart categoryLabelPositions(@NonNull CategoryLabelPositions positions) {
        domainAxis_.setCategoryLabelPositions(positions);
        return this;
    }

    /**
     * Set the chart orientation.
     *
     * @param orientation {@link PlotOrientation}
     * @return this
     */
    public CategoryLineChart orientation(PlotOrientation orientation) {
        plot_.setOrientation(orientation);
        return this;
    }

    /**
     * Set X Axis title
     *
     * @param xAxisTitle x axis title
     * @return this
     */
    public CategoryLineChart xAxisName(String xAxisTitle) {
        domainAxis_.setLabel(xAxisTitle);
        return this;
    }

    /**
     * Set Y Axis title
     *
     * @param yAxisTitle y axis title
     * @return this
     */
    public CategoryLineChart yAxisName(String yAxisTitle) {
        rangeAxis_.setLabel(yAxisTitle);
        return this;
    }

    @Override
    public CategoryLineChart build() {
        DEFAULT_THEME.apply(chart_);
        return this;
    }

    @Override
    public JFreeChart getChart() {
        return chart_;
    }
}
