package pdk.util.graph;

import org.junit.jupiter.api.Test;
import pdk.util.graph.util.CollectListVisitor;

import java.util.ArrayList;
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
        digraph.addEdge(0, 1);
        digraph.addEdge(0, 2);
        digraph.addEdge(1, 3);
        digraph.addEdge(2, 3);

        List<List<Integer>> allPaths = digraph.findAllPaths(0, 3);
        assertEquals(2, allPaths.size());
        assertIterableEquals(List.of(0, 1, 3), allPaths.get(0));
        assertIterableEquals(List.of(0, 2, 3), allPaths.get(1));
    }

    @Test
    void allPath2() {
        Digraph<Integer> graph = new Digraph<>(List.of(0, 1, 2, 3));
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);

        List<List<Integer>> paths = graph.findAllPaths(0, 3);
        assertIterableEquals(List.of(0, 1, 2, 3), paths.get(0));
        assertIterableEquals(List.of(0, 1, 3), paths.get(1));
        assertIterableEquals(List.of(0, 2, 3), paths.get(2));
    }

    @Test
    void allPath3() {
        Digraph<Integer> graph = new Digraph<>(0, 1, 2, 3, 4);
        graph.addEdge(0, 1);
        graph.addEdge(0, 3);
        graph.addEdge(0, 4);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);

        List<List<Integer>> paths = graph.findAllPaths(0, 4);
        assertEquals(5, paths.size());
        assertIterableEquals(List.of(0, 1, 2, 3, 4), paths.get(0));
        assertIterableEquals(List.of(0, 1, 3, 4), paths.get(1));
        assertIterableEquals(List.of(0, 1, 4), paths.get(2));
        assertIterableEquals(List.of(0, 3, 4), paths.get(3));
        assertIterableEquals(List.of(0, 4), paths.get(4));
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

        assertIterableEquals(List.of("A", "B"), g.getShortestPath("A", "B").nodeList());
        assertIterableEquals(List.of("A", "C"), g.getShortestPath("A", "C").nodeList());
        assertIterableEquals(List.of("A", "B", "D"), g.getShortestPath("A", "D").nodeList());
        assertIterableEquals(List.of("A", "B", "D", "E"), g.getShortestPath("A", "E").nodeList());
        assertIterableEquals(List.of("A", "B", "D", "F"), g.getShortestPath("A", "F").nodeList());
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
        digraph.addEdge('A', 'B');
        digraph.addEdge('B', 'C');

        Digraph<Character> reverse = digraph.reverse();
        for (Edge<Character> edge : reverse.getEdgeSet()) {
            Character target = edge.getTarget();
            if (target == 1) {
                assertEquals('C', edge.getSource());
            } else if (target == 0) {
                assertEquals('B', edge.getSource());
            }
        }
    }

    // sample from introduction to algorithms
    @Test
    void topologicalSort2() {
        Digraph<String> graph = new Digraph<>("A", "B", "C", "D", "E");
        graph.addEdge("B", "A");
        graph.addEdge("B", "E");
        graph.addEdge("C", "A");
        graph.addEdge("D", "B");
        graph.addEdge("D", "C");
        graph.addEdge("E", "A");
        graph.addEdge("E", "C");
        List<String> nodeList = new ArrayList<>();
        graph.topological(nodeList::add);
        assertIterableEquals(List.of("D", "B", "E", "C", "A"), nodeList);
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

        CollectListVisitor<String> visitor = new CollectListVisitor<>();
        graph.topological(visitor);

        assertIterableEquals(List.of("A", "B", "C", "D", "E", "F", "G", "H"), visitor.getList());
    }

    @Test
    void topo2() {
        Digraph<String> graph = new Digraph<>(List.of("v1", "v2", "v3", "v4", "v5"));

        graph.addEdge("v1", "v3");
        graph.addEdge("v1", "v4");
        graph.addEdge("v2", "v4");
        graph.addEdge("v4", "v3");
        graph.addEdge("v4", "v5");
        graph.addEdge("v3", "v5");

        CollectListVisitor<String> visitor = new CollectListVisitor<>();
        graph.topological(visitor);
        assertIterableEquals(List.of("v1", "v2", "v4", "v3", "v5"), visitor.getList());
    }
}