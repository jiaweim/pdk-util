package pdk.util.graph.util;

import org.junit.jupiter.api.Test;
import pdk.util.ArrayUtils;
import pdk.util.graph.Digraph;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 17 Apr 2025, 5:16 PM
 */
class BellmanFordShortestPathTest {

    // https://www.w3schools.com/dsa/dsa_algo_graphs_bellmanford.php
    @Test
    void testPath() {
        Digraph<String> g = new Digraph<>(List.of("A", "B", "C", "D", "E"));
        g.addEdge("A", "C", 4);
        g.addEdge("A", "E", 5);
        g.addEdge("B", "C", -4);
        g.addEdge("C", "A", -3);
        g.addEdge("D", "A", 4);
        g.addEdge("D", "C", 7);
        g.addEdge("D", "E", 3);
        g.addEdge("E", "C", 3);
        g.addEdge("E", "B", 2);

        BellmanFordShortestPath<String> shortestPath = new BellmanFordShortestPath<>(g, "D");
        assertEquals(-2, shortestPath.getWeight("A"), 1E-10);
        assertEquals(5, shortestPath.getWeight("B"), 1E-10);
        assertEquals(1, shortestPath.getWeight("C"), 1E-10);
        assertEquals(0, shortestPath.getWeight("D"), 1E-10);
        assertEquals(3, shortestPath.getWeight("E"), 1E-10);


    }

    @Test
    void testNegativeCycle() {
        Digraph<Integer> g = new Digraph<>(ArrayUtils.rangeClosed(0, 7));
        g.addEdgeByIndex(4, 5, 0.35);
        g.addEdgeByIndex(5, 4, -0.66);
        g.addEdgeByIndex(4, 7, 0.37);
        g.addEdgeByIndex(5, 7, 0.28);
        g.addEdgeByIndex(7, 5, 0.28);
        g.addEdgeByIndex(5, 1, 0.32);
        g.addEdgeByIndex(0, 4, 0.38);
        g.addEdgeByIndex(0, 2, 0.26);
        g.addEdgeByIndex(7, 3, 0.39);
        g.addEdgeByIndex(1, 3, 0.29);
        g.addEdgeByIndex(2, 7, 0.34);
        g.addEdgeByIndex(6, 2, 0.40);
        g.addEdgeByIndex(3, 6, 0.52);
        g.addEdgeByIndex(6, 0, 0.58);
        g.addEdgeByIndex(6, 4, 0.93);

        BellmanFordShortestPath<Integer> shortestPath = new BellmanFordShortestPath<>(g, 0);
        assertTrue(shortestPath.hasNegativeCycle());
    }
}