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

    private final BoxMullerNormalizedGaussianSampler normSampler_;

    /**
     * Create a {@link AMGSampler} with default batch size 50.
     *
     * @param data
     * @param model
     * @param seed
     */
    public AMGSampler(double[] data, Model model, Integer seed) {
        this(data, model, seed, 50);
    }

    /**
     * Create a {@link AMGSampler}
     *
     * @param data      init dataset
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
        for (int i = 0; i < currentState_.length; i++) {
            currentState_[i] = model.modelParameters[i].initialGuess_;
        }
        this.normSampler_ = BoxMullerNormalizedGaussianSampler.of(rng_);
    }


    public AMGSampler(WeightPoint2D[] data, Model model, Integer seed) {
        this(data, model, seed, 50);
    }

    public AMGSampler(WeightPoint2D[] data, Model model, Integer seed, int batchSize) {
        this.rng_ = RandomSource.XO_SHI_RO_256_PP.create(seed);
        this.model_ = model;
        this.data_ = data;
        this.batchSize_ = batchSize;
        this.logSd_ = new double[model.modelParameters.length];
        this.acceptanceCount = new double[model.modelParameters.length];
        this.currentState_ = new double[model.modelParameters.length];
        for (int i = 0; i < currentState_.length; i++) {
            currentState_[i] = model.modelParameters[i].initialGuess_;
        }
        this.normSampler_ = BoxMullerNormalizedGaussianSampler.of(rng_);
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

        for (int paramIdx = 0; paramIdx < model_.modelParameters.length; paramIdx++) {
            // 从以当前值为中心、标准差为 exp(log_sd[i]) 的正态分布中抽取候选值
            double paramProp = normSampler_.sample() * Math.exp(logSd_[paramIdx]) + currentState_[paramIdx];

            // 复制当前状态，只替换第 paramIdx 个分量。
            double[] prop = currentState_.clone();
            prop[paramIdx] = paramProp;

            // 计算接受概率
            double acceptProb = 0; // 当参数无效，默认概率为 0（比如标准差为负数）
            if (model_.modelParameters[paramIdx].isValid(paramProp)) {
                // 提议分布对称（正态随机游走）,接受概率为 min(1,prop/curr)
                double currPostDens = model_.logPosteriorProbability(currentState_, data_);
                double propPostDens = model_.logPosteriorProbability(prop, data_);
                if (!Double.isFinite(currPostDens)) {
                    acceptProb = 1; // 跳出不良区域
                } else {
                    acceptProb = Math.exp(propPostDens - currPostDens);
                }
            }
            if (acceptProb > rng_.nextDouble()) {
                acceptanceCount[paramIdx]++;
                currentState_ = prop;
            }
        }
        // 每完成 batch_size 次完整 Gibbs 扫描（即 chain.length % batch_size == 0），进行自适应调整
        if (markovChain_.size() % batchSize_ == 0) {
            batchCount_++;
            for (int paramI = 0; paramI < model_.modelParameters.length; paramI++) {
                // 目标接受率：0.44，这是一维随机游走 Metropolis 在目标分布为正态时的最优值。
                // 接受率高于目标 → 增大步长（log_sd 增加 → 标准差变大）；低于目标 → 减小步长。
                // 批次大小固定为 50，平衡了自适应稳定性和计算开销。
                if (acceptanceCount[paramI] / batchSize_ > 0.44) {
                    logSd_[paramI] += Math.min(0.01, 1 / Math.sqrt(batchCount_));
                } else if (acceptanceCount[paramI] / batchSize_ < 0.44) {
                    logSd_[paramI] -= Math.min(0.01, 1 / Math.sqrt(batchCount_));
                }
                acceptanceCount[paramI] = 0;
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
