package pdk.util.graph;

import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 21 Jul 2025, 9:52 AM
 */
class GraphTest {

    @Test
    void createUndirectedGraph() {
        UndirectedGraph<Integer> graph = new UndirectedGraph<>(List.of(1, 2, 3, 4, 5));
        graph.addEdge(1, 2);
        graph.addEdge(1, 5);
        graph.addEdge(2, 3);
        graph.addEdge(2, 4);
        graph.addEdge(2, 5);
        graph.addEdge(3, 4);
        graph.addEdge(4, 5);
        System.out.println(graph);
    }

    @Test
    void createDirectedGraph() {
        Digraph<Integer> graph = new Digraph<>(List.of(1, 2, 3, 4, 5, 6));
        graph.addEdge(1, 2);
        graph.addEdge(1, 4);
        graph.addEdge(2, 5);
        graph.addEdge(3, 5);
        graph.addEdge(3, 6);
        graph.addEdge(4, 2);
        graph.addEdge(5, 4);
        System.out.println(graph);
    }
}