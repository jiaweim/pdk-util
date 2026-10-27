package pdk.util.svm.linear;

/**
 * Outcome of a parameter search, as returned by {@link LinearSVM#findParameters(int, double, double)}.
 *
 * <p>Note that the meaning of {@link #getBestScore()} depends on the solver:
 * it is an accuracy for classification and a mean squared error for regression,
 * so the direction in which it should be optimised differs as well.
 *
 * <p>Only {@link SolverType#L2R_LR}, {@link SolverType#L2R_L2LOSS_SVC} and
 * {@link SolverType#L2R_L2LOSS_SVR} are supported by the parameter search;
 * any other solver causes {@code findParameters} to throw
 * {@link IllegalArgumentException}.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Oct 2026, 5:33 PM
 */
public final class ParameterSearchResult {

    private final double bestC;
    private final double bestScore;
    private final double bestP;

    /**
     * Creates a new search result.
     *
     * @param bestC     the penalty parameter {@code C} that achieved {@code bestScore}
     * @param bestScore the cross-validation accuracy or mean squared error achieved
     *                  with {@code bestC} and {@code bestP}
     * @param bestP     the epsilon of the epsilon-insensitive loss, or {@code -1}
     *                  if the solver does not use {@code p}
     */
    public ParameterSearchResult(double bestC, double bestScore, double bestP) {
        this.bestC = bestC;
        this.bestScore = bestScore;
        this.bestP = bestP;
    }

    /**
     * Returns the penalty parameter {@code C} that yielded {@link #getBestScore()}.
     *
     * <p>{@code C} trades the regularization term off against the training loss:
     * the larger {@code C}, the more weight is put on fitting the training data.
     * The search starts at the value passed to {@code findParameters} and doubles
     * it until an upper bound is reached ({@code 1024} for classification,
     * {@code 1048576} for regression).
     *
     * @return the best {@code C} found by the search, always positive
     */
    public double getBestC() {
        return bestC;
    }

    /**
     * Returns the score obtained with {@link #getBestC()} and {@link #getBestP()}.
     *
     * <p>For the classification solvers {@link SolverType#L2R_LR} and
     * {@link SolverType#L2R_L2LOSS_SVC} this is the cross-validation accuracy in
     * {@code [0, 1]}, and a <b>higher</b> value is better. For the regression
     * solver {@link SolverType#L2R_L2LOSS_SVR} it is the cross-validation mean
     * squared error, and a <b>lower</b> value is better.
     *
     * @return the cross-validation accuracy, or the mean squared error for regression
     */
    public double getBestScore() {
        return bestScore;
    }

    /**
     * Returns the epsilon of the epsilon-insensitive loss that yielded
     * {@link #getBestScore()}.
     *
     * <p>Only meaningful for {@link SolverType#L2R_L2LOSS_SVR}. Classification
     * solvers do not use {@code p} and do not search it, in which case this
     * method returns {@code -1}.
     *
     * @return the best {@code p} found by the search, or {@code -1} if the solver
     * does not use {@code p}
     */
    public double getBestP() {
        return bestP;
    }

    @Override
    public String toString() {
        return "ParameterSearchResult{" +
                "bestC=" + bestC +
                ", bestScore=" + bestScore +
                ", bestP=" + bestP +
                '}';
    }
}
