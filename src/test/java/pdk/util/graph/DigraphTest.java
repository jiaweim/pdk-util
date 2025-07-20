package pdk.util.graph;

import it.unimi.dsi.fastutil.ints.IntList;
import org.junit.jupiter.api.Test;
import pdk.util.graph.util.CollectListVisitor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 20 Jul 2025, 9:52 PM
 */
class DigraphTest {

    @Test
    void getPath() {
        Digraph<Integer> digraph = new Digraph<>(List.of(0, 1, 2, 3));
        digraph.addEdgeByIndex(0, 1);
        digraph.addEdgeByIndex(0, 2);
        digraph.addEdgeByIndex(1, 3);
        digraph.addEdgeByIndex(2, 3);

        List<IntList> allPaths = digraph.findAllPaths(0, 3);
        assertEquals(2, allPaths.size());
        assertArrayEquals(new int[]{0, 1, 3}, allPaths.get(0).toIntArray());
        assertArrayEquals(new int[]{0, 2, 3}, allPaths.get(1).toIntArray());
    }

    @Test
    void allPath2() {
        Digraph<Integer> graph = new Digraph<>(List.of(0, 1, 2, 3));
        graph.addEdgeByIndex(0, 1);
        graph.addEdgeByIndex(0, 2);
        graph.addEdgeByIndex(1, 2);
        graph.addEdgeByIndex(1, 3);
        graph.addEdgeByIndex(2, 3);

        List<IntList> paths = graph.findAllPaths(0, 3);
        assertArrayEquals(new int[]{0, 1, 2, 3}, paths.get(0).toIntArray());
        assertArrayEquals(new int[]{0, 1, 3}, paths.get(1).toIntArray());
        assertArrayEquals(new int[]{0, 2, 3}, paths.get(2).toIntArray());
    }

    @Test
    void allPath3() {
        Digraph<Integer> graph = new Digraph<>(0, 1, 2, 3, 4);
        graph.addEdgeByIndex(0, 1);
        graph.addEdgeByIndex(0, 3);
        graph.addEdgeByIndex(0, 4);
        graph.addEdgeByIndex(1, 2);
        graph.addEdgeByIndex(1, 3);
        graph.addEdgeByIndex(1, 4);
        graph.addEdgeByIndex(2, 3);
        graph.addEdgeByIndex(3, 4);

        List<IntList> paths = graph.findAllPaths(0, 4);
        assertEquals(5, paths.size());
        assertArrayEquals(new int[]{0, 1, 2, 3, 4}, paths.get(0).toIntArray());
        assertArrayEquals(new int[]{0, 1, 3, 4}, paths.get(1).toIntArray());
        assertArrayEquals(new int[]{0, 1, 4}, paths.get(2).toIntArray());
        assertArrayEquals(new int[]{0, 3, 4}, paths.get(3).toIntArray());
        assertArrayEquals(new int[]{0, 4}, paths.get(4).toIntArray());
    }

    void allPath4() {
        Digraph<Integer> graph = new Digraph<>(0, 1, 2, 3);

    }

    @Test
    void shortestPath() {
        Digraph<String> g = new Digraph<>(List.of("A", "B", "C", "D", "E", "F"));
        g.addEdge("A", "B", 10);
        g.addEdge("A", "C", 15);
        g.addEdge("B", "D", 12);
        g.addEdge("B", "F", 15);
        g.addEdge("C", "E", 10);
        g.addEdge("D", "E", 2);
        g.addEdge("D", "F", 1);
        g.addEdge("F", "E", 5);


        assertIterableEquals(List.of("A", "B"), g.getShortestPath(0, 1).nodeList());
        assertIterableEquals(List.of("A", "C"), g.getShortestPath(0, 2).nodeList());
        assertIterableEquals(List.of("A", "B", "D"), g.getShortestPath(0, 3).nodeList());
        assertIterableEquals(List.of("A", "B", "D", "E"), g.getShortestPath(0, 4).nodeList());
        assertIterableEquals(List.of("A", "B", "D", "F"), g.getShortestPath(0, 5).nodeList());
    }

    @Test
    void cycle() {
        Digraph<String> digraph = new Digraph<>(List.of("A", "B", "C", "D"));
        digraph.addEdge("A", "B");
        digraph.addEdge("B", "C");
        digraph.addEdge("C", "A");
        digraph.addEdge("D", "C");
        assertTrue(digraph.hasCycle());

        Digraph<String> g2 = new Digraph<>(List.of("A", "B", "C", "D", "E", "F", "G", "H"));
        g2.addEdge("A", "C");
        g2.addEdge("C", "D");
        g2.addEdge("D", "E");
        g2.addEdge("E", "F");
        g2.addEdge("E", "C");
        g2.addEdge("B", "G");
        g2.addEdge("G", "H");
        g2.addEdge("H", "F");
        g2.addEdge("H", "G");
        assertTrue(g2.hasCycle());

        g2.removeEdge("E", "C");
        g2.removeEdge("H", "G");
        assertFalse(g2.hasCycle());
    }

    @Test
    void reverse() {
        Digraph<Character> digraph = new Digraph<>(List.of('A', 'B', 'C'));
        digraph.addEdgeByIndex(0, 1);
        digraph.addEdgeByIndex(1, 2);

        Digraph<Character> reverse = digraph.reverse();
        for (Edge edge : reverse.getEdgeSet()) {
            int target = edge.getTarget();
            if (target == 1) {
                assertEquals(2, edge.getSource());
            } else if (target == 0) {
                assertEquals(1, edge.getSource());
            }
        }
    }

    @Test
    void topologicalSort() {
        Digraph<String> graph = new Digraph<>(List.of("A", "B", "C", "D", "E", "F", "G", "H"));

        graph.addEdge("A", "D");
        graph.addEdge("A", "E");
        graph.addEdge("B", "E");
        graph.addEdge("C", "F");
        graph.addEdge("D", "G");
        graph.addEdge("E", "G");
        graph.addEdge("F", "H");
        graph.addEdge("G", "H");

        CollectListVisitor visitor = new CollectListVisitor();
        graph.topological(visitor);

        assertIterableEquals(List.of(0, 1, 2, 3, 4, 5, 6, 7), visitor.getList());
    }

    @Test
    void topo2() {
        Digraph<String> graph = new Digraph<>(List.of("v1", "v2", "v3", "v4", "v5"));

        graph.addEdgeByIndex(0, 2);
        graph.addEdgeByIndex(0, 3);
        graph.addEdgeByIndex(1, 3);
        graph.addEdgeByIndex(3, 2);
        graph.addEdgeByIndex(3, 4);
        graph.addEdgeByIndex(2, 4);

        CollectListVisitor visitor = new CollectListVisitor();
        graph.topological(visitor);
        assertIterableEquals(List.of(0, 1, 3, 2, 4), visitor.getList());
    }
}