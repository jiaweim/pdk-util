package pdk.util.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import pdk.util.IBuilder;
import pdk.util.tuple.Tuple2;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 09 Apr 2026, 11:13 AM
 */
public class CategoryChartBuilder implements IBuilder<JFreeChart> {

    public static CategoryChartBuilder create() {
        return new CategoryChartBuilder();
    }

    private String title_ = "";
    private String categoryAxisName_ = "";
    private String valueAxisName_ = "";
    private CategoryLabelPositions categoryLabelPositions_ = CategoryLabelPositions.STANDARD;

    private final ArrayList<Tuple2<CategoryDataset, CategoryChartType>> datasetList_ = new ArrayList<>();
    private boolean addLegend_ = true;
    private boolean addTooltips_ = false;
    private PlotOrientation orientation_ = PlotOrientation.VERTICAL;

    private CategoryChartBuilder() {}

    /**
     * Set chart title
     *
     * @param title new title
     * @return this
     */
    public CategoryChartBuilder title(String title) {
        this.title_ = title;
        return this;
    }

    /**
     * Add a new {@link CategoryDataset} to plot
     *
     * @param dataset   {@link CategoryDataset} instance
     * @param chartType {@link CategoryChartType}
     * @return this
     */
    public CategoryChartBuilder addDataset(CategoryDataset dataset, CategoryChartType chartType) {
        datasetList_.add(new Tuple2<>(dataset, chartType));
        return this;
    }

    /**
     * Set the category axis name
     *
     * @param categoryAxisName axis name
     * @return this
     */
    public CategoryChartBuilder categoryAxisName(String categoryAxisName) {
        categoryAxisName_ = categoryAxisName;
        return this;
    }

    /**
     * Set the category axis label positions
     *
     * @param categoryLabelPositions {@link CategoryLabelPositions} instance
     * @return this
     */
    public CategoryChartBuilder categoryLabelPositions(CategoryLabelPositions categoryLabelPositions) {
        this.categoryLabelPositions_ = categoryLabelPositions;
        return this;
    }

    /**
     * Set the value axis name
     *
     * @param valueAxisName axis name
     * @return this
     */
    public CategoryChartBuilder valueAxisName(String valueAxisName) {
        valueAxisName_ = valueAxisName;
        return this;
    }

    /**
     * Whether add legend in the chart
     *
     * @param addLegend true if add legend
     * @return this
     */
    public CategoryChartBuilder addLegend(boolean addLegend) {
        addLegend_ = addLegend;
        return this;
    }

    /**
     * Generate tool tips
     *
     * @param addTooltips whether generate tool tips
     * @return this
     */
    public CategoryChartBuilder addTooltips(boolean addTooltips) {
        addTooltips_ = addTooltips;
        return this;
    }

    /**
     * Set {@link PlotOrientation} of the chart
     *
     * @param orientation {@link PlotOrientation} instance
     * @return this
     */
    public CategoryChartBuilder orientation(PlotOrientation orientation) {
        orientation_ = orientation;
        return this;
    }

    @Override
    public JFreeChart build() {
        CategoryAxis xAxis = new CategoryAxis(categoryAxisName_);
        xAxis.setCategoryLabelPositions(categoryLabelPositions_);

        ValueAxis yAxis = new NumberAxis(valueAxisName_);

        CategoryPlot plot = new CategoryPlot();
        plot.setDomainAxis(xAxis);
        plot.setRangeAxis(yAxis);

        for (int i = 0; i < datasetList_.size(); i++) {
            Tuple2<CategoryDataset, CategoryChartType> dataset = datasetList_.get(i);
            CategoryChartType type = dataset._2;
            CategoryItemRenderer renderer = null;
            switch (type) {
                case LINE -> renderer = new LineAndShapeRenderer(true, false);
                case LINE_SHAPE -> renderer = new LineAndShapeRenderer(true, true);
            }
            if (renderer != null) {
                plot.setRenderer(i, renderer);
                plot.setDataset(i, dataset._1);
            }
        }

        Map<Integer, CategoryItemRenderer> renderers = plot.getRenderers();


        JFreeChart chart = new JFreeChart(title_, JFreeChart.DEFAULT_TITLE_FONT, plot, addLegend_);
        ChartUtils.defaultTheme.apply(chart);

        plot.setOrientation(orientation_);
        if (addTooltips_) {
            StandardCategoryToolTipGenerator generator = new StandardCategoryToolTipGenerator();
            for (CategoryItemRenderer renderer : renderers.values()) {
                renderer.setDefaultToolTipGenerator(generator);
            }
        }

        return chart;
    }
}
