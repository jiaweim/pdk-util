package pdk.util.chart;

import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

/**
 * XYChart types.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 01 Apr 2026, 1:05 PM
 */
public enum XYChartType {
    SCATTER,
    LINE,
    AREA,
    BAR,
    HISTOGRAM;

    public XYItemRenderer getRenderer() {
        switch (this) {
            case LINE:
                return new XYLineAndShapeRenderer(true, false);
            case BAR:
                return new XYBarRenderer();
            default:
                return null;
        }
    }
}
