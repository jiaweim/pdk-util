package pdk.util.math.distribution;

import org.apache.commons.numbers.combinatorics.LogBinomialCoefficient;
import org.apache.commons.numbers.gamma.LogBeta;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.sampling.distribution.InverseTransformDiscreteSampler;
import org.apache.commons.statistics.distribution.BetaDistribution;
import org.apache.commons.statistics.distribution.DiscreteDistribution;

/**
 * Beta-Binomial Distribution implementation.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 02 Apr 2026, 5:22 PM
 */
public class BetaBinomial implements DiscreteDistribution {

    public static BetaBinomial of(int numberOfTrials, double alpha, double beta) {
        return new BetaBinomial(numberOfTrials, alpha, beta);
    }

    /**
     * number of trails
     */
    private final int numberOfTrials_;
    /**
     * shape parameter of beta
     */
    private final double alpha_;
    /**
     * shape parameter of beta
     */
    private final double beta_;

    private BetaDistribution betaDistribution;

    private double logBetaAlphaBeta;

    public BetaBinomial(int numberOfTrials, double alpha, double beta) {
        this.numberOfTrials_ = numberOfTrials;
        this.alpha_ = alpha;
        this.beta_ = beta;
        this.betaDistribution = BetaDistribution.of(alpha, beta);
        this.logBetaAlphaBeta = LogBeta.value(alpha, beta);
    }

    @Override
    public double probability(int x) {
        if (x < 0 || x > numberOfTrials_) {
            return 0.0;
        }
        double logPMF = LogBinomialCoefficient.value(numberOfTrials_, x)
                + LogBeta.value(x + alpha_, numberOfTrials_ - x + beta_)
                - logBetaAlphaBeta;

        return Math.exp(logPMF);
    }

    @Override
    public double cumulativeProbability(int x) {
        if (x < 0) {
            return 0.0;
        }
        if (x >= numberOfTrials_) {
            return 1.0;
        }
        double sum = 0.0;
        for (int i = 0; i <= x; i++) {
            sum += probability(i);
        }
        return Math.min(sum, 1.0);
    }

    @Override
    public int inverseCumulativeProbability(double p) {
        if (p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException(String.valueOf(p));
        }
        if (p == 0.0) {
            return 0;
        }
        if (p == 1.0) {
            return numberOfTrials_;
        }

        double cumulative = 0.0;
        for (int k = 0; k <= numberOfTrials_; k++) {
            cumulative += probability(k);
            if (cumulative >= p) {
                return k;
            }
        }
        // 理论上应永远不会执行到这里（因为累加到 n 时 cumulative = 1）
        return numberOfTrials_;
    }

    @Override
    public double getMean() {
        return numberOfTrials_ * alpha_ / (alpha_ + beta_);
    }

    @Override
    public double getVariance() {
        double alphaBeta = alpha_ + beta_;
        double numerator = numberOfTrials_ * alpha_ * beta_ * (alphaBeta + numberOfTrials_);
        double denominator = alphaBeta * alphaBeta * (alphaBeta + 1);

        return numerator / denominator;
    }

    @Override
    public int getSupportLowerBound() {
        return 0;
    }

    @Override
    public int getSupportUpperBound() {
        return numberOfTrials_;
    }

    @Override
    public Sampler createSampler(UniformRandomProvider rng) {
        return InverseTransformDiscreteSampler.of(rng, this::inverseCumulativeProbability)::sample;
    }
}
