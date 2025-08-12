package pdk.util.graph.util;

import pdk.util.graph.Digraph;
import pdk.util.graph.Edge;

import java.util.*;

import static pdk.util.ArgUtils.checkNotNull;

/**
 * Find the nodes reachable from a given source node (or nodes) in a digraph using depth first search (unweighted).
 * <p>
 * The constructor takes time O(V+E).
 * ⭐
 *
 * @param <V> type of the node
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 02 Dec 2024, 5:19 PM
 */
public class DiDFS<V> {

    private final Digraph<V> digraph;
    // visited[v]=true if v is reachable from source s
    private final HashMap<V, Boolean> visited;
    // number of nodes reachable from source s
    private int count = 0;

    /**
     * Computes the nodes in digraph that are connected to the source node <code>s</code>.
     *
     * @param g digraph
     * @param s source node
     */
    public DiDFS(Digraph<V> g, V s) {
        this.digraph = g;
        visited = new HashMap<>(g.getNodeCount());
        for (V v : g.getNodeSet()) {
            visited.put(v, false);
        }

        dfs(g, s);
    }

    public DiDFS(Digraph<V> g, Collection<V> sources) {
        checkNotNull(sources);
        this.digraph = g;
        if (sources.isEmpty()) {
            throw new IllegalArgumentException("zero source node");
        }
        visited = new HashMap<>(g.getNodeCount());
        for (V v : g.getNodeSet()) {
            visited.put(v, false);
        }

        for (V s : sources) {
            if (!visited.get(s))
                dfs(g, s);
        }
    }

    private void dfs(Digraph<V> g, V v) {
        count++;
        visited.put(v, true);
        for (Edge<V> edge : g.getOutgoingEdges(v)) {
            V target = edge.getTarget();
            if (!visited.get(target))
                dfs(g, target);
        }

    }

    /**
     * Is there a directed path from the source node to the node <code>v</code>
     *
     * @param v the node
     * @return true if there is a directed path, false otherwise
     */
    public boolean isReachable(V v) {
        return visited.get(v);
    }

    /**
     * @return the number of nodes reachable from the source node
     */
    public int getCount() {
        return count;
    }

    /**
     * @return all reachable nodes
     */
    public List<V> getReachableNodes() {
        List<V> list = new ArrayList<>(visited.size());
        for (Map.Entry<V, Boolean> entry : visited.entrySet()) {
            if (entry.getValue()) {
                list.add(entry.getKey());
            }
        }
        return list;
    }

}

