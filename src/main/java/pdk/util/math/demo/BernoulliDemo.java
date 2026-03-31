package pdk.util.math.demo;

import org.apache.commons.statistics.distribution.BetaDistribution;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import pdk.util.ArrayUtils;
import pdk.util.chart.ChartUtils;
import pdk.util.math.BernoulliSampler;
import pdk.util.math.SamplingUtils;
import pdk.util.math.StatUtils;

/**
 * Below we plot a sequence of updating posterior probabilities as we observe increasing
 * amounts of data (coin flips).
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 31 Mar 2026, 1:57 PM
 */
public class BernoulliDemo {
    static void main() {
        double[] xValues = ArrayUtils.linspace(0, 1, 100);

        int n_trails = 0;
        BernoulliSampler sampler = BernoulliSampler.of(SamplingUtils.rng, 0.5);
        int[] samples = sampler.samples(n_trails).toArray();
        int heads = StatUtils.sum(samples);

        BetaDistribution betaDistribution = BetaDistribution.of(1 + heads, 1 + n_trails - heads);
        double[] yValues = new double[xValues.length];
        for (int i = 0; i < xValues.length; i++) {
            yValues[i] = betaDistribution.density(xValues[i]);
        }

        JFreeChart chart = ChartUtils.createXYLineChart("Bayesian posterior probabilities", "probability of heads", "",
                xValues, yValues, PlotOrientation.VERTICAL, false, false, false);
        ChartUtils.showChart(chart);
    }
}
