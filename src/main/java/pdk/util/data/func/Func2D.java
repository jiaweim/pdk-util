package pdk.util.data.func;

import org.jfree.data.xy.XYDataset;
import pdk.util.chart.Data;
import pdk.util.chart.LineChart;
import pdk.util.data.Point2D;

import java.util.ArrayList;
import java.util.List;

import static pdk.util.ArgUtils.checkArgument;

/**
 * A function for the form {@code y=f(x)}
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 03 Jul 2025, 9:18 AM
 */
public interface Func2D {

    /**
     * Return the value of the function for a given input x
     *
     * @param x the x value
     * @return the y value
     */
    double f(double x);

    /**
     * Sampling this function over a fixed range
     *
     * @param start   the start value for the range, inclusive
     * @param end     the end value for the range, inclusive
     * @param samples the number of sample point
     * @return list of {@link Point2D}
     */
    default List<Point2D> sample(double start, double end, int samples) {
        checkArgument(start < end, "Require start < end");
        double step = (end - start) / (samples - 1);
        List<Point2D> list = new ArrayList<>(samples);
        for (int i = 0; i < samples; i++) {
            double x = start + (step * i);
            double y = f(x);
            list.add(new Point2D(x, y));
        }
        return list;
    }

    /**
     * Create a chart for this function, used to display this function.
     *
     * @param start           start x
     * @param end             end x
     * @param numberOfSamples number of data points
     * @return {@link LineChart}
     */
    default LineChart show(double start, double end, int numberOfSamples) {
        List<Point2D> sample = sample(start, end, numberOfSamples);
        XYDataset dataset = Data.xyDataset().addSeries("", sample).build();
        LineChart chart = LineChart.lineChart()
                .dataset(dataset)
                .xAxisName("X")
                .yAxisName("Y")
                .build();
        return chart;
    }

}
