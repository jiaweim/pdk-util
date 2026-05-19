package pdk.util.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.data.category.CategoryDataset;
import org.jspecify.annotations.Nullable;
import pdk.util.IBuilder;

/**
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 19 May 2026, 5:32 PM
 */
public class RadarChart implements IBuilder<RadarChart>, Chart {

    public static RadarChart create() {
        return new RadarChart();
    }

    private final JFreeChart chart_;
    private final SpiderWebPlot plot_;

    private RadarChart() {
        plot_ = new SpiderWebPlot();
        chart_ = new JFreeChart(null, DEFAULT_TITLE_FONT, plot_, false);
        DEFAULT_THEME.apply(chart_);
    }

    /**
     * Sets the dataset used by the plot.
     *
     * @param dataset the dataset.
     */
    public RadarChart dataset(@Nullable CategoryDataset dataset) {
        plot_.setDataset(dataset);
        return this;
    }

    /**
     * Sets the starting angle.
     * <p>
     * The initial default value is 90 degrees, which corresponds to 12 o'clock.
     * A value of zero corresponds to 3 o'clock... this is the encoding used by
     * Java's Arc2D class.
     *
     * @param angle the angle (in degrees).
     */
    public RadarChart startAngle(double angle) {
        plot_.setStartAngle(angle);
        return this;
    }

    /**
     * Sets the interior gap.
     * <p>
     * This controls the space between the edges of the
     * plot and the plot area itself (the region where the axis labels appear).
     *
     * @param percent the gap (as a percentage of the available drawing space).
     */
    public RadarChart interiorGap(double percent) {
        plot_.setInteriorGap(percent);
        return this;
    }

    /**
     * Sets the maximum value any category axis can take.
     *
     * @param value the maximum value.
     */
    public RadarChart maxValue(double value) {
        plot_.setMaxValue(value);
        return this;
    }

    @Override
    public RadarChart build() {
        return this;
    }

    @Override
    public JFreeChart getChart() {
        return chart_;
    }
}
