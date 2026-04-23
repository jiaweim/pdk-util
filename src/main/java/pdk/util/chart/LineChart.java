package pdk.util.chart;

import org.apache.commons.statistics.distribution.NormalDistribution;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.event.AxisChangeEvent;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import pdk.util.IBuilder;
import pdk.util.data.Point2D;
import pdk.util.math.DistributionUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Line Chart and scatter chart.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 13 Apr 2026, 9:06 AM
 */
public class LineChart implements IBuilder<LineChart>, Chart {

    public static DatasetBuilder dataset() {
        return new DatasetBuilder();
    }

    public static LineChart chart() {
        return new LineChart();
    }

    public static class DatasetBuilder implements IBuilder<XYDataset> {

        private final XYSeriesCollection dataset_ = new XYSeriesCollection();

        private DatasetBuilder() {}

        /**
         * Add a data series
         *
         * @param series {@link XYSeries}
         * @return this
         */
        public DatasetBuilder addSeries(XYSeries series) {
            Objects.requireNonNull(series);
            dataset_.addSeries(series);
            return this;
        }

        /**
         * Add a data series
         *
         * @param name series name
         * @param x    x values
         * @param y    y values
         * @return this
         */
        public DatasetBuilder addSeries(String name, double[] x, double[] y) {
            Objects.requireNonNull(x);
            Objects.requireNonNull(y);

            XYSeries series = new XYSeries(name);
            for (int i = 0; i < x.length; i++) {
                series.add(x[i], y[i]);
            }
            dataset_.addSeries(series);

            return this;
        }


        /**
         * Add a data series
         *
         * @param name   series name
         * @param points {@link Point2D} list
         * @return this
         */
        public DatasetBuilder addSeries(String name, List<Point2D> points) {
            Objects.requireNonNull(points);
            XYSeries series = new XYSeries(name);
            for (Point2D point : points) {
                series.add(point.getX(), point.getY());
            }
            dataset_.addSeries(series);
            return this;
        }

        @Override
        public XYDataset build() {
            return dataset_;
        }
    }

    private final JFreeChart chart_;
    private final XYPlot xyPlot_;
    private final NumberAxis domainAxis_;
    private final NumberAxis rangeAxis_;
    private final XYLineAndShapeRenderer renderer_;

    private LineChart() {
        domainAxis_ = new NumberAxis();
        rangeAxis_ = new NumberAxis();
        renderer_ = new XYLineAndShapeRenderer(true, false);

        xyPlot_ = new XYPlot(null, domainAxis_, rangeAxis_, renderer_);
        chart_ = new JFreeChart(null, DEFAULT_TITLE_FONT, xyPlot_, false);
    }

    /**
     * Set true to create line chart
     *
     * @param showLine whether show lines between data points
     * @return this
     */
    public LineChart showLine(boolean showLine) {
        renderer_.setDefaultLinesVisible(showLine);
        return this;
    }

    /**
     * Set true to create scatter chart
     *
     * @param showShape whether show shapes of data points
     * @return this
     */
    public LineChart showShape(boolean showShape) {
        renderer_.setDefaultShapesVisible(showShape);
        return this;
    }


    /**
     * Sets the paint used for a series outline.
     *
     * @param series the series index (zero-based).
     * @param paint  the paint ({@code null} permitted).
     */
    public LineChart seriesOutlinePaint(int series, Paint paint) {
        renderer_.setSeriesOutlinePaint(series, paint, false);
        return this;
    }

    /**
     * Sets the flag that controls whether the outline paint is used to draw
     * shape outlines, and sends a {@link RendererChangeEvent} to all
     * registered listeners.
     * <p>
     *
     * @param flag the flag.
     */
    public LineChart useOutlinePaint(boolean flag) {
        renderer_.setUseOutlinePaint(flag);
        return this;
    }

    /**
     * Set the dataset to plot
     *
     * @param dataset {@link XYDataset}
     * @return this
     */
    public LineChart dataset(XYDataset dataset) {
        xyPlot_.setDataset(dataset);
        return this;
    }

    /**
     * Set the chart title.
     *
     * @param title new title
     * @return this
     */
    public LineChart title(String title) {
        chart_.setTitle(title);
        return this;
    }

    /**
     * Set the chart orientation.
     *
     * @param orientation {@link PlotOrientation}
     * @return this
     */
    public LineChart orientation(PlotOrientation orientation) {
        xyPlot_.setOrientation(orientation);
        return this;
    }

    /**
     * Set X Axis title
     *
     * @param xAxisTitle x axis title
     * @return this
     */
    public LineChart xAxisName(String xAxisTitle) {
        domainAxis_.setLabel(xAxisTitle);
        return this;
    }

    /**
     * Sets the flag that indicates whether the axis range, if
     * automatically calculated, is forced to include zero.
     * <p>
     * If the flag is changed to {@code true}, the axis range is
     * recalculated.
     * <p>
     * Any change to the flag will trigger an {@link AxisChangeEvent}.
     *
     * @param xAxisIncludesZero the new value of the flag.
     */
    public LineChart xAxisIncludesZero(boolean xAxisIncludesZero) {
        domainAxis_.setAutoRangeIncludesZero(xAxisIncludesZero);
        return this;
    }

    /**
     * Set X Axis title
     *
     * @param yAxisTitle y axis title
     * @return this
     */
    public LineChart yAxisName(String yAxisTitle) {
        rangeAxis_.setLabel(yAxisTitle);
        return this;
    }

    /**
     * Sets the flag that indicates whether the axis range, if
     * automatically calculated, is forced to include zero.
     * <p>
     * If the flag is changed to {@code true}, the axis range is
     * recalculated.
     * <p>
     * Any change to the flag will trigger an {@link AxisChangeEvent}.
     *
     * @param yAxisIncludesZero the new value of the flag.
     */
    public LineChart yAxisIncludesZero(boolean yAxisIncludesZero) {
        rangeAxis_.setAutoRangeIncludesZero(yAxisIncludesZero);
        return this;
    }

    /**
     * Sets the flag that controls whether the zero baseline is
     * displayed for the domain axis.
     *
     * @param visible the flag.
     */
    public LineChart domainZeroBaselineVisible(boolean visible) {
        xyPlot_.setDomainZeroBaselineVisible(visible);
        return this;
    }

    /**
     * Sets the flag that controls whether the zero baseline is
     * displayed for the range axis.
     *
     * @param visible the flag.
     */
    public LineChart rangeZeroBaselineVisible(boolean visible) {
        xyPlot_.setRangeZeroBaselineVisible(visible);
        return this;
    }

    /**
     * Whether to create and display the legend.
     *
     * @param createLegend true if add legend
     * @return this
     */
    public LineChart addLegend(boolean createLegend) {
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
    public LineChart addTooltips(boolean addTooltip) {
        if (addTooltip) {
            renderer_.setDefaultToolTipGenerator(new StandardXYToolTipGenerator());
        }
        return this;
    }

    /**
     * Whether grid-lines are drawn against the domain axis.
     *
     * @param showDomainGridlines true if show grid lines
     * @return this
     */
    public LineChart showDomainGridlines(boolean showDomainGridlines) {
        xyPlot_.setDomainGridlinesVisible(showDomainGridlines);
        return this;
    }

    /**
     * Whether grid-lines are drawn against the range axis.
     *
     * @param showRangeGridlines true if show grid lines
     * @return this
     */
    public LineChart showRangeGridlines(boolean showRangeGridlines) {
        xyPlot_.setRangeGridlinesVisible(showRangeGridlines);
        return this;
    }

    @Override
    public LineChart build() {
        DEFAULT_THEME.apply(chart_);
        return this;
    }

    @Override
    public JFreeChart getChart() {
        return chart_;
    }

    static void main() {
        NormalDistribution distribution = NormalDistribution.of(20.6, 1.62);
        ArrayList<Point2D> samples = DistributionUtils.sample(distribution, 20.6 - 10, 20.6 + 10, 500);

        XYDataset dataset = LineChart.dataset().addSeries("Line", samples).build();
        LineChart lineChart = LineChart.chart()
                .dataset(dataset)
                .addLegend(false)
                .build();
        lineChart.show();
    }
}
