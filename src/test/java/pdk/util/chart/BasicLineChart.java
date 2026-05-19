package pdk.util.chart;

import org.jfree.data.category.CategoryDataset;
import pdk.util.chart.util.Data;
import pdk.util.chart.util.ShapeUtils;

import java.awt.*;

/**
 * https://echarts.apache.org/examples/en/editor.html?c=line-simple
 * <p>
 * 不是完全实现，无法避免直线从圆中穿过。
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 19 May 2026, 2:38 PM
 */
public class BasicLineChart {
    static void main() {
        CategoryDataset dataset = Data.categoryDataset()
                .addSeries("Category",
                        new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"},
                        new double[]{150, 230, 224, 218, 135, 147, 260}).build();
        CategoryLineChart chart = CategoryLineChart.create()
                .dataset(dataset)
                .seriesShape(0, ShapeUtils.createCircle(6))
                .seriesShapesFilled(0, false)
                .seriesLinesWidth(0, 2F)
                .seriesOutlineStroke(0, new BasicStroke(2F))
                .addLegend(false)
                .build();
        chart.show();
    }
}
