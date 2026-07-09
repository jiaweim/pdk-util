package pdk.util.graph.util;

import org.junit.jupiter.api.Test;
import pdk.util.ArrayUtils;
import pdk.util.graph.Digraph;
import pdk.util.graph.Edge;
import pdk.util.graph.GraphPath;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

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
        g.addEdge(4, 5, 0.35);
        g.addEdge(5, 4, -0.66);
        g.addEdge(4, 7, 0.37);
        g.addEdge(5, 7, 0.28);
        g.addEdge(7, 5, 0.28);
        g.addEdge(5, 1, 0.32);
        g.addEdge(0, 4, 0.38);
        g.addEdge(0, 2, 0.26);
        g.addEdge(7, 3, 0.39);
        g.addEdge(1, 3, 0.29);
        g.addEdge(2, 7, 0.34);
        g.addEdge(6, 2, 0.40);
        g.addEdge(3, 6, 0.52);
        g.addEdge(6, 0, 0.58);
        g.addEdge(6, 4, 0.93);

        BellmanFordShortestPath<Integer> shortestPath = new BellmanFordShortestPath<>(g, 0);
        assertTrue(shortestPath.hasNegativeCycle());
    }

    @Test
    void testSimplePathNoNegativeEdges() {
        Digraph<String> graph = new Digraph<>(List.of("A", "B", "C", "D", "E"));
        graph.addEdge("A", "B", 1.0);
        graph.addEdge("B", "C", 2.0);
        graph.addEdge("A", "C", 4.0);

        BellmanFordShortestPath<String> sp = new BellmanFordShortestPath<>(graph, "A");
        assertEquals(0.0, sp.getWeight("A"));
        assertEquals(1.0, sp.getWeight("B"));
        assertEquals(3.0, sp.getWeight("C")); // A->B->C
        assertTrue(sp.hasPathTo("B"));
        assertTrue(sp.hasPathTo("C"));
        assertFalse(sp.hasPathTo("D")); // D 不可达
    }


    @Test
    void testGraphWithNegativeWeightsNoCycle() {
        Digraph<String> graph = new Digraph<>(List.of("A", "B", "C", "D", "E"));

        graph.addEdge("A", "B", 4.0);
        graph.addEdge("A", "C", 5.0);
        graph.addEdge("C", "B", -3.0);  // 负边但无环
        graph.addEdge("B", "D", 2.0);
        graph.addEdge("C", "D", 3.0);

        BellmanFordShortestPath<String> sp = new BellmanFordShortestPath<>(graph, "A");
        // A->C->B: 5 + (-3) = 2 < 4
        assertEquals(2.0, sp.getWeight("B"));
        // A->C->D: 5+3=8, A->C->B->D: 2+2=4
        assertEquals(4.0, sp.getWeight("D"));
    }

    @Test
    void testPathNodesAndEdgesOrder() {
        Digraph<String> graph = new Digraph<>(List.of("A", "B", "C", "D", "E"));
        graph.addEdge("A", "B", 1.0);
        graph.addEdge("B", "C", 2.0);
        BellmanFordShortestPath<String> sp = new BellmanFordShortestPath<>(graph, "A");
        GraphPath<String> path = sp.getPath("C");
        assertNotNull(path);

        // 节点顺序 A -> B -> C
        List<String> nodes = new ArrayList<>(path.getNodeList());
        assertEquals(Arrays.asList("A", "B", "C"), nodes);

        // 边顺序 A->B , B->C
        List<Edge<String>> edges = new ArrayList<>(path.getEdgeList());
        assertEquals(2, edges.size());
        assertEquals("A", edges.get(0).getSource());
        assertEquals("B", edges.get(0).getTarget());
        assertEquals("B", edges.get(1).getSource());
        assertEquals("C", edges.get(1).getTarget());
    }

    @Test
    void testPathToSource() {
        Digraph<String> graph = new Digraph<>(List.of("A", "B", "C", "D", "E"));

        BellmanFordShortestPath<String> sp = new BellmanFordShortestPath<>(graph, "A");
        GraphPath<String> path = sp.getPath("A");
        assertNotNull(path);
        assertEquals(Collections.singletonList("A"), new ArrayList<>(path.getNodeList()));
        assertTrue(new ArrayList<>(path.getEdgeList()).isEmpty());
    }

    // ========== 负环检测 ==========
    @Test
    void testNegativeCycleExists() {
        Digraph<String> graph = new Digraph<>(List.of("A", "B", "C", "D", "E"));

        graph.addEdge("A", "B", 1.0);
        graph.addEdge("B", "C", 2.0);
        graph.addEdge("C", "A", -4.0);  // 总权重 -1
        BellmanFordShortestPath<String> sp = new BellmanFordShortestPath<>(graph, "A");
        assertTrue(sp.hasNegativeCycle());
        Queue<String> cycle = sp.getCycle();
        assertNotNull(cycle);
        assertTrue(new HashSet<>(cycle).containsAll(Arrays.asList("A", "B", "C")));
    }

    @Test
    void testNegativeCycleThrowsOnQuery() {
        Digraph<String> graph = new Digraph<>(List.of("A", "B", "C", "D", "E"));
        graph.addEdge("A", "B", 1.0);
        graph.addEdge("B", "A", -2.0);
        BellmanFordShortestPath<String> sp = new BellmanFordShortestPath<>(graph, "A");
        assertThrows(UnsupportedOperationException.class, () -> sp.getWeight("B"));
        assertThrows(UnsupportedOperationException.class, () -> sp.getPath("B"));
    }

    @Test
    void testNoNegativeCycleWhenNone() {
        Digraph<String> graph = new Digraph<>(List.of("A", "B", "C", "D", "E"));
        graph.addEdge("A", "B", 1.0);
        graph.addEdge("B", "C", 2.0);
        BellmanFordShortestPath<String> sp = new BellmanFordShortestPath<>(graph, "A");
        assertFalse(sp.hasNegativeCycle());
        assertNull(sp.getCycle());
    }

    // ========== 边界与特殊值 ==========
    @Test
    void testDisconnectedNodes() {
        Digraph<String> graph = new Digraph<>(List.of("A", "B", "C", "D", "E"));
        graph.addEdge("A", "B", 1.0);
        BellmanFordShortestPath<String> sp = new BellmanFordShortestPath<>(graph, "A");
        assertFalse(sp.hasPathTo("C"));
        assertEquals(Double.POSITIVE_INFINITY, sp.getWeight("C"));
        assertNull(sp.getPath("C"));
    }

    @Test
    void testZeroWeightEdges() {
        Digraph<String> graph = new Digraph<>(List.of("A", "B", "C", "D", "E"));
        graph.addEdge("A", "B", 0.0);
        graph.addEdge("B", "C", 0.0);
        BellmanFordShortestPath<String> sp = new BellmanFordShortestPath<>(graph, "A");
        assertEquals(0.0, sp.getWeight("C"));
        assertTrue(sp.hasPathTo("C"));
    }


    @Test
    void testVerySmallNegativeWeight() {
        Digraph<String> graph = new Digraph<>(List.of("A", "B", "C", "D", "E"));
        // 权重极小的边，绝对值小于 EPSILON，不应形成负环
        graph.addEdge("A", "B", -1e-15);
        graph.addEdge("B", "A", -1e-15);
        BellmanFordShortestPath<String> sp = new BellmanFordShortestPath<>(graph, "A");
        // 由于 EPSILON 过滤，不会更新距离，因此无负环
        assertFalse(sp.hasNegativeCycle());
    }

    @Test
    void testSingleNodeGraph() {
        Digraph<String> graph = new Digraph<>(List.of("A", "B", "C", "D", "E"));
        BellmanFordShortestPath<String> sp = new BellmanFordShortestPath<>(graph, "A");
        assertEquals(0.0, sp.getWeight("A"));
        assertTrue(sp.hasPathTo("A"));
        GraphPath<String> p = sp.getPath("A");
        assertNotNull(p);
        assertEquals(1, p.getNodeList().size());
        assertTrue(p.getNodeList().contains("A"));
    }

    // ========== 多次松弛触发负环检测 ==========
    @Test
    void testNegativeCycleDetectedAfterManyRelaxations() {
        Digraph<String> graph = new Digraph<>(List.of("A", "B", "C", "D", "E"));

        for (int i = 0; i < 10; i++) {
            graph.addNode("N" + i);
        }
        for (int i = 0; i < 9; i++) {
            graph.addEdge("N" + i, "N" + (i + 1), 1.0);
        }
        graph.addEdge("N9", "N0", -15.0);  // 负环
        BellmanFordShortestPath<String> sp = new BellmanFordShortestPath<>(graph, "N0");
        assertTrue(sp.hasNegativeCycle());
    }

    // ========== 多路径与收敛一致性 ==========
    @Test
    void testMultiplePathsToSameNode() {
        Digraph<String> graph = new Digraph<>(List.of("A", "B", "C", "D", "E"));
        graph.addEdge("A", "B", 2.0);
        graph.addEdge("A", "C", 1.0);
        graph.addEdge("C", "B", 0.5);
        BellmanFordShortestPath<String> sp = new BellmanFordShortestPath<>(graph, "A");
        assertEquals(1.5, sp.getWeight("B")); // A→C→B
    }

    @Test
    void testRepeatedQueryConsistency() {
        Digraph<String> graph = new Digraph<>(List.of("A", "B", "C", "D", "E"));
        graph.addEdge("A", "B", 1.0);
        graph.addEdge("B", "C", 1.0);
        graph.addEdge("A", "C", 3.0);
        BellmanFordShortestPath<String> sp = new BellmanFordShortestPath<>(graph, "A");
        double first = sp.getWeight("C");
        assertEquals(2.0, first);
        assertEquals(first, sp.getWeight("C")); // 多次查询不变
    }
}