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
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import pdk.util.IBuilder;

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

    private JFreeChart chart_;
    private CategoryDataset dataset_;
    private String title_;
    private PlotOrientation orientation_ = PlotOrientation.VERTICAL;
    private String domainAxisTitle_;
    private String rangeAxisTitle_;
    private CategoryAxis domainAxis_;
    private NumberAxis rangeAxis_;
    private boolean showLegend_ = true;
    private boolean showTooltips_ = true;
    private boolean showDomainGridlines_ = true;
    private boolean showRangeGridlines_ = true;

    // CategoryAxis
    /**
     * The amount of space reserved between categories.
     * a percentage of the axis length
     */
    private double categoryMargin_ = 0.20;

    // BarRenderer
    /**
     * The margin between items (bars) within a category.
     * a percentage of the available space for all bars.
     */
    private double itemMargin_ = 0.20;


    private CategoryBarChart() {}

    /**
     * Set the dataset for this plot
     *
     * @param dataset {@link CategoryDataset} instance
     * @return this
     */
    public CategoryBarChart dataset(CategoryDataset dataset) {
        dataset_ = dataset;
        return this;
    }

    /**
     * Set the chart title.
     *
     * @param title new title
     * @return this
     */
    public CategoryBarChart title(String title) {
        this.title_ = title;
        return this;
    }

    /**
     * Set the chart orientation.
     *
     * @param orientation {@link PlotOrientation}
     * @return this
     */
    public CategoryBarChart orientation(PlotOrientation orientation) {
        this.orientation_ = orientation;
        return this;
    }

    /**
     * Set X Axis title
     *
     * @param xAxisTitle x axis title
     * @return this
     */
    public CategoryBarChart xAxisName(String xAxisTitle) {
        this.domainAxisTitle_ = xAxisTitle;
        return this;
    }

    /**
     * Set X Axis title
     *
     * @param yAxisTitle y axis title
     * @return this
     */
    public CategoryBarChart yAxisName(String yAxisTitle) {
        this.rangeAxisTitle_ = yAxisTitle;
        return this;
    }

    /**
     * Whether to create and display the legend.
     *
     * @param createLegend true if add legend
     * @return this
     */
    public CategoryBarChart addLegend(boolean createLegend) {
        this.showLegend_ = createLegend;
        return this;
    }

    /**
     * configure chart to generate tool tips
     *
     * @param addTooltip true if generate tool tips
     * @return this
     */
    public CategoryBarChart addTooltips(boolean addTooltip) {
        this.showTooltips_ = addTooltip;
        return this;
    }

    /**
     * Whether grid-lines are drawn against the domain axis.
     *
     * @param showDomainGridlines true if show grid lines
     * @return this
     */
    public CategoryBarChart showDomainGridlines(boolean showDomainGridlines) {
        this.showDomainGridlines_ = showDomainGridlines;
        return this;
    }

    /**
     * Whether grid-lines are drawn against the range axis.
     *
     * @param showRangeGridlines true if show grid lines
     * @return this
     */
    public CategoryBarChart showRangeGridlines(boolean showRangeGridlines) {
        this.showRangeGridlines_ = showRangeGridlines;
        return this;
    }

    /**
     * Set the amount of space reserved between categories.
     *
     * @param categoryMargin a percentage of the axis length, default to be 0.2
     * @return this
     */
    public CategoryBarChart categoryMargin(double categoryMargin) {
        this.categoryMargin_ = categoryMargin;
        return this;
    }

    /**
     * Set the margin between items (bars) within a category.
     *
     * @param itemMargin a percentage of the available space for all bars.
     * @return this
     */
    public CategoryBarChart itemMargin(double itemMargin) {
        this.itemMargin_ = itemMargin;
        return this;
    }

    @Override
    public CategoryBarChart build() {
        domainAxis_ = new CategoryAxis(domainAxisTitle_);
        domainAxis_.setCategoryMargin(categoryMargin_);

        rangeAxis_ = new NumberAxis(rangeAxisTitle_);

        BarRenderer renderer = new BarRenderer();
        renderer.setItemMargin(itemMargin_);
        if (orientation_ == PlotOrientation.HORIZONTAL) {
            ItemLabelPosition position1 = new ItemLabelPosition(
                    ItemLabelAnchor.OUTSIDE3, TextAnchor.CENTER_LEFT);
            renderer.setDefaultPositiveItemLabelPosition(position1);
            ItemLabelPosition position2 = new ItemLabelPosition(
                    ItemLabelAnchor.OUTSIDE9, TextAnchor.CENTER_RIGHT);
            renderer.setDefaultNegativeItemLabelPosition(position2);
        } else if (orientation_ == PlotOrientation.VERTICAL) {
            ItemLabelPosition position1 = new ItemLabelPosition(
                    ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER);
            renderer.setDefaultPositiveItemLabelPosition(position1);
            ItemLabelPosition position2 = new ItemLabelPosition(
                    ItemLabelAnchor.OUTSIDE6, TextAnchor.TOP_CENTER);
            renderer.setDefaultNegativeItemLabelPosition(position2);
        }
        if (showTooltips_) {
            renderer.setDefaultToolTipGenerator(new StandardCategoryToolTipGenerator());
        }


        CategoryPlot plot = new CategoryPlot(dataset_, domainAxis_, rangeAxis_, renderer);
        plot.setOrientation(orientation_);

        // grid lines
        plot.setDomainGridlinesVisible(showDomainGridlines_);
        plot.setRangeGridlinesVisible(showRangeGridlines_);

        chart_ = new JFreeChart(title_, DEFAULT_TITLE_FONT, plot, showLegend_);
        DEFAULT_THEME.apply(chart_);
        return this;
    }

    @Override
    public JFreeChart getChart() {
        return chart_;
    }

    static void main() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String series1 = "Males";
        String series2 = "Females";

        String category1 = "18 to 39";
        String category2 = "40 - 59";
        String category3 = "60 and over";

        dataset.addValue(5.5, series1, category1);
        dataset.addValue(10.3, series2, category1);
        dataset.addValue(8.4, series1, category2);
        dataset.addValue(20.1, series2, category2);
        dataset.addValue(12.8, series1, category3);
        dataset.addValue(24.3, series2, category3);

        CategoryBarChart chart = CategoryBarChart.create()
                .dataset(dataset)
                .title("Antidepressant Medication Usage")
                .xAxisName("Age Category")
                .yAxisName("Percent")
                .showDomainGridlines(true)
                .showRangeGridlines(true)
                .categoryMargin(0.5)
                .itemMargin(0.0)
                .build();
        chart.show();
    }
}
