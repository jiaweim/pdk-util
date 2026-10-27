package pdk.util.svm.linear;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;

import static java.util.Objects.requireNonNull;
import static pdk.util.svm.linear.SolverType.*;

/**
 * Linear SVM model.
 * <p>
 * Base class for training and evaluating linear SVC/SVR models using liblinear.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Oct 2026, 3:25 PM
 */
public class LinearSVM {

    protected static final Logger LOG = LoggerFactory.getLogger(LinearSVM.class);

    /**
     * The liblinear parameter object, Shared with the solver.
     */
    protected Parameter parameter_ = new Parameter();
    /**
     * The dataset currently loaded via {@link #setDataset(Path)}, or {@code null} if none.
     */
    protected SVMDataset dataset_;

    public LinearSVM() {
        parameter_.solverType = SolverType.L2R_L2LOSS_SVC_DUAL;
        parameter_.C = 1.0;
        parameter_.eps = Double.POSITIVE_INFINITY;
        parameter_.p = 0.1;
        Linear.disableDebugOutput();
    }

    /**
     * Sets the solver type.
     *
     * <p>Must be called before {@link #crossValidation(int)}.
     *
     * @param solverType the solver type; must not be {@code null}
     * @throws NullPointerException if {@code solverType} is {@code null}
     */
    public void setSolverType(SolverType solverType) {
        parameter_.solverType = requireNonNull(solverType);
        if (parameter_.eps == Double.POSITIVE_INFINITY) {
            switch (parameter_.solverType) {
                case L2R_LR:
                case L2R_L2LOSS_SVC:
                    parameter_.setEPS(0.01);
                    break;
                case L2R_L2LOSS_SVR:
                    parameter_.setEPS(0.0001);
                    break;
                case L2R_L2LOSS_SVC_DUAL:
                case L2R_L1LOSS_SVC_DUAL:
                case MCSVM_CS:
                case L2R_LR_DUAL:
                    parameter_.setEPS(0.1);
                    break;
                case L1R_L2LOSS_SVC:
                case L1R_LR:
                    parameter_.setEPS(0.01);
                    break;
                case L2R_L1LOSS_SVR_DUAL:
                case L2R_L2LOSS_SVR_DUAL:
                    parameter_.setEPS(0.1);
                    break;
                case ONECLASS_SVM:
                    parameter_.setEPS(0.01);
                    break;
                default:
                    throw new IllegalStateException("unknown solver type: " + parameter_.solverType);
            }
        }
    }

    /**
     * Sets the cost of constraints violation, i.e. the regularization
     * parameter {@code C}. Typical values range from 1 to 1000.
     *
     * <p>Larger values lead to more complex models but also risk
     * overfitting; smaller values shrink the model toward the trivial
     * solution.
     *
     * @param cost the cost value; must be strictly positive
     * @throws IllegalArgumentException if {@code cost <= 0}
     */
    public void setCost(double cost) {
        parameter_.setCost(cost);
    }

    /**
     * Sets the stopping tolerance of the iterative solver. Typical values
     * are around {@code 0.01} for both primal and dual solvers.
     *
     * <p>A smaller tolerance yields a more precise solution at the cost of
     * additional iterations.
     *
     * @param eps the stopping tolerance; must be strictly positive
     * @throws IllegalArgumentException if {@code eps <= 0}
     */
    public void setEPS(double eps) {
        parameter_.setEPS(eps);
    }

    public void setMaxIterations(int maxIterations) {
        parameter_.setMaxIterations(maxIterations);
    }

    /**
     * Sets whether the bias term participates in regularization.
     *
     * <p>By default, liblinear does not regularize the bias feature.
     * Enabling this option can be useful when the bias is expected to
     * carry meaningful magnitude.
     *
     * @param regularizeBias {@code true} to include the bias in
     *                       regularization, {@code false} otherwise
     */
    public void setRegularizeBias(boolean regularizeBias) {
        parameter_.setRegularizeBias(regularizeBias);
    }

    /**
     * Assigns per-class weights to overcome class imbalance.
     *
     * <p>The two arrays must have the same length. Each entry {@code i}
     * specifies that class {@code labels[i]} is weighted by
     * {@code weights[i]} during training, scaling its contribution to the
     * loss.
     *
     * @param weights the per-class weights
     * @param labels  the class labels corresponding to {@code weights};
     *                must have the same length as {@code weights}
     * @throws IllegalArgumentException if the array lengths differ
     */
    public void setWeights(double[] weights, int[] labels) {
        parameter_.setWeights(weights, labels);
    }

    public void setRandom(Random random) {
        parameter_.setRandom(random);
    }

    /**
     * Set the dataset used to train the model.
     *
     * @param datasetFile {@link Path} of the dataset.
     * @throws InvalidInputDataException if the content violates the format rules
     * @throws IOException               if an I/O error occurs while reading or closing the stream
     */
    public void setDataset(Path datasetFile) throws InvalidInputDataException, IOException {
        setDataset(datasetFile, false);
    }

    /**
     * Set the dataset used to train the model.
     *
     * @param datasetFile {@link Path} of the dataset.
     * @throws InvalidInputDataException if the content violates the format rules
     * @throws IOException               if an I/O error occurs while reading or closing the stream
     */
    public void setDataset(Path datasetFile, boolean addBias) throws InvalidInputDataException, IOException {
        double bias = addBias ? 1.0 : -1.0;
        this.dataset_ = SVMDataset.read(datasetFile, bias);
    }

    public void setQuietMode(boolean quietMode) {
        if (quietMode) {
            Linear.disableDebugOutput();
        } else {
            Linear.enableDebugOutput();
        }
    }

    /**
     * Runs {@code numberOfFold}-fold cross-validation on the currently
     * loaded dataset using the configured parameters.
     *
     * <p>The returned {@link CVResult} is a {@link RegressionCVResult}
     * when the solver type reports support vector regression, and a
     * {@link ClassificationCVResult} otherwise.
     *
     * <p><strong>Preconditions:</strong> {@link #setSolverType(SolverType)}
     * and {@link #setDataset(Path)} must have been called.
     *
     * @param numberOfFold the number of cross-validation folds; must be
     *                     at least 2 and at most the number of samples
     * @return the cross-validation result, never {@code null}
     * @throws NullPointerException     if the dataset or solver type has
     *                                  not been set
     * @throws IllegalArgumentException if {@code numberOfFold < 2}
     */
    public CVResult crossValidation(int numberOfFold) {
        requireNonNull(dataset_, "dataset is null");
        if (numberOfFold < 2) {
            throw new IllegalArgumentException("Number of Folds must be at least 2");
        }

        double[] target = new double[dataset_.l];
        long start = System.currentTimeMillis();
        Linear.crossValidation(dataset_, parameter_, numberOfFold, target);
        long stop = System.currentTimeMillis();

        LOG.info("Time: {} ms", stop - start);

        if (parameter_.solverType.isSupportVectorRegression()) {
            double totalError = 0;
            double sumv = 0; // sum of predicted values
            double sumy = 0; // sum of y
            double sumvv = 0;
            double sumyy = 0;
            double sumvy = 0;

            for (int i = 0; i < dataset_.l; i++) {
                double y = dataset_.y[i];
                double v = target[i];
                totalError += (v - y) * (v - y);
                sumv += v;
                sumy += y;
                sumvv += v * v;
                sumyy += y * y;
                sumvy += v * y;
            }
            double mse = totalError / dataset_.l;
            double scc = ((dataset_.l * sumvy - sumv * sumy) * (dataset_.l * sumvy - sumv * sumy))
                    / ((dataset_.l * sumvv - sumv * sumv) * (dataset_.l * sumyy - sumy * sumy));
            return new RegressionCVResult(dataset_.l, mse, scc);
        } else {
            int totalCorrect = 0;
            for (int i = 0; i < dataset_.l; i++) {
                if (target[i] == dataset_.y[i]) {
                    totalCorrect++;
                }
            }

            return new ClassificationCVResult(dataset_.l, totalCorrect);
        }
    }

    public ParameterSearchResult findParameters() {
        return findParameters(5, -1, -1);
    }

    public ParameterSearchResult findParameters(int numberOfFold, double startC, double startP) {
        if (numberOfFold < 2) {
            throw new IllegalArgumentException("Number of Folds must be at least 2");
        }
        SolverType solverType = parameter_.solverType;
        if (solverType != L2R_LR
                && solverType != L2R_L2LOSS_SVC
                && solverType != L2R_L2LOSS_SVR) {
            throw new IllegalArgumentException("Solver type " + solverType + " is not supported");
        }

        return Linear.findParameters(dataset_, parameter_, numberOfFold, startC, startP);
    }

    public SVMModel train() {
        return Linear.train(dataset_, parameter_);
    }
}
