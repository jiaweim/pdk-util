package pdk.util.graph;

import org.junit.jupiter.api.Test;
import pdk.util.graph.util.CollectListVisitor;
import pdk.util.graph.util.DepthFirstIterator;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 20 Jul 2025, 9:48 PM
 */
class UndirectedGraphTest {

    @Test
    void test() {
        List<String> nodes = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I");
        UndirectedGraph<String> graph = new UndirectedGraph<>(nodes);
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("A", "D");
        graph.addEdge("A", "E");
        graph.addEdge("B", "F");
        graph.addEdge("F", "H");
        graph.addEdge("D", "G");
        graph.addEdge("G", "I");

        CollectListVisitor visitor = new CollectListVisitor();
        graph.dfs("A", visitor);
        System.out.println(visitor.getList());
    }

    @Test
    void testUndirectedGraph() {

        UndirectedGraph<Integer> graph = new UndirectedGraph<>(List.of(1, 2, 3, 4, 5));

        graph.addEdgeByIndex(1, 4);
        graph.addEdgeByIndex(4, 2);
        graph.addEdgeByIndex(4, 5);
        graph.addEdgeByIndex(2, 5);
        graph.addEdgeByIndex(3, 5);

        DepthFirstIterator<Integer> it = new DepthFirstIterator<>(graph, 1);
        while (it.hasNext()) {
            Integer node = it.next();
            System.out.println(node);
        }
    }

    @Test
    void dfs() {
        UndirectedGraph<Character> graph = new UndirectedGraph<>(List.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'));

        graph.addEdgeByIndex('A', 'B');
        graph.addEdgeByIndex('A', 'C');
        graph.addEdgeByIndex('A', 'D');
        graph.addEdgeByIndex('A', 'E');
        graph.addEdgeByIndex('B', 'F');
        graph.addEdgeByIndex('D', 'G');
        graph.addEdgeByIndex('F', 'H');
        graph.addEdgeByIndex('G', 'I');

        CollectListVisitor list = new CollectListVisitor();
        graph.dfs(0, list);
        assertIterableEquals(List.of('A', 'B', 'F', 'H', 'C', 'D', 'G', 'I', 'E'), list.getList());
    }

    @Test
    void dfs2() {
        UndirectedGraph<Character> graph = new UndirectedGraph<>(List.of('A', 'B', 'C', 'D', 'E'));
        graph.addEdge('A', 'B');
        graph.addEdge('A', 'D');
        graph.addEdge('B', 'C');
        graph.addEdge('D', 'E');

        CollectListVisitor list = new CollectListVisitor();
        graph.dfs('A', list);
        assertIterableEquals(List.of('A', 'B', 'C', 'D', 'E'), list.getList());
    }

    @Test
    void dfs3() {
        // example from https://www.w3schools.com/dsa/dsa_algo_graphs_traversal.php
        UndirectedGraph<Character> graph = new UndirectedGraph<>(List.of('A', 'B', 'C', 'D', 'E', 'F', 'G'));
        graph.addEdge('A', 'C');
        graph.addEdge('A', 'D');
        graph.addEdge('A', 'E');
        graph.addEdge('B', 'C');
        graph.addEdge('B', 'F');
        graph.addEdge('C', 'E');
        graph.addEdge('C', 'F');
        graph.addEdge('C', 'G');

        CollectListVisitor visitor = new CollectListVisitor();
        graph.dfs(3, visitor);
        List<Character> nodes = graph.getNodes(visitor.getList());
        assertIterableEquals(List.of('D', 'A', 'C', 'B', 'F', 'E', 'G'), nodes);
    }

    @Test
    void bfs() {
        UndirectedGraph<Character> graph = new UndirectedGraph<>(List.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'));
        graph.addEdge('A', 'B');
        graph.addEdge('A', 'C');
        graph.addEdge('A', 'D');
        graph.addEdge('A', 'E');
        graph.addEdge('B', 'F');
        graph.addEdge('D', 'G');
        graph.addEdge('F', 'H');
        graph.addEdge('G', 'I');

        CollectListVisitor list = new CollectListVisitor();
        graph.bfs('A', list);
        assertIterableEquals(List.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'), list.getList());

        UndirectedGraph<Character> g = new UndirectedGraph<>(List.of('A', 'B', 'C', 'D', 'E'));
        g.addEdge('A', 'B');
        g.addEdge('B', 'C');
        g.addEdge('A', 'D');
        g.addEdge('D', 'E');

        CollectListVisitor list2 = new CollectListVisitor();
        g.bfs('A', list2);
        assertIterableEquals(List.of('A', 'B', 'D', 'C', 'E'), list2.getList());
    }

    @Test
    void bfs2() {
        // example from https://www.w3schools.com/dsa/dsa_algo_graphs_traversal.php
        UndirectedGraph<Character> graph = new UndirectedGraph<>(List.of('A', 'B', 'C', 'D', 'E', 'F', 'G'));
        graph.addEdge('A', 'C');
        graph.addEdge('A', 'D');
        graph.addEdge('A', 'E');
        graph.addEdge('B', 'C');
        graph.addEdge('B', 'F');
        graph.addEdge('C', 'E');
        graph.addEdge('C', 'F');
        graph.addEdge('C', 'G');

        List<Integer> list = graph.bfs('D');
        List<Character> nodes = graph.getNodes(list);
        assertIterableEquals(List.of('D', 'A', 'C', 'E', 'B', 'F', 'G'), nodes);
    }

    @Test
    void mst() {
        UndirectedGraph<Character> graph = new UndirectedGraph<>(List.of('A', 'B', 'C', 'D', 'E'));
        Edge e1 = new Edge('A', 'B');
        Edge e2 = new Edge('A', 'C');
        Edge e3 = new Edge('A', 'D');
        Edge e4 = new Edge('A', 'E');
        Edge e5 = new Edge('B', 'C');
        Edge e6 = new Edge('B', 'D');
        Edge e7 = new Edge('B', 'E');
        Edge e8 = new Edge('C', 'D');
        Edge e9 = new Edge('C', 'E');
        Edge e10 = new Edge('D', 'E');
        graph.addAllEdges(Arrays.asList(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10));

        MinimumSpanningTree tree = graph.mst('A');
        assertEquals(Set.of(e1, e5, e8, e10), tree.getEdges());
    }
}