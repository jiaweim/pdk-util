package pdk.util.graph.util;

import pdk.util.graph.Digraph;
import pdk.util.graph.GraphPath;

import java.util.HashMap;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 03 Dec 2024, 3:14 PM
 */
public class DijkstraAllShortestPath<V> {

    private final HashMap<V, DijkstraShortestPath<V>> all;

    public DijkstraAllShortestPath(Digraph<V> digraph) {
        int V = digraph.getNodeCount();
        all = new HashMap<>(V);
        for (V v : digraph.getNodeSet()) {
            all.put(v, new DijkstraShortestPath<>(digraph, v));
        }
    }

    /**
     * Return the shortest path from the source node to given target node
     *
     * @param s start node
     * @param t end note
     * @return shortest path
     */
    public GraphPath<V> getPath(V s, V t) {
        return all.get(s).getPath(t);
    }
}
