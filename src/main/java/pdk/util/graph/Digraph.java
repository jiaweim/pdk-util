package pdk.util.graph;

import org.jheaps.AddressableHeap;
import org.jheaps.tree.PairingHeap;
import org.jspecify.annotations.Nullable;
import pdk.util.graph.util.BellmanFordShortestPath;
import pdk.util.graph.util.BreadthFirstIterator;

import java.util.*;

/**
 * Directed Graph implementation, the node should implement hashCode and equals.
 *
 * @param <V> type of the node
 * @author Jiawei Mao
 * @version 2.0.0
 * @since 23 Nov 2024, 00:02
 */
public class Digraph<V> extends AbstractGraph<V> {

    private final HashMap<V, ArrayList<Edge<V>>> incomingEdgeMap;
    private final HashMap<V, ArrayList<Edge<V>>> outgoingEdgeMap;

    @SafeVarargs
    public Digraph(V... nodes) {
        this(Arrays.asList(nodes));
    }

    /**
     * Create a directed-graph with given nodes
     */
    public Digraph(Collection<V> nodes) {
        super(nodes);
        incomingEdgeMap = new HashMap<>(getNodeCount());
        outgoingEdgeMap = new HashMap<>(getNodeCount());

        for (V v : nodeSet_) {
            incomingEdgeMap.put(v, new ArrayList<>(2));
            outgoingEdgeMap.put(v, new ArrayList<>(2));
        }
    }

    /**
     * Create an empty directed graph
     */
    public Digraph() {
        incomingEdgeMap = new HashMap<>();
        outgoingEdgeMap = new HashMap<>();
    }

    @Override
    public boolean addNode(V node) {
        if (containsNode(node))
            return false;
        nodeSet_.add(node);
        incomingEdgeMap.put(node, new ArrayList<>(2));
        outgoingEdgeMap.put(node, new ArrayList<>(2));
        return true;
    }

    @Override
    public boolean addEdge(Edge<V> edge) {
        if (containsEdge(edge))
            return false;

        V source = edge.getSource();
        V target = edge.getTarget();
        if (!containsNode(source)) {
            addNode(source);
        }
        if (!containsNode(target)) {
            addNode(target);
        }

        outgoingEdgeMap.get(source).add(edge);
        incomingEdgeMap.get(target).add(edge);
        edgeSet_.add(edge);

        return true;
    }

    @Override
    public @Nullable Edge<V> getEdge(V source, V target) {
        for (Edge<V> edge : outgoingEdgeMap.get(source)) {
            if (edge.getTarget() == target) { // directed graph
                return edge;
            }
        }

        return null;
    }

    @Override
    public boolean removeEdge(Edge<V> edge) {
        if (edgeSet_.contains(edge)) {
            V source = edge.getSource();
            V target = edge.getTarget();
            outgoingEdgeMap.get(source).remove(edge);
            incomingEdgeMap.get(target).remove(edge);
            edgeSet_.remove(edge);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getInDegree(V node) {
        return incomingEdgeMap.get(node).size();
    }

    @Override
    public int getOutDegree(V node) {
        return outgoingEdgeMap.get(node).size();
    }

    @Override
    public int getDegree(V node) {
        return getInDegree(node) + getOutDegree(node);
    }

    @Override
    public List<Edge<V>> getIncomingEdges(V node) {
        return Collections.unmodifiableList(incomingEdgeMap.get(node));
    }

    @Override
    public List<Edge<V>> getOutgoingEdges(V node) {
        return Collections.unmodifiableList(outgoingEdgeMap.get(node));
    }

    @Override
    public List<Edge<V>> getEdges(V node) {
        List<Edge<V>> edges = new ArrayList<>(getDegree(node));
        edges.addAll(incomingEdgeMap.get(node));
        edges.addAll(outgoingEdgeMap.get(node));
        return edges;
    }


    /**
     * @return a copy of the graph with all edges in reverse direction.
     * @since 2024-12-2 ⭐
     */
    public Digraph<V> reverse() {
        Digraph<V> graph = new Digraph<>(nodeSet_);
        for (Edge<V> edge : getEdgeSet()) {
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
        int V = getNodeCount();
        Map<V, Boolean> visited = new HashMap<>();
        Map<V, Boolean> beingVisited = new HashMap<>();
        for (V v : nodeSet_) {
            visited.put(v, false);
            beingVisited.put(v, false);
        }

        for (V v : nodeSet_) {
            if (!visited.get(v) && hasCycle(v, visited, beingVisited)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasCycle(V node, Map<V, Boolean> visited, Map<V, Boolean> beingVisited) {
        beingVisited.put(node, true);
        for (Edge<V> edge : getOutgoingEdges(node)) {
            V u = edge.getTarget();
            if (beingVisited.get(u)) {
                return true;
            } else if (!visited.get(u) && hasCycle(u, visited, beingVisited)) {
                return true;
            }
        }
        beingVisited.put(node, false);
        visited.put(node, true);
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
    public BellmanFordShortestPath<V> getShortestPathFinder(V startNode) {
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
    public GraphPath<V> getShortestPath(V startNode, V endNode) {
        for (Edge<V> edge : getEdgeSet()) {
            if (edge.getWeight() < 0) {
                throw new IllegalArgumentException("Edge " + edge + " has negative weight");
            }
        }
        int V = getNodeCount();
        Map<V, Double> distTo = new HashMap<>(V);
        Map<V, Edge<V>> edgeTo = new HashMap<>(V);
        Set<V> visited = new HashSet<>(V);

        for (V v : nodeSet_) {
            distTo.put(v, Double.MAX_VALUE);
        }
        distTo.put(startNode, 0.0);

        PairingHeap<Double, V> heap = new PairingHeap<>();
        heap.insert(distTo.get(startNode), startNode);
        while (!heap.isEmpty()) {
            AddressableHeap.Handle<Double, V> handle = heap.deleteMin();
            V v = handle.getValue();
            if (v == endNode) {
                break;
            }
            for (Edge<V> edge : getOutgoingEdges(v)) {
                V w = edge.getTarget();
                if (visited.contains(w)) {
                    continue;
                }
                if (distTo.get(w) > distTo.get(v) + edge.getWeight()) {
                    distTo.put(w, distTo.get(v) + edge.getWeight());
                    edgeTo.put(w, edge);
                }
                heap.insert(distTo.get(w), w);
            }
            visited.add(v);
        }
        if (distTo.get(endNode) == Double.MAX_VALUE) {
            return null;
        }

        LinkedList<Edge<V>> edges = new LinkedList<>();
        LinkedList<V> nodeList = new LinkedList<>();
        nodeList.add(endNode);

        V tmp = endNode;
        while (edgeTo.get(tmp) != null) {
            edges.add(edgeTo.get(tmp));
            tmp = edgeTo.get(tmp).getSource();
            nodeList.add(tmp);
        }
        return new GraphPath<>(this, startNode, endNode,
                nodeList.reversed(), edges.reversed(), distTo.get(endNode));
    }

    /**
     * Convert list of node to list of edge
     *
     * @param path node list
     * @return edge list
     */
    public List<Edge<V>> getPath(List<V> path) {
        List<Edge<V>> edges = new ArrayList<>(path.size());
        for (int i = 0; i < path.size() - 1; i++) {
            Edge<V> edge = getEdge(path.get(i), path.get(i + 1));
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
    public List<List<V>> findAllPaths(Collection<V> srcNodes, Collection<V> destNodes) {
        List<List<V>> res = new ArrayList<>();
        for (V srcNode : srcNodes) {
            for (V destNode : destNodes) {
                List<V> path = new ArrayList<>();
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
    public List<List<V>> findAllPaths(V src, V dest) {
        List<List<V>> res = new ArrayList<>();
        List<V> path = new ArrayList<>();
        path.add(src);
        dfs(src, dest, path, res);

        return res;
    }

    private void dfs(V src, V dest, List<V> path, List<List<V>> res) {
        if (src == dest) {
            res.add(new ArrayList<>(path));
            return;
        }

        for (Edge<V> edge : getOutgoingEdges(src)) {
            V nextNode = edge.getTarget();
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
     * @param visitor {@link NodeVisitor}😀
     */
    public void topological(NodeVisitor<V> visitor) {
        int count = getNodeCount();
        ArrayDeque<V> queue = new ArrayDeque<>();
        HashMap<V, Integer> indegree = new HashMap<>(count);
        for (V v : nodeSet_) {
            int d = incomingEdgeMap.get(v).size();
            indegree.put(v, d);
            if (d == 0) {
                queue.addLast(v);
            }
        }

        while (!queue.isEmpty()) {
            V v = queue.pollFirst();
            visitor.visit(v);
            for (Edge<V> edge : getOutgoingEdges(v)) {
                V target = edge.getTarget();
                Integer tIndegree = indegree.get(target);
                if (tIndegree > 0) {
                    indegree.put(target, tIndegree - 1);
                    if (indegree.get(target) == 0) {
                        queue.addLast(target);
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
