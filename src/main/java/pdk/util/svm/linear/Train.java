package pdk.util.svm.linear;

import java.io.IOException;
import java.nio.file.Path;

import static pdk.util.svm.linear.Linear.atof;
import static pdk.util.svm.linear.Linear.atoi;
import static pdk.util.svm.linear.SolverType.*;

/**
 * Class to train a linear-SVM model.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 28 May 2026, 4:33 PM
 */
public class Train {

    static void main(String[] args) throws IOException, InvalidInputDataException {
        new Train().run(args);
    }

    /**
     * Bias value (default: 1), which can be overridden via the command-line argument `-B`.
     */
    private double bias = 1;
    /**
     * Whether to perform parameter search (option `-C`)
     */
    private boolean find_parameters = false;
    /**
     * Whether the user explicitly specified the `-c` parameter
     */
    private boolean C_specified = false;
    /**
     * Whether the user explicitly specified the `-p` parameter
     */
    private boolean P_specified = false;
    private boolean solver_specified = false;
    private boolean cross_validation = false;
    private String inputFilename;
    private String modelFilename;
    private int nr_fold;
    private Parameter param = null;
    private SVMDataset prob = null;

    private void do_find_parameters() {
        double start_C, start_p;
        if (C_specified)
            start_C = param.C;
        else
            start_C = -1.0;
        if (P_specified)
            start_p = param.p;
        else
            start_p = -1.0;

        System.out.printf("Doing parameter search with %d-fold cross validation.%n", nr_fold);
        ParameterSearchResult result = Linear.findParameters(prob, param, nr_fold, start_C, start_p);
        if (param.getSolverType() == L2R_LR || param.getSolverType() == L2R_L2LOSS_SVC)
            System.out.printf("Best C = %g  CV accuracy = %g%%\n", result.getBestC(), 100.0 * result.getBestScore());
        else if (param.getSolverType() == L2R_L2LOSS_SVR)
            System.out.printf("Best C = %g Best p = %g  CV MSE = %g\n", result.getBestC(), result.getBestP(), result.getBestScore());
    }

    private void do_cross_validation() {
        double total_error = 0;
        double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;
        double[] target = new double[prob.l];

        long start, stop;
        start = System.currentTimeMillis();
        Linear.crossValidation(prob, param, nr_fold, target);
        stop = System.currentTimeMillis();
        System.out.println("time: " + (stop - start) + " ms");

        if (param.solverType.isSupportVectorRegression()) {
            for (int i = 0; i < prob.l; i++) {
                double y = prob.y[i];
                double v = target[i];
                total_error += (v - y) * (v - y);
                sumv += v;
                sumy += y;
                sumvv += v * v;
                sumyy += y * y;
                sumvy += v * y;
            }
            System.out.printf("Cross Validation Mean squared error = %g%n", total_error / prob.l);
            System.out.printf("Cross Validation Squared correlation coefficient = %g%n", //
                    ((prob.l * sumvy - sumv * sumy) * (prob.l * sumvy - sumv * sumy))
                            / ((prob.l * sumvv - sumv * sumv) * (prob.l * sumyy - sumy * sumy)));
        } else {
            int total_correct = 0;
            for (int i = 0; i < prob.l; i++)
                if (target[i] == prob.y[i])
                    ++total_correct;

            System.out.printf("correct: %d%n", total_correct);
            System.out.printf("Cross Validation Accuracy = %g%%%n", 100.0 * total_correct / prob.l);
        }
    }

    private void exit_with_help() {
        System.out.printf("Usage: train [options] training_set_file [model_file]%n" //
                + "options:%n"
                + "-s type : set type of solver (default 1)%n"
                + "  for multi-class classification%n"
                + "        0 -- L2-regularized logistic regression (primal)%n"
                + "        1 -- L2-regularized L2-loss support vector classification (dual)%n"
                + "        2 -- L2-regularized L2-loss support vector classification (primal)%n"
                + "        3 -- L2-regularized L1-loss support vector classification (dual)%n"
                + "        4 -- support vector classification by Crammer and Singer%n"
                + "        5 -- L1-regularized L2-loss support vector classification%n"
                + "        6 -- L1-regularized logistic regression%n"
                + "        7 -- L2-regularized logistic regression (dual)%n"
                + "  for regression%n"
                + "       11 -- L2-regularized L2-loss support vector regression (primal)%n"
                + "       12 -- L2-regularized L2-loss support vector regression (dual)%n"
                + "       13 -- L2-regularized L1-loss support vector regression (dual)%n"
                + "  for outlier detection%n"
                + "       21 -- one-class support vector machine (dual)%n"
                + "-c cost : set the parameter C (default 1)%n"
                + "-p epsilon : set the epsilon in loss function of SVR (default 0.1)%n"
                + "-n nu : set the parameter nu of one-class SVM (default 0.5)%n"
                + "-e epsilon : set tolerance of termination criterion%n"
                + "       -s 0 and 2%n"
                + "               |f'(w)|_2 <= eps*min(pos,neg)/l*|f'(w0)|_2,%n"
                + "               where f is the primal function and pos/neg are # of%n"
                + "               positive/negative data (default 0.01)%n"
                + "       -s 11%n"
                + "               |f'(w)|_2 <= eps*|f'(w0)|_2 (default 0.0001)%n"
                + "       -s 1, 3, 4, 7, and 21%n"
                + "               Dual maximal violation <= eps; similar to libsvm (default 0.1 except 0.01 for -s 21)%n"
                + "      -s 5 and 6%n"
                + "               |f'(w)|_1 <= eps*min(pos,neg)/l*|f'(w0)|_1,%n"
                + "               where f is the primal function (default 0.01)%n"
                + "       -s 12 and 13%n"
                + "               |f'(alpha)|_1 <= eps |f'(alpha0)|,%n"
                + "               where f is the dual function (default 0.1)%n"
                + "-B bias : if bias >= 0, instance x becomes [x; bias]; if < 0, no bias term added (default -1)%n"
                + "-R : not regularize the bias; must with -B 1 to have the bias; DON'T use this unless you know what it is%n"
                + "       (for -s 0, 2, 5, 6, 11)%n"
                + "-wi weight: weights adjust the parameter C of different classes (see README for details)%n"
                + "-v n: n-fold cross validation mode%n"
                + "-C : find parameters (C for -s 0, 2 and C, p for -s 11)%n"
                + "-q : quiet mode (no outputs)%n");
        System.exit(1);
    }

    public SVMDataset getProblem() {
        return prob;
    }

    public double getBias() {
        return bias;
    }

    public Parameter getParameter() {
        return param;
    }

    public void parseCommandLine(String[] argv) {
        int i;

        // eps: see setting below
        param = new Parameter(L2R_L2LOSS_SVC_DUAL, 1, Double.POSITIVE_INFINITY, 0.1);
        // default values
        bias = -1;
        cross_validation = false;

        // parse options
        for (i = 0; i < argv.length; i++) {
            if (argv[i].charAt(0) != '-')
                break;
            if (++i >= argv.length)
                exit_with_help();
            switch (argv[i - 1].charAt(1)) {
                case 's':
                    param.solverType = SolverType.getById(atoi(argv[i]));
                    solver_specified = true;
                    break;
                case 'c':
                    param.setCost(atof(argv[i]));
                    C_specified = true;
                    break;
                case 'p': // SVR epsilon
                    P_specified = true;
                    param.setEpsilon(atof(argv[i]));
                    break;
                case 'n': // one-class nu
                    param.nu = atof(argv[i]);
                    break;
                case 'e':
                    param.setEPS(atof(argv[i]));
                    break;
                case 'B':
                    bias = atof(argv[i]);
                    break;
                case 'w':
                    int weightLabel = atoi(argv[i - 1].substring(2));
                    double weight = atof(argv[i]);
                    param.weightLabel = addToArray(param.weightLabel, weightLabel);
                    param.weight = addToArray(param.weight, weight);
                    break;
                case 'v':
                    cross_validation = true;
                    nr_fold = atoi(argv[i]);
                    if (nr_fold < 2) {
                        System.err.println("n-fold cross validation: n must >= 2");
                        exit_with_help();
                    }
                    break;
                case 'q':
                    i--;
                    Linear.disableDebugOutput();
                    break;
                case 'C':
                    find_parameters = true;
                    i--;
                    break;
                case 'R':
                    param.regularize_bias = false;
                    i--;
                    break;
                default:
                    System.err.println("unknown option");
                    exit_with_help();
            }
        }

        // determine filenames

        if (i >= argv.length)
            exit_with_help();

        inputFilename = argv[i];

        if (i < argv.length - 1)
            modelFilename = argv[i + 1];
        else {
            int p = argv[i].lastIndexOf('/');
            ++p; // whew...
            modelFilename = argv[i].substring(p) + ".model";
        }

        // default solver for parameter selection is L2R_L2LOSS_SVC
        if (find_parameters) {
            if (!cross_validation)
                nr_fold = 5;
            if (!solver_specified) {
                System.err.printf("Solver not specified. Using -s 2%n");
                param.setSolverType(L2R_L2LOSS_SVC);
            } else if (param.getSolverType() != L2R_LR
                    && param.getSolverType() != L2R_L2LOSS_SVC
                    && param.getSolverType() != L2R_L2LOSS_SVR) {
                System.err.printf("Warm-start parameter search only available for -s 0, -s 2 and -s 11%n");
                exit_with_help();
            }
        }

        if (param.eps == Double.POSITIVE_INFINITY) {
            switch (param.solverType) {
                case L2R_LR:
                case L2R_L2LOSS_SVC:
                    param.setEPS(0.01);
                    break;
                case L2R_L2LOSS_SVR:
                    param.setEPS(0.0001);
                    break;
                case L2R_L2LOSS_SVC_DUAL:
                case L2R_L1LOSS_SVC_DUAL:
                case MCSVM_CS:
                case L2R_LR_DUAL:
                    param.setEPS(0.1);
                    break;
                case L1R_L2LOSS_SVC:
                case L1R_LR:
                    param.setEPS(0.01);
                    break;
                case L2R_L1LOSS_SVR_DUAL:
                case L2R_L2LOSS_SVR_DUAL:
                    param.setEPS(0.1);
                    break;
                case ONECLASS_SVM:
                    param.setEPS(0.01);
                    break;
                default:
                    throw new IllegalStateException("unknown solver type: " + param.solverType);
            }
        }
    }

    private static int[] addToArray(int[] array, int newElement) {
        int length = array != null ? array.length : 0;
        int[] newArray = new int[length + 1];
        if (array != null && length > 0) {
            System.arraycopy(array, 0, newArray, 0, length);
        }
        newArray[length] = newElement;
        return newArray;
    }

    private static double[] addToArray(double[] array, double newElement) {
        int length = array != null ? array.length : 0;
        double[] newArray = new double[length + 1];
        if (array != null && length > 0) {
            System.arraycopy(array, 0, newArray, 0, length);
        }
        newArray[length] = newElement;
        return newArray;
    }

    private void run(String[] args) throws IOException, InvalidInputDataException {
        parseCommandLine(args);
        this.prob = SVMDataset.read(Path.of(inputFilename), bias);
        if (find_parameters) {
            do_find_parameters();
        } else if (cross_validation)
            do_cross_validation();
        else {
            SVMModel model = Linear.train(prob, param);
            model.save(Path.of(modelFilename));
        }
    }

    boolean isFindParameters() {
        return find_parameters;
    }

    int getNumFolds() {
        return nr_fold;
    }
}
