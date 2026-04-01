package pdk.util.math.bayes;

import pdk.util.data.WeightPoint2D;

/**
 * This class is a generic framework for a model that describes a set of data points.
 * <p>
 * It must be inherited by a more specific model with defined parameters and a probability
 * function to be used.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 01 Apr 2026, 2:49 PM
 */
public abstract class Model {

    private Parameter[] modelParameters;

    public Model() {}

    /**
     * Calculate the probability of the model, given a single data point
     *
     * @param parameters model parameters
     * @param point      {@link WeightPoint2D}
     * @return probability value
     */
    public abstract double getProbability(double[] parameters, WeightPoint2D point);

    /**
     * Calculates the log prior probability of a set of parameters
     *
     * @param parameters
     * @return
     */
    private double logPriorProbability(double[] parameters) {
        double logP = 0;
        for (int p = 0; p < parameters.length; p++) {
            double prior = modelParameters[p].priorDistribution.density(parameters[p]);
            logP += Math.log(prior);
        }
        return logP;
    }
}
