package pdk.util.svm.linear;

/**
 * Linear Support Vector Regression.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 21 Oct 2026, 6:42 PM
 */
public class LinearSVR extends LinearSVM {

    public LinearSVR() {
        this(SolverType.L2R_L2LOSS_SVR);
    }

    public LinearSVR(SolverType solverType) {
        super();
        setSolverType(solverType);
    }

    public LinearSVR(SolverType solverType, double p) {
        setSolverType(solverType);
        setCost(1.0);
        setEpsilon(p);
    }

    public LinearSVR(SolverType solverType, double cost, double eps, double p) {
        setSolverType(solverType);
        setCost(cost);
        setEPS(eps);
        setEpsilon(p);
    }

    @Override
    public void setSolverType(SolverType solverType) {
        int id = solverType.getId();
        if (id < 11 || id > 13) {
            throw new IllegalArgumentException("SolverType " + solverType + " is invalid in regression");
        }
        super.setSolverType(solverType);
    }

    /**
     * Sets the epsilon parameter in the loss function of epsilon-SVR (default 0.1).
     *
     * @param epsilon SVR epsilon; must be greater than or equal to 0
     * @throws IllegalArgumentException if {@code p < 0}
     */
    public void setEpsilon(double epsilon) {
        if (epsilon < 0) {
            throw new IllegalArgumentException("epsilon must not be less than 0");
        }
        parameter_.p = epsilon;
    }

    /**
     * Returns the epsilon parameter in the loss function of epsilon-SVR (default 0.1).
     *
     * @return the current SVR epsilon value
     */
    public double getEpsilon() {
        return parameter_.p;
    }
}
