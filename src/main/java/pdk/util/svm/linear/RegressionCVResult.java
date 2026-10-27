package pdk.util.svm.linear;

import pdk.util.DecimalFormatUtils;

import java.text.DecimalFormat;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Oct 2026, 3:49 PM
 */
public class RegressionCVResult implements CVResult {

    private int numberOfSamples;
    private double meanSquaredError;
    private double squaredCorrelationCoefficient;

    public RegressionCVResult(int numberOfSamples, double meanSquaredError, double squaredCorrelationCoefficient) {
        this.numberOfSamples = numberOfSamples;
        this.meanSquaredError = meanSquaredError;
        this.squaredCorrelationCoefficient = squaredCorrelationCoefficient;
    }

    @Override
    public int getNumberOfSamples() {
        return numberOfSamples;
    }

    @Override
    public String toString() {
        DecimalFormat f6 = DecimalFormatUtils.F6;
        return "Cross Validation{" +
                "numberOfSamples=" + numberOfSamples +
                ", Mean squared error=" + f6.format(meanSquaredError) +
                ", Squared correlation coefficient=" + f6.format(squaredCorrelationCoefficient) +
                '}';
    }
}
