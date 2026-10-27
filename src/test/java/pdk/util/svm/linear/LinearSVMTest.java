package pdk.util.svm.linear;

import org.junit.jupiter.api.Test;
import pdk.util.io.FileUtils;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static pdk.util.svm.linear.SolverType.L2R_L2LOSS_SVR;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Oct 2026, 7:33 PM
 */
class LinearSVMTest {

    @Test
    void findParameters() throws InvalidInputDataException, IOException {
        URL url = LinearTest.class.getResource("dna.scale");
        LinearSVM linearSVM = new LinearSVM();
        linearSVM.setDataset(FileUtils.toPath(url), false);
        linearSVM.setSolverType(L2R_L2LOSS_SVR);
        ParameterSearchResult result = linearSVM.findParameters();

        assertEquals(0.015625, result.getBestC());
        assertEquals(0.29743037982927906, result.getBestScore(), 0.0000001);
        assertEquals(0.15, result.getBestP());
    }
}