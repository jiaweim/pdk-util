package pdk.util.svm.linear;

/**
 * A twice-differentiable objective function used by Newton-type solvers.
 *
 * <p>Implementations provide the function value, gradient, Hessian-vector
 * product, and a diagonal preconditioner. All vector arguments are dense
 * arrays of length {@link #get_nr_variable()}; output arguments ({@code g},
 * {@code Hs}, {@code M}) are <strong>pre-allocated by the caller</strong>
 * and are overwritten by the method.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Oct 2026, 1:13 PM
 */
interface Function {

    /**
     * Computes the objective value at {@code w}.
     *
     * @param w the point at which to evaluate the function; must have
     *          length {@link #get_nr_variable()}
     * @return the function value {@code f(w)}
     */
    double fun(double[] w);

    /**
     * Computes the gradient at {@code w} and writes it into {@code g}.
     *
     * <p>{@code g} is an output parameter and must be pre-allocated by the
     * caller with length {@link #get_nr_variable()}. The current point is
     * implicitly set to {@code w}, so that subsequent calls to
     * {@link #Hv(double[], double[])} and
     * {@link #get_diag_preconditioner(double[])} refer to {@code w}.
     *
     * @param w the point at which to evaluate the gradient; must have
     *          length {@link #get_nr_variable()}
     * @param g output parameter; on return, contains {@code ∇f(w)}
     */
    void grad(double[] w, double[] g);

    /**
     * Computes the Hessian-vector product {@code H * s} at the current
     * point, where {@code H} is the Hessian (or a positive-definite
     * approximation of it) of the objective.
     *
     * <p>{@code Hs} is an output parameter and must be pre-allocated by
     * the caller with length {@link #get_nr_variable()}.
     *
     * @param s  the vector to multiply by the Hessian; must have length
     *           {@link #get_nr_variable()}
     * @param Hs output parameter; on return, contains {@code H * s}
     */
    void Hv(double[] s, double[] Hs);

    /**
     * Returns the number of variables (the dimension of {@code w}).
     *
     * <p>This method must return a constant value for the lifetime of the
     * instance.
     *
     * @return the number of variables
     */
    int get_nr_variable();

    /**
     * Computes a diagonal preconditioner at the current point and writes
     * it into {@code M}.
     *
     * <p>{@code M} is an output parameter and must be pre-allocated by
     * the caller with length {@link #get_nr_variable()}. Each entry should
     * approximate the corresponding diagonal entry of the Hessian and be
     * strictly positive, so that the preconditioned conjugate gradient
     * method used by TRON remains well-defined.
     *
     * @param M output parameter; on return, contains the diagonal
     *          preconditioner
     */
    void get_diag_preconditioner(double[] M);

    /**
     * Performs a backtracking Armijo line search along direction {@code s}
     * starting from {@code w}, and, on success, overwrites {@code w} with
     * the accepted iterate and updates {@code f}.
     *
     * <p>The accepted step satisfies the sufficient-decrease condition
     * {@code f(w + α s) - f(w) <= η α (gᵀ s)} with {@code η = 0.01},
     * where {@code g} is the gradient at {@code w}. The step size
     * {@code α} starts at the given value and is halved up to 20 times
     * until the condition is met.
     *
     * <p><strong>Preconditions:</strong>
     * <ul>
     *   <li>{@code w}, {@code s}, and {@code g} are non-null and all have
     *       length {@link #get_nr_variable()};</li>
     *   <li>{@code f.get()} equals the objective value {@code f(w)} at
     *       the current {@code w};</li>
     *   <li>{@code g} is the gradient at {@code w};</li>
     *   <li>{@code s} is a descent direction, i.e. {@code gᵀ s < 0};</li>
     *   <li>{@code alpha > 0}.</li>
     * </ul>
     *
     * <p>On success, {@code w} is overwritten with {@code w + α s} and
     * {@code f} is set to {@code f(w + α s)}. On failure (20 halvings
     * without satisfying the condition), {@code w} is left unchanged,
     * {@code f} is restored to its original value, and {@code 0} is
     * returned. This implementation does <strong>not</strong> update
     * {@code g} on success, unlike some upstream variants.
     *
     * <p>Note: this default implementation is currently unused, mirroring
     * the corresponding upstream {@code function::linesearch_and_update}
     * in {@code newton.cpp}, which also appears to be unused.
     *
     * @param w     on input, the current point; on success, the accepted
     *              iterate; must have length {@link #get_nr_variable()}
     * @param s     the search direction; must have length
     *              {@link #get_nr_variable()} and satisfy {@code gᵀ s < 0}
     * @param f     on input, {@code f(w)}; on success, {@code f(w + α s)};
     *              on failure, restored to the original value
     * @param g     the gradient at {@code w}; must have length
     *              {@link #get_nr_variable()}
     * @param alpha the initial step size; must be positive
     * @return the accepted step size, or {@code 0} if the line search
     * failed
     */
    default double linesearch_and_update(double[] w, double[] s, MutableDouble f, double[] g, double alpha) {
        double gTs = 0;
        double eta = 0.01;
        int n = get_nr_variable();
        int max_num_linesearch = 20;
        double[] w_new = new double[n];
        double fold = f.get();

        for (int i = 0; i < n; i++)
            gTs += s[i] * g[i];

        int num_linesearch = 0;
        for (num_linesearch = 0; num_linesearch < max_num_linesearch; num_linesearch++) {
            for (int i = 0; i < n; i++)
                w_new[i] = w[i] + alpha * s[i];
            f.set(fun(w_new));
            if (f.get() - fold <= eta * alpha * gTs)
                break;
            else
                alpha *= 0.5;
        }

        if (num_linesearch >= max_num_linesearch) {
            f.set(fold);
            return 0;
        } else
            System.arraycopy(w_new, 0, w, 0, n);

        return alpha;
    }
}
