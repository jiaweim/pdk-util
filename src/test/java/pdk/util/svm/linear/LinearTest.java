package pdk.util.svm.linear;

import org.junit.jupiter.api.Test;
import pdk.util.io.FileUtils;

import java.net.URL;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static pdk.util.svm.linear.SolverType.*;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Oct 2026, 6:48 PM
 */
class LinearTest {

    private static final Random random = new Random(12345);

    static SVMModel createRandomModel() {
        return createRandomModel(L2R_LR);
    }

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

    static SVMDataset createRandomDataset(int numClasses) {
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


    /**
     * create a very simple problem and check if the clearly separated examples are recognized as such
     */
    @Test
    void testTrainPredict() {
        SVMDataset prob = new SVMDataset();
        prob.bias = -1;
        prob.l = 4;
        prob.n = 4;
        prob.x = new Feature[4][];
        prob.x[0] = new Feature[2];
        prob.x[1] = new Feature[1];
        prob.x[2] = new Feature[1];
        prob.x[3] = new Feature[3];

        prob.x[0][0] = new Feature(1, 1);
        prob.x[0][1] = new Feature(2, 1);

        prob.x[1][0] = new Feature(3, 1);
        prob.x[2][0] = new Feature(3, 1);

        prob.x[3][0] = new Feature(1, 2);
        prob.x[3][1] = new Feature(2, 1);
        prob.x[3][2] = new Feature(4, 1);

        prob.y = new double[4];
        prob.y[0] = 0;
        prob.y[1] = 1;
        prob.y[2] = 1;
        prob.y[3] = 0;

        for (SolverType solver : SolverType.values()) {
            if (solver.isOneClass()) {
                continue;
            }
            for (double C = 0.1; C <= 100.; C *= 1.2) {
                // compared the behavior with the C version
                if (C < 0.2)
                    if (solver == L1R_L2LOSS_SVC)
                        continue;
                if (C < 0.7)
                    if (solver == L1R_LR)
                        continue;

                if (solver.isSupportVectorRegression()) {
                    continue;
                }

                Parameter param = new Parameter(solver, C, 0.1, 0.1);
                SVMModel model = Linear.train(prob, param);

                double[] featureWeights = model.getFeatureWeights();
                if (solver == MCSVM_CS) {
                    assertEquals(8, featureWeights.length);
                } else {
                    assertEquals(4, featureWeights.length);
                }

                int i = 0;
                for (double value : prob.y) {
                    double prediction = Linear.predict(model, prob.x[i]);
                    assertEquals(value, prediction, "prediction with solver " + solver);
                    if (model.isProbabilityModel()) {
                        double[] estimates = new double[model.getNrClass()];
                        double probabilityPrediction = Linear.predictProbability(model, prob.x[i], estimates);
                        assertEquals(prediction, probabilityPrediction);
                        assertTrue(estimates[(int) probabilityPrediction] >= 1.0 / model.getNrClass());
                        double estimationSum = 0;
                        for (double estimate : estimates) {
                            estimationSum += estimate;
                        }
                        assertEquals(1.0, estimationSum, 0.001);
                    }
                    i++;
                }
            }
        }
    }

    @Test
    void testCrossValidation() {
        int numClasses = random.nextInt(10) + 1;

        SVMDataset prob = createRandomDataset(numClasses);

        Parameter param = new Parameter(L2R_LR, 10, 0.01);
        int nr_fold = 10;
        double[] target = new double[prob.l];
        Linear.crossValidation(prob, param, nr_fold, target);

        for (double clazz : target) {
            assertTrue(clazz >= 0);
            assertTrue(clazz < numClasses);
        }
    }


    @Test
    void testTrainUnsortedProblem() {
        SVMDataset prob = new SVMDataset();
        prob.bias = -1;
        prob.l = 1;
        prob.n = 2;
        prob.x = new Feature[4][];
        prob.x[0] = new Feature[2];

        prob.x[0][0] = new Feature(2, 1);
        prob.x[0][1] = new Feature(1, 1);

        prob.y = new double[4];
        prob.y[0] = 0;

        Parameter param = new Parameter(L2R_LR, 10, 0.1);

        assertThrows(IllegalArgumentException.class,
                () -> Linear.train(prob, param));
    }

    @Test
    void testTrainTooLargeProblem() {
        SVMDataset prob = new SVMDataset();
        prob.l = 1000;
        prob.n = 20000000;
        prob.x = new Feature[prob.l][];
        prob.y = new double[prob.l];
        for (int i = 0; i < prob.l; i++) {
            prob.x[i] = new Feature[]{};
            prob.y[i] = i;
        }

        for (SolverType solverType : SolverType.values()) {
            if (solverType.isSupportVectorRegression() || solverType.isOneClass()) {
                continue;
            }
            Parameter param = new Parameter(solverType, 10, 0.1);
            assertThrows(IllegalArgumentException.class,
                    () -> Linear.train(prob, param));
        }
    }

    @Test
    void testTrain_IllegalParameters_BiasWithOneClassSvm() {
        SVMDataset prob = createRandomDataset(2);
        prob.bias = 1;

        Parameter param = new Parameter(ONECLASS_SVM, 10, 0.1);

        assertThrows(IllegalArgumentException.class,
                () -> Linear.train(prob, param),
                "prob->bias >=0, but this is ignored in ONECLASS_SVM");
    }

    @Test
    void testTrain_IllegalParameters_RegularizeBias() {
        SVMDataset prob = createRandomDataset(2);
        prob.bias = -1;

        Parameter param = new Parameter(L2R_L1LOSS_SVR_DUAL, 10, 0.1);
        param.setRegularizeBias(false);

        assertThrows(IllegalArgumentException.class,
                () -> Linear.train(prob, param),
                "To not regularize bias, must specify -B 1 along with -R");

        prob.bias = 1;

        assertThrows(IllegalArgumentException.class,
                () -> Linear.train(prob, param),
                "-R option supported only for solver L2R_LR, L2R_L2LOSS_SVC, L1R_L2LOSS_SVC, L1R_LR, and L2R_L2LOSS_SVR");

        param.setSolverType(L1R_LR);

        SVMModel model = Linear.train(prob, param);
        assertEquals(1.0, model.bias);
    }

    @Test
    void testTrain_IllegalParameters_P() {
        SVMDataset prob = createRandomDataset(2);
        Parameter param = new Parameter(L2R_L2LOSS_SVR, 10, 0.1);
        param.p = -1;

        assertThrows(IllegalArgumentException.class,
                () -> Linear.train(prob, param)
                , "p < 0");

        // It must not fail for solvers other than L2R_L2LOSS_SVR
        for (SolverType solverType : values()) {
            if (solverType != L2R_L2LOSS_SVR) {
                param.solverType = solverType;
                Linear.train(prob, param);
            }
        }
    }

    @Test
    void testTrain_IllegalParameters_InitialSol() {
        SVMDataset prob = createRandomDataset(2);

        Parameter param = new Parameter(L2R_L1LOSS_SVR_DUAL, 10, 0.1);
        param.setInitSol(new double[prob.n]);

        assertThrows(IllegalArgumentException.class,
                () -> Linear.train(prob, param)
                , "Initial-solution specification supported only for solvers L2R_LR, L2R_L2LOSS_SVC, and L2R_L2LOSS_SVR");

        param.setSolverType(L2R_LR);

        SVMModel model = Linear.train(prob, param);
        assertNotNull(model);
    }

    @Test
    void testPredictProbabilityWrongSolver() {
        SVMDataset prob = new SVMDataset();
        prob.l = 1;
        prob.n = 1;
        prob.x = new Feature[prob.l][];
        prob.y = new double[prob.l];
        for (int i = 0; i < prob.l; i++) {
            prob.x[i] = new Feature[]{};
            prob.y[i] = i;
        }

        Parameter param = new Parameter(L2R_L1LOSS_SVC_DUAL, 10, 0.1);
        SVMModel model = Linear.train(prob, param);

        assertThrows(IllegalArgumentException.class, () -> Linear.predictProbability(model, prob.x[0], new double[1])
                , "probability output is only supported for logistic regression."
                        + " This is currently only supported by the following solvers:"
                        + " L2R_LR, L1R_LR, L2R_LR_DUAL");
    }

    @Test
    void testFindBestParametersOnIrisDataSet() throws Exception {
        URL url = LinearTest.class.getResource("iris.scale");
        SVMDataset dataset = SVMDataset.read(FileUtils.toPath(url), -1);

        Parameter param = new Parameter(L2R_L2LOSS_SVC, 1, 0.001, 0.1);
        ParameterSearchResult result = Linear.findParameters(dataset, param, 5, -1, -1);
        assertEquals(4, result.getBestC());
        assertEquals(0.88, result.getBestScore());
        assertEquals(-1, result.getBestP());
    }

    @Test
    void testFindBestParameterC_IllegalSolver() throws Exception {
        URL url = LinearTest.class.getResource("iris.scale");
        SVMDataset dataset = SVMDataset.read(FileUtils.toPath(url), -1);

        EnumSet<SolverType> supportedSolvers = EnumSet.of(L2R_LR, L2R_L2LOSS_SVC, L2R_L2LOSS_SVR);
        for (SolverType illegalSolver : EnumSet.complementOf(supportedSolvers)) {
            Parameter param = new Parameter(illegalSolver, 1, 0.001, 0.1);
            assertThrows(IllegalArgumentException.class,
                    () -> Linear.findParameters(dataset, param, 5, -1, -1),
                    "Unsupported solver: " + illegalSolver);
        }
    }

    @Test
    void testFindBestParametersOnSpliceDataSet() throws Exception {
        URL url = LinearTest.class.getResource("splice");
        SVMDataset dataset = SVMDataset.read(FileUtils.toPath(url), -1);

        Parameter param = new Parameter(L2R_L2LOSS_SVC, 1, 0.001, 0.1);
        ParameterSearchResult result = Linear.findParameters(dataset, param, 5, -1, -1);
        assertEquals(0.001953125, result.getBestC());
        assertEquals(0.81, result.getBestScore());
        assertEquals(-1, result.getBestP());
    }


    @Test
    void testFindBestParametersOnSpliceDataSet_L2R_LR() throws Exception {
        URL url = LinearTest.class.getResource("splice");
        SVMDataset dataset = SVMDataset.read(FileUtils.toPath(url), -1);

        Parameter param = new Parameter(L2R_LR, 1, 0.001, 0.1);
        ParameterSearchResult result = Linear.findParameters(dataset, param, 5, -1, -1);
        assertEquals(0.015625, result.getBestC());
        assertEquals(0.812, result.getBestScore());
        assertEquals(-1, result.getBestP());
    }

    @Test
    void testFindBestParametersOnSpliceDataSet_L2R_L2LOSS_SVR() throws Exception {
        URL url = LinearTest.class.getResource("splice");
        SVMDataset dataset = SVMDataset.read(FileUtils.toPath(url), -1);
        Parameter param = new Parameter(L2R_L2LOSS_SVR, 1, 0.001, 0.1);
        ParameterSearchResult result = Linear.findParameters(dataset, param, 5, -1, -1);
        assertEquals(0.00390625, result.getBestC());
        assertEquals(0.5699399182191544, result.getBestScore(), 0.0000001);
        assertEquals(0.0, result.getBestP());
    }

    @Test
    void testFindBestParametersOnDnaScaleDataSet() throws Exception {
        URL url = LinearTest.class.getResource("dna.scale");
        SVMDataset dataset = SVMDataset.read(FileUtils.toPath(url), -1);

        Parameter param = new Parameter(L2R_L2LOSS_SVC, 1, 0.001, 0.1);
        ParameterSearchResult result = Linear.findParameters(dataset, param, 5, -1, -1);
        assertEquals(0.015625, result.getBestC());
        assertEquals(0.9475, result.getBestScore());
        assertEquals(-1, result.getBestP());
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