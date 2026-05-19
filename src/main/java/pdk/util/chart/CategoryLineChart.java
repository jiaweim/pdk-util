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
import org.jspecify.annotations.Nullable;
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

    public static CategoryLineChart create() {
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
        DEFAULT_THEME.apply(chart_);
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

    //region Renderer properties

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
     * Sets the default shape.
     *
     * @param shape the shape.
     */
    public CategoryLineChart defaultShape(@NonNull Shape shape) {
        renderer_.setDefaultShape(shape, false);
        return this;
    }

    /**
     * Sets the shape of a given series.
     *
     * @param shape the shape.
     */
    public CategoryLineChart seriesShape(int series, @NonNull Shape shape) {
        renderer_.setSeriesShape(series, shape, false);
        return this;
    }

    /**
     * Sets the outline stroke used for a series.
     * <p>
     * For example, it can be used to set the border width of each data point.
     *
     * @param series the series index (zero-based).
     * @param stroke the stroke ({@code null} permitted).
     */
    public CategoryLineChart seriesOutlineStroke(int series, @Nullable Stroke stroke) {
        renderer_.setSeriesOutlineStroke(series, stroke);
        return this;
    }


    /**
     * Sets the 'shapes filled' flag for a series.
     *
     * @param series the series index (zero-based).
     * @param filled the flag.
     */
    public CategoryLineChart seriesShapesFilled(int series, boolean filled) {
        renderer_.setSeriesShapesFilled(series, filled);
        return this;
    }


    /**
     * Set the line width of a given series
     *
     * @param series series index
     * @param width  line width
     * @return this
     */
    public CategoryLineChart seriesLinesWidth(int series, float width) {
        renderer_.setSeriesStroke(series, new BasicStroke(width));
        return this;
    }

    //endregion

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
        return this;
    }

    @Override
    public JFreeChart getChart() {
        return chart_;
    }
}
