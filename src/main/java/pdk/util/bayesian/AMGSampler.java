package pdk.util.bayesian;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.sampling.distribution.BoxMullerNormalizedGaussianSampler;
import org.apache.commons.rng.simple.RandomSource;
import pdk.util.data.WeightPoint2D;

import java.util.ArrayList;
import java.util.List;

/**
 * This class uses an adaptive Metropolis within Gibbs sampler to estimate the parameters of a model that describes
 * a series of data points.
 * <p>
 * The parameters are estimated by sampling from a normal distribution and then calculating their probability of describing the data.
 * The sampler then adapts to more probable parameter values over n iterations of the algorithm. Eventually, after enough iterations,
 * the algorithm hopefully converges on an accurate answer, with some amount of measureable uncertainty for each parameter.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 02 Apr 2026, 6:01 PM
 */
public class AMGSampler {

    /**
     * An array storing all samples, where each element is a parameter array
     */
    private List<double[]> markovChain_;
    /**
     * Current parameter state
     */
    private double[] currentState_;
    private double[] proposedState_;
    private final UniformRandomProvider rng_;
    private final Model model_;
    private final WeightPoint2D[] data_;
    /**
     * Standard deviation of parameters (log), default to be 0 (std=1)
     */
    private final double[] logSd_;
    /**
     * The number of sampling points for each parameter
     */
    private final double[] acceptanceCount;
    private final int batchSize_;
    private int batchCount_;

    private final BoxMullerNormalizedGaussianSampler sampler_;

    public AMGSampler(double[] data, Model model, Integer seed) {
        this(data, model, seed, 3);
    }

    /**
     *
     * @param data
     * @param model
     * @param seed      seed for random number generator
     * @param batchSize Batch size for adaptive adjustment (adjust the step size after every 50 updates)
     */
    public AMGSampler(double[] data, Model model, Integer seed, int batchSize) {
        this.rng_ = RandomSource.XO_SHI_RO_256_PP.create(seed);
        this.model_ = model;
        this.data_ = new WeightPoint2D[data.length];
        for (int i = 0; i < data.length; i++) {
            data_[i] = new WeightPoint2D(data[i], 0);
        }
        this.batchSize_ = batchSize;
        this.logSd_ = new double[model.modelParameters.length];
        this.acceptanceCount = new double[model.modelParameters.length];
        this.currentState_ = new double[model.modelParameters.length];
        this.proposedState_ = new double[model.modelParameters.length];
        for (int i = 0; i < currentState_.length; i++) {
            currentState_[i] = model.modelParameters[i].initialGuess_;
        }
        this.sampler_ = BoxMullerNormalizedGaussianSampler.of(rng_);
    }


    public AMGSampler(WeightPoint2D[] data, Model model, Integer seed) {
        this(data, model, seed, 3);
    }

    public AMGSampler(WeightPoint2D[] data, Model model, Integer seed, int batchSize) {
        this.rng_ = RandomSource.XO_SHI_RO_256_PP.create(seed);
        this.model_ = model;
        this.data_ = data;
        this.batchSize_ = batchSize;
        this.logSd_ = new double[model.modelParameters.length];
        this.acceptanceCount = new double[model.modelParameters.length];
        this.currentState_ = new double[model.modelParameters.length];
        this.proposedState_ = new double[model.modelParameters.length];
        for (int i = 0; i < currentState_.length; i++) {
            currentState_[i] = model.modelParameters[i].initialGuess_;
        }
        this.sampler_ = BoxMullerNormalizedGaussianSampler.of(rng_);
    }

    /**
     * @return the markov chain after sampling
     */
    public List<double[]> getMarkovChain() {
        return markovChain_;
    }

    /**
     * Burn in and them sample the MC chain.
     *
     * @param burnin number of burin samplings
     * @param n      number of samples
     */
    public void run(int burnin, int n) {
        nSamples(burnin);
        nSamples(n);
    }

    /**
     * Samples the parameter distribution via MCMC.
     *
     * @return a sample
     */
    public double[] nextSample() {
        markovChain_.add(currentState_.clone());

        for (int p = 0; p < model_.modelParameters.length; p++) {
            double parameterProposal = sampler_.sample() * Math.exp(logSd_[p]) + currentState_[p];
            proposedState_ = currentState_.clone();
            proposedState_[p] = parameterProposal;

            double acceptProb = 0;
            if (model_.modelParameters[p].isValid(parameterProposal)) {
                double currentPosteriorProb = model_.logPosteriorProbability(currentState_, data_);
                double proposedPosteriorProb = model_.logPosteriorProbability(proposedState_, data_);
                if (!Double.isFinite(currentPosteriorProb)) {
                    acceptProb = 1;
                } else {
                    acceptProb = Math.exp(proposedPosteriorProb - currentPosteriorProb);
                }
            }
            if (acceptProb > rng_.nextDouble()) {
                acceptanceCount[p]++;
                currentState_ = proposedState_;
            }
        }
        if (markovChain_.size() % batchSize_ == 0) {
            batchCount_++;
            for (int paramIndex = 0; paramIndex < model_.modelParameters.length; paramIndex++) {
                if (acceptanceCount[paramIndex] / batchSize_ > 0.44) {
                    logSd_[paramIndex] += Math.min(0.01, 1.0 / Math.sqrt(batchCount_));
                } else if (acceptanceCount[paramIndex] / batchSize_ < 0.44) {
                    logSd_[paramIndex] -= Math.min(0.01, 1.0 / Math.sqrt(batchCount_));
                }
                acceptanceCount[paramIndex] = 0;
            }
        }

        return currentState_;
    }

    /**
     * Resets the MCMC chain and samples the parameter distribution n times via MCMC, storing it in a new chain.
     *
     * @param n number of samples
     * @return last sample
     */
    private double[] nSamples(int n) {
        markovChain_ = new ArrayList<>();
        for (int i = 0; i < n - 1; i++) {
            nextSample();
        }
        return nextSample();
    }
}
