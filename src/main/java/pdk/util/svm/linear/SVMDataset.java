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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

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

    /**
     * Return the number of samples in this dataset.
     *
     * @return the number of samples.
     */
    public int size() {
        return l;
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
        int lineNr = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                lineNr++;
                StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");
                String token;
                try {
                    token = st.nextToken();
                } catch (NoSuchElementException e) {
                    throw new InvalidInputDataException("empty line", lineNr, e);
                }

                // 第一列是 Y
                try {
                    vy.add(atof(token));
                } catch (NumberFormatException e) {
                    throw new InvalidInputDataException("invalid label: " + token, lineNr, e);
                }
                int m = st.countTokens() / 2;
                Feature[] x;
                if (bias >= 0) {
                    x = new Feature[m + 1]; // 为 bias 留出位置
                } else {
                    x = new Feature[m];
                }

                int indexBefore = 0;
                for (int j = 0; j < m; j++) {

                    token = st.nextToken();
                    int index;
                    try {
                        index = atoi(token);
                    } catch (NumberFormatException e) {
                        throw new InvalidInputDataException("invalid index: " + token, lineNr, e);
                    }

                    // assert that indices are valid and sorted
                    if (index <= 0)
                        throw new InvalidInputDataException("invalid index: " + index, lineNr);
                    if (index <= indexBefore)
                        throw new InvalidInputDataException("indices must be sorted in ascending order", lineNr);
                    indexBefore = index;

                    token = st.nextToken();
                    try {
                        double value = atof(token);
                        x[j] = new Feature(index, value);
                    } catch (NumberFormatException e) {
                        throw new InvalidInputDataException("invalid value: " + token, lineNr);
                    }
                }
                if (m > 0) {
                    maxIndex = Math.max(maxIndex, x[m - 1].getIndex());
                }
                vx.add(x);
            }
        }

        return new SVMDataset(vy, vx, maxIndex, bias);
    }
}
