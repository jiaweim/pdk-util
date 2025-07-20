package pdk.util.graph;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.jheaps.AddressableHeap;
import org.jheaps.tree.PairingHeap;
import org.jspecify.annotations.Nullable;
import pdk.util.graph.util.BellmanFordShortestPath;
import pdk.util.graph.util.BreadthFirstIterator;

import java.util.*;

import static pdk.util.ArgUtils.*;

/**
 * Directed Graph implementation, the node should implement hashCode and equals.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 23 Nov 2024, 00:02
 */
public class Digraph<V> extends AbstractGraph<V> {

    private final ArrayList<Edge>[] incomingEdges;
    private final ArrayList<Edge>[] outgoingEdges;

    @SafeVarargs
    public Digraph(V... nodes) {
        this(Arrays.asList(nodes));
    }

    /**
     * Create a directed-graph with given nodes
     */
    public Digraph(Collection<V> nodes) {
        super(nodes);
        incomingEdges = new ArrayList[nodes.size()];
        outgoingEdges = new ArrayList[nodes.size()];
        for (int i = 0; i < V; i++) {
            incomingEdges[i] = new ArrayList<>(2);
            outgoingEdges[i] = new ArrayList<>(2);
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

        outgoingEdges[source].add(edge);
        incomingEdges[target].add(edge);
        edgeSet.add(edge);
        return true;
    }

    @Override
    public Edge getEdge(int source, int target) {
        if (source < V && target < V) {
            for (Edge edge : outgoingEdges[source]) {
                if (edge.getTarget() == target) {
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
            outgoingEdges[source].remove(edge);
            incomingEdges[target].remove(edge);
            edgeSet.remove(edge);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Edge> getIncomingEdges(int node) {
        return Collections.unmodifiableList(incomingEdges[node]);
    }

    @Override
    public List<Edge> getOutgoingEdges(int node) {
        return Collections.unmodifiableList(outgoingEdges[node]);
    }

    @Override
    public int getDegree(int node) {
        return getInDegree(node) + getOutDegree(node);
    }

    @Override
    public int getInDegree(int node) {
        return incomingEdges[node].size();
    }

    @Override
    public int getOutDegree(int node) {
        return outgoingEdges[node].size();
    }

    @Override
    public List<Edge> getEdges(int node) {
        ArrayList<Edge> edges = new ArrayList<>();
        edges.addAll(incomingEdges[node]);
        edges.addAll(outgoingEdges[node]);
        return Collections.unmodifiableList(edges);
    }

    /**
     * @return a copy of the graph with all edges in reverse direction.
     * @since 2024-12-2 ⭐
     */
    public Digraph<V> reverse() {
        Digraph<V> graph = new Digraph<>(nodeList);
        for (Edge edge : getEdgeSet()) {
            graph.addEdge(edge.reverse());
        }
        return graph;
    }

    /**
     * Detect if this directed graph has a directed cycle.
     * If you want to get the cycle path, please use {@link pdk.util.graph.util.DirectedCycle}
     *
     * @return true if this graph has a cycle
     * @since 2024-12-03 ⭐
     */
    public boolean hasCycle() {
        boolean[] visited = new boolean[V];
        boolean[] beingVisited = new boolean[V];
        for (int v = 0; v < V; v++) {
            if (!visited[v] && hasCycle(v, visited, beingVisited)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasCycle(int node, boolean[] visited, boolean[] beingVisited) {
        beingVisited[node] = true;
        for (Edge edge : getOutgoingEdges(node)) {
            int u = edge.getTarget();
            if (beingVisited[u]) {
                return true;
            } else if (!visited[u] && hasCycle(u, visited, beingVisited)) {
                return true;
            }
        }
        beingVisited[node] = false;
        visited[node] = true;
        return false;
    }

    /**
     * Bellman-Ford shortest path. features:
     * <ul>
     * <li>allow positive, negative, and zero edge weight</li>
     * <li>allow cycle</li>
     * <li>disallow negative cycle</li>
     * </ul>
     * <p>
     * If there are only positive edges,  Dijkstra algorithm is recommended, as it is faster.
     *
     * @param startNode start node
     * @return {@link BellmanFordShortestPath} instance
     */
    public BellmanFordShortestPath<V> getShortestPathFinder(int startNode) {
        return new BellmanFordShortestPath<>(this, startNode);
    }

    /**
     * Return the shortest path between <code>startNode</code> and <code>endNode</code> using Dijkstra algorithm
     *
     * @param startNode a node
     * @param endNode   a node
     * @return a path
     * @since 2024-11-03⭐
     */
    @Nullable
    public GraphPath<V> getShortestPath(int startNode, int endNode) {
        checkElementIndex(startNode, V);
        checkElementIndex(endNode, V);

        for (Edge edge : getEdgeSet()) {
            if (edge.getWeight() < 0) {
                throw new IllegalArgumentException("Edge " + edge + " has negative weight");
            }
        }
        double[] distTo = new double[V];
        Edge[] edgeTo = new Edge[V];
        boolean[] visited = new boolean[V];

        for (int v = 0; v < V; v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        distTo[startNode] = 0;

        PairingHeap<Double, Integer> heap = new PairingHeap<>();
        heap.insert(distTo[startNode], startNode);
        while (!heap.isEmpty()) {
            AddressableHeap.Handle<Double, Integer> handle = heap.deleteMin();
            Integer v = handle.getValue();
            if (v == endNode) {
                break;
            }
            for (Edge edge : getOutgoingEdges(v)) {
                int w = edge.getTarget();
                if (visited[w]) {
                    continue;
                }
                if (distTo[w] > distTo[v] + edge.getWeight()) {
                    distTo[w] = distTo[v] + edge.getWeight();
                    edgeTo[w] = edge;
                }
                heap.insert(distTo[w], w);
            }
            visited[v] = true;
        }
        if (distTo[endNode] == Double.POSITIVE_INFINITY) {
            return null;
        }

        LinkedList<Edge> edges = new LinkedList<>();
        LinkedList<Integer> nodeList = new LinkedList<>();
        nodeList.add(endNode);

        int tmp = endNode;
        while (edgeTo[tmp] != null) {
            edges.add(edgeTo[tmp]);
            tmp = edgeTo[tmp].getSource();
            nodeList.add(tmp);
        }
        return new GraphPath<>(this, getNode(startNode), getNode(endNode),
                getNodes(nodeList.reversed()), edges.reversed(), distTo[endNode]);
    }

    /**
     * Convert list of node indexes to list of edge
     *
     * @param path node list
     * @return edge list
     */
    public List<Edge> getPath(IntList path) {
        List<Edge> edges = new ArrayList<>(path.size());
        for (int i = 0; i < path.size() - 1; i++) {
            Edge edge = getEdge(path.getInt(i), path.getInt(i + 1));
            edges.add(edge);
        }
        return edges;
    }

    /**
     * Find all paths from source nodes to target nodes using depth first search
     *
     * @param srcNodes  source nodes
     * @param destNodes target nodes
     * @return paths
     */
    public List<IntList> findAllPaths(Collection<Integer> srcNodes, Collection<Integer> destNodes) {
        List<IntList> res = new ArrayList<>();

        for (int srcNode : srcNodes) {
            for (int destNode : destNodes) {
                IntArrayList path = new IntArrayList();
                path.add(srcNode);
                dfs(srcNode, destNode, path, res);
            }
        }

        return res;
    }

    /**
     * Find all paths between two nodes with depth-first-search
     *
     * @param src  source node
     * @param dest target node
     * @return all paths between given two nodes ⭐
     */
    public List<IntList> findAllPaths(int src, int dest) {
        List<IntList> res = new ArrayList<>();
        IntList path = new IntArrayList();
        path.add(src);
        dfs(src, dest, path, res);

        return res;
    }

    private void dfs(int src, int dest, IntList path, List<IntList> res) {
        if (src == dest) {
            res.add(new IntArrayList(path));
            return;
        }

        for (Edge edge : getOutgoingEdges(src)) {
            int nextNode = edge.getTarget();
            path.add(nextNode);
            dfs(nextNode, dest, path, res);
            path.removeLast();
        }
    }

    /**
     * Return a {@link BreadthFirstIterator} on this graph
     *
     * @param startNode start node
     * @return {@link BreadthFirstIterator} instance
     */
    public BreadthFirstIterator<V> bfs(V startNode) {
        return new BreadthFirstIterator<>(this, startNode);
    }


    /**
     * Topological sort, only works for DAG.
     * <p>
     * O(V+E) in the worst case, O(V) extra space
     *
     * @param visitor {@link NodeVisitor}
     */
    public void topological(NodeVisitor visitor) {
        Queue<Integer> queue = new ArrayDeque<>();
        int[] indegree = new int[V];
        for (int v = 0; v < V; v++) {
            int d = incomingEdges[v].size();
            indegree[v] = d;
            if (d == 0)
                queue.offer(v);
        }
        int count = V;
        while (!queue.isEmpty()) {
            int v = queue.poll();
            visitor.visit(v);
            for (Edge edge : getOutgoingEdges(v)) {
                int target = edge.getTarget();
                if (indegree[target] > 0) {
                    indegree[target]--;
                    if (indegree[target] == 0) {
                        queue.offer(target);
                    }
                }
            }
            count--;
        }

        if (count > 0) {
            throw new NotDAGException();
        }
    }
}
