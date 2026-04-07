package pdk.util.bayesian;

import org.apache.commons.statistics.distribution.ExponentialDistribution;
import org.apache.commons.statistics.distribution.NormalDistribution;
import org.apache.commons.statistics.distribution.UniformContinuousDistribution;
import pdk.util.data.WeightPoint2D;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 07 Apr 2026, 4:15 PM
 */
public class BEST extends Model {


    public BEST(double mu1, double sigma1, double mu2, double sigma2,
            double pooledMean, double pooledSD,
            double nu) {
        modelParameters = new Parameter[5];

        modelParameters[0] = new Parameter(NormalDistribution.of(pooledMean, pooledSD * 1000),
                createRange(Double.MIN_VALUE, Double.MAX_VALUE),
                pooledMean);
        modelParameters[1] = new Parameter(NormalDistribution.of(pooledMean, pooledSD * 1000),
                createRange(Double.MIN_VALUE, Double.MAX_VALUE),
                pooledMean);
        modelParameters[2] = new Parameter(UniformContinuousDistribution.of(pooledSD / 1000, pooledSD * 1000),
                createRange(0, Double.MAX_VALUE), pooledSD);
        modelParameters[3] = new Parameter(UniformContinuousDistribution.of(pooledSD / 1000, pooledSD * 1000),
                createRange(0, Double.MAX_VALUE), pooledSD);
        modelParameters[4] = new Parameter(ExponentialDistribution.of(29.0),
                createRange(0, Double.MAX_VALUE), nu);
    }

    @Override
    public double probability(double[] proposalParams, WeightPoint2D data) {
        return 0;
    }
}
