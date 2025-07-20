package pdk.util.graph.util;

import pdk.util.graph.Edge;
import pdk.util.graph.Graph;
import pdk.util.graph.GraphIterator;

import java.util.Stack;

/**
 * depth-first iterator for a directed or undirected graph.
 * <p>
 * If the graph is not connected, it will only iterate nodes connected to the start node.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 27 Nov 2024, 5:46 PM
 */
public class DepthFirstIterator<V> implements GraphIterator<V> {

    private final Graph<V> graph;
    private final Stack<Integer> stack;
    private final boolean[] visited;
    private int currentNode;

    public DepthFirstIterator(Graph<V> graph, V startNode) {
        this.graph = graph;
        stack = new Stack<>();
        stack.push(graph.indexOf(startNode));
        visited = new boolean[graph.getNodeCount()];
    }

    @Override
    public boolean hasNext() {
        while (!stack.isEmpty()) {
            currentNode = stack.pop();
            if (!visited[currentNode]) {
                visited[currentNode] = true;
                for (Edge edge : graph.getOutgoingEdges(currentNode)) {
                    int node = graph.getOppositeNode(edge, currentNode);
                    if (!visited[node]) {
                        stack.push(node);
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public V next() {
        return graph.getNode(currentNode);
    }
}
