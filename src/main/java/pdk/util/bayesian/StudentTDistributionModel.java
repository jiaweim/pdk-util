package pdk.util.bayesian;

import org.apache.commons.statistics.distribution.ExponentialDistribution;
import org.apache.commons.statistics.distribution.NormalDistribution;
import org.apache.commons.statistics.distribution.UniformContinuousDistribution;
import pdk.util.data.WeightPoint2D;
import pdk.util.math.DistributionUtils;

/**
 * This class models the given datapoint(s) with a Student's t distribution model. The Student's t
 * distribution has 3 parameters: mu (mean), sigma (standard deviation), and nu (the degree of normality, or
 * "degrees of freedom").
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 03 Apr 2026, 10:44 AM
 */
public class StudentTDistributionModel extends Model {

    /**
     * Create a
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
                ExponentialDistribution.of(priorNuExponent),
                createRange(0, Double.MAX_VALUE),
                nuInitialGuess
        );
    }

    @Override
    public double probability(double[] proposalParams, WeightPoint2D data) {
        return Math.pow(DistributionUtils.getTDistributionPDF(
                        proposalParams[0],
                        proposalParams[1],
                        proposalParams[2],
                        data.getX()),
                data.getWeight());
    }
}
