package pdk.util.chart;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 23 Apr 2026, 1:21 PM
 */
public class ScatterPlotDemo3 {
    static void main() {
        LineChart chart = LineChart.chart()
                .dataset(ScatterPlotDemo1.create(4, 40))
                .title("Scatter Plot Demo 3")
                .addLegend(true)
                .addTooltips(true)
                .showLine(false)
                .showShape(true)
                .xAxisName("X")
                .yAxisName("Y")
                .xAxisIncludesZero(false)
                .build();
        chart.show();
    }
}
