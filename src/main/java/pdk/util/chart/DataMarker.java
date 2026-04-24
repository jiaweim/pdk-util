package pdk.util.chart;

import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.TextAnchor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import pdk.util.IBuilder;

import java.awt.*;

/**
 * Markers that can be added to plots to highlight a value
 * or range of values.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 24 Apr 2026, 2:49 PM
 */
public class DataMarker implements IBuilder<DataMarker> {

    public static DataMarker value(double value) {
        return new DataMarker(value);
    }

    public static DataMarker range(double start, double end) {
        return new DataMarker(start, end);
    }

    private final Marker marker_;

    private DataMarker(double value) {
        this.marker_ = new ValueMarker(value);
    }

    private DataMarker(double start, double end) {
        this.marker_ = new IntervalMarker(start, end);
    }

    /**
     * Sets the label (if {@code null} no label is displayed).
     *
     * @param label the label
     */
    public DataMarker label(@Nullable String label) {
        this.marker_.setLabel(label);
        return this;
    }

    /**
     * Sets the label font.
     *
     * @param font the font.
     */
    public DataMarker labelFont(@NonNull Font font) {
        this.marker_.setLabelFont(font);
        return this;
    }


    /**
     * Sets the label anchor.
     * <p>
     * The anchor defines the position of the label
     * anchor, relative to the bounds of the marker.
     *
     * @param anchor the anchor
     */
    public DataMarker labelAnchor(@NonNull RectangleAnchor anchor) {
        this.marker_.setLabelAnchor(anchor);
        return this;
    }

    /**
     * Sets the label text anchor.
     *
     * @param anchor the label text anchor.
     */
    public DataMarker labelTextAnchor(@NonNull TextAnchor anchor) {
        marker_.setLabelTextAnchor(anchor);
        return this;
    }

    /**
     * Sets the paint.
     *
     * @param paint the paint.
     */
    public DataMarker paint(@NonNull Paint paint) {
        this.marker_.setPaint(paint);
        return this;
    }

    /**
     * Return the underlying marker
     *
     * @return {@link Marker}
     */
    public Marker getMarker() {
        return this.marker_;
    }

    @Override
    public DataMarker build() {
        return this;
    }
}
