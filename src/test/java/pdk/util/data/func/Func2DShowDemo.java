package pdk.util.data.func;

import pdk.chart.fluent.XYChart;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 24 Apr 2026, 12:50 PM
 */
public class Func2DShowDemo {
    static void main() {
        Func2D func2D = x -> x * x + 2;

        XYChart chart = func2D.show(-40, 40, 400);
        chart.domainAxis()
                .range(-40, 40).done()
                .lineAndShapeRenderer(0)
                .seriesLineWidth(0, 2f)
                .done()
                .show();
    }
}
