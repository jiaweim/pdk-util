package pdk.util.svm.linear;

/**
 * Cross-validation result.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Oct 2026, 3:43 PM
 */
public interface CVResult {

    /**
     * Returns the total number of samples evaluated across all
     * cross-validation folds.
     *
     * @return the total number of samples
     */
    int getNumberOfSamples();
}
