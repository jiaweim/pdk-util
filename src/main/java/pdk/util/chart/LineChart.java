package pdk.util.chart;

import org.apache.commons.statistics.distribution.NormalDistribution;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import pdk.util.IBuilder;
import pdk.util.data.Point2D;
import pdk.util.math.DistributionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static pdk.util.chart.ChartUtils.DEFAULT_TITLE_FONT;

/**
 * Line Chart.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 13 Apr 2026, 9:06 AM
 */
public class LineChart implements IBuilder<LineChart>, Chart {

    public static LineChart create() {
        return new LineChart();
    }

    private JFreeChart chart_;
    private List<XYSeries> dataset_ = new ArrayList<>();
    private String title_;
    private PlotOrientation orientation_ = PlotOrientation.VERTICAL;
    private String domainAxisTitle_;
    private String rangeAxisTitle_;
    private NumberAxis domainAxis_;
    private NumberAxis rangeAxis_;
    private boolean showLegend_ = true;
    private boolean showTooltips_ = true;

    public LineChart() {}

    /**
     * Add a data series
     *
     * @param name series name
     * @param x    x values
     * @param y    y values
     * @return this
     */
    public LineChart addSeries(String name, double[] x, double[] y) {
        Objects.requireNonNull(x);
        Objects.requireNonNull(y);

        XYSeries series = new XYSeries(name);
        for (int i = 0; i < x.length; i++) {
            series.add(x[i], y[i]);
        }
        dataset_.add(series);

        return this;
    }

    /**
     * Add a data series
     *
     * @param name   series name
     * @param points {@link Point2D} list
     * @return this
     */
    public LineChart addSeries(String name, List<Point2D> points) {
        Objects.requireNonNull(points);
        XYSeries series = new XYSeries(name);
        for (Point2D point : points) {
            series.add(point.getX(), point.getY());
        }
        dataset_.add(series);
        return this;
    }

    /**
     * Set the chart title.
     *
     * @param title new title
     * @return this
     */
    public LineChart title(String title) {
        this.title_ = title;
        return this;
    }

    /**
     * Set the chart orientation.
     *
     * @param orientation {@link PlotOrientation}
     * @return this
     */
    public LineChart orientation(PlotOrientation orientation) {
        this.orientation_ = orientation;
        return this;
    }

    /**
     * Set X Axis title
     *
     * @param xAxisTitle x axis title
     * @return this
     */
    public LineChart xAxisName(String xAxisTitle) {
        this.domainAxisTitle_ = xAxisTitle;
        return this;
    }

    /**
     * Set X Axis title
     *
     * @param yAxisTitle y axis title
     * @return this
     */
    public LineChart yAxisName(String yAxisTitle) {
        this.rangeAxisTitle_ = yAxisTitle;
        return this;
    }

    /**
     * Whether to create and display the legend.
     *
     * @param createLegend true if add legend
     * @return this
     */
    public LineChart addLegend(boolean createLegend) {
        this.showLegend_ = createLegend;
        return this;
    }

    /**
     * configure chart to generate tool tips
     *
     * @param addTooltip true if generate tool tips
     * @return this
     */
    public LineChart addTooltips(boolean addTooltip) {
        this.showTooltips_ = addTooltip;
        return this;
    }

    @Override
    public LineChart build() {
        domainAxis_ = new NumberAxis(domainAxisTitle_);
        rangeAxis_ = new NumberAxis(rangeAxisTitle_);

        XYSeriesCollection dataset = new XYSeriesCollection();
        for (XYSeries series : dataset_) {
            dataset.addSeries(series);
        }

        XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
        XYPlot plot = new XYPlot(dataset, domainAxis_, rangeAxis_, renderer);
        plot.setOrientation(orientation_);

        if (showTooltips_) {
            renderer.setDefaultToolTipGenerator(new StandardXYToolTipGenerator());
        }

        this.chart_ = new JFreeChart(title_, DEFAULT_TITLE_FONT, plot, showLegend_);
        ChartUtils.DEFAULT_THEME.apply(chart_);
        return this;
    }

    @Override
    public JFreeChart getChart() {
        return chart_;
    }

    static void main() {
        NormalDistribution distribution = NormalDistribution.of(20.6, 1.62);
        ArrayList<Point2D> samples = DistributionUtils.sample(distribution, 20.6 - 10, 20.6 + 10, 500);

        LineChart lineChart = LineChart.create()
                .addSeries("Line", samples)
                .addLegend(false)
                .build();
        lineChart.show();
    }
}
