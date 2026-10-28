package pdk.util.svm.linear;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 24 Oct 2025, 6:50 PM
 */
class PredictTest {

    private final SVMModel testModel = LinearTest.createRandomModel();
    private final StringBuilder sb = new StringBuilder();
    private final Writer writer = new StringWriter();

    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        Linear.setDebugOutput(new PrintStream(byteArrayOutputStream));
        assertTrue(testModel.getNrClass() >= 2);
        assertTrue(testModel.getNrFeature() >= 10);
    }

    @AfterEach
    public void tearDown() {
        Linear.enableDebugOutput();
    }

    private void testWithLines(StringBuilder sb) throws Exception {
        try (StringReader stringReader = new StringReader(sb.toString());
             BufferedReader reader = new BufferedReader(stringReader)) {
            Predict.doPredict(reader, writer, testModel, false);
        }
    }

    @Test
    void testDoPredictCorruptLine() {
        sb.append(testModel.label[0]).append(" abc").append("\n");

        assertThrows(RuntimeException.class,
                () -> testWithLines(sb),
                "Wrong input format at line 1");
    }

    @Test
    void testDoPredictCorruptLine2() {
        sb.append(testModel.label[0]).append(" 1:").append("\n");

        assertThrows(RuntimeException.class,
                () -> testWithLines(sb),
                "Can't convert empty string to integer");
    }

    @Test
    void testDoPredict() throws Exception {
        sb.append(testModel.label[0]).append(" 1:0.32393").append("\n");
        sb.append(testModel.label[1]).append(" 2:-71.555   9:88223").append("\n");
        testWithLines(sb);
        assertFalse(writer.toString().isEmpty());
    }

}