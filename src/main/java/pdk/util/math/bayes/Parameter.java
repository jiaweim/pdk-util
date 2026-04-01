package pdk.util.math.bayes;

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

    protected ContinuousDistribution priorDistribution;
    private RangeSet<Double> acceptableParameterRanges;
    private double initialGuess;

    public Parameter(ContinuousDistribution priorDistribution, RangeSet<Double> acceptableParameterRanges, double initialGuess) {
        this.priorDistribution = priorDistribution;
        this.acceptableParameterRanges = acceptableParameterRanges;
        this.initialGuess = initialGuess;
    }
}
