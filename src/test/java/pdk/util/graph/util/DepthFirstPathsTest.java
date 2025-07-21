package pdk.util.graph.util;

import org.junit.jupiter.api.Test;
import pdk.util.graph.UndirectedGraph;

import java.util.List;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 02 Dec 2024, 1:21 PM
 */
class DepthFirstPathsTest {
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

        DepthFirstPaths<Integer> paths = new DepthFirstPaths<>(graph, 0);
        System.out.println(paths.pathTo(5));
    }
}