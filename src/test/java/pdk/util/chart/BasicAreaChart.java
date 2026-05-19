package pdk.util.chart;

import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.CategoryDataset;
import pdk.util.chart.util.Data;

/**
 * https://echarts.apache.org/examples/en/editor.html?c=area-basic
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 19 May 2026, 5:18 PM
 */
public class BasicAreaChart {
    static void main() {
        CategoryDataset dataset = Data.categoryDataset()
                .addSeries("",
                        new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"},
                        new double[]{820, 932, 901, 934, 1290, 1330, 1320})
                .build();
        CategoryAreaChart chart = CategoryAreaChart.create()
                .dataset(dataset)
                .domainAxisUpperMargin(0.0)
                .domainAxisLowerMargin(0.0)
                .axisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0))
                .build();
        chart.show();
    }
}
