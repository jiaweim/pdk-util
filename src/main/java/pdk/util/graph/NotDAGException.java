package pdk.util.graph;

/**
 * An exception to signal that Topological sort is used for a non-directed
 * acyclic graph.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 23 Nov 2024, 17:59
 */
public class NotDAGException extends IllegalArgumentException {

    private static final String GRAPH_IS_NOT_A_DAG = "Graph is not a DAG";

    public NotDAGException() {
        super(GRAPH_IS_NOT_A_DAG);
    }
}
