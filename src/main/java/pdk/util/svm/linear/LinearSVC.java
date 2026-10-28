package pdk.util.svm.linear;

/**
 * Linear Support Vector Classification.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 21 Oct 2026, 6:41 PM
 */
public class LinearSVC extends LinearSVM {

    public LinearSVC() {
        this(SolverType.L2R_L2LOSS_SVC_DUAL);
    }

    /**
     * Create a Linear Support Vector Classification with given {@link SolverType}
     *
     * @param solverType {@link SolverType} instance (0-7)
     */
    public LinearSVC(SolverType solverType) {
        super();
        setSolverType(solverType);
    }

    /**
     *
     * @param solverType
     */
    public void setSolverType(SolverType solverType) {
        int id = solverType.getId();
        if (id < 0 || id > 7) {
            throw new IllegalArgumentException("solverType id must be between 0 and 7 for classification");
        }
        super.setSolverType(solverType);
    }

    public ClassificationCVResult crossValidation(int numberOfFold) {
        if (dataset_ == null) {
            throw new IllegalStateException("Dataset has not been initialized");
        }

        double[] target = new double[dataset_.l];
        long start = System.currentTimeMillis();
        Linear.crossValidation(dataset_, parameter_, numberOfFold, target);
        long stop = System.currentTimeMillis();

        LOG.info("Time: {} ms", stop - start);

        int totalCorrect = 0;
        for (int i = 0; i < dataset_.l; i++) {
            if (target[i] == dataset_.y[i]) {
                totalCorrect++;
            }
        }

        return new ClassificationCVResult(dataset_.l, totalCorrect);
    }

    public ParameterSearchResult findParameters() {
        return findParameters(5, -1.0, -1.0);
    }

    public ParameterSearchResult findParameters(double startC) {
        return findParameters(5, startC, -1.0);
    }

    public ParameterSearchResult findParameters(int numberOfFold, double startC) {
        return findParameters(numberOfFold, startC, -1.0);
    }

    @Override
    public ParameterSearchResult findParameters(int numberOfFold, double startC, double startP) {
        if (numberOfFold < 2) {
            throw new IllegalArgumentException("Number of Folds must be at least 2");
        }
        if (dataset_ == null) {
            throw new IllegalStateException("Dataset has not been initialized");
        }
        SolverType solverType = parameter_.solverType;
        if (solverType != SolverType.L2R_LR && solverType != SolverType.L2R_L2LOSS_SVC) {
            throw new IllegalArgumentException("Solver Type " + solverType + " is not supported in find parameters");
        }
        return Linear.findParameters(dataset_, parameter_, numberOfFold, startC, startP);
    }
}
