package pdk.util.data.func;

import pdk.util.chart.LineChart;

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
        LineChart chart = func2D.show(-40, 40, 400);
        chart.xAxisRange(-2, 2)
                .yAxisRange(0, 5);
        chart.show();
    }
}
