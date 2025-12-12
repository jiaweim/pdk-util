package pdk.util.math;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.sampling.distribution.ChengBetaSampler;
import org.apache.commons.rng.sampling.distribution.SharedStateContinuousSampler;
import org.junit.jupiter.api.Test;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 12 Dec 2025, 9:50 AM
 */
class DistributionUtilsTest {

    @Test
    void beta() {
        int trails = 100000;
        SharedStateContinuousSampler sampler1 = ChengBetaSampler.of(SamplingUtils.rng, 3 + 36, 7 + 114);
        SharedStateContinuousSampler sampler2 = ChengBetaSampler.of(SamplingUtils.rng, 3 + 50, 7 + 100);
        double[] aSamples = sampler1.samples(trails).toArray();
        double[] bSamples = sampler2.samples(trails).toArray();
        int n = 0;
        for (int i = 0; i < aSamples.length; i++) {
            if (bSamples[i] > aSamples[i]) {
                n++;
            }
        }
        System.out.println((double) n / trails);
    }

    @Test
    void pi() {
        int n = 1000000;
        UniformRandomProvider rng = SamplingUtils.rng;
        int inside = 0;
        for (int i = 0; i < n; i++) {
            double x = rng.nextDouble();
            double y = rng.nextDouble();
            if (x * x + y * y <= 1) {
                inside++;
            }
        }
        System.out.println(4.0 * inside / n);
    }

    @Test
    void binom() {

    }
}