package pdk.util.chart;

import org.jfree.data.category.CategoryDataset;
import pdk.util.chart.util.Data;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 19 May 2026, 5:41 PM
 */
public class BasicRadarChart {
    static void main() {
        String cat1 = "Sales";
        String cat2 = "Administration";
        String cat3 = "Information Technology";
        String cat4 = "Customer Support";
        String cat5 = "Development";
        String cat6 = "Marketing";
        String[] categories = {cat1, cat2, cat3, cat4, cat5, cat6};

        double maxSales = 6500;
        double maxAdministration = 16000;
        double maxInformationTechnology = 30000;
        double maxCustomerSupport = 38000;
        double maxDevelopment = 52000;
        double maxMarketing = 25000;

        CategoryDataset dataset = Data.categoryDataset()
                .addSeries("Allocated Budget", categories,
                        new double[]{4200 / maxSales * 100, 3000 / maxAdministration * 100, 20000 / maxInformationTechnology * 100, 35000 / maxCustomerSupport * 100, 50000 / maxDevelopment * 100, 18000 / maxMarketing * 100})
                .addSeries("Actual Spending", categories,
                        new double[]{5000 / maxSales * 100, 14000 / maxAdministration * 100, 28000 / maxInformationTechnology * 100, 26000 / maxCustomerSupport * 100, 42000 / maxDevelopment * 100, 21000 / maxMarketing * 100})
                .build();
        RadarChart chart = RadarChart.create()
                .dataset(dataset)
                .maxValue(100)
                .build();

        chart.show();
    }
}
