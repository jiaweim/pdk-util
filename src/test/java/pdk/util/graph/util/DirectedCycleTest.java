package pdk.util.graph.util;

import org.junit.jupiter.api.Test;
import pdk.util.ArrayUtils;
import pdk.util.graph.Digraph;

import java.util.Deque;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 17 Apr 2025, 5:26 PM
 */
class DirectedCycleTest {

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

        DirectedCycle<Integer> diCycle = new DirectedCycle<>(digraph);
        Iterable<Integer> cycle = diCycle.getCycle();

        assertIterableEquals(List.of(0, 5, 4, 2, 0), cycle);
    }

    @Test
    void test2() {
        Digraph<String> g = new Digraph<>(List.of("A", "B", "C", "D", "E"));
        g.addEdge("A", "B");
        g.addEdge("A", "C");
        g.addEdge("B", "C");
        g.addEdge("C", "D");
        g.addEdge("D", "E");
        g.addEdge("E", "B"); // back-edge

        DirectedCycle<String> diCycle = new DirectedCycle<>(g);
        assertTrue(diCycle.hasCycle());
        assertIterableEquals(List.of("B", "C", "D", "E", "B"), diCycle.getCycle());
    }

    @Test
    void test3() {
        Digraph<String> g = new Digraph<>(List.of("A", "B", "C", "D", "E", "F", "G", "H"));
        g.addEdge("A", "C");
        g.addEdge("C", "D");
        g.addEdge("D", "E");
        g.addEdge("E", "F");
        g.addEdge("E", "C");
        g.addEdge("B", "G");
        g.addEdge("G", "H");
        g.addEdge("H", "G");
        g.addEdge("H", "F");

        DirectedCycle<String> cycle = new DirectedCycle<>(g);
        assertTrue(cycle.hasCycle());
        assertIterableEquals(List.of("C", "D", "E", "C"), cycle.getCycle());
    }

    @Test
    void test4() {
        Digraph<String> g = new Digraph<>(List.of("A", "B", "C", "D", "E", "F"));
        g.addEdge("A", "B");
        g.addEdge("B", "C");
        g.addEdge("C", "E");
        g.addEdge("E", "D");
        g.addEdge("D", "F");
        g.addEdge("D", "A"); // back-edge
        g.addEdge("D", "B"); // back-edge
        g.addEdge("F", "E"); // back-edge
        DirectedCycle<String> cycle = new DirectedCycle<>(g);
        assertTrue(cycle.hasCycle());
        assertIterableEquals(List.of("E", "D", "F", "E"), cycle.getCycle());
    }

    @Test
    public void testEmptyGraph() {

        Digraph<Integer> graph = new Digraph<>(List.of());
        DirectedCycle<Integer> finder = new DirectedCycle<>(graph);

        assertFalse(finder.hasCycle());
        assertNull(finder.getCycle());
    }

    @Test
    public void testSingleVertex() {
        Digraph<Integer> graph = new Digraph<>(List.of(1));
        DirectedCycle<Integer> finder = new DirectedCycle<>(graph);

        assertFalse(finder.hasCycle());
    }

    @Test
    public void testSingleEdge() {
        Digraph<Integer> graph = new Digraph<>(List.of(1, 2));
        graph.addEdge(1, 2, 1);

        DirectedCycle<Integer> finder = new DirectedCycle<>(graph);

        assertFalse(finder.hasCycle());
    }

    @Test
    public void testLinearGraph() {
        Digraph<Integer> graph = new Digraph<>(List.of(1, 2, 3, 4));

        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 1);

        DirectedCycle<Integer> finder = new DirectedCycle<>(graph);
        assertFalse(finder.hasCycle());
    }

    @Test
    public void testSimpleCycle() {

        Digraph<Integer> graph = new Digraph<>(List.of(1, 2, 3));

        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 1, 1);

        DirectedCycle<Integer> finder = new DirectedCycle<>(graph);

        assertTrue(finder.hasCycle());
        assertCycle(graph, finder.getCycle());
    }

    @Test
    public void testSelfLoop() {
        Digraph<Integer> graph = new Digraph<>(List.of(1));
        graph.addEdge(1, 1, 1);

        DirectedCycle<Integer> finder = new DirectedCycle<>(graph);

        assertTrue(finder.hasCycle());
        assertCycle(graph, finder.getCycle());
    }

    @Test
    public void testDisconnectedGraphWithoutCycle() {
        Digraph<Integer> graph = new Digraph<>(List.of(1, 2, 3, 4, 5));

        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(4, 5, 1);

        DirectedCycle<Integer> finder = new DirectedCycle<>(graph);

        assertFalse(finder.hasCycle());
    }

    @Test
    public void testDisconnectedGraphWithCycle() {
        Digraph<Integer> graph = new Digraph<>(List.of(1, 2, 3, 4, 5));

        graph.addEdge(1, 2, 1);
        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 5, 1);
        graph.addEdge(5, 3, 1);

        DirectedCycle<Integer> finder = new DirectedCycle<>(graph);

        assertTrue(finder.hasCycle());
        assertCycle(graph, finder.getCycle());
    }

    @Test
    public void testCrossEdgeNotCycle() {

        Digraph<Integer> graph = new Digraph<>(List.of(1, 2, 3, 4));

        graph.addEdge(1, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 4, 1);
        graph.addEdge(3, 4, 1);

        DirectedCycle<Integer> finder = new DirectedCycle<>(graph);

        assertFalse(finder.hasCycle());
    }

    @Test
    public void testBackEdgeCycle() {

        Digraph<Integer> graph = new Digraph<>(List.of(1, 2, 3, 4));

        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 2, 1);

        DirectedCycle<Integer> finder = new DirectedCycle<>(graph);

        assertTrue(finder.hasCycle());

        assertCycle(graph, finder.getCycle());
    }

    /**
     * Verify returned cycle.
     */
    private static <V> void assertCycle(Digraph<V> graph, Deque<V> cycle) {

        assertNotNull(cycle);
        assertTrue(cycle.size() >= 2);

        assertEquals(cycle.getFirst(), cycle.getLast());

        List<V> list = List.copyOf(cycle);
        for (int i = 0; i < list.size() - 1; i++) {
            V from = list.get(i);
            V to = list.get(i + 1);

            boolean found = graph.getOutgoingEdges(from)
                    .stream()
                    .anyMatch(e -> e.getTarget().equals(to));

            assertTrue(found, "Missing edge: " + from + " -> " + to);
        }
    }

    @Test
    public void testLongChain() {
        int n = 10000;

        Digraph<Integer> graph = new Digraph<>(java.util.stream.IntStream.range(0, n)
                .boxed().toList());

        for (int i = 0; i < n - 1; i++) {
            graph.addEdge(i, i + 1, 1);
        }

        DirectedCycle<Integer> finder = new DirectedCycle<>(graph);
        assertFalse(finder.hasCycle());
    }

    @Test
    public void testLargeCycle() {

        int n = 5000;
        Digraph<Integer> graph =
                new Digraph<>(java.util.stream.IntStream.range(0, n)
                        .boxed()
                        .toList());

        for (int i = 0; i < n - 1; i++) {
            graph.addEdge(i, i + 1, 1);
        }

        graph.addEdge(n - 1, 0, 1);

        DirectedCycle<Integer> finder = new DirectedCycle<>(graph);

        assertTrue(finder.hasCycle());

        assertCycle(graph, finder.getCycle());
    }
}