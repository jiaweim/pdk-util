package pdk.util.math.demo;

import org.apache.commons.statistics.distribution.PoissonDistribution;
import pdk.chart.BarChartCategory;
import pdk.chart.block.BlockBorder;
import pdk.chart.data.category.DefaultCategoryDataset;

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

        DefaultCategoryDataset<String, String> dataset = new DefaultCategoryDataset<>();
        for (int i = 0; i < a.length; i++) {
            double probability0 = p0.probability(i);
            double probability1 = p1.probability(i);
            dataset.addValue(probability0, series1, String.valueOf(i));
            dataset.addValue(probability1, series2, String.valueOf(i));
        }

        BarChartCategory chart = new BarChartCategory();
        chart.dataset(dataset)
                .axisName("k", "probability of k")
                .seriesPaint(0, colors[0])
                .seriesPaint(1, colors[1]);

        pdk.chart.legend.LegendTitle legendTitle = new pdk.chart.legend.LegendTitle(chart.getPlot());
        legendTitle.setPosition(pdk.chart.api.RectangleEdge.RIGHT);
        legendTitle.setBackgroundPaint(new Color(255, 255, 224, 200));// 半透明背景，避免遮挡
        legendTitle.setFrame(new BlockBorder(Color.LIGHT_GRAY)); // 添加边框
        legendTitle.setItemFont(new Font("宋体", Font.PLAIN, 12)); // 设置字体
        chart.addSubtitle(0, legendTitle);
        chart.show();


    }
}
