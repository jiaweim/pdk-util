package pdk.util.graph;

import org.jspecify.annotations.Nullable;

/**
 * Interface for algorithms finding shortest or longest path in a graph.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 25 Nov 2024, 5:32 PM
 */
public interface PathFinder<V> {

    /**
     * Return the index of given node
     *
     * @param node a node
     * @return index of the node
     */
    int indexOf(V node);

    /**
     * return the path weight from the start node to given target node
     *
     * @param targetNode target node
     * @return path weight
     */
    default double getWeight(V targetNode) {
        return getWeight(indexOf(targetNode));
    }

    /**
     * return the path weight from the start node to given target node
     *
     * @param targetNode target node
     * @return path weight
     */
    double getWeight(int targetNode);

    /**
     * Return true if the graph has a path from source node to target node
     *
     * @param targetNode target node
     * @return true if the graph has a path from the source node to this node
     */
    default boolean hasPathTo(V targetNode) {
        return hasPathTo(indexOf(targetNode));
    }

    /**
     * Return true if the graph has a path from source node to target node
     *
     * @param node target node
     * @return true if the graph has a path from the source node to this node
     */
    boolean hasPathTo(int node);

    /**
     * Return the shortest path from the source node to given target node
     *
     * @param targetNode a node
     * @return shortest path
     */
    @Nullable
    GraphPath<V> getPath(int targetNode);

    /**
     * Return the shortest path from the source node to given target node
     *
     * @param targetNode a node
     * @return shortest path
     */
    default GraphPath<V> getPath(V targetNode) {
        return getPath(indexOf(targetNode));
    }
}
