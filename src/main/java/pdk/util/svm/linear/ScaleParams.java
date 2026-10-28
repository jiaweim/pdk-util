package pdk.util.svm.linear;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 28 Oct 2026, 3:10 PM
 */
public class ScaleParams {

    protected final double lower;
    protected final double upper;

    protected final boolean yScaling;
    protected final double yLower;
    protected final double yUpper;

    protected final double yMin;
    protected final double yMax;

    /**
     * 1-based index; index 0 unused
     */
    protected final double[] featureMin;
    /**
     * 1-based index, index 0 unused
     */
    protected final double[] featureMax;

    public ScaleParams(double lower, double upper,
            boolean yScaling, double yLower, double yUpper, double yMin, double yMax,
            double[] featureMin, double[] featureMax) {
        this.lower = lower;
        this.upper = upper;
        this.yScaling = yScaling;
        this.yLower = yLower;
        this.yUpper = yUpper;
        this.yMin = yMin;
        this.yMax = yMax;
        this.featureMin = featureMin;
        this.featureMax = featureMax;
    }

    public double getLower() {return lower;}

    public double getUpper() {return upper;}

    public boolean isYScaling() {return yScaling;}

    public double getYLower() {return yLower;}

    public double getYUpper() {return yUpper;}

    public double getYMin() {return yMin;}

    public double getYMax() {return yMax;}

    /**
     * Returns the min value observed for feature {@code index}; {@code index} is 1-based.
     */
    public double getFeatureMin(int index) {return featureMin[index];}

    /**
     * Returns the max value observed for feature {@code index}; {@code index} is 1-based.
     */
    public double getFeatureMax(int index) {return featureMax[index];}

    /**
     * Returns the largest valid feature index, i.e. {@code featureMin.length - 1}.
     */
    public int getMaxIndex() {return featureMin.length - 1;}

    /**
     * Saves these parameters to a text file in the {@code svm-scale} format.
     *
     * @param file the target file
     * @throws IOException if writing fails
     */
    public void save(Path file) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (yScaling) {
            sb.append("y\n");
            sb.append(String.format("%.16g %.16g%n", yLower, yUpper));
            sb.append(String.format("%.16g %.16g%n", yMin, yMax));
        }
        sb.append("x\n");
        sb.append(String.format("%.16g %.16g%n", lower, upper));
        for (int k = 1; k < featureMin.length; k++) {
            if (featureMin[k] != featureMax[k]) {
                sb.append(String.format("%d %.16g %.16g%n", k, featureMin[k], featureMax[k]));
            }
        }
        Files.writeString(file, sb.toString());
    }

    /**
     * Loads parameters from a text file in the {@code svm-scale} format.
     *
     * @param file the source file
     * @return the loaded parameters
     * @throws IOException if reading fails or the file is malformed
     */
    public static ScaleParams load(Path file) throws IOException {
        List<String> lines = Files.readAllLines(file);
        int pos = 0;
        boolean yScaling = false;
        double yLower = 0, yUpper = 0, yMin = 0, yMax = 0;

        if (lines.get(pos).trim().equals("y")) {
            pos++;
            String[] p = lines.get(pos++).trim().split("\\s+");
            yLower = Double.parseDouble(p[0]);
            yUpper = Double.parseDouble(p[1]);
            p = lines.get(pos++).trim().split("\\s+");
            yMin = Double.parseDouble(p[0]);
            yMax = Double.parseDouble(p[1]);
            yScaling = true;
        }
        if (!lines.get(pos++).trim().equals("x")) {
            throw new IOException("Expected 'x' marker after y section");
        }
        String[] p = lines.get(pos++).trim().split("\\s+");
        double lower = Double.parseDouble(p[0]);
        double upper = Double.parseDouble(p[1]);

        int maxIdx = 0;
        for (int i = pos; i < lines.size(); i++) {
            String s = lines.get(i).trim();
            if (s.isEmpty()) continue;
            int idx = Integer.parseInt(s.split("\\s+")[0]);
            if (idx > maxIdx) maxIdx = idx;
        }

        double[] fMin = new double[maxIdx + 1];
        double[] fMax = new double[maxIdx + 1];
        for (int i = pos; i < lines.size(); i++) {
            String s = lines.get(i).trim();
            if (s.isEmpty()) continue;
            String[] tokens = s.split("\\s+");
            int idx = Integer.parseInt(tokens[0]);
            fMin[idx] = Double.parseDouble(tokens[1]);
            fMax[idx] = Double.parseDouble(tokens[2]);
        }
        return new ScaleParams(lower, upper, yScaling, yLower, yUpper, yMin, yMax, fMin, fMax);
    }
}
