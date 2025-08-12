package pdk.util.graph;

import org.jspecify.annotations.Nullable;
import pdk.util.graph.util.CollectListVisitor;

import java.util.*;

/**
 * Undirected graph.
 * <p>
 * If an undirected graph with N nodes has more than N-1 edges, it must have cycles.
 *
 * @param <V> type of the node
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Nov 2024, 3:36 PM
 */
public class UndirectedGraph<V> extends AbstractGraph<V> {

    private final HashMap<V, ArrayList<Edge<V>>> adjacencyMap_;

    /**
     * Create a {@link UndirectedGraph} containing given nodes
     *
     * @param nodes nodes
     */
    @SafeVarargs
    public UndirectedGraph(V... nodes) {
        this(Arrays.asList(nodes));
    }

    public UndirectedGraph(Collection<V> nodes) {
        super(nodes);
        adjacencyMap_ = new HashMap<>(getNodeCount());
        for (V node : nodeSet_) {
            adjacencyMap_.put(node, new ArrayList<>(2));
        }
    }

    /**
     * Create an empty {@link UndirectedGraph}
     */
    public UndirectedGraph() {
        adjacencyMap_ = new HashMap<>();
    }

    @Override
    public boolean addNode(V node) {
        if (containsNode(node))
            return false;
        nodeSet_.add(node);
        adjacencyMap_.put(node, new ArrayList<>(2));
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
        adjacencyMap_.get(source).add(edge);
        adjacencyMap_.get(target).add(edge);
        edgeSet_.add(edge);
        return true;
    }


    @Override
    public @Nullable Edge<V> getEdge(V source, V target) {
        for (Edge<V> edge : adjacencyMap_.get(source)) {
            if (getOppositeNode(edge, source) == target) {
                return edge;
            }
        }
        return null;
    }

    @Override
    public int getInDegree(V node) {
        return adjacencyMap_.get(node).size();
    }

    @Override
    public int getOutDegree(V node) {
        return adjacencyMap_.get(node).size();
    }

    @Override
    public int getDegree(V node) {
        return adjacencyMap_.get(node).size();
    }

    @Override
    public List<Edge<V>> getEdges(V node) {
        return Collections.unmodifiableList(adjacencyMap_.get(node));
    }

    @Override
    public List<Edge<V>> getIncomingEdges(V node) {
        return Collections.unmodifiableList(adjacencyMap_.get(node));
    }

    @Override
    public List<Edge<V>> getOutgoingEdges(V node) {
        return Collections.unmodifiableList(adjacencyMap_.get(node));
    }


    @Override
    public boolean removeEdge(Edge<V> edge) {
        if (edgeSet_.contains(edge)) {
            V source = edge.getSource();
            V target = edge.getTarget();

            adjacencyMap_.get(source).remove(edge);
            adjacencyMap_.get(target).remove(edge);
            edgeSet_.remove(edge);
            return true;
        }
        return false;
    }

    /**
     * depth first search for undirected graph
     *
     * @param startNode start node
     * @param visitor   {@link NodeVisitor}
     * @since 2024-11-28 ⭐
     */
    public void dfs(V startNode, NodeVisitor<V> visitor) {
        visitor.visit(startNode);

        HashMap<V, Boolean> visited = new HashMap<>();
        for (V v : nodeSet_) {
            visited.put(v, false);
        }
        visited.put(startNode, true);

        ArrayDeque<V> stack = new ArrayDeque<>();
        stack.push(startNode);

        while (!stack.isEmpty()) {
            V node = stack.peekFirst();
            V nextUnvisited = null;
            for (Edge<V> edge : getEdges(node)) {
                V v = getOppositeNode(edge, node);
                if (!visited.get(v)) {
                    nextUnvisited = v;
                    break;
                }
            }
            if (nextUnvisited == null) {
                stack.pop();
            } else {
                visited.put(nextUnvisited, true);
                visitor.visit(nextUnvisited);
                stack.push(nextUnvisited);
            }
        }
    }

    /**
     * Generate minimum spanning tree for undirected unweighted graph using depth-first search.
     *
     * @param startNode a node
     * @return {@link SpanningTree}
     * @since 2024-11-29⭐
     */
    public SpanningTree<V> mst(V startNode) {
        int V = getNodeCount();
        HashMap<V, Boolean> visited = new HashMap<>(V);
        for (V v : nodeSet_) {
            visited.put(v, false);
        }
        visited.put(startNode, true);

        ArrayDeque<V> stack = new ArrayDeque<>();
        stack.push(startNode);

        Set<Edge<V>> edges = new HashSet<>();
        while (!stack.isEmpty()) {
            V node = stack.peekFirst();

            // get a unvisited neighbor
            V v = null;
            Edge<V> vEdge = null;
            for (Edge<V> edge : getEdges(node)) {
                V v1 = getOppositeNode(edge, node);
                if (!visited.get(v1)) {
                    v = v1;
                    vEdge = edge;
                    break;
                }
            }

            if (v == null) { // no unvisited neighbor
                stack.pop();
            } else {
                visited.put(v, true);
                stack.push(v);
                edges.add(vEdge);
            }
        }
        return new SpanningTree<>(edges, edges.size());
    }

    /**
     * depth first search for undirected, unweighted graph
     *
     * @param startNode start node index
     * @param visitor   {@link NodeVisitor} to visit node
     * @since 2024-11-29 ⭐😀
     */
    public void bfs(V startNode, NodeVisitor<V> visitor) {
        visitor.visit(startNode);
        Set<V> visited = new HashSet<>();
        visited.add(startNode);

        ArrayDeque<V> queue = new ArrayDeque<>();
        queue.addLast(startNode);

        while (!queue.isEmpty()) {
            V v1 = queue.removeFirst();
            for (Edge<V> edge : getOutgoingEdges(v1)) {
                V v2 = getOppositeNode(edge, v1);
                if (visited.contains(v2))
                    continue;
                visited.add(v2);
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
     * @since 2024-11-29 ⭐😀
     */
    public List<V> bfs(V startNode) {
        CollectListVisitor<V> visitor = new CollectListVisitor<>();
        bfs(startNode, visitor);
        return visitor.getList();
    }
}
