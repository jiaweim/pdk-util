package pdk.util.math.demo;

import org.apache.commons.statistics.distribution.PoissonDistribution;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.category.DefaultCategoryDataset;
import pdk.util.chart.ChartUtils;

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

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String series1 = "λ=1.5";
        String series2 = "λ=4.25";

        PoissonDistribution p0 = PoissonDistribution.of(lambdas[0]);
        PoissonDistribution p1 = PoissonDistribution.of(lambdas[1]);

        for (int i = 0; i < a.length; i++) {
            double probability0 = p0.probability(i);
            double probability1 = p1.probability(i);
            dataset.addValue(probability0, series1, String.valueOf(i));
            dataset.addValue(probability1, series2, String.valueOf(i));
        }
        JFreeChart chart = ChartUtils.createBarChart("", "k", "probability of k", dataset);
        CategoryPlot plot = chart.getCategoryPlot();

        LegendTitle legend = chart.getLegend();
        legend.setPosition(RectangleEdge.RIGHT);
        legend.setBackgroundPaint(new Color(255, 255, 224, 200)); // 半透明背景，避免遮挡
        legend.setFrame(new BlockBorder(Color.LIGHT_GRAY));        // 添加边框
        legend.setItemFont(new Font("宋体", Font.PLAIN, 12));      // 设置字体

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, colors[0]);
        renderer.setSeriesPaint(1, colors[1]);
        ChartUtils.showChart(chart);
    }
}
