package pdk.util.svm.linear;

import pdk.util.DecimalFormatUtils;

import java.text.DecimalFormat;

/**
 * Cross-validation result for a classification task.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Oct 2026, 3:36 PM
 */
public class ClassificationCVResult implements CVResult {

    /**
     * The total number of samples evaluated across all cross-validation
     * folds.
     */
    private final int totalSamples;
    /**
     * The number of samples whose predicted label matched the true label.
     */
    private final int numberOfCorrect;
    /**
     * The classification accuracy, defined as
     * {@code numberOfCorrect / (double) totalSamples}.
     */
    private final double accuracy;

    /**
     * Creates a new classification cross-validation result.
     *
     * @param totalSamples    the total number of samples evaluated across
     *                        all folds; must be strictly positive
     * @param numberOfCorrect the number of correctly classified samples;
     *                        must be in the range {@code [0, totalSamples]}
     * @throws IllegalArgumentException if {@code totalSamples <= 0}, or if
     *                                  {@code numberOfCorrect < 0}, or if
     *                                  {@code numberOfCorrect > totalSamples}
     */
    public ClassificationCVResult(int totalSamples, int numberOfCorrect) {
        this.totalSamples = totalSamples;
        this.numberOfCorrect = numberOfCorrect;
        this.accuracy = numberOfCorrect / (double) totalSamples;
    }

    /**
     * Returns the classification accuracy of the model under
     * cross-validation.
     *
     * <p>The accuracy is a value in {@code [0.0, 1.0]}, where {@code 0.0}
     * indicates that no sample was correctly classified and {@code 1.0}
     * indicates perfect classification.
     *
     * @return the accuracy, defined as {@code numberOfCorrect / (double) totalSamples}
     */
    public double getAccuracy() {
        return accuracy;
    }

    /**
     * Returns the number of samples whose predicted label matched the true
     * label across all cross-validation folds.
     *
     * @return the number of correctly classified samples
     */
    public int getNumberOfCorrect() {
        return numberOfCorrect;
    }

    @Override
    public int getNumberOfSamples() {
        return totalSamples;
    }

    @Override
    public String toString() {
        DecimalFormat percent = DecimalFormatUtils.getPercent(4);
        return "Cross Validation{" +
                "totalSamples=" + totalSamples +
                ", numberOfCorrect=" + numberOfCorrect +
                ", accuracy=" + percent.format(accuracy) +
                '}';
    }
}
