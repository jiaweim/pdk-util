package pdk.util.svm.linear;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pdk.util.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Oct 2026, 5:51 PM
 */
class SVMDatasetTest {

    @Test
    void testReadProblem(@TempDir Path tempDir) throws Exception {
        Path problemPath = tempDir.resolve("dataset");

        List<String> lines = Arrays.asList(
                "1 1:1  3:1  4:1   6:1",
                "2 2:1  3:1  5:1   7:1",
                "1 3:1  5:1",
                "1 1:1  4:1  7:1",
                "2 4:1  5:1  7:1");

        IOUtils.writeLines(problemPath, lines);

        SVMDataset dataset = SVMDataset.read(problemPath, 1);

        assertEquals(1, dataset.bias);
        assertEquals(dataset.y.length, lines.size());
        assertArrayEquals(new double[]{1, 2, 1, 1, 2}, dataset.y);
        assertEquals(8, dataset.n);
        assertEquals(dataset.l, dataset.y.length);
        assertEquals(dataset.x.length, dataset.y.length);

        validate(dataset);
    }

    @Test
    void testReadProblemFromStream() throws Exception {
        String data = """
                1 1:1  3:1  4:1   6:1
                2 2:1  3:1  5:1   7:1
                1 3:1  5:1
                1 1:1  4:1  7:1
                2 4:1  5:1  7:1
                """;

        Charset charset = StandardCharsets.UTF_8;
        SVMDataset prob = SVMDataset.read(new ByteArrayInputStream(data.getBytes(charset)), charset, 1);
        assertEquals(1, prob.bias);
        assertEquals(5, prob.y.length);
        assertArrayEquals(new double[]{1, 2, 1, 1, 2}, prob.y);
        assertEquals(8, prob.n);
        assertEquals(prob.l, prob.y.length);
        assertEquals(prob.x.length, prob.y.length);

        validate(prob);
    }

    private void validate(SVMDataset prob) {
        for (Feature[] nodes : prob.x) {
            assertTrue(nodes.length <= prob.n);
            for (Feature node : nodes) {
                // bias term
                if (prob.bias >= 0 && nodes[nodes.length - 1] == node) {
                    assertEquals(node.getIndex(), prob.n);
                    assertEquals(node.getValue(), prob.bias);
                } else {
                    assertTrue(node.getIndex() < prob.n);
                }
            }
        }
    }

    @Test
    void testReadProblemEmptyLine(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("problem");

        List<String> lines = Arrays.asList(
                "1 1:1  3:1  4:1   6:1",
                "2 ");
        IOUtils.writeLines(file, lines);

        SVMDataset dataset = SVMDataset.read(file, -1.0);
        assertEquals(-1, dataset.bias);
        assertEquals(dataset.y.length, lines.size());
        assertArrayEquals(new double[]{1, 2}, dataset.y);
        assertEquals(6, dataset.n);
        assertEquals(dataset.l, dataset.y.length);
        assertEquals(dataset.x.length, dataset.y.length);

        assertEquals(4, dataset.x[0].length);
        assertEquals(0, dataset.x[1].length);
    }


    @Test
    void testReadUnsortedProblem(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("problem");

        List<String> lines = Arrays.asList(
                "1 1:1  3:1  4:1   6:1",
                "2 2:1  3:1  5:1   7:1",
                "1 3:1  5:1  4:1"); // here's the mistake: not correctly sorted
        IOUtils.writeLines(file, lines);

        assertThrows(InvalidInputDataException.class,
                () -> SVMDataset.read(file),
                "indices must be sorted in ascending order");
    }

    @Test
    void testReadProblemWithInvalidIndex(@TempDir Path tempDir) {
        Path file = tempDir.resolve("problem");

        List<String> lines = Arrays.asList(
                "1 1:1  3:1  4:1   6:1",
                "2 2:1  3:1  5:1  -4:1");
        IOUtils.writeLines(file, lines);

        assertThrows(InvalidInputDataException.class,
                () -> SVMDataset.read(file),
                "invalid index: -4");
    }

    @Test
    void testReadProblemWithZeroIndex(@TempDir Path tempDir) {
        Path file = tempDir.resolve("problem");

        List<String> lines = Collections.singletonList("1 0:1  1:1");
        IOUtils.writeLines(file, lines);

        assertThrows(InvalidInputDataException.class,
                () -> SVMDataset.read(file),
                "invalid index: 0");
    }

    @Test
    void testReadWrongDataset(@TempDir Path tempDir) {
        Path file = tempDir.resolve("dataset");
        List<String> lines = Arrays.asList(
                "1 1:1  3:1  4:1   6:1",
                "2 2:1  3:1  5:1   7:1",
                "1 3:1  5:a"); // here's the mistake: incomplete line

        IOUtils.writeLines(file, lines);
        assertThrows(InvalidInputDataException.class, () -> SVMDataset.read(file), "invalid value: a");
    }


    /**
     * compared input/output values with the C version (1.51)
     *
     * <pre>
     * IN:
     * res prob.l = 4
     * res prob.n = 4
     * 0: (2,1) (4,1)
     * 1: (1,1)
     * 2: (3,1)
     * 3: (2,2) (3,1) (4,1)
     *
     * TRANSPOSED:
     *
     * res prob.l = 4
     * res prob.n = 4
     * 0: (2,1)
     * 1: (1,1) (4,2)
     * 2: (3,1) (4,1)
     * 3: (1,1) (4,1)
     * </pre>
     */
    @Test
    void testTranspose() {
        SVMDataset prob = new SVMDataset();
        prob.bias = -1;
        prob.l = 4;
        prob.n = 4;
        prob.x = new Feature[4][];
        prob.x[0] = new Feature[2];
        prob.x[1] = new Feature[1];
        prob.x[2] = new Feature[1];
        prob.x[3] = new Feature[3];

        prob.x[0][0] = new Feature(2, 1);
        prob.x[0][1] = new Feature(4, 1);

        prob.x[1][0] = new Feature(1, 1);
        prob.x[2][0] = new Feature(3, 1);

        prob.x[3][0] = new Feature(2, 2);
        prob.x[3][1] = new Feature(3, 1);
        prob.x[3][2] = new Feature(4, 1);

        prob.y = new double[4];
        prob.y[0] = 0;
        prob.y[1] = 1;
        prob.y[2] = 1;
        prob.y[3] = 0;

        SVMDataset transposed = prob.transpose();

        assertEquals(1, transposed.x[0].length);
        assertEquals(2, transposed.x[1].length);
        assertEquals(2, transposed.x[2].length);
        assertEquals(2, transposed.x[3].length);

        assertEquals(new Feature(2, 1), transposed.x[0][0]);

        assertEquals(new Feature(1, 1), transposed.x[1][0]);
        assertEquals(new Feature(4, 2), transposed.x[1][1]);

        assertEquals(new Feature(3, 1), transposed.x[2][0]);
        assertEquals(new Feature(4, 1), transposed.x[2][1]);

        assertEquals(new Feature(1, 1), transposed.x[3][0]);
        assertEquals(new Feature(4, 1), transposed.x[3][1]);

        assertArrayEquals(prob.y, transposed.y);
    }


    /**
     *
     * compared input/output values with the C version (1.51)
     *
     * <pre>
     * IN:
     * res prob.l = 5
     * res prob.n = 10
     * 0: (1,7) (3,3) (5,2)
     * 1: (2,1) (4,5) (5,3) (7,4) (8,2)
     * 2: (1,9) (3,1) (5,1) (10,7)
     * 3: (1,2) (2,2) (3,9) (4,7) (5,8) (6,1) (7,5) (8,4)
     * 4: (3,1) (10,3)
     *
     * TRANSPOSED:
     *
     * res prob.l = 5
     * res prob.n = 10
     * 0: (1,7) (3,9) (4,2)
     * 1: (2,1) (4,2)
     * 2: (1,3) (3,1) (4,9) (5,1)
     * 3: (2,5) (4,7)
     * 4: (1,2) (2,3) (3,1) (4,8)
     * 5: (4,1)
     * 6: (2,4) (4,5)
     * 7: (2,2) (4,4)
     * 8:
     * 9: (3,7) (5,3)
     * </pre>
     */
    @Test
    void testTranspose2() throws Exception {
        SVMDataset prob = new SVMDataset();
        prob.bias = -1;
        prob.l = 5;
        prob.n = 10;
        prob.x = new Feature[5][];
        prob.x[0] = new Feature[3];
        prob.x[1] = new Feature[5];
        prob.x[2] = new Feature[4];
        prob.x[3] = new Feature[8];
        prob.x[4] = new Feature[2];

        prob.x[0][0] = new Feature(1, 7);
        prob.x[0][1] = new Feature(3, 3);
        prob.x[0][2] = new Feature(5, 2);

        prob.x[1][0] = new Feature(2, 1);
        prob.x[1][1] = new Feature(4, 5);
        prob.x[1][2] = new Feature(5, 3);
        prob.x[1][3] = new Feature(7, 4);
        prob.x[1][4] = new Feature(8, 2);

        prob.x[2][0] = new Feature(1, 9);
        prob.x[2][1] = new Feature(3, 1);
        prob.x[2][2] = new Feature(5, 1);
        prob.x[2][3] = new Feature(10, 7);

        prob.x[3][0] = new Feature(1, 2);
        prob.x[3][1] = new Feature(2, 2);
        prob.x[3][2] = new Feature(3, 9);
        prob.x[3][3] = new Feature(4, 7);
        prob.x[3][4] = new Feature(5, 8);
        prob.x[3][5] = new Feature(6, 1);
        prob.x[3][6] = new Feature(7, 5);
        prob.x[3][7] = new Feature(8, 4);

        prob.x[4][0] = new Feature(3, 1);
        prob.x[4][1] = new Feature(10, 3);

        prob.y = new double[5];
        prob.y[0] = 0;
        prob.y[1] = 1;
        prob.y[2] = 1;
        prob.y[3] = 0;
        prob.y[4] = 1;

        SVMDataset transposed = prob.transpose();

        assertEquals(3, transposed.x[0].length);
        assertEquals(2, transposed.x[1].length);
        assertEquals(4, transposed.x[2].length);
        assertEquals(2, transposed.x[3].length);
        assertEquals(4, transposed.x[4].length);
        assertEquals(1, transposed.x[5].length);
        assertEquals(2, transposed.x[7].length);
        assertEquals(2, transposed.x[7].length);
        assertEquals(0, transposed.x[8].length);
        assertEquals(2, transposed.x[9].length);

        assertEquals(new Feature(1, 7), transposed.x[0][0]);
        assertEquals(new Feature(3, 9), transposed.x[0][1]);
        assertEquals(new Feature(4, 2), transposed.x[0][2]);

        assertEquals(new Feature(2, 1), transposed.x[1][0]);
        assertEquals(new Feature(4, 2), transposed.x[1][1]);

        assertEquals(new Feature(1, 3), transposed.x[2][0]);
        assertEquals(new Feature(3, 1), transposed.x[2][1]);
        assertEquals(new Feature(4, 9), transposed.x[2][2]);
        assertEquals(new Feature(5, 1), transposed.x[2][3]);

        assertEquals(new Feature(2, 5), transposed.x[3][0]);
        assertEquals(new Feature(4, 7), transposed.x[3][1]);

        assertEquals(new Feature(1, 2), transposed.x[4][0]);
        assertEquals(new Feature(2, 3), transposed.x[4][1]);
        assertEquals(new Feature(3, 1), transposed.x[4][2]);
        assertEquals(new Feature(4, 8), transposed.x[4][3]);

        assertEquals(new Feature(4, 1), transposed.x[5][0]);

        assertEquals(new Feature(2, 4), transposed.x[6][0]);
        assertEquals(new Feature(4, 5), transposed.x[6][1]);

        assertEquals(new Feature(2, 2), transposed.x[7][0]);
        assertEquals(new Feature(4, 4), transposed.x[7][1]);

        assertEquals(new Feature(3, 7), transposed.x[9][0]);
        assertEquals(new Feature(5, 3), transposed.x[9][1]);

        assertArrayEquals(prob.y, transposed.y);
    }


    /**
     * compared input/output values with the C version (1.51)
     * <p>
     * IN:
     * res prob.l = 3
     * res prob.n = 4
     * 0: (1,2) (3,1) (4,3)
     * 1: (1,9) (2,7) (3,3) (4,3)
     * 2: (2,1)
     * <p>
     * TRANSPOSED:
     * <p>
     * res prob.l = 3
     * * res prob.n = 4
     * 0: (1,2) (2,9)
     * 1: (2,7) (3,1)
     * 2: (1,1) (2,3)
     * 3: (1,3) (2,3)
     *
     */
    @Test
    void testTranspose3() throws Exception {
        SVMDataset prob = new SVMDataset();
        prob.l = 3;
        prob.n = 4;
        prob.y = new double[3];
        prob.x = new Feature[4][];
        prob.x[0] = new Feature[3];
        prob.x[1] = new Feature[4];
        prob.x[2] = new Feature[1];
        prob.x[3] = new Feature[1];

        prob.x[0][0] = new Feature(1, 2);
        prob.x[0][1] = new Feature(3, 1);
        prob.x[0][2] = new Feature(4, 3);
        prob.x[1][0] = new Feature(1, 9);
        prob.x[1][1] = new Feature(2, 7);
        prob.x[1][2] = new Feature(3, 3);
        prob.x[1][3] = new Feature(4, 3);

        prob.x[2][0] = new Feature(2, 1);

        prob.x[3][0] = new Feature(3, 2);

        SVMDataset transposed = prob.transpose();
        assertEquals(4, transposed.x.length);
        assertEquals(2, transposed.x[0].length);

        assertEquals(new Feature(1, 2), transposed.x[0][0]);
        assertEquals(new Feature(2, 9), transposed.x[0][1]);

        assertEquals(new Feature(2, 7), transposed.x[1][0]);
        assertEquals(new Feature(3, 1), transposed.x[1][1]);

        assertEquals(new Feature(1, 1), transposed.x[2][0]);
        assertEquals(new Feature(2, 3), transposed.x[2][1]);

        assertEquals(new Feature(1, 3), transposed.x[3][0]);
        assertEquals(new Feature(2, 3), transposed.x[3][1]);
    }
}