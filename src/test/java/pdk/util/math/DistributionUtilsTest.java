package pdk.util.math;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.statistics.distribution.NormalDistribution;
import org.hipparchus.distribution.continuous.TDistribution;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 12 Dec 2025, 9:50 AM
 */
class DistributionUtilsTest {

//    @Test
//    void beta() {
//        int trails = 100000;
//        RandomDataGenerator rng = new RandomDataGenerator();
//        UncorrelatedRandomVectorGenerator sampler = new UncorrelatedRandomVectorGenerator()
//
//        SharedStateContinuousSampler sampler1 = ChengBetaSampler.of(SamplingUtils.rng, 3 + 36, 7 + 114);
//        SharedStateContinuousSampler sampler2 = ChengBetaSampler.of(SamplingUtils.rng, 3 + 50, 7 + 100);
//        double[] aSamples = sampler1.samples(trails).toArray();
//        double[] bSamples = sampler2.samples(trails).toArray();
//        int n = 0;
//        for (int i = 0; i < aSamples.length; i++) {
//            if (bSamples[i] > aSamples[i]) {
//                n++;
//            }
//        }
//        System.out.println((double) n / trails);
//    }

    @Test
    void getNormalPDF() {
        NormalDistribution distribution = NormalDistribution.of(0, 1);
        for (int i = -100; i <= 100; i++) {
            double density1 = DistributionUtils.getNormalPDF(0, 1, i);
            double density2 = distribution.density(i);
            assertEquals(density1, density2, 1E-15);
        }
    }

    @Test
    void getTDistributionPDF() {
        double pdf = DistributionUtils.getTDistributionPDF(0, 1, 15, 1.25);
        assertEquals(0.17758247, pdf, 1E-8);
        for (int i = 1; i < 100; i++) {
            TDistribution tDistribution = new TDistribution(i);
            for (int j = -50; j <= 50; j++) {
                double d2 = tDistribution.density(j);
                double d1 = DistributionUtils.getTDistributionPDF(0, 1, i, j);
                assertEquals(d1, d2, 1E-10);
            }
        }
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