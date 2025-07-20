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
 * @since 03 Dec 2024, 4:14 PM
 */
class DAGShortestPathTest {

    @Test
    void test() {
        Digraph<Integer> digraph = new Digraph<>(ArrayUtils.rangeClosed(0, 7));
        digraph.addEdgeByIndex(5, 4, 0.35);
        digraph.addEdgeByIndex(4, 7, 0.37);
        digraph.addEdgeByIndex(5, 7, 0.28);
        digraph.addEdgeByIndex(5, 1, 0.32);
        digraph.addEdgeByIndex(4, 0, 0.38);
        digraph.addEdgeByIndex(0, 2, 0.26);
        digraph.addEdgeByIndex(3, 7, 0.39);
        digraph.addEdgeByIndex(1, 3, 0.29);
        digraph.addEdgeByIndex(7, 2, 0.34);
        digraph.addEdgeByIndex(6, 2, 0.40);
        digraph.addEdgeByIndex(3, 6, 0.52);
        digraph.addEdgeByIndex(6, 0, 0.58);
        digraph.addEdgeByIndex(6, 4, 0.93);

        DAGShortestPath<Integer> path = new DAGShortestPath<>(digraph, 5);
        GraphPath<Integer> p0 = path.getPath(0);
        assertEquals(0.73, p0.weight());
        assertIterableEquals(List.of(5, 4, 0), p0.nodeList());

        GraphPath<Integer> p1 = path.getPath(1);
        assertEquals(0.32, p1.weight());
        assertIterableEquals(List.of(5, 1), p1.nodeList());

        GraphPath<Integer> p2 = path.getPath(2);
        assertEquals(0.62, p2.weight(), 1e-10);
        assertIterableEquals(List.of(5, 7, 2), p2.nodeList());

        GraphPath<Integer> p3 = path.getPath(3);
        assertEquals(0.61, p3.weight(), 1e-10);
        assertIterableEquals(List.of(5, 1, 3), p3.nodeList());

        GraphPath<Integer> p4 = path.getPath(4);
        assertEquals(0.35, p4.weight(), 1e-10);
        assertIterableEquals(List.of(5, 4), p4.nodeList());

        GraphPath<Integer> p5 = path.getPath(5);
        assertEquals(0., p5.weight(), 1e-10);
        assertIterableEquals(List.of(5), p5.nodeList());

        GraphPath<Integer> p6 = path.getPath(6);
        assertEquals(1.13, p6.weight(), 1e-10);
        assertIterableEquals(List.of(5, 1, 3, 6), p6.nodeList());

        GraphPath<Integer> p7 = path.getPath(7);
        assertEquals(0.28, p7.weight(), 1e-10);
        assertIterableEquals(List.of(5, 7), p7.nodeList());
    }
}