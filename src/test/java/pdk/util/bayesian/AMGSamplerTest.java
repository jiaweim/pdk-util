package pdk.util.bayesian;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import org.junit.jupiter.api.Test;
import pdk.util.ArrayUtils;
import pdk.util.data.WeightPoint2D;
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
        WeightPoint2D[] dataArray = new WeightPoint2D[data.length];
        for (int i = 0; i < data.length; i++) {
            dataArray[i] = new WeightPoint2D(data[i], 0);
        }

        // // let's do a Bayesian estimate of the mean and standard deviation of the data set that includes the outlier...
        AMGSampler sampler = new AMGSampler(dataArray, new StudentTDistributionModel(
                StatUtils.mean(data), StatUtils.standardDeviation(data) * 1000000, StatUtils.mean(data),
                StatUtils.standardDeviation(data) / 1000, StatUtils.standardDeviation(data) * 1000, StatUtils.standardDeviation(data),
                1.0 / 29.0, 5),
                0
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

        System.out.println(Model.getHighestDensityInterval(mus.toDoubleArray(), 0.95));

        System.out.println(muPointEstimate);
        System.out.println(sigmaPointEstimate);
        System.out.println(nuPointEstimate);

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
        ), 0);
        var sampler2 = new AMGSampler(data2, new StudentTDistributionModel(
                pooledMean, pooledSd * 1000000, pooledMean,
                pooledSd / 1000, pooledSd * 1000, pooledSd,
                1.0 / 29.0, 5
        ), 1);
        sampler1.run(10000,1000);
        sampler2.run(10000,1000);

        List<double[]> chain1 = sampler1.getMarkovChain();
        List<double[]> chain2 = sampler2.getMarkovChain();

        // calculate difference in means
        DoubleList meanDiffs = new  DoubleArrayList();
        for(int i = 0; i< chain1.size(); i++) {
            meanDiffs.add(chain1.get(i)[0]-chain2.get(i)[0]);
        }
        double avgMeanDiff = StatUtils.mean(meanDiffs);
        System.out.println("meanDiffs: " + avgMeanDiff);

        Tuple2<Double, Double> hdi = Model.getHighestDensityInterval(meanDiffs.toDoubleArray());
        System.out.println(hdi);

    }
}