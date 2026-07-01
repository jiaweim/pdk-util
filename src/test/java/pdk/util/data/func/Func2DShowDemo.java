package pdk.util.data.func;

import pdk.chart.Chart;
import pdk.chart.plot.XYPlot;

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

        Chart chart = func2D.show(-40, 40, 400);
        XYPlot plot = chart.getXYPlot();
        plot.getRangeAxisAsNumber()
                .range(0, 40);
        plot.getLineAndShapeRenderer()
                .seriesLineWidth(0, 2f);
        chart.show();
    }
}
