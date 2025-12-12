package pdk.util.chart;

import org.apache.commons.statistics.distribution.ContinuousDistribution;
import org.apache.commons.statistics.distribution.NormalDistribution;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.UIUtils;
import org.jfree.chart.urls.StandardXYURLGenerator;
import org.jfree.data.general.DatasetUtils;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.svg.SVGGraphics2D;
import org.jfree.svg.SVGUtils;
import org.jspecify.annotations.NonNull;
import pdk.util.data.Point2D;
import pdk.util.math.DistributionUtils;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static pdk.util.ArgUtils.checkNotNull;

/**
 * Chart Utilities.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 11 Dec 2025, 11:04 AM
 */
public final class ChartUtils {

    private static final PdkChartTheme defaultTheme = new PdkChartTheme("pdk");

    private ChartUtils() {}

    /**
     * Creates a line chart (based on an {@link XYDataset}) with default
     * settings.
     *
     * @param title       the chart title ({@code null} permitted).
     * @param xAxisLabel  a label for the X-axis ({@code null} permitted).
     * @param yAxisLabel  a label for the Y-axis ({@code null} permitted).
     * @param dataset     the dataset for the chart ({@code null} permitted).
     * @param orientation the plot orientation (horizontal or vertical)
     *                    ({@code null} NOT permitted).
     * @param legend      a flag specifying whether a legend is required.
     * @param tooltips    configure chart to generate tool tips?
     * @param urls        configure chart to generate URLs?
     * @return The chart.
     */
    public static JFreeChart createXYLineChart(String title, String xAxisLabel,
            String yAxisLabel, XYDataset dataset, PlotOrientation orientation,
            boolean legend, boolean tooltips, boolean urls) {
        checkNotNull(orientation);

        NumberAxis xAxis = new NumberAxis(xAxisLabel);
        xAxis.setAutoRangeIncludesZero(false);
        NumberAxis yAxis = new NumberAxis(yAxisLabel);
        XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        plot.setOrientation(orientation);
        if (tooltips) {
            renderer.setDefaultToolTipGenerator(new StandardXYToolTipGenerator());
        }
        if (urls) {
            renderer.setURLGenerator(new StandardXYURLGenerator());
        }

        JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, legend);
        defaultTheme.apply(chart);
        return chart;
    }

    /**
     * Writes the current content to the specified file in SVG format.
     *
     * @param chart  the {@link JFreeChart}
     * @param width  the chart width.
     * @param height the chart height.
     * @param file   the output file ({@code null} not permitted).
     */
    public static void writeToSVG(@NonNull JFreeChart chart, int width, int height, @NonNull File file) {
        SVGGraphics2D graphics2D = new SVGGraphics2D(width, height);
        Rectangle2D drawArea = new Rectangle2D.Double(0, 0, width, height);
        chart.draw(graphics2D, drawArea);
        String element = graphics2D.getSVGElement();
        try {
            SVGUtils.writeToSVG(file, element);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Display a {@link JFreeChart}
     *
     * @param jFreeChart {@link JFreeChart} instance
     */
    public static void showChart(JFreeChart jFreeChart) {
        ApplicationFrame frame = new ApplicationFrame("");
        ChartPanel chartPanel = new ChartPanel(jFreeChart);
        frame.setContentPane(chartPanel);
        frame.pack();
        UIUtils.centerFrameOnScreen(frame);
        frame.setVisible(true);
    }

    /**
     * Create a {@link JFreeChart} to show the probability density for a {@link ContinuousDistribution},
     * you can display it with {@link #showChart(JFreeChart)}.
     *
     * @param distribution {@link ContinuousDistribution}
     * @param start        start x
     * @param end          end x
     * @param samples      number of data points
     * @return {@link JFreeChart}
     */
    public static JFreeChart pdfChart(ContinuousDistribution distribution, double start, double end, int samples) {
        XYDataset dataset = DatasetUtils.sampleFunction2D(distribution::density, start, end, samples, "");
        JFreeChart chart = ChartFactory.createXYLineChart("", "X", "Probability Density", dataset,
                PlotOrientation.VERTICAL, false, false, false);
        XYPlot xyPlot = chart.getXYPlot();
        xyPlot.setBackgroundPaint(Color.WHITE);
        xyPlot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        xyPlot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        chart.setBackgroundPaint(Color.WHITE);
        return chart;
    }

    /**
     * Create a {@link JFreeChart} for a {@link ContinuousDistribution}, you can display it with {@link #showChart(JFreeChart)}.
     * </p>
     * <img src="shade_chart.png" width="140"/>
     *
     * @param distribution {@link ContinuousDistribution}
     * @param start        start x
     * @param end          end x
     * @param areaStart    start x of shaded area
     * @param areaEnd      end x of shaded area
     * @param samples      number of data points
     * @return {@link JFreeChart}
     */
    public static JFreeChart pdfChart(ContinuousDistribution distribution,
            double start, double end, double areaStart, double areaEnd, int samples) {

        double shadowStart = Math.max(start, areaStart);
        double shadowEnd = Math.min(end, areaEnd);

        ArrayList<Point2D> sample = DistributionUtils.sample(distribution, start, end, samples);
        XYSeries lineSeries = new XYSeries("Line");
        XYSeries areaSeries = new XYSeries("Area");
        for (Point2D point2D : sample) {
            double x = point2D.getX();
            lineSeries.add(x, point2D.getY());
            if (x >= shadowStart && x <= shadowEnd) {
                areaSeries.add(x, point2D.getY());
            }
        }

        XYSeriesCollection dataset1 = new XYSeriesCollection();
        dataset1.addSeries(lineSeries);
        XYSeriesCollection dataset2 = new XYSeriesCollection();
        dataset2.addSeries(areaSeries);

        NumberAxis xAxis = new NumberAxis("X");
        xAxis.setAutoRangeIncludesZero(false);
        NumberAxis yAxis = new NumberAxis("Probability Density");
        XYPlot plot = new XYPlot();
        plot.setDomainAxis(xAxis);
        plot.setRangeAxis(yAxis);

        plot.setDataset(0, dataset1);
        plot.setDataset(1, dataset2);

        plot.setRenderer(0, new XYLineAndShapeRenderer(true, false));
        plot.setRenderer(1, new XYAreaRenderer(XYAreaRenderer.AREA));

        plot.setOrientation(PlotOrientation.VERTICAL);

        JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, false);
        return chart;
    }


    /**
     * Create a {@link JFreeChart} for two {@link ContinuousDistribution}s, you can display it with {@link #showChart(JFreeChart)}.
     * </p>
     * <img src="shade_chart.png" width="140"/>
     *
     * @param distribution1 {@link ContinuousDistribution}
     * @param distribution2 {@link ContinuousDistribution}
     * @param start         start x
     * @param end           end x
     * @param samples       number of data points
     * @return {@link JFreeChart}
     */
    public static JFreeChart pdfChart(
            ContinuousDistribution distribution1, String name1,
            ContinuousDistribution distribution2, String name2,
            double start, double end, int samples) {

        ArrayList<Point2D> sample1 = DistributionUtils.sample(distribution1, start, end, samples);
        ArrayList<Point2D> sample2 = DistributionUtils.sample(distribution2, start, end, samples);

        XYSeries series1 = new XYSeries(name1);
        XYSeries series2 = new XYSeries(name2);

        for (Point2D point2D : sample1) {
            series1.add(point2D.getX(), point2D.getY());
        }

        for (Point2D point2D : sample2) {
            series2.add(point2D.getX(), point2D.getY());
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);

        JFreeChart chart = createXYLineChart("", "X", "Probability Density", dataset, PlotOrientation.VERTICAL, true,
                true, false);

        return chart;
    }

    static void main() {
        double mean = 20.6;
        double sigma = 1.62;
        NormalDistribution distribution = NormalDistribution.of(mean, 1.62);
        System.out.println(distribution.probability(mean - 6 * sigma, mean + 6 * sigma));
        JFreeChart chart = pdfChart(distribution, mean - 10, mean + 10, mean - sigma * 4, mean + sigma * 4, 500);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.getDomainAxis().setLabel("");
        plot.getRangeAxis().setLabel("");
        chart.setBackgroundPaint(Color.WHITE);
        showChart(chart);
    }

}
