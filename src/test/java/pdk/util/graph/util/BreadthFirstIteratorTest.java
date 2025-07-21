package pdk.util.graph.util;

import org.junit.jupiter.api.Test;
import pdk.util.graph.Digraph;
import pdk.util.graph.UndirectedGraph;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 17 Apr 2025, 7:02 PM
 */
class BreadthFirstIteratorTest {

    @Test
    void testUndirected() {
        UndirectedGraph<String> graph = new UndirectedGraph<>("r", "s", "t", "u",
                "v", "w", "x", "y");
        graph.addEdge("r", "s");
        graph.addEdge("r", "v");
        graph.addEdge("s", "w");
        graph.addEdge("t", "u");
        graph.addEdge("t", "w");
        graph.addEdge("t", "x");
        graph.addEdge("u", "x");
        graph.addEdge("u", "y");
        graph.addEdge("w", "x");
        graph.addEdge("x", "y");
        List<String> nodes = new ArrayList<>();
        BreadthFirstIterator<String> it = new BreadthFirstIterator<>(graph, "s");
        while (it.hasNext()) {
            String node = it.next();
            nodes.add(node);
        }
        assertIterableEquals(List.of("s", "r", "w", "v", "t", "x", "u", "y"), nodes);
        assertEquals(0, it.getDepth("s"));
        assertEquals(1, it.getDepth("r"));
        assertEquals(1, it.getDepth("w"));
        assertEquals(2, it.getDepth("v"));
        assertEquals(2, it.getDepth("t"));
        assertEquals(2, it.getDepth("x"));
        assertEquals(3, it.getDepth("u"));
        assertEquals(3, it.getDepth("y"));
    }

    @Test
    void testUndirectedMethod() {
        UndirectedGraph<String> graph = new UndirectedGraph<>("r", "s", "t", "u",
                "v", "w", "x", "y");
        graph.addEdge("r", "s");
        graph.addEdge("r", "v");
        graph.addEdge("s", "w");
        graph.addEdge("t", "u");
        graph.addEdge("t", "w");
        graph.addEdge("t", "x");
        graph.addEdge("u", "x");
        graph.addEdge("u", "y");
        graph.addEdge("w", "x");
        graph.addEdge("x", "y");
        List<String> nodes = graph.bfs("s");
        assertIterableEquals(List.of("s", "r", "w", "v", "t", "x", "u", "y"), nodes);
    }

    @Test
    void test() {
        // example from https://www.w3schools.com/dsa/dsa_algo_graphs_traversal.php
        Digraph<String> digraph = new Digraph<>("A", "B", "C", "D", "E", "F", "G");
        digraph.addEdge("A", "C");
        digraph.addEdge("B", "C");
        digraph.addEdge("C", "E");
        digraph.addEdge("C", "F");
        digraph.addEdge("C", "G");
        digraph.addEdge("D", "A");
        digraph.addEdge("D", "E");
        digraph.addEdge("E", "A");
        digraph.addEdge("F", "B");

        BreadthFirstIterator<String> it = new BreadthFirstIterator<>(digraph, "D");
        assertTrue(it.hasNext());

        String node = it.next();
        assertEquals("D", node);

        assertTrue(it.hasNext());
        node = it.next();
        assertEquals("A", node);

        assertTrue(it.hasNext());
        node = it.next();
        assertEquals("E", node);

        assertTrue(it.hasNext());
        node = it.next();
        assertEquals("C", node);

        assertTrue(it.hasNext());
        node = it.next();
        assertEquals("F", node);

        assertTrue(it.hasNext());
        node = it.next();
        assertEquals("G", node);

        assertTrue(it.hasNext());
        node = it.next();
        assertEquals("B", node);

        assertFalse(it.hasNext());
    }
}