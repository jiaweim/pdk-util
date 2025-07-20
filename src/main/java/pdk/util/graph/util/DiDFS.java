package pdk.util.graph.util;

import pdk.util.graph.Digraph;
import pdk.util.graph.Edge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static pdk.util.ArgUtils.checkNotNull;

/**
 * Find the nodes reachable from a given source node (or nodes) in a digraph using depth first search (unweighted).
 * <p>
 * The constructor takes time O(V+E).
 * ⭐
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 02 Dec 2024, 5:19 PM
 */
public class DiDFS<V> {

    private final Digraph<V> digraph;
    // visited[v]=true if v is reachable from source s
    private final boolean[] visited;
    // number of nodes reachable from source s
    private int count = 0;

    /**
     * Computes the nodes in digraph that are connected to the source node <code>s</code>.
     *
     * @param g digraph
     * @param s source node
     */
    public DiDFS(Digraph<V> g, int s) {
        this.digraph = g;
        visited = new boolean[g.getNodeCount()];
        validateNode(s);
        dfs(g, s);
    }

    public DiDFS(Digraph<V> g, Collection<Integer> sources) {
        checkNotNull(sources);
        this.digraph = g;
        if (sources.isEmpty()) {
            throw new IllegalArgumentException("zero source node");
        }

        visited = new boolean[g.getNodeCount()];
        for (int s : sources) {
            if (!visited[s])
                dfs(g, s);
        }
    }

    private void dfs(Digraph<V> g, int v) {
        count++;
        visited[v] = true;
        for (Edge edge : g.getOutgoingEdges(v)) {
            int target = edge.getTarget();
            if (!visited[target])
                dfs(g, target);
        }

    }

    private void validateNode(int v) {
        if (v < 0 || v >= visited.length) {
            throw new IllegalArgumentException("node " + v + " is not between 0 and " + (visited.length - 1));
        }
    }

    /**
     * Is there a directed path from the source node to the node <code>v</code>
     *
     * @param v the node
     * @return true if there is a directed path, false otherwise
     */
    public boolean isReachable(int v) {
        validateNode(v);
        return visited[v];
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
        List<V> list = new ArrayList<>(visited.length);
        for (int v = 0; v < visited.length; v++) {
            if (visited[v]) {
                list.add(digraph.getNode(v));
            }
        }
        return list;
    }

}

