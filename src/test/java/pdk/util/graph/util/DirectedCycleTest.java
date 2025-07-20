package pdk.util.graph.util;

import org.junit.jupiter.api.Test;
import pdk.util.ArrayUtils;
import pdk.util.graph.Digraph;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertIterableEquals(List.of("B", "C", "D", "E", "B"), g.getNodes(diCycle.getCycle()));
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
        assertIterableEquals(List.of("C", "D", "E", "C"), g.getNodes(cycle.getCycle()));
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
        assertIterableEquals(List.of("E", "D", "F", "E"), g.getNodes(cycle.getCycle()));
    }
}