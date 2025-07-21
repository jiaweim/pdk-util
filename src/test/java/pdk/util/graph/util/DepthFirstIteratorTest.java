package pdk.util.graph.util;

import org.junit.jupiter.api.Test;
import pdk.util.graph.Digraph;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 17 Apr 2025, 7:34 PM
 */
class DepthFirstIteratorTest {

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

        DepthFirstIterator<String> it = new DepthFirstIterator<>(digraph, "D");
        while (it.hasNext()) {
            String node = it.next();
        }

    }
}