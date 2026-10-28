package pdk.util.svm.linear;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static pdk.util.svm.linear.Linear.atof;
import static pdk.util.svm.linear.Linear.atoi;

/**
 * <p>Describes the dataset</p>
 * <p>
 * For example, if we have the following training data:
 * <pre>
 *  LABEL       ATTR1   ATTR2   ATTR3   ATTR4   ATTR5
 *  -----       -----   -----   -----   -----   -----
 *  1           0       0.1     0.2     0       0
 *  2           0       0.1     0.3    -1.2     0
 *  1           0.4     0       0       0       0
 *  2           0       0.1     0       1.4     0.5
 *  3          -0.1    -0.2     0.1     1.1     0.1
 *
 *  and bias = 1, then the components of dataset are:
 *
 *  l = 5
 *  n = 6
 *
 *  y -&gt; 1 2 1 2 3
 *
 *  x -&gt; [ ] -&gt; (2,0.1) (3,0.2) (6,1) (-1,?)
 *       [ ] -&gt; (2,0.1) (3,0.3) (4,-1.2) (6,1) (-1,?)
 *       [ ] -&gt; (1,0.4) (6,1) (-1,?)
 *       [ ] -&gt; (2,0.1) (4,1.4) (5,0.5) (6,1) (-1,?)
 *       [ ] -&gt; (1,-0.1) (2,-0.2) (3,0.1) (4,1.1) (5,0.1) (6,1) (-1,?)
 * </pre>
 */
public class SVMDataset {

    /**
     * the number of training data
     */
    public int l;

    /**
     * the number of features (including the bias feature if bias &gt;= 0)
     */
    public int n;

    /**
     * an array containing the target values
     */
    public double[] y;

    /**
     * array of sparse feature nodes
     */
    public Feature[][] x;

    /**
     * If bias &gt;= 0, we assume that one additional feature is added
     * to the end of each data instance
     */
    public double bias = -1;

    public SVMDataset() {}

    /**
     * Constructs a training problem from in-memory data.
     *
     * <p><b>Index convention.</b> Feature indices are 1-based and must lie in
     * {@code [1, maxIndex]}. If {@code bias >= 0}, a bias (intercept) term is
     * appended to every sample as an extra feature with index
     * {@code maxIndex + 1} and value {@code bias}; the feature dimension
     * {@link #n} then becomes {@code maxIndex + 1}. Passing {@code bias < 0}
     * (by convention {@code -1}) disables the bias term and {@code n} equals
     * {@code maxIndex}. Note that {@code bias == 0} still <em>enables</em> the
     * bias term.
     *
     * <p><b>Slot reservation.</b> When {@code bias >= 0}, every feature array in
     * {@code vx} must reserve its <em>last</em> slot as {@code null}; that slot
     * is filled in place with the bias node. Arrays that are empty or whose last
     * element is already occupied are rejected.
     *
     * <p><b>Side effect.</b> The feature arrays are <em>not</em> defensively
     * copied: {@code x[i]} shares the same array object as {@code vx.get(i)}, and
     * the last slot of each array is overwritten with the bias node when
     * {@code bias >= 0}. Callers must not reuse or mutate these arrays afterwards
     * if a pristine copy is needed.
     *
     * <p>Labels in {@code vy} are copied by value into {@code y} in the same
     * order, so {@code y[i]} corresponds to {@code vx.get(i)}.
     *
     * @param vy       per-sample labels; must not be null and its size must equal
     *                 {@code vx.size()}; interpreted as class labels for
     *                 classification or real-valued targets for regression
     * @param vx       per-sample feature arrays; must not be null, must not
     *                 contain null elements, and each array must satisfy the
     *                 slot-reservation rule above
     * @param maxIndex the largest feature index occurring in {@code vx};
     *                 must be non-negative
     * @param bias     value of the bias feature; {@code >= 0} enables the bias
     *                 term, negative values (conventionally {@code -1}) disable it
     * @throws NullPointerException     if {@code vy} or {@code vx} is null
     * @throws IllegalArgumentException if {@code vx.size() != vy.size()},
     *                                  {@code maxIndex} is negative, or any feature
     *                                  array violates the slot-reservation rule
     *                                  (empty, or last element not {@code null})
     */
    SVMDataset(List<Double> vy, List<Feature[]> vx, int maxIndex, double bias) {
        requireNonNull(vy);
        requireNonNull(vx);
        if (vx.size() != vy.size()) {
            throw new IllegalArgumentException("vx.size()=" + vx.size() + " != vy.size()=" + vy.size());
        }

        this.bias = bias;
        this.l = vy.size();
        this.n = maxIndex;
        if (bias >= 0) {
            this.n++;
        }
        this.x = new Feature[l][];
        for (int i = 0; i < l; i++) {
            x[i] = vx.get(i);
            if (bias >= 0) {
                if (x[i].length == 0 || x[i][x[i].length - 1] != null) {
                    throw new IllegalArgumentException("Feature array for sample " + i
                            + " must reserve the last slot for bias");
                }
                x[i][x[i].length - 1] = new Feature(maxIndex + 1, bias);
            }
        }
        y = new double[l];
        for (int i = 0; i < l; i++) {
            y[i] = vy.get(i);
        }
    }

    /**
     * Return the number of features (including the bias feature if bias &ge; 0)
     *
     * @return number of features
     */
    public int getNumberOfFeatures() {
        return n;
    }

    public void setNumberOfFeatures(int n) {
        this.n = n;
    }

    /**
     * Return the number of samples in this dataset.
     *
     * @return the number of samples.
     */
    public int size() {
        return l;
    }

    public void setSize(int size) {
        this.l = size;
    }

    /**
     * transpose matrix X from row format to column format
     *
     * @return
     * @since 2026-10-27⭐
     */
    public SVMDataset transpose() {
        int l = this.l;
        int n = this.n;
        int[] col_ptr = new int[n + 1];

        SVMDataset prob_col = new SVMDataset();
        prob_col.l = l;
        prob_col.n = n;
        prob_col.y = new double[l];
        prob_col.x = new Feature[n][];

        System.arraycopy(this.y, 0, prob_col.y, 0, l);

        for (int i = 0; i < l; i++) {
            for (Feature x : this.x[i]) {
                col_ptr[x.getIndex()]++;
            }
        }

        for (int i = 0; i < n; i++) {
            prob_col.x[i] = new Feature[col_ptr[i + 1]];
            col_ptr[i] = 0; // reuse the array to count the nr of elements
        }

        for (int i = 0; i < l; i++) {
            for (int j = 0; j < this.x[i].length; j++) {
                Feature x = this.x[i][j];
                int index = x.getIndex() - 1;
                prob_col.x[index][col_ptr[index]] = new Feature(i + 1, x.getValue());
                col_ptr[index]++;
            }
        }

        return prob_col;
    }

    /**
     * Read a dataset in LibSVM format from a file with default charset.
     *
     * @param path {@link Path} to the dataset file
     * @return {@link SVMDataset}
     * @throws InvalidInputDataException if the content violates the format rules
     * @throws IOException               if an I/O error occurs while reading or
     *                                   closing the stream
     * @since 2026-10-27⭐
     */
    public static SVMDataset read(Path path) throws InvalidInputDataException, IOException {
        return read(path, Charset.defaultCharset(), -1.0);
    }

    /**
     * Read a dataset in LibSVM format from a file with default charset.
     *
     * @param path {@link Path} to the dataset file
     * @param bias constant value of the bias feature; {@code >= 0}
     *             enables the bias term, negative values disable it
     * @return {@link SVMDataset}
     * @throws InvalidInputDataException if the content violates the format rules
     * @throws IOException               if an I/O error occurs while reading or
     *                                   closing the stream
     */
    public static SVMDataset read(Path path, double bias) throws InvalidInputDataException, IOException {
        return read(path, Charset.defaultCharset(), bias);
    }

    /**
     * Read a dataset from a file
     *
     * @param path    {@link Path} to the dataset file
     * @param charset the character encoding of the stream; must not be null
     *                (typically {@link java.nio.charset.StandardCharsets#UTF_8})
     * @param bias    constant value of the bias feature; {@code >= 0}
     *                enables the bias term, negative values disable it
     * @return {@link SVMDataset}
     * @throws InvalidInputDataException if the content violates the format rules
     * @throws IOException               if an I/O error occurs while reading or
     *                                   closing the stream
     */
    public static SVMDataset read(Path path, Charset charset, double bias) throws InvalidInputDataException, IOException {
        return read(Files.newInputStream(path), charset, bias);
    }

    /**
     * Read a dataset in LIBLINEAR's sparse text format from the given {@code inputstream}
     *
     * @param inputStream {@link InputStream} to read from
     * @param bias        constant value of the bias feature; {@code >= 0}
     *                    enables the bias term, negative values disable it
     * @return a fully populated {@link SVMDataset}; never {@code null} (an empty
     * dataset produces a problem with {@code l == 0} instead)
     * @throws InvalidInputDataException if the content violates the format rules
     * @throws IOException               if an I/O error occurs while reading or
     *                                   closing the stream
     */
    public static SVMDataset read(InputStream inputStream, double bias) throws InvalidInputDataException, IOException {
        return read(inputStream, Charset.defaultCharset(), bias);
    }

    /**
     * Reads a training dataset in LIBLINEAR's sparse text format from the given
     * stream and constructs a {@link SVMDataset}.
     *
     * <p><b>Format.</b> Each non-empty line encodes one sample:
     * <pre>&lt;label&gt; &lt;index1&gt;:&lt;value1&gt; &lt;index2&gt;:&lt;value2&gt; ...</pre>
     * Fields are separated by whitespace (space, tab, newline, carriage return,
     * form feed) or a colon. The label is parsed as a real number (class label
     * for classification, target value for regression). Feature indices must be
     * positive integers in strictly ascending order; feature values are reals.
     *
     * <p><b>Bias term.</b> If {@code bias >= 0}, every sample array gets an
     * additional trailing slot reserved for a bias feature of the given constant
     * value (as required by {@link #SVMDataset(List, List, int, double)}); a
     * negative value (conventionally {@code -1}) omits the bias term. The
     * feature dimension {@code n} is derived from the largest index encountered
     * and increased by one when the bias term is enabled.
     *
     * <p><b>Stream handling.</b> This method takes ownership of the stream: the
     * given {@code inputStream} (and any stream chained behind it) is
     * <em>closed</em> when this method returns, normally or exceptionally.
     * Do not pass {@code System.in} or a stream you intend to keep reading from
     * afterwards. Callers that need to preserve the stream should wrap it in a
     * close-shielding stream first.
     *
     * <p><b>Error reporting.</b> Malformed content fails fast with an
     * {@link InvalidInputDataException} carrying the 1-based line number and a
     * descriptive message: empty line, invalid label, invalid index
     * (non-positive or out of order), or invalid value. Note that a trailing
     * token not forming a complete {@code index:value} pair is silently ignored,
     * and the special literals {@code NaN} / {@code Infinity} are accepted as
     * labels or values.
     *
     * <p>An empty input yields a degenerate problem with {@code l == 0};
     * consider validating the result before training or cross-validating on it.
     *
     * @param inputStream the stream to read from; must not be null. It is closed
     *                    by this method, whether reading succeeds or fails
     * @param charset     the character encoding of the stream; must not be null
     *                    (typically {@link java.nio.charset.StandardCharsets#UTF_8})
     * @param bias        constant value of the bias feature; {@code >= 0}
     *                    enables the bias term, negative values disable it
     * @return a fully populated {@link SVMDataset}; never {@code null} (an empty
     * dataset produces a problem with {@code l == 0} instead)
     * @throws IOException               if an I/O error occurs while reading or
     *                                   closing the stream
     * @throws InvalidInputDataException if the content violates the format rules
     *                                   listed above
     * @throws NullPointerException      if {@code inputStream} or {@code charset} is null
     */
    public static SVMDataset read(InputStream inputStream, Charset charset, double bias) throws IOException, InvalidInputDataException {
        DoubleList vy = new DoubleArrayList();
        List<Feature[]> vx = new ArrayList<>();
        int maxIndex = 0;
        boolean hasBias = bias >= 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            String line;
            int lineNr = 0;
            while ((line = br.readLine()) != null) {
                lineNr++;
                int len = line.length();
                if (len == 0)
                    continue;

                // 1. skip leading space
                int pos = 0;
                while (pos < len && Character.isWhitespace(line.charAt(pos))) {
                    pos++;
                }
                if (pos == len) { // empty line
                    continue;
                }

                // 2. parse label
                int start = pos;
                while (pos < len && !Character.isWhitespace(line.charAt(pos))) {
                    pos++;
                }

                String token = line.substring(start, pos);
                try {
                    vy.add(atof(token));
                } catch (Exception e) {
                    throw new InvalidInputDataException("Invalid label: " + token, lineNr, e);
                }

                // 3. parse features
                List<Feature> features = new ArrayList<>();
                int prevIdx = 0;
                while (pos < len) {
                    // skip space between fields
                    while (pos < len && Character.isWhitespace(line.charAt(pos))) {
                        pos++;
                    }
                    if (pos == len)
                        break;

                    // 3a. index
                    start = pos;
                    while (pos < len && !Character.isWhitespace(line.charAt(pos))) {
                        pos++;
                    }
                    int colon = line.indexOf(':', start);
                    if (colon < start || colon >= pos) {
                        throw new InvalidInputDataException("Missing ':' at line ", lineNr);
                    }

                    token = line.substring(start, colon);
                    final int index;
                    try {
                        index = atoi(token);
                    } catch (NumberFormatException e) {
                        throw new InvalidInputDataException("invalid index: " + token, lineNr, e);
                    }
                    if (index <= 0)
                        throw new InvalidInputDataException("invalid index: " + index, lineNr);
                    if (index <= prevIdx)
                        throw new InvalidInputDataException("indices must be sorted in ascending order", lineNr);
                    prevIdx = index;

                    token = line.substring(colon + 1, pos);
                    final double value;
                    try {
                        value = atof(token);
                    } catch (Exception e) {
                        throw new InvalidInputDataException("invalid value: " + token, lineNr);
                    }
                    Feature feature = new Feature(index, value);
                    features.add(feature);
                }
                if (!features.isEmpty()) {
                    maxIndex = Math.max(maxIndex, features.getLast().getIndex());
                }
                int extra = hasBias ? 1 : 0;
                Feature[] array = new Feature[features.size() + extra];
                features.toArray(array);

                vx.add(array);
            }
        }

        return new SVMDataset(vy, vx, maxIndex, bias);
    }

    public static SVMDataset readTestDataset(InputStream inputStream, Charset charset, int numberOfFeatures,
            double bias) throws InvalidInputDataException, IOException {
        DoubleList vy = new DoubleArrayList();
        List<Feature[]> vx = new ArrayList<>();
        int n = bias >= 0 ? (numberOfFeatures + 1) : numberOfFeatures;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            String line;
            int lineNr = 0;
            while ((line = br.readLine()) != null) {
                lineNr++;
                int len = line.length();
                if (len == 0)
                    continue;

                // 1. skip leading space
                int pos = 0;
                while (pos < len && Character.isWhitespace(line.charAt(pos))) {
                    pos++;
                }
                if (pos == len) { // empty line
                    continue;
                }

                // 2. parse label
                int start = pos;
                while (pos < len && !Character.isWhitespace(line.charAt(pos))) {
                    pos++;
                }

                String token = line.substring(start, pos);
                try {
                    vy.add(atof(token));
                } catch (Exception e) {
                    throw new InvalidInputDataException("Invalid label: " + token, lineNr, e);
                }

                // 3. parse features
                List<Feature> x = new ArrayList<>();
                while (pos < len) {
                    // skip space between fields
                    while (pos < len && Character.isWhitespace(line.charAt(pos))) {
                        pos++;
                    }
                    if (pos == len)
                        break;

                    // 3a. index
                    start = pos;
                    while (pos < len && !Character.isWhitespace(line.charAt(pos))) {
                        pos++;
                    }
                    int colon = line.indexOf(':', start);
                    if (colon < start || colon >= pos) {
                        throw new InvalidInputDataException("Missing ':' at line ", lineNr);
                    }

                    token = line.substring(start, colon);
                    final int index;
                    try {
                        index = atoi(token);
                    } catch (NumberFormatException e) {
                        throw new InvalidInputDataException("invalid index: " + token, lineNr, e);
                    }

                    if (index <= 0)
                        throw new InvalidInputDataException("invalid index: " + index, lineNr);

                    token = line.substring(colon + 1, pos);
                    final double value;
                    try {
                        value = atof(token);
                    } catch (Exception e) {
                        throw new InvalidInputDataException("invalid value: " + token, lineNr);
                    }
                    // feature indices larger than those in training are not used
                    if (index <= numberOfFeatures) {
                        Feature node = new Feature(index, value);
                        x.add(node);
                    }
                }

                if (bias >= 0) {
                    Feature node = new Feature(n, bias);
                    x.add(node);
                }

                Feature[] nodes = new Feature[x.size()];
                nodes = x.toArray(nodes);
                vx.add(nodes);
            }
        }

        return new SVMDataset(vy, vx, numberOfFeatures, bias);
    }

    /**
     * Computes scaling parameters for this problem without modifying it.
     *
     * <p>See {@link #applyScale(ScaleParams)} for how the parameters are
     * applied, and {@link #scale(double, double)} for a one-shot convenience
     * method.
     *
     * @param lower the target lower bound for features
     * @param upper the target upper bound for features; must be > {@code lower}
     * @return the fitted parameters
     * @throws IllegalArgumentException if {@code upper <= lower}
     */
    public ScaleParams fitScale(double lower, double upper) {
        return fitScale(lower, upper, Double.NaN, Double.NaN);
    }

    /**
     * Computes scaling parameters for features and labels without modifying
     * this problem.
     *
     * <p>Feature scaling uses {@code [lower, upper]} as target range. If both
     * {@code yLower} and {@code yUpper} are non-{@code NaN}, labels are also
     * scaled from {@code [yMin, yMax]} to {@code [yLower, yUpper]}.
     *
     * <p><strong>Implicit zeros.</strong> Features that are absent from a sample
     * are treated as zero. If feature {@code k} is not present in every sample,
     * zero participates in the min/max computation for {@code k}. This matches
     * the semantics of {@code svm-scale}.
     *
     * <p><strong>Bias.</strong> When {@code bias >= 0}, the bias feature
     * ({@code index == n}) is ignored: it is neither scaled nor dropped.
     *
     * @param lower  the target lower bound for features
     * @param upper  the target upper bound for features; must be > {@code lower}
     * @param yLower the target lower bound for labels, or {@code NaN} to skip
     * @param yUpper the target upper bound for labels, or {@code NaN} to skip
     * @return the fitted parameters
     * @throws IllegalArgumentException if bounds are inconsistent
     */
    public ScaleParams fitScale(double lower, double upper,
            double yLower, double yUpper) {
        if (!(upper > lower)) {
            throw new IllegalArgumentException(
                    "upper must be > lower, got lower=" + lower + ", upper=" + upper);
        }
        boolean yScaling = !Double.isNaN(yLower) && !Double.isNaN(yUpper);
        if (yScaling && !(yUpper > yLower)) {
            throw new IllegalArgumentException(
                    "yUpper must be > yLower, got yLower=" + yLower + ", yUpper=" + yUpper);
        }

        int maxIndex = bias >= 0 ? n - 1 : n;
        double[] featMin = new double[maxIndex + 1];
        double[] featMax = new double[maxIndex + 1];
        int[] featCount = new int[maxIndex + 1];
        Arrays.fill(featMin, Double.POSITIVE_INFINITY);
        Arrays.fill(featMax, Double.NEGATIVE_INFINITY);

        for (int i = 0; i < l; i++) {
            for (Feature f : x[i]) {
                if (f == null) continue;
                int idx = f.getIndex();
                if (idx < 1 || idx > maxIndex)
                    continue;
                double v = f.getValue();
                if (v < featMin[idx]) featMin[idx] = v;
                if (v > featMax[idx]) featMax[idx] = v;
                featCount[idx]++;
            }
        }

        // 合并隐式零
        for (int k = 1; k <= maxIndex; k++) {
            if (featCount[k] == 0) {
                // 该特征未出现过：全为 0，min == max == 0，后续 apply 时丢弃
                featMin[k] = 0;
                featMax[k] = 0;
            } else if (featCount[k] < l) { // 该特征存在部分缺失
                // 至少一个样本隐式为零
                if (featMin[k] > 0)
                    featMin[k] = 0;
                if (featMax[k] < 0)
                    featMax[k] = 0;
            }
        }

        // 目标 y 的 min/max
        double yMin = 0, yMax = 0;
        if (yScaling) {
            yMin = Double.POSITIVE_INFINITY;
            yMax = Double.NEGATIVE_INFINITY;
            for (double v : y) {
                if (v < yMin) yMin = v;
                if (v > yMax) yMax = v;
            }
        }

        return new ScaleParams(lower, upper, yScaling, yLower, yUpper, yMin, yMax,
                featMin, featMax);
    }

    /**
     * Applies the given scaling parameters to this problem <strong>in
     * place</strong>, modifying both {@link #x} and, if enabled, {@link #y}.
     *
     * <p>For every explicitly stored feature {@code (k, v)}:
     * <ul>
     *   <li>If {@code k} is the bias feature, it is kept unchanged.</li>
     *   <li>If {@code min_k == max_k} (single-valued feature, no information),
     *       the entry is dropped.</li>
     *   <li>Otherwise {@code v} is mapped to {@code [lower, upper]}; if the
     *       scaled value is exactly zero, the entry is dropped to preserve
     *       sparsity.</li>
     * </ul>
     *
     * <p>When {@code params.isYScaling()} is {@code true}, every {@link #y}
     * value is mapped from {@code [yMin, yMax]} to {@code [yLower, yUpper]}.
     * If all labels are identical ({@code yMin == yMax}), labels are set to
     * {@code yLower} without interpolation.
     *
     * <p>The {@code Feature[]} arrays are reallocated; references held by the
     * caller before this call become stale.
     *
     * @param params the parameters produced by {@link #fitScale}
     */
    public void applyScale(ScaleParams params) {
        int maxIndex = bias >= 0 ? n - 1 : n;

        for (int i = 0; i < l; i++) {
            Feature[] row = x[i];
            List<Feature> newRow = new ArrayList<>(row.length);
            for (Feature f : row) {
                if (f == null) {
                    newRow.add(null);                 // 未填的 bias 槽位
                    continue;
                }
                int idx = f.getIndex();
                if (idx < 1 || idx > maxIndex) {
                    newRow.add(f);                    // bias：原样保留
                    continue;
                }
                double min = params.featureMin[idx];
                double max = params.featureMax[idx];
                if (min == max)
                    continue;             // 单值特征：丢弃
                double v = f.getValue();
                double scaled;
                if (v == min) scaled = params.lower;
                else if (v == max) scaled = params.upper;
                else scaled = params.lower + (params.upper - params.lower)
                            * (v - min) / (max - min);
                if (scaled != 0) {
                    newRow.add(new Feature(idx, scaled));
                }
            }
            x[i] = newRow.toArray(new Feature[0]);
        }

        if (params.yScaling) {
            if (params.yMax == params.yMin) {
                Arrays.fill(y, params.yLower);
            } else {
                double span = params.yMax - params.yMin;
                for (int i = 0; i < l; i++) {
                    if (y[i] == params.yMin) y[i] = params.yLower;
                    else if (y[i] == params.yMax) y[i] = params.yUpper;
                    else y[i] = params.yLower + (params.yUpper - params.yLower)
                                * (y[i] - params.yMin) / span;
                }
            }
        }
    }

    /**
     * Fits scaling parameters and applies them to this problem in place.
     *
     * <p>Equivalent to calling {@link #fitScale(double, double)} followed by
     * {@link #applyScale(ScaleParams)}, and returning the fitted parameters so
     * they can be persisted and reused on test data.
     *
     * @param lower the target lower bound for features
     * @param upper the target upper bound for features; must be > {@code lower}
     * @return the parameters that were applied
     * @throws IllegalArgumentException if {@code upper <= lower}
     */
    public ScaleParams scale(double lower, double upper) {
        ScaleParams p = fitScale(lower, upper);
        applyScale(p);
        return p;
    }

    /**
     * Fits and applies scaling parameters for features and labels in place.
     *
     * @param lower  the target lower bound for features
     * @param upper  the target upper bound for features; must be > {@code lower}
     * @param yLower the target lower bound for labels, or {@code NaN} to skip
     * @param yUpper the target upper bound for labels, or {@code NaN} to skip
     * @return the parameters that were applied
     * @throws IllegalArgumentException if bounds are inconsistent
     */
    public ScaleParams scale(double lower, double upper,
            double yLower, double yUpper) {
        ScaleParams p = fitScale(lower, upper, yLower, yUpper);
        applyScale(p);
        return p;
    }
}
