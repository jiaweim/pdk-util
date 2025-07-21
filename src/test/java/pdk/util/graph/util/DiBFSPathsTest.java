package pdk.util.graph.util;

import org.junit.jupiter.api.Test;
import pdk.util.ArrayUtils;
import pdk.util.graph.Digraph;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 02 Dec 2024, 7:17 PM
 */
class DiBFSPathsTest {

    @Test
    void test() {
        Digraph<Integer> digraph = new Digraph<>(ArrayUtils.rangeClosed(0, 12));
        digraph.addEdge(0, 1);
        digraph.addEdge(0, 5);
        digraph.addEdge(2, 0);
        digraph.addEdge(2, 3);
        digraph.addEdge(3, 2);
        digraph.addEdge(3, 5);
        digraph.addEdge(4, 2);
        digraph.addEdge(4, 3);
        digraph.addEdge(5, 4);
        digraph.addEdge(6, 0);
        digraph.addEdge(6, 4);
        digraph.addEdge(6, 9);
        digraph.addEdge(7, 6);
        digraph.addEdge(7, 8);
        digraph.addEdge(8, 7);
        digraph.addEdge(8, 9);
        digraph.addEdge(9, 10);
        digraph.addEdge(9, 11);
        digraph.addEdge(10, 12);
        digraph.addEdge(11, 4);
        digraph.addEdge(11, 12);
        digraph.addEdge(12, 9);

        DiBFSPaths<Integer> paths = new DiBFSPaths<>(digraph, 3);
        assertIterableEquals(List.of(3, 2, 0), paths.getPath(0));
        assertIterableEquals(List.of(3, 2, 0, 1), paths.getPath(1));
        assertIterableEquals(List.of(3, 2), paths.getPath(2));
        assertIterableEquals(List.of(3), paths.getPath(3));
        assertIterableEquals(List.of(3, 5, 4), paths.getPath(4));
        assertIterableEquals(List.of(3, 5), paths.getPath(5));

        assertFalse(paths.hasPathTo(6));
        assertFalse(paths.hasPathTo(7));
        assertFalse(paths.hasPathTo(8));
        assertFalse(paths.hasPathTo(9));
        assertFalse(paths.hasPathTo(10));
        assertFalse(paths.hasPathTo(11));
        assertFalse(paths.hasPathTo(12));
    }
}