package pdk.util.graph.util;

import org.junit.jupiter.api.Test;
import pdk.util.graph.Digraph;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 17 Apr 2025, 7:02 PM
 */
class BreadthFirstIteratorTest {

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