package pdk.util.graph.util;

import pdk.util.graph.NodeVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link NodeVisitor} implementation that just collect all nodes
 *
 * @param <V> type of the node
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 30 Nov 2024, 18:35
 */
public class CollectListVisitor<V> implements NodeVisitor<V> {

    private final List<V> list;

    public CollectListVisitor() {
        list = new ArrayList<>();
    }

    public List<V> getList() {
        return list;
    }

    @Override
    public void visit(V node) {
        list.add(node);
    }
}
