package pdk.util.graph.util;

import org.junit.jupiter.api.Test;
import pdk.util.graph.UndirectedGraph;

import java.util.List;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 17 Apr 2025, 5:19 PM
 */
class BreadthFirstPathsTest {
    @Test
    void test() {
        UndirectedGraph<Integer> graph = new UndirectedGraph<>(List.of(0, 1, 2, 3, 4, 5));
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 5);

        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 4);
        graph.addEdge(3, 5);

        BreadthFirstPaths<Integer> paths = new BreadthFirstPaths<>(graph, 0);
        System.out.println(paths.pathTo(0));
        System.out.println(paths.pathTo(1));
        System.out.println(paths.pathTo(2));
        System.out.println(paths.pathTo(3));
        System.out.println(paths.pathTo(4));
        System.out.println(paths.pathTo(5));
    }
}