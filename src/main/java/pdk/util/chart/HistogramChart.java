package pdk.util.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;
import pdk.util.IBuilder;

import java.awt.*;
import java.util.Random;

/**
 * Class to create Histogram.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 23 Apr 2026, 12:05 PM
 */
public class HistogramChart implements IBuilder<HistogramChart>, Chart {

    public static DatasetBuilder dataset() {
        return new DatasetBuilder();
    }

    public static HistogramChart chart() {
        return new HistogramChart();
    }

    public static class DatasetBuilder implements IBuilder<HistogramDataset> {

        private final HistogramDataset dataset = new HistogramDataset();

        private DatasetBuilder() {}

        /**
         * Adds a series to the dataset. Any data value less than minimum will be
         * assigned to the first bin, and any data value greater than maximum will
         * be assigned to the last bin.  Values falling on the boundary of
         * adjacent bins will be assigned to the higher indexed bin.
         *
         * @param key     the series key ({@code null} not permitted).
         * @param values  the raw observations.
         * @param bins    the number of bins (must be at least 1).
         * @param minimum the lower bound of the bin range.
         * @param maximum the upper bound of the bin range.
         */
        public DatasetBuilder addSeries(Comparable key, double[] values, int bins,
                double minimum, double maximum) {
            dataset.addSeries(key, values, bins, minimum, maximum);
            return this;
        }

        @Override
        public HistogramDataset build() {
            return dataset;
        }
    }

    private final NumberAxis xAxis_;
    private final NumberAxis yAxis_;
    private final XYPlot xyPlot_;
    private final JFreeChart chart_;
    private final XYBarRenderer xyBarRenderer_;

    private HistogramChart() {
        xAxis_ = new NumberAxis();
        xAxis_.setAutoRangeIncludesZero(false);

        yAxis_ = new NumberAxis();
        xyBarRenderer_ = new XYBarRenderer();
        xyPlot_ = new XYPlot();
        xyPlot_.setDomainAxis(xAxis_);
        xyPlot_.setRangeAxis(yAxis_);
        xyPlot_.setRenderer(xyBarRenderer_);
        chart_ = new JFreeChart(null, null, xyPlot_, false);
    }

    /**
     * Sets whether bar outlines are drawn.
     *
     * @param draw the flag.
     */
    public HistogramChart drawBarOutline(boolean draw) {
        xyBarRenderer_.setDrawBarOutline(draw);
        return this;
    }

    /**
     * Set the dataset to plot
     *
     * @param dataset {@link IntervalXYDataset} instance
     * @return this
     */
    public HistogramChart dataset(IntervalXYDataset dataset) {
        xyPlot_.setDataset(dataset);
        return this;
    }

    /**
     * Set the chart title.
     *
     * @param title new title
     * @return this
     */
    public HistogramChart title(String title) {
        chart_.setTitle(title);
        return this;
    }

    /**
     * Set the chart orientation.
     *
     * @param orientation {@link PlotOrientation}
     * @return this
     */
    public HistogramChart orientation(PlotOrientation orientation) {
        xyPlot_.setOrientation(orientation);
        return this;
    }

    /**
     * Set X Axis title
     *
     * @param xAxisTitle x axis title
     * @return this
     */
    public HistogramChart xAxisName(String xAxisTitle) {
        xAxis_.setLabel(xAxisTitle);
        return this;
    }

    /**
     * Set X Axis title
     *
     * @param yAxisTitle y axis title
     * @return this
     */
    public HistogramChart yAxisName(String yAxisTitle) {
        yAxis_.setLabel(yAxisTitle);
        return this;
    }

    /**
     * Whether to create and display the legend.
     *
     * @param createLegend true if add legend
     * @return this
     */
    public HistogramChart addLegend(boolean createLegend) {
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
    public HistogramChart addTooltips(boolean addTooltip) {
        if (addTooltip) {
            xyBarRenderer_.setDefaultToolTipGenerator(new StandardXYToolTipGenerator());
        }
        return this;
    }

    /**
     * Whether grid-lines are drawn against the domain axis.
     *
     * @param showDomainGridlines true if show grid lines
     * @return this
     */
    public HistogramChart showDomainGridlines(boolean showDomainGridlines) {
        xyPlot_.setDomainGridlinesVisible(showDomainGridlines);
        return this;
    }

    /**
     * Whether grid-lines are drawn against the range axis.
     *
     * @param showRangeGridlines true if show grid lines
     * @return this
     */
    public HistogramChart showRangeGridlines(boolean showRangeGridlines) {
        xyPlot_.setRangeGridlinesVisible(showRangeGridlines);
        return this;
    }

    @Override
    public HistogramChart build() {
        DEFAULT_THEME.apply(chart_);
        return this;
    }

    @Override
    public JFreeChart getChart() {
        return chart_;
    }

    static void main() {
        DatasetBuilder datasetBuilder = HistogramChart.dataset();
        double[] values = new double[1000];
        Random random = new Random(12345678L);
        for (int i = 0; i < 1000; i++) {
            values[i] = random.nextGaussian() + 5;
        }
        datasetBuilder.addSeries("H1", values, 100, 2.0, 8.0);

        values = new double[1000];
        for (int i = 0; i < 1000; i++) {
            values[i] = random.nextGaussian() + 7;
        }
        datasetBuilder.addSeries("H2", values, 100, 4.0, 10.0);
        HistogramDataset dataset = datasetBuilder.build();

        HistogramChart chart = HistogramChart.chart()
                .title("Histogram Demo")
                .dataset(dataset)
                .orientation(PlotOrientation.VERTICAL)
                .addLegend(true)
                .addTooltips(true)
                .drawBarOutline(false)
                .build();
        chart.show();
    }
}
