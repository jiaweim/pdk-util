package pdk.util.chart.util;

import java.awt.geom.Ellipse2D;

/**
 * Utilities classes for drawing shapes.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 19 May 2026, 2:50 PM
 */
public final class ShapeUtils {

    /**
     * Create a circle of given size
     *
     * @param size Diameter of a circle
     * @return {@link Ellipse2D}
     */
    public static Ellipse2D.Double createCircle(int size) {
        double delta = size / 2.0;

        return new Ellipse2D.Double(-delta, -delta, size, size);
    }
}
