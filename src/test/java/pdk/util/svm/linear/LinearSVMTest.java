package pdk.util.svm.linear;

import org.junit.jupiter.api.Test;
import pdk.util.io.FileUtils;

import java.io.IOException;
import java.net.URL;
import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;
import static pdk.util.svm.linear.LinearTest.createRandomDataset;
import static pdk.util.svm.linear.SolverType.*;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Oct 2026, 7:33 PM
 */
class LinearSVMTest {

    @Test
    void testTrain_IllegalParameters_P() {
        assertThrows(IllegalArgumentException.class, () -> {
            new LinearSVR(L2R_L2LOSS_SVR, 10, 0.1, -1);
        });


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
        SVMDataset dataset = createRandomDataset(2);

        LinearSVM svr = new LinearSVM();
        svr.setSolverType(L2R_L1LOSS_SVR_DUAL);
        svr.setCost(10);
        svr.setInitSolution(new double[dataset.n]);
        svr.setDataset(dataset);

        assertThrows(IllegalArgumentException.class, svr::train
                , "Initial-solution specification supported only for solvers L2R_LR, L2R_L2LOSS_SVC, and L2R_L2LOSS_SVR");

        svr.setSolverType(L2R_LR);

        SVMModel model = svr.train();
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

        LinearSVC svc = new LinearSVC(L2R_L1LOSS_SVC_DUAL);
        svc.setCost(10);
        svc.setDataset(prob);

        SVMModel model = svc.train();
        assertThrows(IllegalArgumentException.class, () -> Linear.predictProbability(model, prob.x[0], new double[1])
                , "probability output is only supported for logistic regression."
                        + " This is currently only supported by the following solvers:"
                        + " L2R_LR, L1R_LR, L2R_LR_DUAL");
    }

    @Test
    void testFindBestParametersOnIrisDataSet() throws Exception {
        URL url = LinearTest.class.getResource("iris.scale");
        LinearSVC svc = new LinearSVC(L2R_L2LOSS_SVC);
        svc.setEPS(0.001);
        svc.setDataset(FileUtils.toPath(url));

        ParameterSearchResult result = svc.findParameters();
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
            LinearSVM svm = new LinearSVM();
            svm.setSolverType(illegalSolver);
            svm.setEPS(0.001);
            svm.setDataset(dataset);

            assertThrows(IllegalArgumentException.class,
                    () -> svm.findParameters(),
                    "Unsupported solver: " + illegalSolver);
        }
    }

    @Test
    void findParameters() throws InvalidInputDataException, IOException {
        URL url = LinearTest.class.getResource("dna.scale");
        LinearSVM svm = new LinearSVM();
        svm.setDataset(FileUtils.toPath(url), false);
        svm.setSolverType(L2R_L2LOSS_SVR);
        ParameterSearchResult result = svm.findParameters();

        assertEquals(0.015625, result.getBestC());
        assertEquals(0.29743037982927906, result.getBestScore(), 0.0000001);
        assertEquals(0.15, result.getBestP());
    }

    @Test
    void testFindBestParametersOnDnaScaleDataSet() throws Exception {
        URL url = LinearTest.class.getResource("dna.scale");

        LinearSVC svc = new LinearSVC(L2R_L2LOSS_SVC);
        svc.setDataset(FileUtils.toPath(url), false);
        svc.setEPS(0.001);

        ParameterSearchResult result = svc.findParameters();
        assertEquals(0.015625, result.getBestC());
        assertEquals(0.9475, result.getBestScore());
        assertEquals(-1, result.getBestP());
    }

    @Test
    void testFindBestParametersOnSpliceDataSet_L2R_LR() throws Exception {
        URL url = LinearTest.class.getResource("splice");

        LinearSVC svc = new LinearSVC(L2R_LR);
        svc.setDataset(FileUtils.toPath(url), false);
        svc.setEPS(0.001);

        ParameterSearchResult result = svc.findParameters();
        assertEquals(0.015625, result.getBestC());
        assertEquals(0.812, result.getBestScore());
        assertEquals(-1, result.getBestP());
    }

    @Test
    void testFindBestParametersOnSpliceDataSet() throws Exception {
        URL url = LinearTest.class.getResource("splice");
        SVMDataset dataset = SVMDataset.read(FileUtils.toPath(url), -1);
        LinearSVC svc = new LinearSVC(L2R_L2LOSS_SVC);
        svc.setEPS(0.001);
        svc.setDataset(dataset);

        ParameterSearchResult result = svc.findParameters();
        assertEquals(0.001953125, result.getBestC());
        assertEquals(0.81, result.getBestScore());
        assertEquals(-1, result.getBestP());
    }

}