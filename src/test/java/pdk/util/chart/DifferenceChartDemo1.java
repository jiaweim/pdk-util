package pdk.util.chart;

import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.XYDataset;
import pdk.util.chart.util.Data;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 30 Apr 2026, 6:24 PM
 */
public class DifferenceChartDemo1 {
    static void main() {

        TimeSeries series1 = new TimeSeries("Random 1");
        TimeSeries series2 = new TimeSeries("Random 2");
        double value1 = 0.0;
        double value2 = 0.0;
        Day day = new Day();
        for (int i = 0; i < 200; i++) {
            value1 = value1 + Math.random() - 0.5;
            value2 = value2 + Math.random() - 0.5;
            series1.add(day, value1);
            series2.add(day, value2);
            day = (Day) day.next();
        }

        XYDataset dataset = Data.timeDataset()
                .addSeries(series1)
                .addSeries(series2)
                .build();

    }
}
