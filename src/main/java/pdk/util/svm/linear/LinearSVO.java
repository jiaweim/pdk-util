package pdk.util.svm.linear;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Oct 2026, 4:59 PM
 */
public class LinearSVO extends LinearSVM {

    public LinearSVO() {
        super();
        setSolverType(SolverType.ONECLASS_SVM);
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
        parameter_.nu = nu;
    }

    /**
     * Returns the nu parameter for one-class SVM.
     *
     * @return the current nu value
     */
    public double getNu() {
        return parameter_.nu;
    }


}
