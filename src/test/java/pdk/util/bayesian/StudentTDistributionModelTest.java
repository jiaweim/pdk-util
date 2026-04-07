package pdk.util.bayesian;

import org.junit.jupiter.api.Test;
import pdk.util.ArrayUtils;
import pdk.util.math.StatUtils;
import pdk.util.tuple.Tuple2;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 07 Apr 2026, 1:21 PM
 */
class StudentTDistributionModelTest {

    @Test
    void test() {
        double[] y1 = new double[]{
                5.77, 5.33, 4.59, 4.33, 3.66, 4.48};
        double[] y2 = new double[]{
                3.88, 3.55, 3.29, 2.59, 2.33, 3.59
        };

        double[] data = ArrayUtils.concat(y1, y2);
        double mean = StatUtils.mean(data);
        double std = StatUtils.standardDeviation(data);

        AMGSampler sampler1 = new AMGSampler(y1,
                new StudentTDistributionModel(mean, std * 2, StatUtils.mean(y1),
                        0.1, 10, std,
                        1 / 29.0, 5
                ), 0);
        AMGSampler sampler2 = new AMGSampler(y2,
                new StudentTDistributionModel(mean, std * 2, StatUtils.mean(y2),
                        0.1, 10, std,
                        1 / 29.0, 5
                ), 1);
        sampler1.run(1000, 1000);
        sampler2.run(1000, 1000);
    }

    /**
     * Drug trial evaluation
     */
    @Test
    void medium() {
        double[] y1 = new double[]{
                101, 100, 102, 104, 102, 97, 105, 105, 98, 101,
                100, 123, 105, 103, 100, 95, 102, 106, 109, 102,
                82, 102, 100, 102, 102, 101, 102, 102, 103, 103,
                97, 97, 103, 101, 97, 104, 96, 103, 124, 101,
                101, 100, 101, 101, 104, 100, 101};

        double[] y2 = new double[]{
                99, 101, 100, 101, 102, 100, 97, 101, 104, 101,
                102, 102, 100, 105, 88, 101, 100, 104, 100, 100,
                100, 101, 102, 103, 97, 101, 101, 100, 101, 99,
                101, 100, 100, 101, 100, 99, 101, 100, 102, 99,
                100, 99};
        assertEquals(101.91, StatUtils.mean(y1), 0.01);
        assertEquals(100.36, StatUtils.mean(y2), 0.01);

        AMGSampler sampler1 = new AMGSampler(y1, new StudentTDistributionModel(y1, y2, 5), 0);
        AMGSampler sampler2 = new AMGSampler(y2, new StudentTDistributionModel(y1, y2, 5), 1);
        sampler1.run(1000, 100000);
        sampler2.run(1000, 100000);

        List<double[]> chain1 = sampler1.getMarkovChain();
        List<double[]> chain2 = sampler2.getMarkovChain();
        double[] means1 = StudentTDistributionModel.getMeans(chain1);
        double[] means2 = StudentTDistributionModel.getMeans(chain2);

        assertEquals(101.55, StatUtils.mean(means1), 0.01);
        assertEquals(100.54, StatUtils.mean(means2), 0.01);

        Tuple2<Double, Double> hdi1 = Model.getHighestDensityInterval(means1.clone());
        Tuple2<Double, Double> hdi2 = Model.getHighestDensityInterval(means2.clone());

        assertEquals(100.83, hdi1._1, 1E-2);
        assertEquals(102.29, hdi1._2, 1E-2);

        assertEquals(100.09, hdi2._1, 1E-2);
        assertEquals(100.98, hdi2._2, 1E-2);

        double[] meanDiffs = new double[means1.length];
        for (int i = 0; i < meanDiffs.length; i++) {
            meanDiffs[i] = means1[i] - means2[i];
        }
        assertEquals(1.01, StatUtils.mean(meanDiffs), 0.01);
        Tuple2<Double, Double> diffHDI = Model.getHighestDensityInterval(meanDiffs);
        assertEquals(0.165, diffHDI._1, 1E-2);
        assertEquals(1.86, diffHDI._2, 1E-2);

        double[] sds1 = StudentTDistributionModel.getSDs(chain1);
        double[] sds2 = StudentTDistributionModel.getSDs(chain2);

        double[] sdDiffs = new double[sds1.length];
        for (int i = 0; i < sdDiffs.length; i++) {
            sdDiffs[i] = sds1[i] - sds2[i];
        }

        System.out.println(Model.getHighestDensityInterval(sdDiffs));

    }
}