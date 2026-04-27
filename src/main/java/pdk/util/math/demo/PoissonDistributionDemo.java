package pdk.util.math.demo;

import org.apache.commons.statistics.distribution.PoissonDistribution;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.category.CategoryDataset;
import pdk.util.chart.CategoryBarChart;
import pdk.util.chart.Data;
import pdk.util.chart.util.LegendTitleBuilder;

import java.awt.*;
import java.util.stream.IntStream;

/**
 * Show the differences in Poisson distributions with different lambdas.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 31 Mar 2026, 3:38 PM
 */
public class PoissonDistributionDemo {
    static void main() {
        double[] lambdas = new double[]{1.5, 4.25};
        int[] a = IntStream.range(0, 16).toArray();

        Color[] colors = new Color[]{
                new Color(126, 178, 208),
                new Color(194, 99, 119)
        };

        String series1 = "λ=1.5";
        String series2 = "λ=4.25";

        PoissonDistribution p0 = PoissonDistribution.of(lambdas[0]);
        PoissonDistribution p1 = PoissonDistribution.of(lambdas[1]);

        Data.CategoryDatasetBuilder datasetBuilder = Data.categoryDataset();

        for (int i = 0; i < a.length; i++) {
            double probability0 = p0.probability(i);
            double probability1 = p1.probability(i);
            datasetBuilder.add(series1, String.valueOf(i), probability0)
                    .add(series2, String.valueOf(i), probability1);
        }

        CategoryDataset dataset1 = datasetBuilder.build();

        CategoryBarChart chart1 = CategoryBarChart.create()
                .dataset(dataset1)
                .xAxisName("k")
                .yAxisName("probability of k")
                .seriesPaint(0, colors[0])
                .seriesPaint(1, colors[1])
                .build();

        LegendTitle legend = LegendTitleBuilder.create(chart1)
                .position(RectangleEdge.RIGHT)
                .backgroundPaint(new Color(255, 255, 224, 200)) // 半透明背景，避免遮挡
                .frame(new BlockBorder(Color.LIGHT_GRAY)) // 添加边框
                .itemFont(new Font("宋体", Font.PLAIN, 12)) // 设置字体
                .build();
        chart1.legend(legend);
        chart1.show();
    }
}
