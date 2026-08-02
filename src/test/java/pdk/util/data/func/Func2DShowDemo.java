package pdk.util.data.func;

import pdk.chart.LineChart;
import pdk.chart.axis.NumberAxis;

import java.awt.*;

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
        NumberAxis yAxis = chart.getRangeAxisAsNumber();
        yAxis.setRange(0, 40);
        chart.setSeriesStroke(0, new BasicStroke(2f));
        chart.show();
    }
}
