package pdk.util.svm.linear;

import org.junit.jupiter.api.Test;
import pdk.util.io.FileUtils;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pdk.util.svm.linear.SolverType.L2R_L2LOSS_SVR;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Oct 2026, 6:48 PM
 */
class LinearTest {

    private static final Random random = new Random(12345);

    static SVMModel createRandomModel(SolverType solverType) {
        SVMModel model = new SVMModel();
        model.solverType = solverType;
        model.bias = 2;
        model.label = new int[]{1, Integer.MAX_VALUE, 2};
        model.w = new double[model.label.length * 300];
        for (int i = 0; i < model.w.length; i++) {
            // precision should be at least 1e-4
            model.w[i] = Math.round(random.nextDouble() * 100000.0) / 10000.0;
        }

        // force at least one value to be zero
        model.w[random.nextInt(model.w.length)] = 0.0;
        model.w[random.nextInt(model.w.length)] = -0.0;

        model.nr_feature = model.w.length / model.label.length - 1;
        model.nr_class = model.label.length;
        if (solverType.isOneClass()) {
            model.rho = random.nextDouble();
        }
        return model;
    }

    private static SVMDataset createRandomProblem(int numClasses) {
        SVMDataset dataset = new SVMDataset();
        dataset.bias = -1;
        dataset.l = random.nextInt(100) + 1; // number of samples
        dataset.n = random.nextInt(100) + 1; // number of features
        dataset.x = new Feature[dataset.l][];
        dataset.y = new double[dataset.l];

        for (int i = 0; i < dataset.l; i++) {

            dataset.y[i] = random.nextInt(numClasses);

            Set<Integer> randomNumbers = new TreeSet<>();
            int num = random.nextInt(dataset.n) + 1;
            for (int j = 0; j < num; j++) {
                randomNumbers.add(random.nextInt(dataset.n) + 1);
            }
            List<Integer> randomIndices = new ArrayList<>(randomNumbers);
            Collections.sort(randomIndices);

            dataset.x[i] = new Feature[randomIndices.size()];
            for (int j = 0; j < randomIndices.size(); j++) {
                dataset.x[i][j] = new Feature(randomIndices.get(j), random.nextDouble());
            }
        }
        return dataset;
    }

    @Test
    void testAtoi() {
        assertEquals(25, Linear.atoi("+25"));
        assertEquals(-345345, Linear.atoi("-345345"));
        assertEquals(0, Linear.atoi("+0"));
        assertEquals(0, Linear.atoi("0"));
        assertEquals(Integer.MAX_VALUE, Linear.atoi("2147483647"));
        assertEquals(Integer.MIN_VALUE, Linear.atoi("-2147483648"));
    }

    @Test
    void testAtoiInvalidData() {

        assertThrows(NumberFormatException.class, () -> Linear.atoi("+"));
        assertThrows(NumberFormatException.class, () -> Linear.atoi("abc"));
        assertThrows(NumberFormatException.class, () -> Linear.atoi(" "));
    }

    @Test
    void testAtof() {
        assertEquals(25, Linear.atof("+25"));
        assertEquals(-25.12345678, Linear.atof("-25.12345678"));
        assertEquals(0.345345299, Linear.atof("0.345345299"));
    }

    @Test
    void testAtofInvalidData() {
        assertThrows(IllegalArgumentException.class, () -> Linear.atof("0.5t"));
    }

    @Test
    void testFindBestParametersOnDnaScaleDataSet_L2R_L2LOSS_SVR() throws Exception {
        URL url = LinearTest.class.getResource("dna.scale");
        SVMDataset problem = SVMDataset.read(FileUtils.toPath(url), -1);
        Parameter param = new Parameter(L2R_L2LOSS_SVR, 1, 0.0001, 0.1);
        ParameterSearchResult result = Linear.findParameters(problem, param, 5, -1, -1);

        assertEquals(0.015625, result.getBestC());
        assertEquals(0.29743037982927906, result.getBestScore(), 0.0000001);
        assertEquals(0.15, result.getBestP());
    }

    @Test
    void testGetVersion() {
        assertEquals(244, Linear.getVersion());
    }

}