package pdk.util.bayesian;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import org.apache.commons.statistics.distribution.ExponentialDistribution;
import org.apache.commons.statistics.distribution.NormalDistribution;
import org.apache.commons.statistics.distribution.UniformContinuousDistribution;
import pdk.util.ArrayUtils;
import pdk.util.data.WeightPoint2D;
import pdk.util.math.DistributionUtils;
import pdk.util.math.StatUtils;

import java.util.List;

/**
 * This class models the given data point(s) with a Student's t distribution model. The Student's t
 * distribution has 3 parameters: mu (mean), sigma (standard deviation), and nu (the degree of normality, or
 * "degrees of freedom").
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 03 Apr 2026, 10:44 AM
 */
public class StudentTDistributionModel extends Model {

    public static DoubleList meanDiff(List<double[]> chain1, List<double[]> chain2) {
        DoubleList muList = new DoubleArrayList(chain1.size());
        for (int i = 0; i < chain1.size(); i++) {
            muList.add(chain1.get(i)[0] - chain2.get(i)[0]);
        }
        return muList;
    }

    /**
     * Obtain all the mean values in the Markov chain.
     *
     * @param chain Markov chain
     * @return mean values
     */
    public static double[] getMeans(List<double[]> chain) {
        DoubleList muList = new DoubleArrayList(chain.size());
        for (double[] doubles : chain) {
            muList.add(doubles[0]);
        }
        return muList.toDoubleArray();
    }

    /**
     * Obtain all the standard deviation values in the Markov chain.
     *
     * @param chain Markov chain
     * @return standard deviation values
     */
    public static double[] getSDs(List<double[]> chain) {
        DoubleList sdList = new DoubleArrayList(chain.size());
        for (double[] doubles : chain) {
            sdList.add(doubles[1]);
        }
        return sdList.toDoubleArray();
    }

    /**
     * Create a {@link StudentTDistributionModel}
     *
     * @param nuInitialGuess initial value of parameter nu
     */
    public StudentTDistributionModel(double[] y1, double[] y2, double nuInitialGuess) {
        double[] data = ArrayUtils.concat(y1, y2);
        double pooledSd = StatUtils.standardDeviation(data);

        // 所有数据的整体均值
        double pooledMean = StatUtils.mean(data);
        // 极大（乘以 10⁶），使均值先验近乎平坦（无信息）。
        double sdMu = pooledSd * 1000000;

        // 均匀分布的边界，范围极宽（从千分之一到千倍的数据标准差），确保先验对合理的 σ 值几乎无影响。
        double sigmaLow = pooledSd / 1000;
        double sigmaHigh = pooledSd * 1000;

        modelParameters = new Parameter[3];
        // mean
        modelParameters[0] = new Parameter(
                NormalDistribution.of(pooledMean, sdMu),
                createRange(Double.MIN_VALUE, Double.MAX_VALUE),
                pooledMean
        );
        // sigma, Uniform distribution
        modelParameters[1] = new Parameter(
                UniformContinuousDistribution.of(sigmaLow, sigmaHigh),
                createRange(0, Double.MAX_VALUE),
                pooledSd
        );
        // nu, exponential distribution
        modelParameters[2] = new Parameter(
                ExponentialDistribution.of(29.0), // 常用在 BEST 模型中：让 ν 偏向 30 左右（接近正态），但允许厚尾（ν 较小时也有非零概率）
                createRange(0, Double.MAX_VALUE),
                nuInitialGuess
        );
    }


    /**
     * Create a {@link StudentTDistributionModel}
     *
     * @param priorMuMean     prior mean of the μ parameter
     * @param priorMuSd       prior standard deviation of the μ parameter
     * @param muInitialGuess  Initial value of parameter μ
     * @param priorSdStart    Prior minimum of parameter σ
     * @param priorSdEnd      Prior maximum of parameter σ
     * @param sdInitialGuess  Initial value of parameter σ
     * @param priorNuExponent The degrees of freedom are sampled from an exponential distribution, where this
     *                        represents the mean of the exponential distribution.
     * @param nuInitialGuess  initial value of paramter nu
     */
    public StudentTDistributionModel(double priorMuMean, double priorMuSd, double muInitialGuess,
            double priorSdStart, double priorSdEnd, double sdInitialGuess, double priorNuExponent, double nuInitialGuess) {
        modelParameters = new Parameter[3];
        // mean
        modelParameters[0] = new Parameter(
                NormalDistribution.of(priorMuMean, priorMuSd),
                createRange(Double.MIN_VALUE, Double.MAX_VALUE),
                muInitialGuess
        );
        // sigma, Uniform distribution
        modelParameters[1] = new Parameter(
                UniformContinuousDistribution.of(priorSdStart, priorSdEnd),
                createRange(0, Double.MAX_VALUE),
                sdInitialGuess
        );
        // nu, exponential distribution
        modelParameters[2] = new Parameter(
                ExponentialDistribution.of(1 / priorNuExponent),
                createRange(0, Double.MAX_VALUE),
                nuInitialGuess
        );
    }

    @Override
    public double probability(double[] proposalParams, WeightPoint2D data) {
        return Math.pow(DistributionUtils.getStudentTPDF(
                        proposalParams[0],
                        proposalParams[1],
                        proposalParams[2],
                        data.getX()),
                data.getWeight());
    }
}
