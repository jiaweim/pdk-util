package pdk.util.graph.util;

import pdk.util.graph.Edge;

import java.util.List;

/**
 * Stack used to replace recursion in DFS.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 09 Jul 2026, 12:56 PM
 */
public final class Frame<V> {

    final V vertex;
    final List<Edge<V>> edges;
    int index;

    public Frame(V vertex, List<Edge<V>> edges) {
        this.vertex = vertex;
        this.edges = edges;
    }
}
