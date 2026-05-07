package pdk.util.chart;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import java.awt.*;
import java.util.List;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 07 May 2026, 1:15 PM
 */
public class BoxAndWhiskerChartDemo1 {
    static void main() {
        int SERIES_COUNT = 3;
        int CATEGORY_COUNT = 5;
        int VALUE_COUNT = 20;
        DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
        for (int s = 0; s < SERIES_COUNT; s++) {
            for (int c = 0; c < CATEGORY_COUNT; c++) {
                List<Double> values = new java.util.ArrayList<>();
                for (int i = 0; i < VALUE_COUNT; i++) {
                    double v = 0 + (Math.random() * (20.0 - 0.0));
                    values.add(v);
                }

                dataset.add(values, "Series " + s, "Category " + c);
            }
        }

        BoxWhiskerChart chart = BoxWhiskerChart.create()
                .dataset(dataset)
                .title("Box and Whisker Chart Demo 1")
                .xAxisName("Category")
                .yAxisName("Value")
                .addLegend(true)
                .backgroundPaint(Color.WHITE)
                .plotBackgroundPaint(Color.LIGHT_GRAY)
                .domainGridlinePaint(Color.WHITE)
                .domainGridlinesVisible(true)
                .rangeGridlinePaint(Color.WHITE)
                .rangeAxisStandardTickUnits(NumberAxis.createIntegerTickUnits())
                .build();
        chart.show();
    }
}
