package pdk.util.chart;

import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.VerticalAlignment;
import org.jfree.chart.util.UnitType;
import org.jfree.data.category.CategoryDataset;
import pdk.util.chart.util.Data;
import pdk.util.chart.util.LegendTitleBuilder;
import pdk.util.chart.util.TextTitleBuilder;

import java.awt.*;

/**
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 19 May 2026, 4:47 PM
 */
public class AreaChartDemo1 {
    static void main() {
        CategoryDataset dataset = Data.categoryDataset()
                .addSeries("Series 1",
                        new String[]{"Type 1", "Type 2", "Type 3", "Type 4", "Type 5", "Type 6", "Type 7", "Type 8"},
                        new double[]{1.0, 4.0, 3.0, 5.0, 5.0, 7.0, 7.0, 8.0})
                .addSeries("Series 2",
                        new String[]{"Type 1", "Type 2", "Type 3", "Type 4", "Type 5", "Type 6", "Type 7", "Type 8"},
                        new double[]{5.0, 7.0, 6.0, 8.0, 4.0, 4.0, 2.0, 1.0})
                .addSeries("Series 3",
                        new String[]{"Type 1", "Type 2", "Type 3", "Type 4", "Type 5", "Type 6", "Type 7", "Type 8"},
                        new double[]{4.0, 3.0, 2.0, 3.0, 6.0, 3.0, 4.0, 3.0})
                .build();

        CategoryAreaChart chart = CategoryAreaChart.create()
                .dataset(dataset)
                .title("Area Chart")
                .xAxisName("Category")
                .yAxisName("Value")
                .orientation(PlotOrientation.VERTICAL)
                .addLegend(true)
                .addTooltips(true)
                .addSubtitle(TextTitleBuilder.create("An area chart demonstration.  We use this subtitle as an "
                                + "example of what happens when you get a really long title or "
                                + "subtitle.")
                        .font(new Font("SansSerif", Font.PLAIN, 12))
                        .position(RectangleEdge.TOP)
                        .padding(new RectangleInsets(UnitType.RELATIVE, 0.05, 0.05, 0.05, 0.05))
                        .verticalAlignment(VerticalAlignment.BOTTOM)
                        .build())
                .backgroundPaint(Color.WHITE)
                .foregroundAlpha(0.5F)
                .build();

        chart.show();
    }
}
