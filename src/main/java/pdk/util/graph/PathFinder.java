package pdk.util.graph;

import org.jspecify.annotations.Nullable;

/**
 * Interface for algorithms finding shortest or longest path in a graph.
 *
 * @param <V> type of the node
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 25 Nov 2024, 5:32 PM
 */
public interface PathFinder<V> {

    /**
     * return the path weight from the start node to given target node
     *
     * @param targetNode target node
     * @return path weight
     */
    double getWeight(V targetNode);

    /**
     * Return true if the graph has a path from source node to target node
     *
     * @param targetNode target node
     * @return true if the graph has a path from the source node to this node
     */
    boolean hasPathTo(V targetNode);

    /**
     * Return the shortest path from the source node to given target node
     *
     * @param targetNode a node
     * @return shortest path
     */
    @Nullable
    GraphPath<V> getPath(V targetNode);
}
