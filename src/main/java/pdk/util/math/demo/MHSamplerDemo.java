package pdk.util.math.demo;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.statistics.distribution.NormalDistribution;
import org.hipparchus.random.RandomDataGenerator;
import pdk.chart.data.xy.XYSeries;
import pdk.chart.data.xy.XYSeriesCollection;
import pdk.chart.fluent.XYChart;
import pdk.chart.fluent.XYChartType;
import pdk.util.math.SamplingUtils;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 31 Mar 2026, 7:08 PM
 */
public class MHSamplerDemo {
    static void main() {

        // 目标分布
        NormalDistribution gaussian = NormalDistribution.of(3, 2);

        RandomDataGenerator rng = new RandomDataGenerator();
        UniformRandomProvider uniformRng = SamplingUtils.rng;
        int T = 5000;
        double[] pi = new double[T]; // 采样 5000 个点（包括预热阶段）
        double sigma = 1; // 状态转移高斯分布的方差
        int t = 0;
        while (t < T - 1) {
            t++;

            // Q(x|x_t): 正态分布，位置为 pi[t-1]，随机抽样，得到下一个样本
            double piStar = rng.nextNormal(pi[t - 1], sigma);
            // Q 分布对称，采用简化公式
            double alpha = Math.min(1, gaussian.density(piStar) / gaussian.density(pi[t - 1]));
            double u = uniformRng.nextDouble();
            if (u < alpha) {
                pi[t] = piStar;
            } else {
                pi[t] = pi[t - 1];
            }
        }

        pdk.chart.data.statistics.HistogramDataset dataset1 = new pdk.chart.data.statistics.HistogramDataset();
        dataset1.addSeries("", pi, 50);
        dataset1.setType(pdk.chart.data.statistics.HistogramType.SCALE_AREA_TO_1);

        double[] yValues = new double[T];
        for (int i = 0; i < T; i++) {
            yValues[i] = gaussian.density(pi[i]);
        }

        XYSeries<String> taget = new XYSeries<>("Target", pi, yValues);
        XYSeriesCollection<String> dataset2 = new XYSeriesCollection<>(taget);

        XYChart.create()
                .dataset(dataset2, XYChartType.SCATTER)
                .addDataset(dataset1, XYChartType.HISTOGRAM)
                .showLegend(true).show();
    }
}
