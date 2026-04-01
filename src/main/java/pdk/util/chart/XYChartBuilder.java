package pdk.util.chart;

import com.google.common.collect.ArrayListMultimap;
import org.apache.commons.statistics.distribution.NormalDistribution;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import pdk.util.data.Point2D;
import pdk.util.math.DistributionUtils;
import pdk.util.tuple.Tuple;
import pdk.util.tuple.Tuple2;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Builder class for assisting in creating XYChart.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 01 Apr 2026, 1:01 PM
 */
public class XYChartBuilder {

    public static XYChartBuilder start() {
        return new XYChartBuilder();
    }

    private XYChartBuilder() {}

    private HashMap<String, XYSeries> dataset_ = new HashMap<>();
    private HashMap<String, XYChartType> chartTypeMap_ = new HashMap<>();
    private HashMap<String, Double> lineWidthMap_ = new HashMap<>();
    private HashMap<String, Boolean> addTooltipMap_ = new HashMap<>();
    private ArrayList<Tuple2<XYDataset, XYChartType>> xyDatasetList_ = new ArrayList<>();
    private ArrayList<XYAnnotation> xyAnnotationList_ = new ArrayList<>();

    private String title = "";
    private String domainAxisLabel_ = "";
    private String rangeAxisLabel_ = "";
    private boolean xRangeIncludesZero_ = false;
    private boolean yRangeIncludesZero_ = false;
    private boolean xZeroLineVisible_ = false;
    private boolean yZeroLineVisible_ = false;
    private boolean showDomainGridLines_ = true;
    private boolean showRangeGridLines_ = true;
    private boolean addLegend_ = true;
    private PlotOrientation orientation_ = PlotOrientation.VERTICAL;

    /**
     * Set chart title
     *
     * @param title chart title
     * @return this
     */
    public XYChartBuilder title(String title) {
        this.title = title;
        return this;
    }

    /**
     * Set the orientation of the chart.
     *
     * @param orientation {@link PlotOrientation}
     * @return this
     */
    public XYChartBuilder orientation(PlotOrientation orientation) {
        this.orientation_ = orientation;
        return this;
    }

    /**
     * Set X Axis label
     *
     * @param xAxisLabel x axis label
     * @return this
     */
    public XYChartBuilder xAxisName(String xAxisLabel) {
        this.domainAxisLabel_ = xAxisLabel;
        return this;
    }

    /**
     * Whether the x-axis automatically includes the value 0.
     *
     * @param xRangeIncludesZero x range always includes 0
     * @return this
     */
    public XYChartBuilder xRangeIncludesZero(boolean xRangeIncludesZero) {
        this.xRangeIncludesZero_ = xRangeIncludesZero;
        return this;
    }

    /**
     * Whether the y-axis always includes the value 0.
     *
     * @param yRangeIncludesZero y range always includes 0
     * @return this
     */
    public XYChartBuilder yRangeIncludesZero(boolean yRangeIncludesZero) {
        this.yRangeIncludesZero_ = yRangeIncludesZero;
        return this;
    }

    /**
     * Whether the zero baseline is displayed for the domain axis
     *
     * @param xZeroLineVisible flag
     * @return this
     */
    public XYChartBuilder xZeroLineVisible(boolean xZeroLineVisible) {
        this.xZeroLineVisible_ = xZeroLineVisible;
        return this;
    }

    /**
     * Whether the zero baseline is displayed for the range axis
     *
     * @param yZeroLineVisible flag
     * @return this
     */
    public XYChartBuilder yZeroLineVisible(boolean yZeroLineVisible) {
        this.yZeroLineVisible_ = yZeroLineVisible;
        return this;
    }

    /**
     * Set Y axis label
     *
     * @param yAxisLabel y axis label
     * @return this
     */
    public XYChartBuilder yAxisName(String yAxisLabel) {
        this.rangeAxisLabel_ = yAxisLabel;
        return this;
    }

    /**
     * Whether add legend in the chart
     *
     * @param createLegend true if add legend
     * @return this
     */
    public XYChartBuilder addLegend(boolean createLegend) {
        this.addLegend_ = createLegend;
        return this;
    }

    public XYChartBuilder showDomainGridLines(boolean showHorizontalGridLines) {
        this.showDomainGridLines_ = showHorizontalGridLines;
        return this;
    }

    public XYChartBuilder showRangeGridLines(boolean showVerticalGridLines) {
        this.showRangeGridLines_ = showVerticalGridLines;
        return this;
    }

    /**
     * Add a new dataset to plot
     *
     * @param name      dataset name, used to identify the dataset and create the legend
     * @param xValues   x values
     * @param yValues   y values
     * @param chartType {@link XYChartType}
     * @return this
     */
    public XYChartBuilder addSeries(String name, double[] xValues, double[] yValues,
            XYChartType chartType) {
        Objects.requireNonNull(xValues);
        Objects.requireNonNull(yValues);

        XYSeries series = new XYSeries(name);
        for (int i = 0; i < xValues.length; i++) {
            series.add(xValues[i], yValues[i]);
        }
        dataset_.put(name, series);
        chartTypeMap_.put(name, chartType);

        return this;
    }

    /**
     * Add a new data series to the plot
     *
     * @param name      series name
     * @param points    datapoints
     * @param chartType {@link XYChartType}
     * @return this
     */
    public XYChartBuilder addSeries(String name, List<Point2D> points, XYChartType chartType) {
        Objects.requireNonNull(points);
        Objects.requireNonNull(chartType);
        XYSeries series = new XYSeries(name);
        for (Point2D point : points) {
            series.add(point.getX(), point.getY());
        }
        dataset_.put(name, series);
        chartTypeMap_.put(name, chartType);
        return this;
    }

    /**
     * Add a new data series to the plot
     *
     * @param name   series name
     * @param points datapoints
     * @return this
     */
    public XYChartBuilder addLineChart(String name, List<Point2D> points) {
        return addLineChart(name, points, 1.0);
    }

    /**
     * Add a new data series to the plot
     *
     * @param name   series name
     * @param points datapoints
     * @return this
     */
    public XYChartBuilder addLineChart(String name, List<Point2D> points, double lineWidth) {
        Objects.requireNonNull(points);
        XYSeries series = new XYSeries(name);
        for (Point2D point : points) {
            series.add(point.getX(), point.getY());
        }
        dataset_.put(name, series);
        chartTypeMap_.put(name, XYChartType.LINE);
        lineWidthMap_.put(name, lineWidth);
        return this;
    }

    /**
     * Add a new dataset to plot
     *
     * @param series    {@link XYSeries} to show
     * @param chartType {@link XYChartType}
     * @return this
     */
    public XYChartBuilder addSeries(XYSeries series, XYChartType chartType) {
        Objects.requireNonNull(series);

        String key = series.getKey().toString();
        dataset_.put(key, series);
        chartTypeMap_.put(key, chartType);

        return this;
    }

    /**
     * Add a new {@link XYDataset} to plat
     *
     * @param dataset   {@link XYDataset} instance
     * @param chartType {@link XYChartType}
     * @return this
     */
    public XYChartBuilder addDataset(XYDataset dataset, XYChartType chartType) {
        xyDatasetList_.add(Tuple.of(dataset, chartType));
        return this;
    }

    /**
     * Add a new {@link XYDataset} to plat
     *
     * @param dataset       {@link XYDataset} instance
     * @param histogramType {@link HistogramType}
     * @return this
     */
    public XYChartBuilder addDataset(HistogramDataset dataset, HistogramType histogramType) {
        dataset.setType(histogramType);
        xyDatasetList_.add(Tuple.of(dataset, XYChartType.HISTOGRAM));
        return this;
    }

    /**
     * Add a XYPointer Annotation
     *
     * @param label           annotation text
     * @param x               the x-coordinate (measured against the chart's domain axis).
     * @param y               the y-coordinate (measured against the chart's range axis).
     * @param angle           the angle of the arrow's line (in radians).
     * @param labelOffset     the label offset (distance between arrows and annotation text)
     * @param textAnchor      the text anchor (the point on the text bounding rectangle that is aligned to the (x, y) coordinate of the annotation)
     * @param backgroundColor the background paint for the annotation
     * @return this
     */
    public XYChartBuilder addPointerAnnotation(String label, double x, double y, double angle, double labelOffset,
            TextAnchor textAnchor, Color backgroundColor) {
        XYPointerAnnotation annotation = new XYPointerAnnotation(label, x, y, angle);
        annotation.setLabelOffset(labelOffset);
        annotation.setTextAnchor(textAnchor);
        annotation.setBackgroundPaint(backgroundColor);
        xyAnnotationList_.add(annotation);
        return this;
    }

    public JFreeChart build() {
        // Merge the added XYSeries data according to the chart type.
        ArrayListMultimap<XYChartType, String> typeMap = ArrayListMultimap.create();
        for (Map.Entry<String, XYChartType> entry : chartTypeMap_.entrySet()) {
            typeMap.put(entry.getValue(), entry.getKey());
        }

        XYPlot plot = new XYPlot();
        NumberAxis xAxis = new NumberAxis(domainAxisLabel_);
        xAxis.setAutoRangeIncludesZero(xRangeIncludesZero_);
        NumberAxis yAxis = new NumberAxis(rangeAxisLabel_);
        yAxis.setAutoRangeIncludesZero(yRangeIncludesZero_);

        plot.setDomainAxis(xAxis);
        plot.setRangeAxis(yAxis);

        // add dataset
        ArrayList<XYChartType> chartTypes = new ArrayList<>(typeMap.keySet());
        chartTypes.sort(Comparator.naturalOrder());
        for (int i = 0; i < chartTypes.size(); i++) {
            XYChartType chartType = chartTypes.get(i);
            List<String> seriesList = typeMap.get(chartType);
            XYSeriesCollection dataset = new XYSeriesCollection();
            for (String series : seriesList) {
                XYSeries xySeries = dataset_.get(series);
                dataset.addSeries(xySeries);
            }
            plot.setDataset(i, dataset);
        }

        for (int i = 0; i < chartTypes.size(); i++) {
            XYChartType chartType = chartTypes.get(i);

            XYItemRenderer renderer = null;
            switch (chartType) {
                case SCATTER: {
                    renderer = new XYLineAndShapeRenderer(false, true);
                    break;
                }
                case LINE: {
                    renderer = new XYLineAndShapeRenderer(true, false);
                    break;
                }
                case AREA: {
                    renderer = new XYAreaRenderer(XYAreaRenderer.AREA);
                    break;
                }
                case HISTOGRAM: {
                    renderer = new XYBarRenderer();
                    break;
                }
            }
            if (renderer != null) {
                plot.setRenderer(i, renderer);
            }
        }

        int n = chartTypes.size();
        for (int i = 0; i < xyDatasetList_.size(); i++) {
            int index = n + i;
            Tuple2<XYDataset, XYChartType> entry = xyDatasetList_.get(i);
            XYDataset xyDataset = entry._1;
            plot.setDataset(index, xyDataset);

            XYItemRenderer renderer = null;
            switch (entry._2) {
                case SCATTER -> renderer = new XYLineAndShapeRenderer(false, true);
                case LINE -> renderer = new XYLineAndShapeRenderer(true, false);
                case AREA -> renderer = new XYAreaRenderer(XYAreaRenderer.AREA);
                case HISTOGRAM -> renderer = new XYBarRenderer();
            }
            if (renderer != null) {
                plot.setRenderer(index, renderer);
            }
        }

        // add annotations
        for (XYAnnotation annotation : xyAnnotationList_) {
            plot.addAnnotation(annotation, false);
        }

        JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, addLegend_);
        ChartUtils.defaultTheme.apply(chart);

        // Custom settings must be applied after the theme has been applied; otherwise, they will be overwritten by the theme.

        // grid lines
        plot.setRangeGridlinesVisible(showRangeGridLines_);
        plot.setDomainGridlinesVisible(showDomainGridLines_);

        // orientation
        plot.setOrientation(this.orientation_);

        // zero baseline
        plot.setDomainZeroBaselineVisible(this.xZeroLineVisible_);
        plot.setRangeZeroBaselineVisible(this.yZeroLineVisible_);

        Map<Integer, XYItemRenderer> renderers = plot.getRenderers();
        for (Map.Entry<Integer, XYItemRenderer> entry : renderers.entrySet()) {
            Integer key = entry.getKey();
            XYItemRenderer renderer = entry.getValue();
            XYDataset dataset = plot.getDataset(key);

            // set line width
            if (renderer instanceof XYLineAndShapeRenderer) {
                for (int seriesIndex = 0; seriesIndex < dataset.getSeriesCount(); seriesIndex++) {
                    String seriesKey = dataset.getSeriesKey(seriesIndex).toString();
                    Double lineWidth = lineWidthMap_.get(seriesKey);
                    if (lineWidth != null) {
                        renderer.setSeriesStroke(seriesIndex, new BasicStroke(lineWidth.floatValue()));
                    }
                }
            }
        }

        return chart;
    }

    static void main() {
        NormalDistribution distribution = NormalDistribution.of(20.6, 1.62);
        ArrayList<Point2D> samples = DistributionUtils.sample(distribution, 20.6 - 10, 20.6 + 10, 500);
        XYSeries lineSeries = new XYSeries("Line");
        XYSeries areaSeries = new XYSeries("Area");
        for (Point2D point2D : samples) {
            double x = point2D.getX();
            lineSeries.add(x, point2D.getY());
            if (x >= 10.6 && x <= 18) {
                areaSeries.add(x, point2D.getY());
            }
        }

        JFreeChart chart = XYChartBuilder.start()
                .xAxisName("X")
                .yAxisName("Probability Density")
                .addSeries(lineSeries, XYChartType.LINE)
                .addSeries(areaSeries, XYChartType.AREA)
                .addLegend(false)
                .build();
        ChartUtils.showChart(chart);
    }
}
