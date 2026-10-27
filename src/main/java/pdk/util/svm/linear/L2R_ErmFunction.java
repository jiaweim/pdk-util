package pdk.util.svm.linear;

import java.util.Arrays;

/**
 * Abstract implementation of the L2-regularized Empirical Risk Minimization (ERM) function.
 * <p>
 * min_w w^Tw/2 + \sum C_i \xi(w^Tx_i), where \xi() is the loss
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 28 May 2026, 4:00 PM
 */
abstract class L2R_ErmFunction implements Function {

    final double[] C;
    final SVMDataset prob;
    /**
     * Cache the predicted values of $w^T x_i$. The array length equals the number of samples $l$ to avoid redundant computation.
     */
    double[] wx;
    double[] tmp; // a working array
    /**
     * Cache $w^T w$, which is updated synchronously after each weight update to prevent repeated summation.
     */
    private double wTw;
    /**
     * Whether to regularize the bias term (the last weight component).
     * If set to false, the square of the bias term will be subtracted when calculating $w^T w$.
     */
    final boolean regularize_bias;

    /**
     *
     * @param prob      Problem definition, including the number of samples and the feature matrix stored in sparse representation.
     * @param parameter model parameters.
     * @param C         The penalty coefficient $C_i$ for each sample is typically equal to the
     *                  regularization parameter multiplied by the sample weight.
     */
    L2R_ErmFunction(SVMDataset prob, Parameter parameter, double[] C) {
        int l = prob.l;

        this.prob = prob;

        wx = new double[l];
        tmp = new double[l];
        this.C = C;
        this.regularize_bias = parameter.regularize_bias;
    }

    /**
     * Compute the product of matrix {@code X} and vector {@code v}, namely {@code Xv}.
     *
     * @param v  weight vector.
     * @param Xv The returned product result.
     */
    void Xv(double[] v, double[] Xv) {
        int i;
        int l = prob.l;
        Feature[][] x = prob.x;

        for (i = 0; i < l; i++) {
            Xv[i] = SparseOperator.dot(v, x[i]);
        }
    }

    /**
     * Calculate X^Tv, used to compute the gradient in optimization algorithms.
     *
     * @param v   derivative of the loss.
     * @param XTv array to store the gradient.
     */
    void XTv(double[] v, double[] XTv) {
        int l = prob.l;
        int w_size = get_nr_variable();
        Feature[][] x = prob.x;

        Arrays.fill(XTv, 0, w_size, 0);

        for (int i = 0; i < l; i++) {
            SparseOperator.axpy(v[i], x[i], XTv);
        }
    }

    /**
     * The specific loss functions (e.g., logistic loss, squared loss) are implemented by
     * subclasses via this method.
     *
     * @param i
     * @param wx_i
     * @return
     */
    protected abstract double C_times_loss(int i, double wx_i);

    @Override
    public double fun(double[] w) {
        int i;
        double f = 0;
        int l = prob.l;
        int w_size = get_nr_variable();

        wTw = 0;
        Xv(w, wx); // Compute predicted values.

        for (i = 0; i < w_size; i++)
            wTw += w[i] * w[i];
        if (!regularize_bias) // Subtract the square of the bias term if regularize_bias == false.
            wTw -= w[w_size - 1] * w[w_size - 1];
        for (i = 0; i < l; i++) // Iterate samples and accumulate `C_times_loss(i, wx[i])` to obtain the total loss.
            f += C_times_loss(i, wx[i]);
        f = f + 0.5 * wTw;

        return (f);
    }

    @Override
    public int get_nr_variable() {
        return prob.n;
    }

    // On entry *f must be the function value of w
    // On exit w is updated and *f is the new function value
    @Override
    public double linesearch_and_update(double[] w, double[] s, MutableDouble f, double[] g, double alpha) {
        int i;
        int l = prob.l;
        double sTs = 0;
        double wTs = 0;
        double gTs = 0;
        double eta = 0.01;
        int w_size = get_nr_variable();
        int max_num_linesearch = 20;
        double fold = f.get();
        Xv(s, tmp);

        for (i = 0; i < w_size; i++) {
            sTs += s[i] * s[i];
            wTs += s[i] * w[i];
            gTs += s[i] * g[i];
        }
        if (!regularize_bias) {
            // bias not used in calculating (w + \alpha s)^T (w + \alpha s)
            sTs -= s[w_size - 1] * s[w_size - 1];
            wTs -= s[w_size - 1] * w[w_size - 1];
        }

        int num_linesearch = 0;
        for (num_linesearch = 0; num_linesearch < max_num_linesearch; num_linesearch++) {
            double loss = 0;
            for (i = 0; i < l; i++) {
                double inner_product = tmp[i] * alpha + wx[i];
                loss += C_times_loss(i, inner_product);
            }
            f.set(loss + (alpha * alpha * sTs + wTw) / 2.0 + alpha * wTs);
            if (f.get() - fold <= eta * alpha * gTs) {
                for (i = 0; i < l; i++)
                    wx[i] += alpha * tmp[i];
                break;
            } else
                alpha *= 0.5;
        }

        if (num_linesearch >= max_num_linesearch) {
            f.set(fold);
            return 0;
        } else
            for (i = 0; i < w_size; i++)
                w[i] += alpha * s[i];

        wTw += alpha * alpha * sTs + 2 * alpha * wTs;
        return alpha;
    }

}
