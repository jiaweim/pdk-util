package pdk.util.svm.linear;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pdk.util.svm.linear.SolverType.*;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Oct 2026, 3:13 PM
 */
class SolverTypeTest {
    @Test
    void testIsSupportVectorRegression() {
        for (SolverType type : SolverType.values()) {
            boolean regressionSolver = type.isSupportVectorRegression();
            // from check_regression_model() in linear.cpp
            if (type == L2R_L2LOSS_SVR || type == L2R_L1LOSS_SVR_DUAL || type == L2R_L2LOSS_SVR_DUAL) {
                assertTrue(regressionSolver, type + " is a regression solver");
            } else {
                assertFalse(regressionSolver, type + " is not a regression solver");
            }
        }
    }
}