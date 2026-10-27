package pdk.util.svm.linear;

import pdk.util.API;

import java.util.HashMap;
import java.util.Map;

/**
 * Solver types supported by liblinear.
 * <p>
 * The IDs are <strong>intentionally non-contiguous</strong> ({@code 0-7}, {@code 11-13},
 * {@code 21}); the gaps correspond to solvers that were dropped or never
 * ported. <strong>Do not renumber them</strong>, as this would break
 * compatibility with liblinear model files.</p>
 *
 * <h2>Solver overview</h2>
 * <table border="1">
 *   <caption>Grouped by task, regularization, and loss</caption>
 *   <tr>
 *     <th>ID</th><th>Constant</th><th>Task</th><th>Regularization</th><th>Loss</th><th>Solver</th>
 *   </tr>
 *   <tr><td>0</td><td>{@link #L2R_LR}</td><td>Classification</td><td>L2</td><td>Logistic</td><td>primal (Newton)</td></tr>
 *   <tr><td>1</td><td>{@link #L2R_L2LOSS_SVC_DUAL}</td><td>Classification</td><td>L2</td><td>L2 hinge</td><td>dual(coordinate descent)</td></tr>
 *   <tr><td>2</td><td>{@link #L2R_L2LOSS_SVC}</td><td>Classification</td><td>L2</td><td>L2 hinge</td><td>primal</td></tr>
 *   <tr><td>3</td><td>{@link #L2R_L1LOSS_SVC_DUAL}</td><td>Classification</td><td>L2</td><td>L1 hinge</td><td>dual</td></tr>
 *   <tr><td>4</td><td>{@link #MCSVM_CS}</td><td>Classification (multi-class)</td><td>L2</td><td>Hinge</td><td>Crammer-Singer</td></tr>
 *   <tr><td>5</td><td>{@link #L1R_L2LOSS_SVC}</td><td>Classification</td><td>L1</td><td>L2 hinge</td><td>primal</td></tr>
 *   <tr><td>6</td><td>{@link #L1R_LR}</td><td>Classification</td><td>L1</td><td>Logistic</td><td>primal</td></tr>
 *   <tr><td>7</td><td>{@link #L2R_LR_DUAL}</td><td>Classification</td><td>L2</td><td>Logistic</td><td>dual</td></tr>
 *   <tr><td>11</td><td>{@link #L2R_L2LOSS_SVR}</td><td>Regression</td><td>L2</td><td>L2 ε-insensitive</td><td>primal</td></tr>
 *   <tr><td>12</td><td>{@link #L2R_L2LOSS_SVR_DUAL}</td><td>Regression</td><td>L2</td><td>L2 ε-insensitive</td><td>dual</td></tr>
 *   <tr><td>13</td><td>{@link #L2R_L1LOSS_SVR_DUAL}</td><td>Regression</td><td>L2</td><td>L1 ε-insensitive</td><td>dual</td></tr>
 *   <tr><td>21</td><td>{@link #ONECLASS_SVM}</td><td>One-Class</td><td>—</td><td>—</td><td>dual</td></tr>
 * </table>
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 2025-11-18, 20:24
 */
@API(status = API.Status.STABLE, since = "2.44")
public enum SolverType {

    /**
     * L2-regularized logistic regression (primal)
     */
    L2R_LR(0, true, false),

    /**
     * L2-regularized L2-loss SVC (dual)
     */
    L2R_L2LOSS_SVC_DUAL(1, false, false),

    /**
     * L2-regularized L2-loss SVC (primal)
     */
    L2R_L2LOSS_SVC(2, false, false),

    /**
     * L2-regularized L1-loss support vector classification (dual)
     * <p>
     * (fka L1LOSS_SVM_DUAL)
     */
    L2R_L1LOSS_SVC_DUAL(3, false, false),

    /**
     * multi-class support vector classification by Crammer and Singer
     */
    MCSVM_CS(4, false, false),

    /**
     * L1-regularized L2-loss support vector classification
     *
     * @since 1.5
     */
    L1R_L2LOSS_SVC(5, false, false),

    /**
     * L1-regularized logistic regression
     *
     * @since 1.5
     */
    L1R_LR(6, true, false),

    /**
     * L2-regularized logistic regression (dual)
     *
     * @since 1.7
     */
    L2R_LR_DUAL(7, true, false),

    /**
     * L2-regularized L2-loss support vector regression (primal)
     */
    L2R_L2LOSS_SVR(11, false, true),

    /**
     * L2-regularized L2-loss support vector regression (dual)
     *
     * @since 1.91
     */
    L2R_L2LOSS_SVR_DUAL(12, false, true),

    /**
     * L2-regularized L1-loss support vector regression (dual)
     *
     * @since 1.91
     */
    L2R_L1LOSS_SVR_DUAL(13, false, true),

    /**
     * one-class support vector machine (dual)
     *
     * @since 2.40
     */
    ONECLASS_SVM(21, false, false);

    private final boolean logisticRegressionSolver;
    private final boolean supportVectorRegression;
    private final int id;

    SolverType(int id, boolean logisticRegressionSolver, boolean supportVectorRegression) {
        this.id = id;
        this.logisticRegressionSolver = logisticRegressionSolver;
        this.supportVectorRegression = supportVectorRegression;
    }

    private static final Map<Integer, SolverType> SOLVERS_BY_ID = new HashMap<>();

    static {
        for (SolverType solverType : SolverType.values()) {
            SolverType old = SOLVERS_BY_ID.put(solverType.getId(), solverType);
            if (old != null)
                throw new Error("duplicate solver type ID: " + solverType.getId());
        }
    }

    public int getId() {
        return id;
    }

    public static SolverType getById(int id) {
        SolverType solverType = SOLVERS_BY_ID.get(id);
        if (solverType == null) {
            throw new RuntimeException("found no solvertype for id " + id);
        }
        return solverType;
    }

    /**
     * @since 1.9
     */
    public boolean isLogisticRegressionSolver() {
        return logisticRegressionSolver;
    }

    /**
     * @since 1.91
     */
    public boolean isSupportVectorRegression() {
        return supportVectorRegression;
    }

    /**
     * @since 2.40
     */
    public boolean isOneClass() {
        return equals(ONECLASS_SVM);
    }
}
