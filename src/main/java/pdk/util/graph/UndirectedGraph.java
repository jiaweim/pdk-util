package pdk.util.graph;

import org.jspecify.annotations.Nullable;
import pdk.util.graph.util.CollectListVisitor;

import java.util.*;

import static pdk.util.ArgUtils.*;

/**
 * Undirected graph.
 * <p>
 * If an undirected graph with N nodes has more than N-1 edges, it must have cycles.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Nov 2024, 3:36 PM
 */
public class UndirectedGraph<V> extends AbstractGraph<V> {

    private final ArrayList<Edge>[] adjacencyList;

    public UndirectedGraph(Collection<V> nodes) {
        super(nodes);
        adjacencyList = (ArrayList<Edge>[]) new ArrayList[nodes.size()];
        for (int i = 0; i < V; i++) {
            adjacencyList[i] = new ArrayList<>(2);
        }
    }

    @Override
    public boolean addEdgeByIndex(int source, int target, Edge edge) {
        checkNotNull(edge);
        checkElementIndex(source, V);
        checkElementIndex(target, V);
        if (edgeSet.contains(edge)) {
            throw new IllegalArgumentException("The edge already exists");
        }
        checkArgument(edge.getSource() == source);
        checkArgument(edge.getTarget() == target);
        adjacencyList[source].add(edge);
        adjacencyList[target].add(edge);
        return true;
    }

    @Override
    public @Nullable Edge getEdge(int source, int target) {
        if (source < V && target < V) {
            for (Edge edge : adjacencyList[source]) {
                if (getOppositeNode(edge, source) == target) {
                    return edge;
                }
            }
        }
        return null;
    }

    @Override
    public boolean removeEdge(Edge edge) {
        if (edgeSet.contains(edge)) {
            int source = edge.getSource();
            int target = edge.getTarget();
            adjacencyList[source].remove(target);
            adjacencyList[target].remove(source);
            edgeSet.remove(edge);
            return true;
        }
        return false;
    }


    @Override
    public int getInDegree(int node) {
        return adjacencyList[node].size();
    }

    @Override
    public int getOutDegree(int node) {
        return adjacencyList[node].size();
    }

    @Override
    public int getDegree(int node) {
        return adjacencyList[node].size();
    }

    @Override
    public List<Edge> getEdges(int node) {
        return Collections.unmodifiableList(adjacencyList[node]);
    }

    @Override
    public List<Edge> getIncomingEdges(int node) {
        return Collections.unmodifiableList(adjacencyList[node]);
    }

    @Override
    public List<Edge> getOutgoingEdges(int node) {
        return Collections.unmodifiableList(adjacencyList[node]);
    }

    /**
     * depth first search for undirected graph
     *
     * @param startNode start node
     * @param visitor   {@link NodeVisitor}
     * @since 2024-11-28 ⭐
     */
    public void dfs(int startNode, NodeVisitor visitor) {
        visitor.visit(startNode);

        boolean[] visited = new boolean[V];
        visited[startNode] = true;

        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(startNode);

        while (!stack.isEmpty()) {
            Integer node = stack.peek();
            int nextUnvisited = -1;
            for (Edge edge : getEdges(node)) {
                int v = getOppositeNode(edge, node);
                if (!visited[v]) {
                    nextUnvisited = v;
                    break;
                }
            }
            if (nextUnvisited == -1) {
                stack.pop();
            } else {
                visited[nextUnvisited] = true;
                visitor.visit(nextUnvisited);
                stack.push(nextUnvisited);
            }
        }
    }

    /**
     * depth first search for undirected, unweighted graph
     *
     * @param startNode start node
     * @param visitor   {@link NodeVisitor}
     * @since 2024-11-28 ⭐
     */
    public void dfs(V startNode, NodeVisitor visitor) {
        dfs(indexOf(startNode), visitor);
    }

    /**
     * Generate minimum spanning tree for undirected unweighted graph using dfs.
     *
     * @param startNode a node
     * @return {@link MinimumSpanningTree}
     * @since 2024-11-29⭐
     */
    public MinimumSpanningTree mst(int startNode) {
        boolean[] visited = new boolean[V];
        visited[startNode] = true;

        ArrayDeque<Integer> stack = new ArrayDeque<>();
        stack.push(startNode);

        Set<Edge> edges = new HashSet<>();
        while (!stack.isEmpty()) {
            Integer node = stack.peekFirst();

            // get a unvisited neighbor
            int v = -1;
            Edge vEdge = null;
            for (Edge edge : getEdges(node)) {
                int v1 = getOppositeNode(edge, node);
                if (!visited[v1]) {
                    v = v1;
                    vEdge = edge;
                    break;
                }
            }

            if (v == -1) { // no unvisited neighbor
                stack.pop();
            } else {
                visited[v] = true;
                stack.push(v);
                edges.add(vEdge);
            }
        }
        return new MinimumSpanningTree(edges, edges.size());
    }

    /**
     * Generate minimum spanning tree for undirected unweighted graph using dfs.
     *
     * @param startNode a node
     * @return {@link MinimumSpanningTree}
     * @since 2024-11-29⭐
     */
    public MinimumSpanningTree mst(V startNode) {
        return mst(indexOf(startNode));
    }

    /**
     * depth first search for undirected, unweighted graph
     *
     * @param startNode start node index
     * @param visitor   {@link NodeVisitor} to visit node
     * @since 2024-11-29 ⭐
     */
    public void bfs(int startNode, NodeVisitor visitor) {
        visitor.visit(startNode);
        boolean[] visited = new boolean[V];
        visited[startNode] = true;

        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.addLast(startNode);

        while (!queue.isEmpty()) {
            int v1 = queue.removeFirst();
            for (Edge edge : getOutgoingEdges(v1)) {
                int v2 = getOppositeNode(edge, v1);
                if (visited[v2])
                    continue;
                visited[v2] = true;
                visitor.visit(v2);
                queue.addLast(v2);
            }
        }
    }

    /**
     * depth first search for undirected, unweighted graph
     *
     * @param startNode start node
     * @return list of nodes connected to startNode in breadth first order
     * @since 2024-11-29 ⭐
     */
    public List<Integer> bfs(V startNode) {
        CollectListVisitor visitor = new CollectListVisitor();
        int start = indexOf(startNode);
        bfs(start, visitor);
        return visitor.getList();
    }
}
