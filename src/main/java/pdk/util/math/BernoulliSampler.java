package pdk.util.math;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.sampling.distribution.DiscreteSampler;

/**
 * This class implements random sampling from Bernoulli distribution.
 *
 * @author Jiawei Mao
 * @version 1.0.0⭐
 * @since 31 Mar 2026, 2:25 PM
 */
public class BernoulliSampler implements DiscreteSampler {

    /**
     * Create a random sampler for Bernoulli distribution
     *
     * @param rng {@link UniformRandomProvider}
     * @param p   probability of getting heads
     * @return {@link BernoulliSampler} instance
     */
    public static BernoulliSampler of(UniformRandomProvider rng, double p) {
        return new BernoulliSampler(rng, p);
    }

    private final UniformRandomProvider rng_;
    private final double p_;

    private BernoulliSampler(UniformRandomProvider rng, double p) {
        this.rng_ = rng;
        this.p_ = p;
    }

    @Override
    public int sample() {
        return rng_.nextDouble() < p_ ? 1 : 0;
    }
}
