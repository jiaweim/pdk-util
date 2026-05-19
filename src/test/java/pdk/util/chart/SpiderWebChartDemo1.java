package pdk.util.chart;

import org.jfree.data.category.CategoryDataset;
import pdk.util.chart.util.Data;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 19 May 2026, 5:34 PM
 */
public class SpiderWebChartDemo1 {
    static void main() {
        String category1 = "Category 1";
        String category2 = "Category 2";
        String category3 = "Category 3";
        String category4 = "Category 4";
        String category5 = "Category 5";
        String[] categories = {category1, category2, category3, category4, category5};
        CategoryDataset dataset = Data.categoryDataset()
                .addSeries("First", categories, new double[]{1.0, 4.0, 3.0, 5.0, 5.0})
                .addSeries("Second", categories, new double[]{5.0, 7.0, 6.0, 8.0, 4.0,})
                .addSeries("Third", categories, new double[]{4.0, 3.0, 2.0, 3.0, 6.0})
                .build();
        RadarChart chart = RadarChart.create()
                .dataset(dataset)
                .interiorGap(0.2)
                .build();
        chart.show();
    }
}
