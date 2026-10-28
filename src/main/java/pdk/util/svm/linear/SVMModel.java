package pdk.util.svm.linear;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

import static pdk.util.svm.linear.Linear.*;
import static pdk.util.svm.linear.SolverType.MCSVM_CS;


/**
 * <p>Model stores the model obtained from the training procedure</p>
 *
 * <p>use {@link SVMModel#load(Path)} and {@link SVMModel#save(Path)} to load/save it</p>
 */
public final class SVMModel implements Serializable {

    private static final long serialVersionUID = -6456047576741854834L;

    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    double bias;

    /**
     * label of each class
     */
    int[] label;

    int nr_class;

    int nr_feature;

    SolverType solverType;

    /**
     * feature weight array
     */
    double[] w;

    /**
     * one-class SVM only
     */
    double rho;

    /**
     * @return number of classes
     */
    public int getNrClass() {
        return nr_class;
    }

    /**
     * @return number of features
     */
    public int getNrFeature() {
        return nr_feature;
    }

    public int[] getLabels() {
        return Arrays.copyOf(label, nr_class);
    }

    public SolverType getSolverType() {
        return solverType;
    }

    /**
     * The array w gives feature weights; its size is
     * nr_feature*nr_class but is nr_feature if nr_class = 2. We use one
     * against the rest for multi-class classification, so each feature
     * index corresponds to nr_class weight values. Weights are
     * organized in the following way
     *
     * <pre>
     * +------------------+------------------+------------+
     * | nr_class weights | nr_class weights |  ...
     * | for 1st feature  | for 2nd feature  |
     * +------------------+------------------+------------+
     * </pre>
     * <p>
     * If bias &gt;= 0, x becomes [x; bias]. The number of features is
     * increased by one, so w is a (nr_feature+1)*nr_class array. The
     * value of bias is stored in the variable bias.
     *
     * @return a <b>copy of</b> the feature weight array as described
     * @see #getBias()
     */
    public double[] getFeatureWeights() {
        return Arrays.copyOf(w, w.length);
    }

    /**
     * @return true for logistic regression solvers
     */
    public boolean isProbabilityModel() {
        return solverType.isLogisticRegressionSolver();
    }

    /**
     * @see #getFeatureWeights()
     */
    public double getBias() {
        return bias;
    }

    private double get_w_value(int idx, int label_idx) {
        if (idx < 0 || idx > nr_feature) {
            return 0;
        }
        if (solverType.isSupportVectorRegression() || solverType.isOneClass()) {
            return w[idx];
        } else {
            if (label_idx < 0 || label_idx >= nr_class) {
                return 0;
            }
            if (nr_class == 2 && solverType != MCSVM_CS) {
                if (label_idx == 0) {
                    return w[idx];
                } else {
                    return -w[idx];
                }
            } else {
                return w[idx * nr_class + label_idx];
            }
        }
    }

    /**
     * This function gives the coefficient for the feature with feature index =
     * feat_idx and the class with label index = label_idx. Note that feat_idx
     * starts from 1, while label_idx starts from 0. If feat_idx is not in the
     * valid range (1 to nr_feature), then a zero value will be returned. For
     * classification models, if label_idx is not in the valid range (0 to
     * nr_class-1), then a zero value will be returned; for regression models,
     * label_idx is ignored.
     *
     * @since 1.95
     */
    // feat_idx: starting from 1 to nr_feature
    // label_idx: starting from 0 to nr_class-1 for classification models;
    //            for regression and one-class SVM models, label_idx is
    //            ignored.
    public double getDecfunCoef(int featIdx, int labelIdx) {
        if (featIdx > nr_feature) {
            return 0;
        }
        return get_w_value(featIdx - 1, labelIdx);
    }

    /**
     * This function gives the bias term corresponding to the class with the
     * label_idx. For classification models, if label_idx is not in a valid range
     * (0 to nr_class-1), then a zero value will be returned; for regression
     * models, label_idx is ignored.
     *
     * @since 1.95
     */
    public double getDecfunBias(int labelIdx) {
        if (solverType.isOneClass()) {
            throw new IllegalArgumentException("Can not be called for a one-class SVM model");
        }
        int biasIdx = nr_feature;
        if (bias <= 0) {
            return 0;
        } else {
            return bias * get_w_value(biasIdx, labelIdx);
        }
    }

    /**
     * This function gives rho, the bias term used in one-class SVM only.
     * <p>
     * This function can only be called for a one-class SVM model.
     *
     * @since 2.40
     */
    public double getDecfunRho() {
        if (!solverType.isOneClass()) {
            throw new IllegalArgumentException("Can be called only for a one-class SVM model");
        }
        return rho;
    }

    /**
     *
     * @param x          feature values
     * @param dec_values output decision values
     * @return the label
     */
    public double predictValues(Feature[] x, double[] dec_values) {
        return Linear.predictValues(this, x, dec_values);
    }

    /**
     * Predicts the class label of a single instance and writes the estimated
     * class probabilities into {@code prob_estimates}.
     *
     * <p>Probability output is only supported for logistic regression models.
     * For models trained with an SVM solver, the decision values are signed
     * distances to the hyperplane and carry no probability semantics; calling
     * this method on such a model throws {@link IllegalArgumentException}.
     *
     * <p>{@code prob_estimates[i]} corresponds to the class label at index
     * {@code i} of {@link SVMModel#getLabels()}, not necessarily to the integer
     * {@code i}. Callers that need to map a probability back to a label must
     * consult {@link SVMModel#getLabels()}. The entries sum to 1.
     *
     * <p>{@code prob_estimates} is an output parameter that must be
     * pre-allocated by the caller with length at least
     * {@link SVMModel#getNrClass()}; a shorter array causes an
     * {@link ArrayIndexOutOfBoundsException
     *
     * @param x              the feature vector of the instance to predict; must
     *                       not be {@code null}
     * @param prob_estimates the output array for per-class probabilities; must
     *                       be non-null and have length at least {@link SVMModel#getNrClass()}
     * @return the predicted class label
     * @throws IllegalArgumentException if model is not probabilistic (see {@link SVMModel#isProbabilityModel()})
     */
    public double predictProbability(Feature[] x, double[] probEstimates) throws IllegalArgumentException {
        if (!this.isProbabilityModel()) {
            StringBuilder sb = new StringBuilder("probability output is only supported for logistic regression");
            sb.append(". This is currently only supported by the following solvers: ");
            int i = 0;
            for (SolverType solverType : SolverType.values()) {
                if (solverType.isLogisticRegressionSolver()) {
                    if (i++ > 0) {
                        sb.append(", ");
                    }
                    sb.append(solverType.name());
                }
            }
            throw new IllegalArgumentException(sb.toString());
        }
        int nr_w; // number of weights
        if (nr_class == 2) // 二分类，只训练一个权重向量，决策函数为 sign(w^T x)
            nr_w = 1;
        else
            nr_w = nr_class; // One-vs-Rest

        double label = Linear.predictValues(this, x, probEstimates);
        for (int i = 0; i < nr_w; i++)
            probEstimates[i] = 1 / (1 + Math.exp(-probEstimates[i])); // sigmoid 变换

        if (nr_class == 2) // for binary classification
            probEstimates[1] = 1. - probEstimates[0];
        else {
            double sum = 0;
            for (int i = 0; i < nr_class; i++)
                sum += probEstimates[i];

            for (int i = 0; i < nr_class; i++)
                probEstimates[i] = probEstimates[i] / sum;
        }

        return label;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Model");
        sb.append(" bias=").append(bias);
        sb.append(" nr_class=").append(nr_class);
        sb.append(" nr_feature=").append(nr_feature);
        sb.append(" solverType=").append(solverType);
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(bias, nr_class, nr_feature, getSolverType(), rho);
        result = 31 * result + Arrays.hashCode(label);
        result = 31 * result + arrayHashCode(w);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SVMModel model = (SVMModel) o;
        return Double.compare(model.getBias(), getBias()) == 0
                && nr_class == model.nr_class
                && nr_feature == model.nr_feature
                && Double.compare(model.rho, rho) == 0
                && Arrays.equals(label, model.label)
                && getSolverType() == model.getSolverType()
                && arrayEquals(w, model.w);
    }

    /**
     * don't use {@link Arrays#equals(double[], double[])} here, cause 0.0 and -0.0 should be handled the same
     *
     * @see #save(Writer)
     */
    private static boolean arrayEquals(double[] a, double[] a2) {
        if (a == a2)
            return true;
        if (a == null || a2 == null)
            return false;

        int length = a.length;
        if (a2.length != length)
            return false;

        for (int i = 0; i < length; i++)
            if (a[i] != a2[i])
                return false;

        return true;
    }

    /**
     * see {@link Arrays#hashCode(double[])} but treat 0.0 and -0.0 the same
     */
    private static int arrayHashCode(double[] w) {
        if (w == null)
            return 0;

        int result = 1;
        for (double element : w) {
            if (element == -0.0) {
                element = 0.0;
            }
            result = 31 * result + Double.hashCode(element);
        }
        return result;
    }

    public void save(Path modelPath) throws IOException {
        try (Writer writer = Files.newBufferedWriter(modelPath, FILE_CHARSET)) {
            save(writer);
        }
    }

    /**
     * Writes the model to the modelOutput. It uses {@link java.util.Locale#ENGLISH} for number formatting.
     *
     * <p><b>Note: The modelOutput is closed after writing or in case of an exception.</b></p>
     */
    public void save(Writer writer) throws IOException {
        int w_size = nr_feature;
        if (bias >= 0)
            w_size++;

        int nr_w = this.nr_class;
        if (this.nr_class == 2 && this.solverType != MCSVM_CS)
            nr_w = 1;

        try (Formatter formatter = new Formatter(writer, DEFAULT_LOCALE)) {
            printf(formatter, "solver_type %s\n", this.solverType.name());
            printf(formatter, "nr_class %d\n", this.nr_class);

            if (this.label != null) {
                printf(formatter, "label");
                for (int i = 0; i < this.nr_class; i++) {
                    printf(formatter, " %d", this.label[i]);
                }
                printf(formatter, "\n");
            }

            printf(formatter, "nr_feature %d\n", nr_feature);
            printf(formatter, "bias %.17g\n", this.bias);

            if (this.solverType.isOneClass())
                printf(formatter, "rho %.17g\n", this.rho);

            printf(formatter, "w\n");
            for (int i = 0; i < w_size; i++) {
                for (int j = 0; j < nr_w; j++) {
                    double value = this.w[i * nr_w + j];

                    /* this optimization is the reason for {@link Model#equals(double[], double[])} */
                    if (value == 0.0) {
                        printf(formatter, "%d ", 0);
                    } else {
                        printf(formatter, "%.17g ", value);
                    }
                }
                printf(formatter, "\n");
            }

            formatter.flush();
            IOException ioException = formatter.ioException();
            if (ioException != null)
                throw ioException;
        }
    }

    /**
     * Loads the model from the file with ISO-8859-1 charset. It uses {@link java.util.Locale#ENGLISH} for number
     * formatting.
     *
     * @since 2026-10-27⭐
     */
    public static SVMModel load(Path modelPath) throws IOException {
        try (Reader inputReader = Files.newBufferedReader(modelPath, FILE_CHARSET)) {
            return load(inputReader);
        }
    }

    /**
     * Loads the model from inputReader. It uses {@link java.util.Locale#ENGLISH} for number formatting.
     *
     * <p>Note: The inputReader is <b>NOT closed</b> after reading or in case of an exception.</p>
     */
    public static SVMModel load(Reader inputReader) throws IOException {
        SVMModel model = new SVMModel();

        model.label = null;

        Pattern whitespace = Pattern.compile("\\s+");

        BufferedReader reader = null;
        if (inputReader instanceof BufferedReader) {
            reader = (BufferedReader) inputReader;
        } else {
            reader = new BufferedReader(inputReader);
        }

        String line;
        while ((line = reader.readLine()) != null) {
            String[] split = whitespace.split(line);
            if (split[0].equals("solver_type")) {
                model.solverType = SolverType.valueOf(split[1]);
            } else if (split[0].equals("nr_class")) {
                model.nr_class = atoi(split[1]);
            } else if (split[0].equals("nr_feature")) {
                model.nr_feature = atoi(split[1]);
            } else if (split[0].equals("bias")) {
                model.bias = atof(split[1]);
            } else if (split[0].equals("rho")) {
                model.rho = atof(split[1]);
            } else if (split[0].equals("w")) {
                break;
            } else if (split[0].equals("label")) {
                model.label = new int[model.nr_class];
                for (int i = 0; i < model.nr_class; i++) {
                    model.label[i] = atoi(split[i + 1]);
                }
            } else {
                throw new RuntimeException("unknown text in model file: [" + line + "]");
            }
        }

        int w_size = model.nr_feature;
        if (model.bias >= 0)
            w_size++;

        int nr_w = model.nr_class;
        if (model.nr_class == 2 && model.solverType != MCSVM_CS)
            nr_w = 1;

        model.w = new double[w_size * nr_w];
        int[] buffer = new int[128];

        for (int i = 0; i < w_size; i++) {
            for (int j = 0; j < nr_w; j++) {
                int b = 0;
                while (true) {
                    int ch = reader.read();
                    if (ch == -1) {
                        throw new EOFException("unexpected EOF");
                    }
                    if (ch == ' ') {
                        model.w[i * nr_w + j] = atof(new String(buffer, 0, b));
                        break;
                    } else {
                        if (b >= buffer.length) {
                            throw new RuntimeException("illegal weight in model file at index " + (i * nr_w + j) + ", with string content '" +
                                    new String(buffer, 0, buffer.length) + "', is not terminated " +
                                    "with a whitespace character, or is longer than expected (" + buffer.length + " characters max).");
                        }
                        buffer[b++] = ch;
                    }
                }
            }
        }

        return model;
    }
}
