package pdk.util.svm.linear;

/**
 * Linear Support Vector Regression.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 21 Oct 2026, 6:42 PM
 */
public class LinearSVR extends LinearSVM {

    @Override
    public void setSolverType(SolverType solverType) {
        int id = solverType.getId();
        if (id < 11 || id > 13) {
            throw new IllegalArgumentException("SolverType " + solverType + " is invalid in regression");
        }
        super.setSolverType(solverType);
    }

    public void setEpsilon(double epsilon) {
        parameter_.setEpsilon(epsilon);
    }

}
