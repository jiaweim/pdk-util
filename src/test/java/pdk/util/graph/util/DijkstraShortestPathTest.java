package pdk.util.graph.util;

import org.junit.jupiter.api.Test;
import pdk.util.graph.Digraph;
import pdk.util.graph.GraphPath;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 17 Apr 2025, 5:27 PM
 */
class DijkstraShortestPathTest {

    @Test
    void test() {
        String v1 = "v1";
        String v2 = "v2";
        String v3 = "v3";
        String v4 = "v4";
        String v5 = "v5";
        String v6 = "v6";
        String v7 = "v7";

        Digraph<String> graph = new Digraph<>(List.of(v1, v2, v3, v4, v5, v6, v7));

        graph.addEdge(v1, v2, 2);
        graph.addEdge(v1, v4, 1);
        graph.addEdge(v2, v4, 3);
        graph.addEdge(v2, v5, 10);
        graph.addEdge(v3, v1, 4);
        graph.addEdge(v3, v6, 5);
        graph.addEdge(v4, v3, 2);
        graph.addEdge(v4, v5, 2);
        graph.addEdge(v4, v6, 8);
        graph.addEdge(v4, v7, 4);
        graph.addEdge(v5, v7, 6);
        graph.addEdge(v7, v6, 1);

        DijkstraShortestPath<String> path = new DijkstraShortestPath<>(graph, v1);
        assertEquals(0, path.getWeight(v1));
        assertEquals(2, path.getWeight(v2));
        assertEquals(3, path.getWeight(v3));
        assertEquals(1, path.getWeight(v4));
        assertEquals(3, path.getWeight(v5));
        assertEquals(6, path.getWeight(v6));
        assertEquals(5, path.getWeight(v7));

        GraphPath<String> path1 = path.getPath(v2);
        assertEquals(v1, path1.startNode());
        assertEquals(v2, path1.endNode());
        assertEquals(2, path1.weight());

        assertIterableEquals(path.getPath(v2).nodeList(), Arrays.asList(v1, v2));
        assertIterableEquals(path.getPath(v3).nodeList(), Arrays.asList(v1, v4, v3));
        assertIterableEquals(path.getPath(v4).nodeList(), Arrays.asList(v1, v4));
        assertIterableEquals(path.getPath(v5).nodeList(), Arrays.asList(v1, v4, v5));
        assertIterableEquals(path.getPath(v6).nodeList(), Arrays.asList(v1, v4, v7, v6));
        assertIterableEquals(path.getPath(v7).nodeList(), Arrays.asList(v1, v4, v7));
    }

    @Test
    void test2() {
        Digraph<String> g = new Digraph<>(List.of("A", "B", "C", "D", "E", "F"));
        g.addEdge("A", "B", 10);
        g.addEdge("A", "C", 15);
        g.addEdge("B", "D", 12);
        g.addEdge("B", "F", 15);
        g.addEdge("C", "E", 10);
        g.addEdge("D", "E", 2);
        g.addEdge("D", "F", 1);
        g.addEdge("F", "E", 5);

        DijkstraShortestPath<String> path = new DijkstraShortestPath<>(g, 0);
        assertIterableEquals(List.of("A", "B"), path.getPath("B").nodeList());
        assertIterableEquals(List.of("A", "C"), path.getPath("C").nodeList());
        assertIterableEquals(List.of("A", "B", "D"), path.getPath("D").nodeList());
        assertIterableEquals(List.of("A", "B", "D", "E"), path.getPath("E").nodeList());
        assertIterableEquals(List.of("A", "B", "D", "F"), path.getPath("F").nodeList());

        assertEquals(0, path.getWeight("A"));
        assertEquals(10, path.getWeight("B"));
        assertEquals(15, path.getWeight("C"));
        assertEquals(22, path.getWeight("D"));
        assertEquals(24, path.getWeight("E"));
        assertEquals(23, path.getWeight("F"));

    }
}