package pdk.util.chart;

import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 09 Apr 2026, 11:11 AM
 */
public class LineChartDemo1 {

    static void main() {
        CategoryDataset dataset = Data.categoryDataset()
                .add("Classes", "JDK 1.0", 212.0)
                .add("Classes", "JDK 1.1", 504.0)
                .add("Classes", "JDK 1.2", 1520.0)
                .add("Classes", "JDK 1.3", 1842.0)
                .add("Classes", "JDK 1.4", 2991.0)
                .add("Classes", "JDK 1.5", 3500.0)
                .add("Classes", "JDK 1.6", 3793.0)
                .add("Classes", "JDK 1.7", 4024.0)
                .add("Classes", "JDK 8", 4240.0)
                .add("Classes", "JDK 9", 6005.0)
                .add("Classes", "JDK 10", 6002.0)
                .add("Classes", "JDK 11", 4411.0)
                .add("Classes", "JDK 12", 4433.0)
                .add("Classes", "JDK 13", 4545.0)
                .add("Classes", "JDK 14", 4569.0)
                .build();

        CategoryLineChart lineChart = CategoryLineChart.chart()
                .dataset(dataset)
                .title("Java Standard Class Library")
                .xAxisName(null)
                .yAxisName("Class Count")
                .categoryLabelPositions(CategoryLabelPositions.UP_90)
                .addLegend(false)
                .addTooltips(true)
                .orientation(PlotOrientation.VERTICAL)
                .build();
        lineChart.show();
    }
}
