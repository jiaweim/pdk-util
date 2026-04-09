package pdk.util.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 09 Apr 2026, 11:11 AM
 */
public class LineChartDemo1 {
    /**
     * Creates a sample dataset.
     *
     * @return The dataset.
     */
    private static CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(212.0, "Classes", "JDK 1.0");
        dataset.addValue(504.0, "Classes", "JDK 1.1");
        dataset.addValue(1520.0, "Classes", "JDK 1.2");
        dataset.addValue(1842.0, "Classes", "JDK 1.3");
        dataset.addValue(2991.0, "Classes", "JDK 1.4");
        dataset.addValue(3500.0, "Classes", "JDK 1.5");
        dataset.addValue(3793.0, "Classes", "JDK 1.6");
        dataset.addValue(4024.0, "Classes", "JDK 1.7");
        dataset.addValue(4240.0, "Classes", "JDK 8");
        dataset.addValue(6005.0, "Classes", "JDK 9");
        dataset.addValue(6002.0, "Classes", "JDK 10");
        dataset.addValue(4411.0, "Classes", "JDK 11");
        dataset.addValue(4433.0, "Classes", "JDK 12");
        dataset.addValue(4545.0, "Classes", "JDK 13");
        dataset.addValue(4569.0, "Classes", "JDK 14");
        return dataset;
    }

    static void main() {
        JFreeChart chart = CategoryChartBuilder.create()
                .addDataset(createDataset(), CategoryChartType.LINE_SHAPE)
                .title("Java Standard Class Library")
                .categoryAxisName(null)
                .categoryLabelPositions(CategoryLabelPositions.UP_90)
                .valueAxisName("Class Count")
                .addLegend(false)
                .addTooltips(true)
                .orientation(PlotOrientation.VERTICAL)
                .build();
        ChartUtils.showChart(chart, 768, 512);
    }
}
