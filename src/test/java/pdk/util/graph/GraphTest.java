package pdk.util.graph;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 21 Jul 2025, 9:52 AM
 */
class GraphTest {

    @Test
    void createUndirectedGraph() {
        UndirectedGraph<Integer> graph = new UndirectedGraph<>(List.of(1, 2, 3, 4, 5));
        graph.addEdge(1, 2);
        graph.addEdge(1, 5);
        graph.addEdge(2, 3);
        graph.addEdge(2, 4);
        graph.addEdge(2, 5);
        graph.addEdge(3, 4);
        graph.addEdge(4, 5);
        System.out.println(graph);
    }

    @Test
    void createDirectedGraph() {
        Digraph<Integer> graph = new Digraph<>(List.of(1, 2, 3, 4, 5, 6));
        graph.addEdge(1, 2);
        graph.addEdge(1, 4);
        graph.addEdge(2, 5);
        graph.addEdge(3, 5);
        graph.addEdge(3, 6);
        graph.addEdge(4, 2);
        graph.addEdge(5, 4);
        System.out.println(graph);
    }

    @Test
    void breadthFirstIteratorDirected() {
        UndirectedGraph<Integer> graph = new UndirectedGraph<>(List.of(0, 1, 2, 3, 4));
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 3);
        graph.addEdge(1, 2);
        graph.addEdge(2, 4);
        GraphIterator<Integer> it = graph.breadthFirstIterator(0);
        assert it.hasNext();
        assertEquals(0, it.next());

        // 2,3,4 should be equals
        assert it.hasNext();
        Set<Integer> nodes = new HashSet<>();
        nodes.add(it.next());
        assert it.hasNext();
        nodes.add(it.next());
        assert it.hasNext();
        nodes.add(it.next());

        assertEquals(Set.of(1, 2, 3), nodes);

        assertTrue(it.hasNext());
        assertEquals(4, it.next());

        assertFalse(it.hasNext());
    }

    @Test
    void breadthFirstIteratorUndirected() {

        UndirectedGraph<String> graph = Graph.undirectedGraph("r", "s", "t", "u",
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
        GraphIterator<String> it = graph.breadthFirstIterator("s");
        List<String> nodes = new ArrayList<>();
        while (it.hasNext()) {
            String node = it.next();
            nodes.add(node);
        }
        assertIterableEquals(List.of("s", "r", "w", "v", "t", "x", "u", "y"), nodes);

    }

}