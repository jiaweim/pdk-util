package pdk.util.chart.util;

import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.plot.XYPlot;

import java.awt.*;

/**
 * Interface for grid line
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 23 Apr 2026, 11:17 AM
 */
public interface GridLine {

    /**
     * Sets whether the domain grid-lines are visible.
     * <p>
     * If the flag value is changed, a {@link PlotChangeEvent} is sent to all
     * registered listeners.
     *
     * @param visible the new value of the flag.
     */
    default void setDomainGridlinesVisible(XYPlot xyPlot, boolean visible) {
        xyPlot.setDomainGridlinesVisible(visible);
    }

    /**
     * Whether the domain minor grid-lines are visible.
     * <p>
     * If the flag value is changed, a {@link PlotChangeEvent} is sent to all
     * registered listeners.
     *
     * @param visible the new value of the flag.
     */
    default void setDomainMinorGridlinesVisible(XYPlot xyPlot, boolean visible) {
        xyPlot.setDomainMinorGridlinesVisible(visible);
    }

    /**
     * Sets the flag that controls whether the range axis grid lines are visible.
     * <p>
     * If the flag value is changed, a {@link PlotChangeEvent} is sent to all
     * registered listeners.
     *
     * @param visible the new value of the flag.
     */
    default void setRangeGridlinesVisible(XYPlot xyPlot, boolean visible) {
        xyPlot.setRangeGridlinesVisible(visible);
    }

    /**
     * Sets the flag that controls whether the range axis minor grid
     * lines are visible.
     * <p>
     * If the flag value is changed, a {@link PlotChangeEvent} is sent to all
     * registered listeners.
     *
     * @param visible the new value of the flag.
     */
    default void setRangeMinorGridlinesVisible(XYPlot xyPlot, boolean visible) {
        xyPlot.setRangeMinorGridlinesVisible(visible);
    }

    /**
     * Sets the stroke for the grid lines plotted against the range axis,
     * and sends a {@link PlotChangeEvent} to all registered listeners.
     *
     * @param stroke the stroke ({@code null} not permitted).
     */
    default void setRangeGridlineStroke(XYPlot xyPlot, Stroke stroke) {
        xyPlot.setRangeGridlineStroke(stroke);
    }

    /**
     * Sets the stroke for the minor grid lines plotted against the range axis,
     * and sends a {@link PlotChangeEvent} to all registered listeners.
     *
     * @param stroke the stroke ({@code null} not permitted).
     */
    default void setRangeMinorGridlineStroke(XYPlot xyPlot, Stroke stroke) {
        xyPlot.setRangeMinorGridlineStroke(stroke);
    }

    /**
     * Set the range grid-line paint
     *
     * @param xyPlot {@link XYPlot}
     * @param paint  {@link Paint} instance
     */
    default void setRangeGridlinePaint(XYPlot xyPlot, Paint paint) {
        xyPlot.setRangeGridlinePaint(paint);
    }


}
