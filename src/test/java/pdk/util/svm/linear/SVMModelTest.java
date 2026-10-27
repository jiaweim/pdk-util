package pdk.util.svm.linear;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pdk.util.io.IOUtils;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Strings.repeat;
import static org.junit.jupiter.api.Assertions.*;
import static pdk.util.svm.linear.LinearTest.createRandomModel;
import static pdk.util.svm.linear.SolverType.L2R_L2LOSS_SVR;
import static pdk.util.svm.linear.SolverType.L2R_LR;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Oct 2026, 6:52 PM
 */
class SVMModelTest {
    @Test
    void testEqualsAndHashCode_EmptyModel() {
        SVMModel model1 = new SVMModel();
        SVMModel model2 = new SVMModel();

        assertEquals(model1.hashCode(), model2.hashCode());
        assertEquals(model1, model2);
    }

    @Test
    void testEqualsAndHashCode_NegativeZeroesInWeightArray() {
        SVMModel model1 = new SVMModel();
        SVMModel model2 = new SVMModel();

        model1.w = new double[]{1.0, 2.0, -0.0, -0.00};
        model2.w = new double[]{1.0, 2.0, +0.0, -0.000};

        assertEquals(model1.hashCode(), model2.hashCode());
        assertEquals(model1, model2);
    }

    @Test
    void testLoadSaveModel(@TempDir Path tempDir) throws Exception {
        for (SolverType solverType : SolverType.values()) {
            SVMModel model = createRandomModel(solverType);

            Path tempFile = tempDir.resolve("modeltest-" + solverType);
            model.save(tempFile);

            SVMModel loadedModel = SVMModel.load(tempFile);
            assertEquals(model, loadedModel);
        }
    }


    @Test
    void testLoadEmptyModel(@TempDir Path tempDir) throws Exception {
        Path modelPath = tempDir.resolve("empty-model");

        List<String> lines = Arrays.asList("solver_type L2R_LR",
                "nr_class 2",
                "label 1 2",
                "nr_feature 0",
                "bias -1.0",
                "w");
        IOUtils.writeLines(modelPath, lines);

        SVMModel model = SVMModel.load(modelPath);
        assertEquals(L2R_LR, model.getSolverType());
        assertArrayEquals(new int[]{1, 2}, model.getLabels());
        assertEquals(2, model.getNrClass());
        assertEquals(0, model.getNrFeature());
        assertEquals(0, model.getFeatureWeights().length);
        assertEquals(-1.0, model.getBias());
    }

    @Test
    void testLoadSimpleModel(@TempDir Path tempDir) throws Exception {
        Path modelPath = tempDir.resolve("simple-model");

        List<String> lines = Arrays.asList("solver_type L2R_L2LOSS_SVR",
                "nr_class 2",
                "label 1 2",
                "nr_feature 6",
                "bias -1.0",
                "w",
                "0.1 0.2 0.3 ",
                "0.4 0.5 0.6 ");
        IOUtils.writeLines(modelPath, lines);

        SVMModel model = SVMModel.load(modelPath);

        assertEquals(L2R_L2LOSS_SVR, model.getSolverType());
        assertArrayEquals(new int[]{1, 2}, model.getLabels());
        assertEquals(2, model.getNrClass());
        assertEquals(6, model.getNrFeature());
        assertArrayEquals(new double[]{0.1, 0.2, 0.3, 0.4, 0.5, 0.6}, model.getFeatureWeights());
        assertEquals(-1.0, model.getBias(), 0.001);
    }

    @Test
    void testLoadIllegalModel(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("illegal-model");

        List<String> lines = Arrays.asList("solver_type L2R_L2LOSS_SVR",
                "nr_class 2",
                "label 1 2",
                "nr_feature 10",
                "bias -1.0",
                "w",
                "0.1 0.2 0.3 ",
                "0.4 0.5 " + repeat("0", 1024));
        IOUtils.writeLines(file, lines);

        String x = repeat("0", 128);

        assertThrows(RuntimeException.class,
                () -> SVMModel.load(file),
                "illegal weight in model file at index 5, with string content '" + x
                        + "', is not terminated with a whitespace character, or is longer than expected (128 characters max).");
    }
}