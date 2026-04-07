package pdk.util.bayesian;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import pdk.util.data.Point2D;
import pdk.util.data.WeightPoint2D;
import pdk.util.tuple.Tuple;
import pdk.util.tuple.Tuple2;

import java.util.Arrays;

/**
 * A generic framework for a model.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 02 Apr 2026, 6:15 PM
 */
public abstract class Model {

    /**
     * Calculates the 95% highest density interval, e.g. the smallest range that contains the specified percentage of data.
     *
     * @param data An array containing MCMC samples from the posterior distribution.
     * @return HDI (highest density interval)
     */
    public static Tuple2<Double, Double> getHighestDensityInterval(double[] data) {
        return getHighestDensityInterval(data, 0.95);
    }

    /**
     * Calculates the n% highest density interval, e.g. the smallest range that contains the specified percentage of data.
     * n=0.95 means 95% highest density interval.
     *
     * @param data An array containing MCMC samples from the posterior distribution.
     * @param n    data number ratio
     * @return HDI (highest density interval)
     */
    public static Tuple2<Double, Double> getHighestDensityInterval(double[] data, double n) {
        Arrays.sort(data);

        // Determine the number of sample points that the interval should contain
        int hdiPoints = (int) Math.floor(data.length * n);

        // Initialize to the full range of the sample (the widest interval), which will be gradually narrowed down subsequently.
        double lowRange = data[0];
        double highRange = data[data.length - 1];

        // The end index of the window is i+hdiPoints.
        // The number of sample points contained within the window is therefore:(i+hdiPoints)−i+1=hdiPoints+1
        // This implies that the actual coverage probability is approximately (hdiPoints+1)/N, which is usually slightly higher than 95%.

        for (int i = 0; i < data.length - hdiPoints; i++) {
            double width = data[i + hdiPoints] - data[i];
            if (width < (highRange - lowRange)) {
                lowRange = data[i];
                highRange = data[i + hdiPoints];
            }
        }
        return Tuple.of(lowRange, highRange);
    }


    protected Parameter[] modelParameters;

    public Model() {}

    /**
     * Calculate the probability of the model, given a single data point.
     *
     * @param proposalParams proposal parameters
     * @param data           a {@link Point2D}
     * @return probability of the model
     */
    public abstract double probability(double[] proposalParams, WeightPoint2D data);

    /**
     * Calculates the log probability of the prior plus the log probability of the model, given the data
     *
     * @param proposalParams proposal parameters
     * @param data           data
     * @return posterior probability
     */
    public double logPosteriorProbability(double[] proposalParams, WeightPoint2D[] data) {
        return logPriorProbability(proposalParams) + logProbability(proposalParams, data);
    }

    /**
     * Calculates the log probability of the model with the proposed parameters and data (likelihood).
     *
     * @param proposalParams proposed parameters
     * @param data           data
     * @return log probability
     */
    public double logProbability(double[] proposalParams, WeightPoint2D[] data) {
        double logP = 0;
        for (WeightPoint2D datum : data) {
            double prob = probability(proposalParams, datum);
            logP += Math.log(prob);
        }
        return logP;
    }

    /**
     * Calculate the log prior probability of a set of parameters.
     *
     * @param proposalParams proposal parameters
     * @return log prior probability
     */
    public double logPriorProbability(double[] proposalParams) {
        // 概率的 log 值相加，等价于概率相乘，用于计算多个参数的组合概率
        double logP = 0;
        for (int p = 0; p < proposalParams.length; p++) {
            double priorProb = modelParameters[p].priorDistribution_.density(proposalParams[p]);
            logP += Math.log(priorProb);
        }
        return logP;
    }

    protected RangeSet<Double> createRange(double start, double end) {
        RangeSet<Double> rangeSet = TreeRangeSet.create();
        rangeSet.add(Range.closed(start, end));
        return rangeSet;
    }
}
