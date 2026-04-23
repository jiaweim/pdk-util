package pdk.util.chart;

import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 23 Apr 2026, 10:52 AM
 */
public class CategoryBarChartTest {

    static void main() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String series1 = "Males";
        String series2 = "Females";

        String category1 = "18 to 39";
        String category2 = "40 - 59";
        String category3 = "60 and over";

        dataset.addValue(5.5, series1, category1);
        dataset.addValue(10.3, series2, category1);
        dataset.addValue(8.4, series1, category2);
        dataset.addValue(20.1, series2, category2);
        dataset.addValue(12.8, series1, category3);
        dataset.addValue(24.3, series2, category3);

        CategoryBarChart chart = CategoryBarChart.create()
                .dataset(dataset)
                .title("Antidepressant Medication Usage")
                .xAxisName("Age Category")
                .yAxisName("Percent")
                .showDomainGridlines(true)
                .showRangeGridlines(true)
                .categoryMargin(0.5)
                .itemMargin(0.0)
                .build();
        chart.show();
    }
}