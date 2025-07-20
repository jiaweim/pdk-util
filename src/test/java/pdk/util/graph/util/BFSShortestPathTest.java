package pdk.util.graph.util;

import org.junit.jupiter.api.Test;
import pdk.util.graph.Digraph;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 17 Apr 2025, 5:17 PM
 */
class BFSShortestPathTest {

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

        graph.addEdge(v1, v2);
        graph.addEdge(v1, v4);
        graph.addEdge(v2, v4);
        graph.addEdge(v2, v5);
        graph.addEdge(v3, v1);
        graph.addEdge(v3, v6);
        graph.addEdge(v4, v3);
        graph.addEdge(v4, v5);
        graph.addEdge(v4, v6);
        graph.addEdge(v4, v7);
        graph.addEdge(v5, v7);
        graph.addEdge(v7, v6);

        BFSShortestPath<String> path = new BFSShortestPath<>(graph, v3);
        assertEquals(1, path.getWeight(v1));
        assertEquals(2, path.getWeight(v2));
        assertEquals(0, path.getWeight(v3));
        assertEquals(2, path.getWeight(v4));
        assertEquals(3, path.getWeight(v5));
        assertEquals(1, path.getWeight(v6));
        assertEquals(3, path.getWeight(v7));

        assertEquals(v3, path.getParentNode(v1));
        assertEquals(v1, path.getParentNode(v2));
        assertEquals(null, path.getParentNode(v3));
        assertEquals(v1, path.getParentNode(v4));
        assertEquals(v2, path.getParentNode(v5));
        assertEquals(v3, path.getParentNode(v6));
        assertEquals(v4, path.getParentNode(v7));
    }
}