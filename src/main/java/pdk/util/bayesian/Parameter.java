package pdk.util.bayesian;

import com.google.common.collect.RangeSet;
import org.apache.commons.statistics.distribution.ContinuousDistribution;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 01 Apr 2026, 2:46 PM
 */
public class Parameter {

    protected ContinuousDistribution priorDistribution_;
    protected RangeSet<Double> acceptableParameterRanges_;
    protected double initialGuess_;

    /**
     * Create a  parameter
     *
     * @param priorDistribution         Prior distribution of the parameters
     * @param acceptableParameterRanges The range of parameter values
     * @param initialGuess              Initial values of the parameters
     */
    public Parameter(ContinuousDistribution priorDistribution, RangeSet<Double> acceptableParameterRanges,
            double initialGuess) {
        this.priorDistribution_ = priorDistribution;
        this.acceptableParameterRanges_ = acceptableParameterRanges;
        this.initialGuess_ = initialGuess;
    }

    /**
     * Returns true if the parameter is within the valid range.
     *
     * @param guess proposed parameter
     * @return true if the value is valid
     */
    public boolean isValid(double guess) {
        return this.acceptableParameterRanges_.contains(guess);
    }

}
