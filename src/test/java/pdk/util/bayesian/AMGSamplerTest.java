package pdk.util.bayesian;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
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
 * @since 03 Apr 2026, 10:34 AM
 */
class AMGSamplerTest {

    /**
     * This test demonstrates the utility of estimating the mean and standard deviation of a set of datapoints using a Bayesian
     * method (which uses Monte-Carlo sampling) rather than using the standard equations for the mean and standard deviations.
     * <p>
     * The example data contains an outlier, which skews the estimate of mean and standard deviation if you compute
     * these using mean/std dev equations.
     * <p>
     * The Bayesian method fits a series of t-distributions to the data, which is more tolerant of the outlier, providing a better
     * estimate of the mean and standard deviation.
     */
    @Test
    void sample() {
        double[] data = new double[]{1.0, 1.0, 1.1, 0.9, 0.8, 0.9, 1.0, 100.0};

        // the std dev and mean of the data are very high because the data contain an outlier (100)
        assertEquals(13.3, StatUtils.mean(data), 0.1); // mean of the data including outlier
        assertEquals(35.0, StatUtils.standardDeviation(data), 0.1); // sd of the data including outlier

        // let's see what the mean and std dev are without the outlier
        double[] dataWithoutTheOutlier = new double[]{1.0, 1.0, 1.1, 0.9, 0.8, 0.9, 1.0};
        assertEquals(0.098, StatUtils.standardDeviation(dataWithoutTheOutlier), 0.001); // sd of the data excluding the outlier
        assertEquals(0.96, StatUtils.mean(dataWithoutTheOutlier), 0.01);  // mean of the data excluding the outlier

        // let's do a Bayesian estimate of the mean and standard deviation of the data set that includes the outlier...
        AMGSampler sampler = new AMGSampler(data, new StudentTDistributionModel(
                StatUtils.mean(data), StatUtils.standardDeviation(data) * 1000000, StatUtils.mean(data),
                StatUtils.standardDeviation(data) / 1000, StatUtils.standardDeviation(data) * 1000, StatUtils.standardDeviation(data),
                1 / 29.0, 5), 0, 3
        );

        // burn in and then sample the MCMC chain
        sampler.run(1000, 1000);
        List<double[]> chain = sampler.getMarkovChain();
        // each iteration of the MCMC chain produces one t-distribution fit result
        // in this example, 1000 different t-distributions are fit to the data
        // each of these distributions has a mean, sd, and degree of normality
        DoubleList mus = new DoubleArrayList();
        DoubleList sigmas = new DoubleArrayList();
        DoubleList nus = new DoubleArrayList();
        for (double[] doubles : chain) {
            mus.add(doubles[0]);
            sigmas.add(doubles[1]);
            nus.add(doubles[2]);
        }

        assertEquals(1000, mus.size());

        // here are the mean/sd/normality results of the Bayesian estimation:
        // (the point estimate is the average of each parameter over the 1000 iterations)
        double muPointEstimate = StatUtils.mean(mus);
        double sigmaPointEstimate = StatUtils.mean(sigmas);
        double nuPointEstimate = StatUtils.mean(nus);

        assertEquals(0.963, muPointEstimate, 1E-3);
        assertEquals(0.115, sigmaPointEstimate, 1E-3);
        assertEquals(0.631, nuPointEstimate, 1E-3);

        Tuple2<Double, Double> hdi = Model.getHighestDensityInterval(mus.toDoubleArray());
        assertEquals(0.827, hdi._1, 1E-3);
        assertEquals(1.049, hdi._2, 1E-3);
    }

    // Bayesian estimation of the difference in means between two samples.
    @Test
    void twoSampleBayesianEstimation() {
        double[] data1 = new double[]{1.0, 0.9, 1.1, 1.0, 0.9, 1.1, 1.0, 0.9, 1.1};
        double[] data2 = new double[]{1.0, 0.9, 1.1, 1.0, 0.9, 1.1, 1.0, 0.9, 1.1};

        double pooledMean = StatUtils.mean(ArrayUtils.concat(data1, data2));
        double pooledSd = StatUtils.standardDeviation(ArrayUtils.concat(data1, data2));

        var sampler1 = new AMGSampler(data1, new StudentTDistributionModel(
                pooledMean, pooledSd * 1000000, pooledMean,
                pooledSd / 1000, pooledSd * 1000, pooledSd,
                1.0 / 29, 5
        ), 0, 3);
        var sampler2 = new AMGSampler(data2, new StudentTDistributionModel(
                pooledMean, pooledSd * 1000000, pooledMean,
                pooledSd / 1000, pooledSd * 1000, pooledSd,
                1.0 / 29, 5
        ), 1, 3);
        sampler1.run(1000, 1000);
        sampler2.run(1000, 1000);

        List<double[]> chain1 = sampler1.getMarkovChain();
        List<double[]> chain2 = sampler2.getMarkovChain();

        // calculate difference in means
        DoubleList meanDiffs = new DoubleArrayList();
        for (int i = 0; i < chain1.size(); i++) {
            meanDiffs.add(chain1.get(i)[0] - chain2.get(i)[0]);
        }
        double avgMeanDiff = StatUtils.mean(meanDiffs);
        assertEquals(-0.0005, avgMeanDiff, 1E-4);

        Tuple2<Double, Double> hdi = Model.getHighestDensityInterval(meanDiffs.toDoubleArray());
        assertEquals(-0.093, hdi._1, 1E-3);
        assertEquals(0.110, hdi._2, 1E-3);
    }

    /**
     * https://www.sumsar.net/best_online/
     */
    @Test
    void best() {
        double[] y1 = new double[]{1.96, 2.06, 2.03, 2.11, 1.88, 1.88, 2.08, 1.93, 2.03, 2.03, 2.03, 2.08, 2.03, 2.11, 1.93};
        double[] y2 = new double[]{1.83, 1.93, 1.88, 1.85, 1.85, 1.91, 1.91, 1.85, 1.78, 1.91, 1.93, 1.80, 1.80, 1.85, 1.93,
                1.85, 1.83, 1.85, 1.91, 1.85, 1.91, 1.85, 1.80, 1.80, 1.85};
        int n_samples = 20000 + 10;
        int n_burnin = 20000;

        var sampler1 = new AMGSampler(y1, new StudentTDistributionModel(y1, y2, 5), 0, 50);
        var sampler2 = new AMGSampler(y2, new StudentTDistributionModel(y1, y2, 5), 1, 50);

        sampler1.run(n_burnin, n_samples);
        sampler2.run(n_burnin, n_samples);

        List<double[]> chain1 = sampler1.getMarkovChain();
        List<double[]> chain2 = sampler2.getMarkovChain();

        double[] means1 = StudentTDistributionModel.getMeans(chain1);
        double[] means2 = StudentTDistributionModel.getMeans(chain2);

        assertEquals(2.01, StatUtils.mean(means1), 1E-2);
        Tuple2<Double, Double> hdi1 = Model.getHighestDensityInterval(means1);
        assertEquals(1.97, hdi1._1, 1E-2);
        assertEquals(2.06, hdi1._2, 1E-2);

        assertEquals(1.86, StatUtils.mean(means2), 1E-2);
        Tuple2<Double, Double> hdi2 = Model.getHighestDensityInterval(means2);
        assertEquals(1.84, hdi2._1, 1E-2);
        assertEquals(1.88, hdi2._2, 1E-2);

        double[] sds1 = StudentTDistributionModel.getSDs(chain1);
        double[] sds2 = StudentTDistributionModel.getSDs(chain2);

        assertEquals(0.082, StatUtils.mean(sds1), 1E-3);
        assertEquals(0.048, StatUtils.mean(sds2), 1E-3);

        System.out.println(Model.getHighestDensityInterval(sds1));
        System.out.println(Model.getHighestDensityInterval(sds2));
    }
}