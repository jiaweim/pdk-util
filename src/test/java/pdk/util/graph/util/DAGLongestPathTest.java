package pdk.util.graph.util;

import org.junit.jupiter.api.Test;
import pdk.util.ArrayUtils;
import pdk.util.graph.Digraph;
import pdk.util.graph.GraphPath;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 17 Apr 2025, 5:29 PM
 */
class DAGLongestPathTest {

    @Test
    void test() {
        Digraph<Integer> digraph = new Digraph<>(ArrayUtils.rangeClosed(0, 7));
        digraph.addEdge(5, 4, 0.35);
        digraph.addEdge(4, 7, 0.37);
        digraph.addEdge(5, 7, 0.28);
        digraph.addEdge(5, 1, 0.32);
        digraph.addEdge(4, 0, 0.38);
        digraph.addEdge(0, 2, 0.26);
        digraph.addEdge(3, 7, 0.39);
        digraph.addEdge(1, 3, 0.29);
        digraph.addEdge(7, 2, 0.34);
        digraph.addEdge(6, 2, 0.40);
        digraph.addEdge(3, 6, 0.52);
        digraph.addEdge(6, 0, 0.58);
        digraph.addEdge(6, 4, 0.93);

        DAGLongestPath<Integer> longestPath = new DAGLongestPath<>(digraph, 5);
        GraphPath<Integer> p0 = longestPath.getPath(0);
        assertIterableEquals(List.of(5, 1, 3, 6, 4, 0), p0.nodeList());

        GraphPath<Integer> p1 = longestPath.getPath(1);
        assertIterableEquals(List.of(5, 1), p1.nodeList());

        GraphPath<Integer> p2 = longestPath.getPath(2);
        assertIterableEquals(List.of(5, 1, 3, 6, 4, 7, 2), p2.nodeList());

        GraphPath<Integer> p3 = longestPath.getPath(3);
        assertIterableEquals(List.of(5, 1, 3), p3.nodeList());

        GraphPath<Integer> p4 = longestPath.getPath(4);
        assertIterableEquals(List.of(5, 1, 3, 6, 4), p4.nodeList());

        GraphPath<Integer> p5 = longestPath.getPath(5);
        assertIterableEquals(List.of(5), p5.nodeList());

        GraphPath<Integer> p6 = longestPath.getPath(6);
        assertIterableEquals(List.of(5, 1, 3, 6), p6.nodeList());

        GraphPath<Integer> p7 = longestPath.getPath(7);
        assertIterableEquals(List.of(5, 1, 3, 6, 4, 7), p7.nodeList());
    }

    @Test
    void criticalPathAnalysis() {
        Digraph<String> g = new Digraph<>(List.of(
                "s", "01", "02", "11", "12", "21", "22", "31", "32", "41", "42",
                "51", "52", "61", "62", "71", "72", "81", "82", "91", "92", "t"));

        double[] duration = new double[]{
                41.0,
                51.0,
                50.0,
                36.0,
                38.0,
                45.0,
                21.0,
                32.0,
                32.0,
                29.0,
        };

        for (int i = 0; i < 10; i++) {
            g.addEdge("s", i + "1", 0);
            g.addEdge(i + "2", "t", 0);
            g.addEdge(i + "1", i + "2", duration[i]);
        }

        g.addEdge("02", "11", 0);
        g.addEdge("02", "71", 0);
        g.addEdge("02", "91", 0);
        g.addEdge("12", "21", 0);
        g.addEdge("62", "31", 0);
        g.addEdge("62", "81", 0);
        g.addEdge("72", "31", 0);
        g.addEdge("72", "81", 0);
        g.addEdge("82", "21", 0);
        g.addEdge("92", "41", 0);
        g.addEdge("92", "61", 0);

        DAGLongestPath<String> longestPath = new DAGLongestPath<>(g, "s");

        GraphPath<String> patht = longestPath.getPath("t");
        assertIterableEquals(List.of("s", "01", "02", "91", "92", "61", "62", "81", "82", "21", "22", "t"), patht.nodeList());
    }

    @Test
    void test2() {
        String v1 = "1";
        String v2 = "2";
        String v3 = "3";
        String v4 = "4";
        String v5 = "5";
        String v6 = "6";
        String v6_ = "6'";
        String v7 = "7";
        String v7_ = "7'";
        String v8 = "8";
        String v8_ = "8'";
        String v9 = "9";
        String v10 = "10";
        String v10_ = "10'";
        Digraph<String> graph = new Digraph<>(List.of(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v6_, v7_, v8_, v10_));

        graph.addEdge(v1, v2, 3);
        graph.addEdge(v1, v3, 2);
        graph.addEdge(v2, v6_, 0);
        graph.addEdge(v2, v4, 3);
        graph.addEdge(v3, v6_, 0);
        graph.addEdge(v3, v5, 1);
        graph.addEdge(v4, v7_, 0);
        graph.addEdge(v5, v8_, 0);
        graph.addEdge(v5, v9, 4);
        graph.addEdge(v6, v7_, 0);
        graph.addEdge(v6, v8_, 0);
        graph.addEdge(v6_, v6, 2);
        graph.addEdge(v7, v10_, 0);
        graph.addEdge(v7_, v7, 3);
        graph.addEdge(v8, v10_, 0);
        graph.addEdge(v8_, v8, 2);
        graph.addEdge(v9, v10_, 0);
        graph.addEdge(v10_, v10, 1);

        DAGLongestPath<String> longestPath = new DAGLongestPath<>(graph, v1);
        assertEquals(10, longestPath.getWeight(v10));
        assertEquals(7, longestPath.getWeight(v9));
        assertEquals(7, longestPath.getWeight(v8));
        assertEquals(9, longestPath.getWeight(v7));
        assertEquals(5, longestPath.getWeight(v6));
        assertEquals(3, longestPath.getWeight(v5));
        assertEquals(6, longestPath.getWeight(v4));
        assertEquals(2, longestPath.getWeight(v3));
        assertEquals(3, longestPath.getWeight(v2));
        assertEquals(0, longestPath.getWeight(v1));
    }
}