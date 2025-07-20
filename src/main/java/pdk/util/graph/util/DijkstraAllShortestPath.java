package pdk.util.graph.util;

import pdk.util.graph.Digraph;
import pdk.util.graph.GraphPath;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 03 Dec 2024, 3:14 PM
 */
public class DijkstraAllShortestPath<V> {

    private final DijkstraShortestPath<V>[] all;

    public DijkstraAllShortestPath(Digraph<V> digraph) {
        int V = digraph.getNodeCount();
        all = new DijkstraShortestPath[V];
        for (int v = 0; v < V; v++) {
            all[v] = new DijkstraShortestPath<>(digraph, v);
        }
    }

    /**
     * Return the shortest path from the source node to given target node
     *
     * @param s start node
     * @param t end note
     * @return shortest path
     */
    public GraphPath<V> getPath(int s, int t) {
        return all[s].getPath(t);
    }
}
