package pdk.util.graph.util;

import org.junit.jupiter.api.Test;
import pdk.util.ArrayUtils;
import pdk.util.graph.Digraph;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 02 Dec 2024, 6:51 PM
 */
class DiDFSPathsTest {

    @Test
    void test() {
        Digraph<Integer> digraph = new Digraph<>(ArrayUtils.rangeClosed(0, 12));
        digraph.addEdgeByIndex(0, 1);
        digraph.addEdgeByIndex(0, 5);
        digraph.addEdgeByIndex(2, 0);
        digraph.addEdgeByIndex(2, 3);
        digraph.addEdgeByIndex(3, 2);
        digraph.addEdgeByIndex(3, 5);
        digraph.addEdgeByIndex(4, 2);
        digraph.addEdgeByIndex(4, 3);
        digraph.addEdgeByIndex(5, 4);
        digraph.addEdgeByIndex(6, 0);
        digraph.addEdgeByIndex(6, 4);
        digraph.addEdgeByIndex(6, 9);
        digraph.addEdgeByIndex(7, 6);
        digraph.addEdgeByIndex(7, 8);
        digraph.addEdgeByIndex(8, 7);
        digraph.addEdgeByIndex(8, 9);
        digraph.addEdgeByIndex(9, 10);
        digraph.addEdgeByIndex(9, 11);
        digraph.addEdgeByIndex(10, 12);
        digraph.addEdgeByIndex(11, 4);
        digraph.addEdgeByIndex(11, 12);
        digraph.addEdgeByIndex(12, 9);

        DiDFSPaths<Integer> paths = new DiDFSPaths<>(digraph, 3);
        List<Integer> path = paths.getPath(0);
        assertIterableEquals(List.of(3, 2, 0), path);
    }
}