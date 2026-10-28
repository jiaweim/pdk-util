package pdk.util.svm.linear;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pdk.util.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static pdk.util.svm.linear.SolverType.L2R_L2LOSS_SVC;
import static pdk.util.svm.linear.SolverType.L2R_LR;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Oct 2026, 3:14 PM
 */
class TrainTest {

    private final SVMModel testModel = LinearTest.createRandomModel();
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        Linear.setDebugOutput(new PrintStream(byteArrayOutputStream));
        assertTrue(testModel.getNrClass() >= 2);
        assertTrue(testModel.getNrFeature() >= 10);
    }

    @Test
    void testTrainAndPredict(@TempDir Path tempDir) throws Exception {
        String modelFile = tempDir.resolve("model").toString();
        Train.main(new String[]{"-s", "0", "src/test/resources/pdk/util/svm/linear/dna.scale", modelFile});

        Path predictionsFile = tempDir.resolve("predictions");
        Predict.main(new String[]{"-b", "1", "src/test/resources/pdk/util/svm/linear/dna.scale.t", modelFile, predictionsFile.toString()});
        List<String> predictions = Files.readAllLines(predictionsFile, Linear.FILE_CHARSET);
        assertEquals(1187, predictions.size());
        assertEquals("labels 3 1 2", predictions.get(0));
        System.out.println(predictions.getLast());
        System.out.println("Last:"+ predictions.getLast());

        String loggedString = byteArrayOutputStream.toString();
        System.out.println("final: "+ loggedString);
        assertTrue(Pattern.matches("Accuracy = 95[.,]0253% \\(1127/1186\\)", loggedString));
    }

    @Test
    void testCrossValidationOnIris() throws InvalidInputDataException, IOException {
        URL url = TrainTest.class.getResource("iris.scale");
        Path dataPath = FileUtils.toPath(url);
        Linear.disableDebugOutput();
        for (SolverType solverType : SolverType.values()) {
            if (solverType.isOneClass())
                continue;
            if (solverType.isSupportVectorRegression()) {
                LinearSVR linearSVR = new LinearSVR();
                linearSVR.setSolverType(solverType);
                linearSVR.setDataset(dataPath);
                CVResult cvResult = linearSVR.crossValidation(5);
                System.out.println(cvResult);
            } else {
                LinearSVC linearSVC = new LinearSVC();
                linearSVC.setSolverType(solverType);
                linearSVC.setDataset(dataPath);

                CVResult cvResult = linearSVC.crossValidation(5);
                System.out.println(cvResult);
            }
        }
    }

    @Test
    void testDoCrossValidationOnIrisDataSet() throws Exception {
        Linear.disableDebugOutput();
        for (SolverType solver : SolverType.values()) {
            if (solver.isOneClass()) {
                continue;
            }
            Train.main(new String[]{"-v", "5", "-s", "" + solver.getId(), "src/test/resources/pdk/util/svm/linear/iris.scale"});
        }
    }

    // "-C", "src/test/resources/iris.scale"
    @Test
    void testFindBestCOnIris() throws InvalidInputDataException, IOException {
        URL url = TrainTest.class.getResource("iris.scale");
        Path dataPath = FileUtils.toPath(url);

        LinearSVC linearSVC = new LinearSVC();
        linearSVC.setDataset(dataPath);
        linearSVC.setSolverType(L2R_L2LOSS_SVC);
        linearSVC.setQuietMode(true);

        ParameterSearchResult parameters = linearSVC.findParameters();
        assertEquals(4.0, parameters.getBestC(), 0.01);
        assertEquals(0.88, parameters.getBestScore(), 0.001);
    }

    // new String[]{"-s", "11", "-C", "src/test/resources/iris.scale"}
    @Test
    void testFindBestCOnIrisDataSet_L2R_L2LOSS_SVR_DUAL() throws InvalidInputDataException, IOException {
        URL url = TrainTest.class.getResource("iris.scale");
        Path dataPath = FileUtils.toPath(url);

        LinearSVR linearSVR = new LinearSVR();
        linearSVR.setSolverType(SolverType.L2R_L2LOSS_SVR);
        linearSVR.setDataset(dataPath);
        ParameterSearchResult searchResult = linearSVR.findParameters();
        assertEquals(0.5, searchResult.getBestC(), 0.01);
        assertEquals(0.0, searchResult.getBestP(), 0.01);
        assertEquals(3.68994, searchResult.getBestScore(), 1E-5);
    }

    @Test
    void testFindBestCOnSpliceDataSet_L2R_L2LOSS_SVR_DUAL() {

    }

    @Test
    void testParseCommandLine_FindC_SolverAndNumFoldsSpecified() {
        Train train = new Train();

        train.parseCommandLine(new String[]{"-s", "0", "-v", "10", "-C", "model-filename"});
        assertTrue(train.isFindParameters());
        assertEquals(10, train.getNumFolds());
        Parameter param = train.getParameter();
        assertEquals(L2R_LR, param.solverType);
        assertEquals(0.01, param.eps);
        assertEquals(0.1, param.p);
    }


    @Test
    void testParseCommandLine() {
        Train train = new Train();

        for (SolverType solver : SolverType.values()) {
            train.parseCommandLine(new String[]{"-B", "5.3", "-s", "" + solver.getId(), "-p", "0.01", "model-filename"});
            assertFalse(train.isFindParameters());
            assertEquals(0, train.getNumFolds());
            Parameter param = train.getParameter();
            assertEquals(param.solverType, solver);
            // check default eps
            if (solver.getId() == 0 || solver.getId() == 2 //
                    || solver.getId() == 5 || solver.getId() == 6) {
                assertEquals(0.01, param.eps);
            } else if (solver.getId() == 7) {
                assertEquals(0.1, param.eps);
            } else if (solver.getId() == 11) {
                assertEquals(0.0001, param.eps);
            } else if (solver.getId() == 21) {
                assertEquals(0.01, param.eps);
            } else {
                assertEquals(0.1, param.eps);
            }
            // check if bias is set
            assertEquals(5.3, train.getBias());
            assertEquals(0.01, param.p);
        }
    }

    @Test
    void testParseCommandLine_FindC_NoSolverSpecified() {
        Train train = new Train();

        train.parseCommandLine(new String[]{"-C", "model-filename"});
        assertTrue(train.isFindParameters());
        assertEquals(5, train.getNumFolds());
        Parameter param = train.getParameter();
        assertEquals(L2R_L2LOSS_SVC, param.solverType);
        // check default eps
        assertEquals(0.01, param.eps);
        assertEquals(0.1, param.p);
    }

    @Test
    void testParseWeights() {
        Train train = new Train();
        train.parseCommandLine(new String[]{"-v", "10", "-c", "10", "-w1", "1.234", "model-filename"});
        Parameter parameter = train.getParameter();
        assertArrayEquals(new int[]{1}, parameter.weightLabel);
        assertArrayEquals(new double[]{1.234}, parameter.weight);

        train.parseCommandLine(new String[]{"-w1", "1.234", "-w2", "0.12", "-w3", "7", "model-filename"});
        parameter = train.getParameter();
        assertArrayEquals(new int[]{1, 2, 3}, parameter.weightLabel);
        assertArrayEquals(new double[]{1.234, 0.12, 7}, parameter.weight);
    }

    @Test
    void testParseCommandLine_regularizeBias() {
        Train train = new Train();
        train.parseCommandLine(new String[]{"-R", "model-filename"});
        Parameter parameter = train.getParameter();
        assertFalse(parameter.regularize_bias);

        train.parseCommandLine(new String[]{"model-filename"});
        parameter = train.getParameter();
        assertTrue(parameter.regularize_bias);
    }

}