package pdk.util.chart.util;

import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.ui.RectangleAnchor;
import org.jspecify.annotations.NonNull;
import pdk.util.IBuilder;

import java.awt.*;

/**
 * Builder class for {@link CrosshairOverlay}.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Apr 2026, 10:23 AM
 */
public class CrosshairOverlayBuilder implements IBuilder<CrosshairOverlay> {

    public static CrosshairOverlayBuilder create() {
        return new CrosshairOverlayBuilder();
    }

    private final CrosshairOverlay crosshairOverlay;

    public CrosshairOverlayBuilder() {
        crosshairOverlay = new CrosshairOverlay();
    }

    /**
     * Adds a crosshair against the domain axis (x-axis).
     *
     * @param crosshair the crosshair.
     */
    public CrosshairOverlayBuilder addDomainCrosshair(@NonNull Crosshair crosshair) {
        crosshairOverlay.addDomainCrosshair(crosshair);
        return this;
    }

    /**
     * Adds a crosshair against the domain axis (x-axis).
     *
     * @param value  the value
     * @param paint  the line paint
     * @param stroke the line stroke
     */
    public CrosshairOverlayBuilder addDomainCrosshair(double value, @NonNull Paint paint, @NonNull Stroke stroke) {
        crosshairOverlay.addDomainCrosshair(new Crosshair(value, paint, stroke));
        return this;
    }

    /**
     * Sets the flag that controls whether a label is drawn for the
     * crosshair.
     *
     * @param index   index of the crosshair.
     * @param visible the new flag value.
     */
    public CrosshairOverlayBuilder setDomainLabelVisible(int index, boolean visible) {
        crosshairOverlay.getDomainCrosshairs().get(index).setVisible(visible);
        return this;
    }

    /**
     * Sets the label anchor point.
     *
     * @param anchor the anchor.
     */
    public CrosshairOverlayBuilder setDomainLabelAnchor(int index, @NonNull RectangleAnchor anchor) {
        crosshairOverlay.getDomainCrosshairs().get(index).setLabelAnchor(anchor);
        return this;
    }


    /**
     * Adds a crosshair against the range axis.
     *
     * @param crosshair the crosshair.
     */
    public CrosshairOverlayBuilder addRangeCrosshair(@NonNull Crosshair crosshair) {
        crosshairOverlay.addRangeCrosshair(crosshair);
        return this;
    }

    /**
     * Adds a crosshair against the range axis.
     *
     * @param value  the value
     * @param paint  the line paint
     * @param stroke the line stroke
     */
    public CrosshairOverlayBuilder addRangeCrosshair(double value, @NonNull Paint paint, @NonNull Stroke stroke) {
        crosshairOverlay.addRangeCrosshair(new Crosshair(value, paint, stroke));
        return this;
    }

    /**
     * Sets the flag that controls whether a label is drawn for the
     * crosshair.
     *
     * @param index   index of the crosshair.
     * @param visible the new flag value.
     */
    public CrosshairOverlayBuilder setRangeLabelVisible(int index, boolean visible) {
        crosshairOverlay.getRangeCrosshairs().get(index).setVisible(visible);
        return this;
    }

    /**
     * Sets the label anchor point.
     *
     * @param anchor the anchor.
     */
    public CrosshairOverlayBuilder setRangeLabelAnchor(int index, @NonNull RectangleAnchor anchor) {
        crosshairOverlay.getRangeCrosshairs().get(index).setLabelAnchor(anchor);
        return this;
    }

    @Override
    public CrosshairOverlay build() {
        return crosshairOverlay;
    }
}
