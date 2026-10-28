package pdk.util.svm.linear;

import pdk.util.SerializationUtils;

import java.util.Arrays;
import java.util.Random;

/**
 * Represents the training parameters for LIBLINEAR modesl.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Oct 2026, 1:19 PM
 */
public final class Parameter implements Cloneable {

    private static final long DEFAULT_RANDOM_SEED = 0L;

    /**
     * Cost of constraints violation.
     * <p>
     * Larger values penalize training errors more heavily and may lead to
     * a more complex model. Typical values are in the range of {@code 1} to
     * {@code 1000}.
     */
    double C;
    /**
     * Stopping criterion tolerance.
     * <p>
     * The solver stops when the dual/primal residual is smaller than this
     * value. A commonly used value is {@code 0.01}.
     */
    double eps;
    /**
     * Maximum number of iterations.
     */
    int max_iters = 1000;
    /**
     * Solver type used for training.
     */
    SolverType solverType = SolverType.L2R_L2LOSS_SVC_DUAL;
    /**
     * Class weights used to adjust the penalty for specific classes.
     * <p>
     * Each {@code weight[i]} corresponds to {@code weightLabel[i]}.
     * If not set, all classes use the default penalty.
     */
    double[] weight = null;
    /**
     * Class labels associated with {@link #weight}.
     */
    int[] weightLabel = null;
    /**
     * Epsilon in the loss function of epsilon-SVR.
     * <p>
     * Only used for epsilon-support vector regression solvers.
     */
    double p = 0.1;
    /**
     * Parameter nu for one-class SVM.
     * <p>
     * Must be in the open interval {@code (0, 1)}.
     */
    double nu = 0.5;
    /**
     * Initial solution vector.
     * <p>
     * Only supported for {@link SolverType#L2R_LR} and
     * {@link SolverType#L2R_L2LOSS_SVC}.
     */
    double[] init_sol = null;
    /**
     * Whether to regularize the bias term.
     */
    boolean regularize_bias = true;
    /**
     * Whether to recompute weights during training.
     * <p>
     * Mainly used for certain solver types, such as {@code -s 1} and
     * {@code -s 3}, and may be extended to other solvers in the future.
     */
    boolean w_recalc = false;
    /**
     * Random number generator used during training.
     */
    Random random = new Random(DEFAULT_RANDOM_SEED);

    public Parameter() {
    }

    /**
     * Creates a parameter with the specified solver, cost, and stopping
     * criterion.
     *
     * @param solver the solver type
     * @param C      cost of constraints violation; must be greater than 0
     * @param eps    stopping criterion tolerance; must be greater than 0
     */
    public Parameter(SolverType solver, double C, double eps) {
        setSolverType(solver);
        setCost(C);
        setEPS(eps);
    }

    /**
     * Creates a parameter with the specified solver, cost, maximum
     * iterations, and stopping criterion.
     *
     * @param solver    the solver type
     * @param C         cost of constraints violation; must be greater than 0
     * @param max_iters maximum number of iterations; must be greater than 0
     * @param eps       stopping criterion tolerance; must be greater than 0
     */
    public Parameter(SolverType solver, double C, int max_iters, double eps) {
        setSolverType(solver);
        setCost(C);
        setEPS(eps);
        setMaxIterations(max_iters);
    }

    /**
     * Constructor
     *
     * @param solverType {@link SolverType}
     * @param C          cost
     * @param eps        eps is the stopping criterion. (we usually use 0.01).
     * @param p          set the epsilon in loss function of epsilon-SVR (default 0.1)
     */
    public Parameter(SolverType solverType, double C, double eps, double p) {
        setSolverType(solverType);
        setCost(C);
        setEPS(eps);
        setEpsilon(p);
    }

    /**
     * Creates a parameter with full control over solver, cost, iterations,
     * stopping criterion, and SVR epsilon.
     *
     * @param solverType the solver type
     * @param C          cost of constraints violation; must be greater than 0
     * @param eps        stopping criterion tolerance; must be greater than 0
     * @param max_iters  maximum number of iterations; must be greater than 0
     * @param p          epsilon in the loss function of epsilon-SVR;
     *                   must be greater than or equal to 0
     */
    public Parameter(SolverType solverType, double C, double eps, int max_iters, double p) {
        setSolverType(solverType);
        setCost(C);
        setEPS(eps);
        setMaxIterations(max_iters);
        setEpsilon(p);
    }

    /**
     * <p>nr_weight, weight_label, and weight are used to change the penalty
     * for some classes (If the weight for a class is not changed, it is
     * set to 1). This is useful for training classifier using unbalanced
     * input data or with asymmetric misclassification cost.</p>
     *
     * <p>Each weight[i] corresponds to weight_label[i], meaning that
     * the penalty of class weight_label[i] is scaled by a factor of weight[i].</p>
     *
     * <p>If you do not want to change penalty for any of the classes,
     * just set nr_weight to 0.</p>
     */
    public void setWeights(double[] weights, int[] weightLabels) {
        if (weights == null)
            throw new IllegalArgumentException("'weight' must not be null");
        if (weightLabels == null || weightLabels.length != weights.length)
            throw new IllegalArgumentException("'weightLabels' must have same length as 'weight'");
        this.weightLabel = Arrays.copyOf(weightLabels, weightLabels.length);
        this.weight = Arrays.copyOf(weights, weights.length);
    }

    /**
     * Returns a copy of the class weights.
     *
     * @return a copy of the weight array, or {@code null} if no weights are set
     * @see #setWeights(double[], int[])
     */
    public double[] getWeights() {
        if (weight == null) {
            return null;
        }
        return Arrays.copyOf(weight, weight.length);
    }

    /**
     * Returns a copy of the weight labels.
     *
     * @return a copy of the weight label array, or {@code null} if no weights
     * are set
     * @see #setWeights(double[], int[])
     */
    public int[] getWeightLabels() {
        if (weightLabel == null) {
            return null;
        }
        return Arrays.copyOf(weightLabel, weightLabel.length);
    }

    /**
     * Returns the number of configured class weights.
     *
     * @return the number of weights, or {@code 0} if no weights are set
     * @see #setWeights(double[], int[])
     */
    public int getNumWeights() {
        if (weight == null)
            return 0;
        return weight.length;
    }

    /**
     * Sets the cost of constraints violation. (we usually use 1 to 1000)
     * <p>
     * cost 越大，分类错误的惩罚越大，模型趋向于减少分类错误，分类间隔更窄；
     * cost 越小，分类错误的惩罚越小，模型趋向于增大分类间隔，允许少量分类错误。
     *
     * @param C cost value; must be greater than 0
     * @throws IllegalArgumentException if {@code C <= 0}
     */
    public void setCost(double C) {
        if (C <= 0)
            throw new IllegalArgumentException("C must not be <= 0");
        this.C = C;
    }

    /**
     * Returns the cost of constraints violation.
     *
     * @return the current cost value
     */
    public double getCost() {
        return C;
    }

    /**
     * Sets the stopping criterion tolerance. (we usually use 0.01).
     *
     * @param eps tolerance value; must be greater than 0
     * @throws IllegalArgumentException if {@code eps <= 0}
     */
    public void setEPS(double eps) {
        if (eps <= 0)
            throw new IllegalArgumentException("eps must not be <= 0");
        this.eps = eps;
    }

    /**
     * Returns the stopping criterion tolerance.
     *
     * @return the current epsilon value
     */
    public double getEPS() {
        return eps;
    }

    /**
     * Sets the maximum number of iterations.
     *
     * @param iters maximum iterations; must be greater than 0
     * @throws IllegalArgumentException if {@code iters <= 0}
     */
    public void setMaxIterations(int iters) {
        if (iters <= 0)
            throw new IllegalArgumentException("max iters not be <= 0");
        this.max_iters = iters;
    }

    /**
     * Returns the maximum number of iterations.
     *
     * @return the current maximum iteration limit
     */
    public int getMaxIterations() {
        return max_iters;
    }

    /**
     * Sets the solver type.
     *
     * @param solverType the solver type; must not be {@code null}
     * @throws IllegalArgumentException if {@code solverType} is {@code null}
     */
    public void setSolverType(SolverType solverType) {
        if (solverType == null)
            throw new IllegalArgumentException("solver type must not be null");
        this.solverType = solverType;
    }

    /**
     * Returns the current solver type.
     *
     * @return the solver type
     */
    public SolverType getSolverType() {
        return solverType;
    }

    /**
     * Sets the epsilon parameter in the loss function of epsilon-SVR (default 0.1).
     *
     * @param p SVR epsilon; must be greater than or equal to 0
     * @throws IllegalArgumentException if {@code p < 0}
     */
    public void setEpsilon(double p) {
        if (p < 0)
            throw new IllegalArgumentException("p must not be less than 0");
        this.p = p;
    }

    /**
     * Returns the epsilon parameter in the loss function of epsilon-SVR (default 0.1).
     *
     * @return the current SVR epsilon value
     */
    public double getP() {
        return p;
    }

    /**
     * Sets the initial solution vector.
     * <p>
     * This is only supported for {@link SolverType#L2R_LR} and
     * {@link SolverType#L2R_L2LOSS_SVC}. Passing {@code null} clears the
     * initial solution.
     *
     * @param init_sol the initial solution array, or {@code null}
     */
    public void setInitSol(double[] init_sol) {
        if (init_sol == null)
            this.init_sol = null;
        else {
            this.init_sol = Arrays.copyOf(init_sol, init_sol.length);
        }
    }

    /**
     * Returns a copy of the initial solution vector.
     *
     * @return a copy of the initial solution array, or {@code null} if not set
     */
    public double[] getInitSol() {
        if (init_sol == null) {
            return null;
        }
        return Arrays.copyOf(init_sol, init_sol.length);
    }

    /**
     * Sets the nu parameter for one-class SVM.
     *
     * @param nu nu value; must satisfy {@code 0 < nu < 1}
     * @throws IllegalArgumentException if {@code nu <= 0} or {@code nu >= 1}
     */
    public void setNu(double nu) {
        if (nu <= 0)
            throw new IllegalArgumentException("nu must not be <=0");
        if (nu >= 1)
            throw new IllegalArgumentException("nu must not be >=1");
        this.nu = nu;
    }

    /**
     * Returns the nu parameter for one-class SVM.
     *
     * @return the current nu value
     */
    public double getNu() {
        return nu;
    }

    /**
     * Sets whether to regularize the bias term.
     *
     * @param regularizeBias {@code true} to regularize the bias term
     */
    public void setRegularizeBias(boolean regularizeBias) {
        this.regularize_bias = regularizeBias;
    }

    /**
     * Returns whether the bias term is regularized.
     *
     * @return {@code true} if the bias term is regularized
     */
    public boolean isRegularizeBias() {
        return regularize_bias;
    }

    /**
     * Sets the random number generator used during training.
     *
     * @param random the random number generator; must not be {@code null}
     */
    public void setRandom(Random random) {
        this.random = random;
    }

    @Override
    public Parameter clone() {
        Parameter clone = new Parameter(solverType, C, eps, max_iters, p);
        clone.weight = weight == null ? null : weight.clone();
        clone.weightLabel = weightLabel == null ? null : weightLabel.clone();
        clone.init_sol = init_sol;
        clone.p = p;
        clone.nu = nu;
        clone.regularize_bias = regularize_bias;
        clone.random = SerializationUtils.round(this.random);
        return clone;
    }
}
